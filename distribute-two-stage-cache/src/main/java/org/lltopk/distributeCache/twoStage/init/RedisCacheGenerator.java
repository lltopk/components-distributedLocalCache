package org.lltopk.distributeCache.twoStage.init;

import lombok.extern.slf4j.Slf4j;
import org.lltopk.distributeCache.common.model.po.SystemSettingPo;
import org.lltopk.distributeCache.twoStage.mapper.SystemSettingMapper;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class RedisCacheGenerator implements SmartInitializingSingleton {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SystemSettingMapper systemSettingMapper;

    @Override
    public void afterSingletonsInstantiated() {
        log.info("RedisCacheGenerator start...");

        List<SystemSettingPo> systemSettingPos = systemSettingMapper.selectAll();
        for (SystemSettingPo systemSettingPo : systemSettingPos) {
            stringRedisTemplate.opsForValue().set(systemSettingPo.getConfigKey(), systemSettingPo.getConfigValue());
        }
        Set<String> keys = stringRedisTemplate.keys("*");
        for (String key : keys) {
            log.info("RedisCacheGenerator key {} value {}", key, stringRedisTemplate.opsForValue().get(key));
        }

        log.info("RedisCacheGenerator success...");
    }
}
