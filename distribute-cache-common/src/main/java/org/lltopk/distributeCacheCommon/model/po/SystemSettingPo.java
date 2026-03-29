package org.lltopk.distributeCacheCommon.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("distributed_cache_system_setting_po")
public class SystemSettingPo {
    private Long id;

    private String configKey;

    private String configValue;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
