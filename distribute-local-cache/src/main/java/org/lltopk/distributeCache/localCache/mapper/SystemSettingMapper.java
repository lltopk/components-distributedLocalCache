package org.lltopk.distributeCache.localCache.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.lltopk.distributeCache.common.model.po.SystemSettingPo;

import java.util.List;

@Mapper
public interface SystemSettingMapper extends BaseMapper<SystemSettingPo> {
    SystemSettingPo findByConfigKey(@Param("configKey") String configKey);
    List<SystemSettingPo> selectAll();
}
