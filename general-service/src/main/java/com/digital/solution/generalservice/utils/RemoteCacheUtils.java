//package com.digital.solution.generalservice.utils;
//
//import lombok.extern.slf4j.Slf4j;
//import org.infinispan.client.hotrod.RemoteCache;
//import org.infinispan.client.hotrod.RemoteCacheManager;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.TimeUnit;
//
//@Slf4j
//@Component
//public class RemoteCacheUtils {
//
//    @Autowired
//    private RemoteCacheManager remoteCacheManager;
//
//    private static final long DEFAULT_LIFESPAN = -777L; // This is for marker only, don't use it outside this class
//    public static final long UNLIMITED_LIFESPAN = -1L;
//    public static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MINUTES;
//
//    public <K, V> V getCache(String cacheName, K key) {
//        log.info("GetCache name: [{}] and key: [{}]", cacheName, key);
//
//        RemoteCache<K, V> remoteCache = getRemoteCache(cacheName);
//        if (remoteCache != null && key != null) {
//            V value = remoteCache.get(key);
//            log.info("Cache value: [{}]", value);
//            return value;
//        }
//
//        return null;
//    }
//
//    public <K, V> RemoteCache<K, V> getRemoteCache(String cacheName) {
//        return remoteCacheManager.getCache(cacheName);
//    }
//
//    public <K, V> boolean storeCache(String cacheName, K key, V value) {
//        return storeCache(cacheName, key, value, DEFAULT_LIFESPAN, DEFAULT_TIME_UNIT);
//    }
//
//    public <K, V> boolean storeCache(String cacheName, K key, V value, long lifespan, TimeUnit unit) {
//        boolean result = false;
//        RemoteCache<K, V> remoteCache = getRemoteCache(cacheName);
//        if (remoteCache != null && key != null) {
//            V existingValue = remoteCache.get(key);
//            if (existingValue == null) {
//                if (lifespan == DEFAULT_LIFESPAN) {
//                    remoteCache.put(key, value);
//                } else {
//                    remoteCache.put(key, value, lifespan, unit);
//                }
//
//                result = true;
//                log.info("Success store new cache {}: key {}, data {}", cacheName, key, value);
//            } else {
//                if (lifespan <= 0) {
//                    remoteCache.replace(key, value);
//                } else {
//                    remoteCache.replace(key, value, lifespan, unit);
//                }
//
//                result = true;
//                log.info("Success replace cache {}: key {}, data {}", cacheName, key, value);
//            }
//        }
//
//        return result;
//    }
//
//    public <K> boolean removeCache(String cacheName, K key) {
//        log.info("Trying to remove cache with key: " + key);
//        var cache = getRemoteCache(cacheName);
//        if (cache != null) {
//            cache.remove(key);
//        }
//
//        log.info("Success removing cache {}[{}]: ", cacheName, key);
//        return true;
//    }
//}
