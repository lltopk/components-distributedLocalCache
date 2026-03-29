package org.lltopk.distributeCache.localCache.cache;

public interface ILocalCachePublisher {

    /**
     * 刷新缓存(用于刷新本地整个缓存)
     * @param channelTopic
     * @return 订阅者数量
     */
    public Long refresh(String channelTopic);
}
