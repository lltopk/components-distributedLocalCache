package org.lltopk.distributeCache.twoStage.cache;

import org.springframework.data.redis.connection.MessageListener;

public interface ILocalCacheSubscriber extends MessageListener {

    String topic();
}
