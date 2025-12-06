package org.lltopk.distributeLocalCache.biz;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.lltopk.distributeLocalCache.cache.AbstractGuavaCache;
import org.lltopk.distributeLocalCache.entity.BizPo;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class BizListVCache extends AbstractGuavaCache<String, List<BizPo>> {

    @Override
    protected String name() {
        return "BizListCache";
    }

    @Override
    public String topic() {
        return "BizListCache";
    }

    @Override
    protected List<BizPo> loadIfNot(String key) {
        BizPo bizPo1 = new BizPo();
        bizPo1.setId(1l);
        bizPo1.setName(key);
        bizPo1.setAge(11);
        BizPo bizPo2 = new BizPo();
        bizPo2.setId(2l);
        bizPo2.setName(key);
        bizPo2.setAge(12);
        BizPo bizPo3 = new BizPo();
        bizPo3.setId(3l);
        bizPo3.setName(key);
        bizPo3.setAge(13);
        return Lists.newArrayList(bizPo1,bizPo2,bizPo3);
    }

}
