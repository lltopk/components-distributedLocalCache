package org.lltopk.distributeCache.localCache.biz;

import lombok.extern.slf4j.Slf4j;
import org.lltopk.distributeCache.common.model.po.SystemSettingPo;
import org.lltopk.distributeCache.localCache.cache.AbstractGuavaCache;
import org.lltopk.distributeCache.localCache.mapper.SystemSettingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class BizStringVCache extends AbstractGuavaCache<String,String> {

    @Autowired
    SystemSettingMapper systemSettingMapper;

    @Override
    protected java.lang.String name() {
        return "BizStringCache";
    }

    @Override
    public String topic() {
        return "BizStringCache";
    }

    /**
     * 缓存的值是string
     * @param key
     * @return
     */
    @Override
    protected String loadIfNot(String key) {
        SystemSettingPo systemSettingPo = systemSettingMapper.findByConfigKey(key);
        if (Objects.isNull(systemSettingPo)) {
            return "";
        }

        log.info("本地缓存 {}: {} 初始化成功", key, systemSettingPo.getConfigValue());
        return systemSettingPo.getConfigValue();
    }

}
