package org.lltopk.distributeCache.twoStage.service;


import org.lltopk.distributeCache.common.model.form.UpdateSystemConfigForm;

public interface ISystemConfigService {
    Boolean updateSystemConfig(UpdateSystemConfigForm form);

    String getSystemConfig(String configKey);
}
