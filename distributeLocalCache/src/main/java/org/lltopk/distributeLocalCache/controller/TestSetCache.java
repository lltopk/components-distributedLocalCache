package org.lltopk.distributeLocalCache.controller;

import lombok.extern.slf4j.Slf4j;
import org.lltopk.distributeLocalCache.biz.BizListVCache;
import org.lltopk.distributeLocalCache.biz.BizMapVCache;
import org.lltopk.distributeLocalCache.biz.BizStringVCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testSetCache")
@Slf4j
public class TestSetCache {
    @Autowired
    private BizStringVCache bizStringVCache;

    @Autowired
    private BizListVCache bizListVCache;

    @Autowired
    private BizMapVCache bizMapVCache;

    @PostMapping(value = "/setBizStringVCache")
    public Object setBizStringVCache (@RequestParam("key") String key, @RequestParam("value") String value) {
        bizStringVCache.setCache(key, value);
        return "success";
    }
}
