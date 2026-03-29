package org.lltopk.distributeLocalCache.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
public class TestRedisConnection implements SmartInitializingSingleton {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void afterSingletonsInstantiated() {
        log.info("TestRedisConnection start...");
        Set<String> keys = stringRedisTemplate.keys("*");
        for (String key : keys) {
            log.info("TestRedisConnection key {} value {}",key,stringRedisTemplate.opsForValue().get(key));
        }
        log.info("TestConnection success...");
    }
}
