package org.lltopk.distributeLocalCache.biz;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.lltopk.distributeCacheCommon.model.po.BizPo;
import org.lltopk.distributeLocalCache.cache.AbstractGuavaCache;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class BizMapVCache extends AbstractGuavaCache<String, Map<String, BizPo>> {

    @Override
    protected String name() {
        return "BizMapCache";
    }

    @Override
    public String topic() {
        return "BizMapCache";
    }

    @Override
    protected Map<String,BizPo> loadIfNot(String key) {
        Map<String,BizPo> map = Maps.newHashMap();
        BizPo bizPo = new BizPo();
        bizPo.setId(1l);
        bizPo.setName(key);
        bizPo.setAge(11);
        map.put(key,bizPo);
        return map;
    }

}
