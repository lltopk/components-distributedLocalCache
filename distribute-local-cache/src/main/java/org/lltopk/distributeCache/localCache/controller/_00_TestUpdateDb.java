package org.lltopk.distributeCache.localCache.controller;

import lombok.extern.slf4j.Slf4j;
import org.lltopk.distributeCache.common.model.form.UpdateSystemConfigForm;
import org.lltopk.distributeCache.localCache.service.ISystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/updateDb")
@Slf4j
public class _00_TestUpdateDb {
    @Autowired
    private ISystemConfigService systemConfigService;

    @PostMapping(value = "/updateKey")
    public void publishFlushByTopicAndKey(@RequestBody UpdateSystemConfigForm form) {
        systemConfigService.updateSystemConfig(form);
    }

}
