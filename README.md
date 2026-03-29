## 一. Reids分布式缓存

### 模拟分布式节点
1. 启动主应用默认8080端口, 这是第一个主应用
2. 右键配置主应用, 开启Allow Multi Instance,
3. 右键配置主应用, 添加Program arguments参数`--server.port=9090`, 修改应用2端口号为9090

应用启动初始化, 更新所有数据到Redis缓存
更新数据策略: Cache Aside, 先更新数据库, 后删除缓存
查询数据策略: 先查看缓存, 如果没有, 再查数据库, 重新放入缓存

## 二. 分布式本地缓存, Redis只作为发布订阅
本项目利用redis的消息发布订阅模式, 实现了分布式的本地缓存的一致性

由于不需要redis缓存, 也就节省了每次网络请求的开销, 因此本地缓存的性能是很高的

### 本地缓存结构
使用guava, 缓存默认懒加载, 当get不到的时候, 执行回调加载数据库缓存
```java
    // LoadingCache methods

    V getOrLoad(K key) throws ExecutionException {
        return get(key, defaultLoader);
    }
```
其中defaultLoader是builder建造者构建的时候, 传入的自定义回调函数
```java
  public <K1 extends K, V1 extends V> LoadingCache<K1, V1> build(
        CacheLoader<? super K1, V1> loader) {
    checkWeightWithWeigher();
    return new LocalCache.LocalLoadingCache<>(this, loader);
  }
```

继续追踪源码, 回调函数在LocalCache.java中被执行
```java
    public ListenableFuture<V> loadFuture(K key, CacheLoader<? super K, V> loader) {
      try {
        stopwatch.start();
        V previousValue = oldValue.get();
        if (previousValue == null) {
          V newValue = loader.load(key);
          return set(newValue) ? futureValue : Futures.immediateFuture(newValue);
        }
        ListenableFuture<V> newValue = loader.reload(key, previousValue);
        if (newValue == null) {
          return Futures.immediateFuture(null);
        }
        // To avoid a race, make sure the refreshed value is set into loadingValueReference
        // *before* returning newValue from the cache query.
        return transform(
            newValue,
            new com.google.common.base.Function<V, V>() {
              @Override
              public V apply(V newValue) {
                LoadingValueReference.this.set(newValue);
                return newValue;
              }
            },
            directExecutor());
      } catch (Throwable t) {
        ListenableFuture<V> result = setException(t) ? futureValue : fullyFailedFuture(t);
        if (t instanceof InterruptedException) {
          Thread.currentThread().interrupt();
        }
        return result;
      }
    }
```



### 频道自动装配
Redis 的 Pub/Sub：频道不需要提前创建, 发布消息也不会创建频道

因此配置要比mq简单的多, 只需要给订阅者指定监听的频道即可, 一旦有客户端在监听, redis就会自动管理并创建频道

启动服务之后, 可以在redis-cli控制台执行pubsub channels验证当前自动生成的所有订阅者频道
```java
@Configuration
public class RedisPubSubConfig {
    @Autowired
    List<ILocalCacheSubscriber> localCacheSubscribers;

    /**
     * @param connectionFactory
     * @return
     */
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        for (ILocalCacheSubscriber localCacheSubscriber : localCacheSubscribers) {
            container.addMessageListener(localCacheSubscriber, new ChannelTopic(localCacheSubscriber.topic()));
        }
        return container;
    }
}
```

### 业务逻辑
根据单一职责, 设计三个接口:
- [ILocalCacheAccess.java](distributeLocalCache/src/main/java/org/lltopk/distributeLocalCache/cache/ILocalCacheAccess.java)
- [ILocalCachePublisher.java](distributeLocalCache/src/main/java/org/lltopk/distributeLocalCache/cache/ILocalCachePublisher.java)
- [ILocalCacheSubscriber.java](distributeLocalCache/src/main/java/org/lltopk/distributeLocalCache/cache/ILocalCacheSubscriber.java)

分别是缓存的基本访问CRUD功能, 发布频道消息功能, 订阅频道接口消息功能

抽象类[AbstractGuavaCache.java](distributeLocalCache/src/main/java/org/lltopk/distributeLocalCache/cache/AbstractGuavaCache.java)实现上述三个接口

核心的业务逻辑以下, 下文**客户端**代指具备redis监听能力的客户端节点, 也就是我们的业务节点

当有客户端更新数据库的时候, 触发发布频道消息, 各大服务节点监听消息, 清空各自的本地缓存, 用于同步各大节点本地缓存一致性

### redis原生发布订阅命令
进入redis容器
```shell
[root@192 ~]# docker exec -it redis /bin/bash
root@9449807b3731:/data# ls
appendonly.aof  dump.rdb
root@9449807b3731:/data# redis-cli
```

PUBLISH <channel> <message>命令用于发布消息, 其返回值是可以接收消息的客户端数量
```shell
127.0.0.1:6379> pubsub channels
1) "BizStringCache"
2) "BizListCache"
127.0.0.1:6379> pubsub channels
(empty array)
127.0.0.1:6379> pubsub channels
1) "BizStringCache"
2) "BizListCache"
127.0.0.1:6379> pubsub channels
1) "BizStringCache"
2) "BizListCache"
127.0.0.1:6379> PUBLISH BizStringCache "key1"
(integer) 1
127.0.0.1:6379> pubsub channels
1) "BizStringCache"
2) "BizListCache"
127.0.0.1:6379> PUBLISH BizStringCache "key1"
(integer) 1
127.0.0.1:6379> PUBLISH BizStringCache "key1"
(integer) 2
127.0.0.1:6379> PUBLISH BizListCache "key1"
(integer) 2
127.0.0.1:6379> PUBLISH BizMapCache "key1"
(integer) 0
127.0.0.1:6379> PUBLISH BizMapCache "key1"
(integer) 2
127.0.0.1:6379> PUBLISH BizMapCache "refresh"
(integer) 2
127.0.0.1:6379> pubsub channels
1) "BizMapCache"
2) "BizStringCache"
3) "BizListCache"
```

> 在Linux命令文档/帮助的语法说明里 
> 方括号 [ ]：表示可选项。出现在方括号里的内容可以不写。 例：command [op tions] <file> 表示 options 是可选的。 
> 尖括号 < >：表示必填的占位符，需要你用实际值替换，括号本身不输入。 例：cp <源文件> <目标路径> 实际输入应是 cp src.txt /tmp/

### 测试验证
项目启动初始化数据库数据到各个节点的缓存

请求更新数据库接口, 会遵循Cache Aside原则, 先更新数据库, 再发送广播清空各大节点本地缓存

请求查看数据库接口, 此时缓存为空, 自然会查数据库, 然后更新数据到本地缓存


## 三. 分布式两级缓存
以上二者相结合: 

数据更新策略: 先更新数据库, 然后同时删除本地和远程缓存, 其中本地缓存删除用广播

数据查看策略: 
- 先查本地缓存, 如本地缓存在则返回
- 先查本地缓存, 如本地缓存不存在, 则查远程缓存, 若远程缓存存在则返回
- 若以上两级缓存都不存在, 则查数据库, 若数据库也不存在, 则返回空
- 若数据库存在, 回设上述两级缓存

