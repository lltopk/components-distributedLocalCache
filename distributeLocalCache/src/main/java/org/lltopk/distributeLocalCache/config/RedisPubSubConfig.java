package org.lltopk.distributeLocalCache.config;

import org.lltopk.distributeLocalCache.cache.AbstractGuavaCache;
import org.lltopk.distributeLocalCache.cache.ILocalCacheSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.List;

@Configuration
public class RedisPubSubConfig {
    @Autowired
    List<ILocalCacheSubscriber> localCacheSubscribers;

    /**
     *
     Redis 的 Pub/Sub：频道不需要提前创建, 发布消息也不会创建频道

     pubsub channels 会展示当前有订阅者的频道

     因此配置要比mq简单的多, 只需要给订阅者指定监听的频道即可, redis会自动管理
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
