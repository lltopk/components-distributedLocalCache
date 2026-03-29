package org.lltopk.distributeRedisCache.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.lltopk.distributeCacheCommon.model.form.UpdateSystemConfigForm;
import org.lltopk.distributeCacheCommon.model.po.SystemSettingPo;
import org.lltopk.distributeRedisCache.mapper.SystemSettingMapper;
import org.lltopk.distributeRedisCache.service.ISystemConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class ISystemConfigServiceImpl implements ISystemConfigService {
    @Autowired
    private SystemSettingMapper systemSettingMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public Boolean updateSystemConfig(UpdateSystemConfigForm form) {
        SystemSettingPo systemSettingPo = new SystemSettingPo();
        BeanUtils.copyProperties(form,systemSettingPo);
        //先更新数据库, cache aside原则
        systemSettingMapper.updateById(systemSettingPo);

        //后清空缓存
        stringRedisTemplate.delete(systemSettingPo.getConfigKey());
        return true;
    }

    @Override
    public String getSystemConfig(String configKey) {
        String cacheValue = stringRedisTemplate.opsForValue().get(configKey);
        if (StringUtils.isNotBlank(cacheValue)) {
            log.info("redis缓存命中");
            return cacheValue;
        }
        SystemSettingPo systemSettingPo = systemSettingMapper.findByConfigKey(configKey);
        if (Objects.isNull(systemSettingPo)) {
            return null;
        }
        stringRedisTemplate.opsForValue().set(configKey,systemSettingPo.getConfigValue());

        return systemSettingPo.getConfigValue();
    }
}
