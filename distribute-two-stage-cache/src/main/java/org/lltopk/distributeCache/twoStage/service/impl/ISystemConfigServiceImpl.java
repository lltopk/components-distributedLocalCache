package org.lltopk.distributeCache.twoStage.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.lltopk.distributeCache.common.model.form.UpdateSystemConfigForm;
import org.lltopk.distributeCache.common.model.po.SystemSettingPo;
import org.lltopk.distributeCache.twoStage.biz.BizStringVCache;
import org.lltopk.distributeCache.twoStage.cache.CacheFlushPublisher;
import org.lltopk.distributeCache.twoStage.mapper.SystemSettingMapper;
import org.lltopk.distributeCache.twoStage.service.ISystemConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class ISystemConfigServiceImpl implements ISystemConfigService {
    @Autowired
    private SystemSettingMapper systemSettingMapper;
    @Autowired
    private CacheFlushPublisher cacheFlushPublisher;
    @Autowired
    private BizStringVCache bizStringVCache;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public Boolean updateSystemConfig(UpdateSystemConfigForm form) {
        SystemSettingPo systemSettingPo = new SystemSettingPo();
        BeanUtils.copyProperties(form,systemSettingPo);
        //先更新数据库, cache aside原则
        systemSettingMapper.updateById(systemSettingPo);

        //后清空两级缓存
        cacheFlushPublisher.publishFlushByTopicAndKey(bizStringVCache.topic(), systemSettingPo.getConfigKey());
        stringRedisTemplate.delete(systemSettingPo.getConfigKey());
        return true;
    }

    @Override
    public String getSystemConfig(String configKey) {
        String cacheValue = "";

        //先查本地缓存
        try {
            cacheValue = bizStringVCache.get(configKey);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        //本地缓存命中
        if (StringUtils.isNotBlank(cacheValue)) {
            return cacheValue;
        }

        //远程缓存命中
        cacheValue = stringRedisTemplate.opsForValue().get(configKey);
        if (StringUtils.isNotBlank(cacheValue)) {
            return cacheValue;
        }

        SystemSettingPo systemSettingPo = systemSettingMapper.findByConfigKey(configKey);
        if(Objects.isNull(systemSettingPo)){
            return "";
        }

        //最后设置缓存即可
        stringRedisTemplate.opsForValue().set(configKey, systemSettingPo.getConfigValue());
        bizStringVCache.setCache(configKey, systemSettingPo.getConfigValue());
        return cacheValue;
    }
}
