package org.lltopk.distributeLocalCache.mock;

import lombok.extern.slf4j.Slf4j;
import org.lltopk.distributeLocalCache.biz.BizStringVCache;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
public class TestConnection implements SmartInitializingSingleton {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private BizStringVCache bizStringVCache;

    @Override
    public void afterSingletonsInstantiated() {
        log.info("TestConnection start...");
        Set<String> keys = stringRedisTemplate.keys("*");
        for (String key : keys) {
            log.info("test key {} value {}",key,stringRedisTemplate.opsForValue().get(key));
        }
        log.info("TestConnection success...");
    }
}
