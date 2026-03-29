package org.lltopk.distributeCache.twoStage.cache;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface ILocalCacheAccess<K,V> {

    V get(K k) throws ExecutionException;

    void setCache(K k,V v);

    void removeLocalKey(K k);

    void refresh();

    int size();

    boolean containsKey(K key);

    Map<K,V> entries();

}
