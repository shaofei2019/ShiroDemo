package com.example.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


@Component
public class RedisCacheManager implements CacheManager {
    @Autowired
    RedisTemplate redisTemplate;

    /**
     *
     * @param chachename 认证或者授权缓存的统一名称
     * @param <K>
     * @param <V>
     * @return
     * @throws CacheException
     */
    @Override
    public <K, V> Cache<K, V> getCache(String chachename) throws CacheException {
        System.out.println(chachename);
        return new RedisCache<K,V>(redisTemplate,chachename);
    }
}
