package org.lltopk.distributeCache.twoStage.controller;

import lombok.extern.slf4j.Slf4j;
import org.lltopk.distributeCache.twoStage.cache.CacheFlushPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updateDb")
@Slf4j
public class _02_TestRefreshAll {
    @Autowired
    private CacheFlushPublisher cacheFlushPublisher;;


    @PostMapping(value = "/refreshAll")
    public void publishFlushAllToTopic() {
        cacheFlushPublisher.publishFlushAllToTopic("BizStringCache");
        cacheFlushPublisher.publishFlushAllToTopic("BizListCache");
        cacheFlushPublisher.publishFlushAllToTopic("BizMapCache");
    }
}
