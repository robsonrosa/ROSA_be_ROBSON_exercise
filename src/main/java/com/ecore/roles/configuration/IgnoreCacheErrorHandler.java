package com.ecore.roles.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class IgnoreCacheErrorHandler implements CacheErrorHandler {

    @Override
    public void handleCacheGetError(final RuntimeException exception, final Cache cache, final Object key) {
        log.error("handleCacheGetError for key " + key);
    }

    @Override
    public void handleCachePutError(
            final RuntimeException exception,
            final Cache cache,
            final Object key,
            final Object value) {
        log.error("handleCachePutError for key " + key);
    }

    @Override
    public void handleCacheEvictError(final RuntimeException exception, final Cache cache, final Object key) {
        log.error("handleCacheEvictError for key " + key);
    }

    @Override
    public void handleCacheClearError(final RuntimeException exception, final Cache cache) {
        log.error("handleCacheClearError for cache " + cache);
    }
}
