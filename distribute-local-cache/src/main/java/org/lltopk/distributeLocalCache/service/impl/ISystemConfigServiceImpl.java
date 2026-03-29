package org.lltopk.distributeLocalCache.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.lltopk.distributeCacheCommon.model.form.UpdateSystemConfigForm;
import org.lltopk.distributeCacheCommon.model.po.SystemSettingPo;
import org.lltopk.distributeLocalCache.biz.BizStringVCache;
import org.lltopk.distributeLocalCache.cache.CacheFlushPublisher;
import org.lltopk.distributeLocalCache.mapper.SystemSettingMapper;
import org.lltopk.distributeLocalCache.service.ISystemConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Override
    public Boolean updateSystemConfig(UpdateSystemConfigForm form) {
        SystemSettingPo systemSettingPo = new SystemSettingPo();
        BeanUtils.copyProperties(form,systemSettingPo);
        //先更新数据库, cache aside原则
        systemSettingMapper.updateById(systemSettingPo);

        //后清空缓存
        cacheFlushPublisher.publishFlushByTopicAndKey(bizStringVCache.topic(), systemSettingPo.getConfigKey());
        return true;
    }

    @Override
    public String getSystemConfig(String configKey) {
        String cacheValue = "";
        try {
            cacheValue = bizStringVCache.get(configKey);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        if (StringUtils.isNotBlank(cacheValue)) {
            return cacheValue;
        }
        SystemSettingPo systemSettingPo = systemSettingMapper.findByConfigKey(configKey);
        if (Objects.isNull(systemSettingPo)) {
            return null;
        }
        bizStringVCache.setCache(configKey, systemSettingPo.getConfigValue());

        return systemSettingPo.getConfigValue();
    }
}
