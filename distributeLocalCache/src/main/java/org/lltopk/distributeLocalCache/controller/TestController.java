package org.lltopk.distributeLocalCache.controller;

import lombok.extern.slf4j.Slf4j;
import org.lltopk.distributeLocalCache.biz.BizListVCache;
import org.lltopk.distributeLocalCache.biz.BizMapVCache;
import org.lltopk.distributeLocalCache.biz.BizStringVCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testController")
@Slf4j
public class TestController {
    @Autowired
    private BizStringVCache bizStringVCache;

    @Autowired
    private BizListVCache bizListVCache;

    @Autowired
    private BizMapVCache bizMapVCache;

    @PostMapping(value = "/bizStringVCacheEntries")
    public Object getBizStringCache () {
        log.info("remained bizStringVCacheEntries {}", bizStringVCache.entries());
        return bizStringVCache.entries();
    }

    @PostMapping(value = "/bizListVCacheEntries")
    public Object bizListVCacheEntries () {
        log.info("remained bizListVCacheEntries {}", bizListVCache.entries());
        return bizListVCache.entries();
    }

    @PostMapping(value = "/bizMapVCacheEntries")
    public Object bizMapVCacheEntries () {
        log.info("remained bizMapVCacheEntries {}", bizMapVCache.entries());
        return bizMapVCache.entries();
    }

}
