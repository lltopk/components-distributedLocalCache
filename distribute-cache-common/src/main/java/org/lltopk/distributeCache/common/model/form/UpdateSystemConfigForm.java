package org.lltopk.distributeCache.common.model.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSystemConfigForm {

    private Long id;

    private String configKey;

    private String configValue;
}
