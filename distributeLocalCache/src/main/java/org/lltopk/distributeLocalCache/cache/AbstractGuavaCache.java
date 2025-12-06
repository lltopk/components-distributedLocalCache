package org.lltopk.distributeLocalCache.cache;
import com.google.common.cache.*;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lltopk.distributeLocalCache.utils.SpringUtil;
import org.springframework.data.redis.connection.Message;
import org.springframework.lang.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 基于Guava Cache的高级本地缓存实现
 * 提供自动过期、LRU淘汰、统计信息等高级功能
 */
@Slf4j
public abstract class AbstractGuavaCache<K, V> implements ILocalCacheAccess<K, V>, ILocalCacheSubscriber ,ILocalCachePublisher{

    CacheFlushPublisher cacheFlushPublisher;

    /**
     * 确保不要在静态初始化或构造早期阶段调用该工具类获取 Bean；会导致空指针
     *
     * Spring 初始化顺序大致是这样：
     * 实例化 bean（调用构造器）
     * 依赖注入
     * 调用 BeanFactoryAware / ApplicationContextAware（你这里才拿到 SpringUtil.applicationContext）
     * 调用 @PostConstruct
     * bean 准备完成
     */
//    public AbstractGuavaCache(){
//        cacheFlushPublisher = SpringUtil.getBeanByApplicationContextAware(CacheFlushPublisher.class);
//    }


    // 使用Guava Cache替代ConcurrentHashMap，提供更丰富的功能
    protected final LoadingCache<K, V> cache = CacheBuilder.newBuilder()
            //缓存池大小，在缓存项接近该大小时， Guava开始回收旧的缓存项
            .maximumSize(1000)
            //设置时间对象没有被读/写访问则对象从内存中删除
            .expireAfterAccess(5, TimeUnit.MINUTES)
            //移除监听器,缓存项被移除时会触发
            .removalListener(new GuavaRemovalListener())
            .recordStats()//开启Guava Cache的统计功能
            //获取缓存不存在时候加载数据库
            .build(new GuavaCacheLoader());

    /**
     * 注意不要写成GuavaRemovalListener<K,V>, 这会导致泛型遮蔽, 遮蔽外层的<K,V>, 导致编译器混淆
     */
    class GuavaRemovalListener implements RemovalListener<K,V> {

        @Override
        public void onRemoval(RemovalNotification<K,V> notification) {
            log.info("cache {} removed key {} value {}",name(), notification.getKey(),notification.getValue());
        }
    }

    /**
     * 注意不要写成GuavaCacheLoader<K,V>, 这会导致泛型遮蔽, 遮蔽外层的<K,V>, 导致编译器混淆
     */
    class GuavaCacheLoader extends CacheLoader<K,V>{
        @Override
        public V load(K key) throws Exception {
            //loading 是外部类的方法（非静态、受保护），因此内置非静态类能直接访问它
            return loadIfNot(key);
        }
    }

    @Override
    public V get(K key) throws ExecutionException {
        return cache.get(key);
    }

    /**
     * 更新缓存的时候发布通知, 清空其他节点本地缓存
     * @param key
     * @param value
     */
    @Override
    public void setCache(K key, V value) {
        cache.put(key, value);
        sync(topic(),(String)key);
    }

    @Override
    public void removeKey(K key) {
        cache.invalidate(key);
    }

    @Override
    public void refresh() {
        cache.invalidateAll();
        refresh(topic());
    }

    @Override
    public int size() {
        return (int) cache.size();
    }

    @Override
    public boolean containsKey(K key) {
        return cache.getIfPresent(key) != null;
    }

    @Override
    public Map<K,V> entries(){
        return ImmutableMap.copyOf(cache.asMap());
    }

    abstract protected String name();

    abstract protected V loadIfNot(K k) throws Exception;
    /**
     * 获取Guava Cache的统计信息
     *
     * 拿到一个 CacheStats 对象，里面包含：
     *
     * 指标	含义
     * hitCount()	缓存命中的次数
     * missCount()	缓存未命中次数
     * loadSuccessCount()	成功加载次数
     * loadExceptionCount()	加载失败次数
     * totalLoadTime()	加载总耗时（纳秒）
     * evictionCount()	被动回收次数
     */
    public CacheStats stats() {
        return cache.stats();
    }

    /**
     * 抽象类本身是具有抽象性的, 因此Java抽象类实现某个接口后不必须实现所有的方法。
     *
     * 但这里为了统一子类行为, 将onMessage的实现提前到当前抽象类
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, @Nullable byte[] pattern){

        String patternStr = new String(pattern);
        log.info("patternStr {}",patternStr);

        byte[] body = message.getBody();
        String payload = new String(body);
        String senderId = null;
        String key = payload;

        //格式校验
        if (!StringUtils.contains(payload, "|")) {
            return;
        }

        String[] parts = payload.split("\\|");
        senderId = parts[0];
        key = parts[1];

        // 如果是当前节点自己发出的消息，忽略
        CacheFlushPublisher publisher = getCacheFlushPublisher();
        if (Objects.nonNull(senderId) && StringUtils.equals(senderId, publisher.getInstanceId())) {
            log.info("skip self message {}", payload);
            return;
        }

        if (StringUtils.equals(key,"refresh")) {
            //清空所有缓存
            //收到“refresh”消息时，只执行本地 cache.invalidateAll()，不要再调用 refresh()
            // 避免消息回声。
           cache.invalidateAll();
        } else if (Objects.nonNull(key)) {
            // 否则尝试移除特定key
            removeKey((K) key);
        }
    }

    @Override
    public Long sync(String channelTopic, String key){
        return getCacheFlushPublisher().publishFlushByTopicAndKey(channelTopic,key);
    }

    @Override
    public Long refresh(String channelTopic){
        return getCacheFlushPublisher().publishFlushAllToTopic(channelTopic);
    }

    private CacheFlushPublisher getCacheFlushPublisher(){
        if (Objects.nonNull(cacheFlushPublisher)){
            return cacheFlushPublisher;
        }
        cacheFlushPublisher = SpringUtil.getBeanByApplicationContextAware(CacheFlushPublisher.class);
        return cacheFlushPublisher;
    }
}
