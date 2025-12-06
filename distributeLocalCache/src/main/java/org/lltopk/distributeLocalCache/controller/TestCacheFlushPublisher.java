package org.lltopk.distributeLocalCache.controller;

import lombok.extern.slf4j.Slf4j;
import org.lltopk.distributeLocalCache.cache.CacheFlushPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cacheFlushPublisher")
@Slf4j
public class TestCacheFlushPublisher {
    @Autowired
    private CacheFlushPublisher cacheFlushPublisher;

    @PostMapping(value = "/publishFlushAllToTopic")
    public void publishFlushAllToTopic(@RequestParam("channelTopic") String channelTopic) {
        cacheFlushPublisher.publishFlushAllToTopic(channelTopic);
    }

    @PostMapping(value = "/publishFlushByTopicAndKey")
    public void publishFlushByTopicAndKey(@RequestParam("channelTopic") String channelTopic,@RequestParam("key") String key) {
        cacheFlushPublisher.publishFlushByTopicAndKey(channelTopic, key);
    }
}
