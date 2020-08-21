package com.example.cache;


import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

@Component
public class RedisCache<K,V> implements Cache<K,V> {

    RedisTemplate redisTemplate;
    private String cacheName;

    public RedisCache() {
    }

    public RedisCache(RedisTemplate redisTemplate, String chachename) {
        this.redisTemplate = redisTemplate;
        this.cacheName = chachename;
    }

    @Override
    public V get(K k) throws CacheException {
        System.out.println("从缓存中读取数据" + k);
        return (V)redisTemplate.opsForHash().get(this.cacheName,k.toString());
    }

    @Override
    public V put(K k, V v) throws CacheException {
        System.out.println(k + " " + v);
        redisTemplate.opsForHash().put(this.cacheName,k.toString(),v);
        return v;
    }

    @Override
    public V remove(K k) throws CacheException {
        return null;
    }

    @Override
    public void clear() throws CacheException {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set<K> keys() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }


}
