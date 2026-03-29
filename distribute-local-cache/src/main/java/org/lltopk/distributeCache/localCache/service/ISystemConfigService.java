package org.lltopk.distributeCache.localCache.service;


import org.lltopk.distributeCache.common.model.form.UpdateSystemConfigForm;

public interface ISystemConfigService {
    Boolean updateSystemConfig(UpdateSystemConfigForm form);

    String getSystemConfig(String configKey);
}
