package org.lltopk.distributeLocalCache.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.MessageFormat;
import java.util.UUID;

@Component
@Slf4j
public class CacheFlushPublisher {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 为当前节点生成唯一标识
    private final String instanceId = UUID.randomUUID().toString();

    public String getInstanceId() {
        return instanceId;
    }

    public Long publishFlushAllToTopic(String channelTopic) {
        return redisTemplate.convertAndSend(channelTopic, MessageFormat.format("{0}|refresh",instanceId));
    }

    public Long publishFlushByTopicAndKey(String channelTopic,String key) {
        return redisTemplate.convertAndSend(channelTopic, MessageFormat.format("{0}|{1}",instanceId,key));
    }
}
