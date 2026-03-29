package org.lltopk.distributeCache.localCache.init;

import lombok.extern.slf4j.Slf4j;
import org.lltopk.distributeCache.localCache.biz.BizListVCache;
import org.lltopk.distributeCache.localCache.biz.BizMapVCache;
import org.lltopk.distributeCache.localCache.biz.BizStringVCache;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class LocalCacheGenerator implements SmartInitializingSingleton {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private BizStringVCache bizStringVCache;
    @Autowired
    private BizListVCache bizListVCache;
    @Autowired
    private BizMapVCache bizMapVCache;
    @Override
    public void afterSingletonsInstantiated() {
        log.info("LocalCacheGenerator start...");
        try {
            bizStringVCache.get("configKey1");
            bizStringVCache.get("configKey2");
            log.info("bizStringCache entries {}", bizStringVCache.entries());

            bizListVCache.get("key1");
            bizListVCache.get("key2");
            log.info("bizListCache entries {}", bizListVCache.entries());
            bizMapVCache.get("key1");
            bizMapVCache.get("key2");
            log.info("bizMapVCache entries {}", bizMapVCache.entries());
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        log.info("LocalCacheGenerator success...");
    }
}
