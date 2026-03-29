package org.lltopk.distributeLocalCache.service;


import org.lltopk.distributeCacheCommon.model.form.UpdateSystemConfigForm;
import org.lltopk.distributeCacheCommon.model.po.SystemSettingPo;

public interface ISystemConfigService {
    Boolean updateSystemConfig(UpdateSystemConfigForm form);

    String getSystemConfig(String configKey);
}
