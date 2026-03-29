package org.lltopk.distributeLocalCache.cache;

import com.google.common.cache.CacheStats;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.lltopk.distributeLocalCache.biz.BizListVCache;
import org.lltopk.distributeLocalCache.biz.BizMapVCache;
import org.lltopk.distributeLocalCache.biz.BizStringVCache;
import org.lltopk.distributeLocalCache.model.po.BizPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.ExecutionException;

@SpringBootTest
@Slf4j
public class AbstractGuavaCacheTest {


    @Autowired
    BizStringVCache bizStringVCache;

    @Autowired
    BizListVCache bizListVCache;

    @Autowired
    private BizMapVCache bizMapVCache;

    @Test
    public void testStringCacheOperations() {

        try {
            String value1 = bizStringVCache.get("key1");
            String value2 = bizStringVCache.get("key2");
            log.info("key1 {} value1 {}","key1",value1);
            log.info("key2 {} value2 {}","key2",value2);
            bizStringVCache.removeKey("key1");
            log.info("entries {}", bizStringVCache.entries());
            bizStringVCache.get("key2");
            CacheStats stats = bizStringVCache.stats();
            log.info("stats {}",stats);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testListCacheOperations() {

        try {
            List<BizPo> value1 = bizListVCache.get("key1");
            List<BizPo> value2 = bizListVCache.get("key2");
            log.info("key1 {} value1 {}","key1",value1);
            log.info("key2 {} value2 {}","key2",value2);
            bizListVCache.removeKey("key1");
            log.info("entries {}", bizListVCache.entries());
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testMapCacheOperations() {
        log.info("remained bizMapVCacheEntries {}", bizMapVCache.entries());
    }
}
