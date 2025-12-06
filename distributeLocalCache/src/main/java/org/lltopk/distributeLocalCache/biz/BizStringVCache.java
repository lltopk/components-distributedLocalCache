package org.lltopk.distributeLocalCache.biz;

import lombok.extern.slf4j.Slf4j;
import org.lltopk.distributeLocalCache.cache.AbstractGuavaCache;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BizStringVCache extends AbstractGuavaCache<String,String> {

    @Override
    protected java.lang.String name() {
        return "BizStringCache";
    }

    @Override
    public String topic() {
        return "BizStringCache";
    }

    @Override
    protected String loadIfNot(String key) {
        return key.toUpperCase();//模拟数据加载
    }

}
