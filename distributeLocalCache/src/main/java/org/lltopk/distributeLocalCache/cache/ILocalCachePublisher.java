package org.lltopk.distributeLocalCache.cache;

import org.springframework.data.redis.connection.MessageListener;

public interface ILocalCachePublisher {

    /**
     * 同步缓存的指定key(用于清空本地缓存)
     * @param channelTopic
     * @param key
     * @return 订阅者数量
     */
    public Long sync(String channelTopic, String key);

    /**
     * 刷新缓存(用于刷新本地整个缓存)
     * @param channelTopic
     * @return 订阅者数量
     */
    public Long refresh(String channelTopic);
}
