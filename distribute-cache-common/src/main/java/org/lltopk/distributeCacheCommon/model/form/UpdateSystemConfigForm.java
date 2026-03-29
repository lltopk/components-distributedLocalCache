package org.lltopk.distributeCacheCommon.model.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSystemConfigForm {

    private Long id;

    private String configKey;

    private String configValue;
}
