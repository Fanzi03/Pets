package org.example.cache;

import java.util.function.Supplier;

public interface CacheCheckService {
   
    CacheService getCacheService();

    default <T> T getFromCacheOrCompute(String cacheKey, Class<T> clazz, Supplier<T> supplier, int ttl){
        T cached = (T) getCacheService().get(cacheKey, clazz);
        if(cached != null) return cached;

        T result = (T) supplier.get();
        getCacheService().put(cacheKey, result, ttl);
        return result;
    }

    default <T> T getFromCacheOrCompute(String cacheKey, Class<T> clazz, Supplier<T> supplier){
        return getFromCacheOrCompute(cacheKey, clazz, supplier, 3600);
    }
}
