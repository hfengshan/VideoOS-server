package com.videojj.videoservice.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * <p>java方式：caffeine缓存配置</p>
 *
 */
@Configuration
@EnableCaching
public class CaffeineConfig {

    private static final int DEFAULT_MAXSIZE = 1000;
    private static final int DEFAULT_TTL = 300;

    /**
     * 个性化配置缓存  缓存5分钟，超过5分钟，就去请求redis缓存
     */
    @Bean
    public CacheManager cacheManager1() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();

        ArrayList<CaffeineCache> cachelist = new ArrayList<>();
        /**将目前所有的cache的名称，放入到缓存管理器里面*/
        for (Caches c : Caches.values()) {
            cachelist.add(new CaffeineCache(c.name(),
                    Caffeine.newBuilder().recordStats()
                            .expireAfterWrite(c.getTtl(), TimeUnit.SECONDS)
                            .maximumSize(c.getMaxSize())
                            .build())
            );
        }
        simpleCacheManager.setCaches(cachelist);
        return simpleCacheManager;
    }

    /**
     * 定义cache名称、超时时长秒、最大个数
     * 每个cache缺省3600秒过期，最大个数1000
     */
    public enum Caches {

        plan;

        private int maxSize = DEFAULT_MAXSIZE;    //最大數量
        private int ttl = DEFAULT_TTL;        //过期时间（秒）

        Caches() {
        }

        Caches(int ttl) {
            this.ttl = ttl;
        }

        Caches(int ttl, int maxSize) {
            this.ttl = ttl;
            this.maxSize = maxSize;
        }

        public int getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(int maxSize) {
            this.maxSize = maxSize;
        }

        public int getTtl() {
            return ttl;
        }

        public void setTtl(int ttl) {
            this.ttl = ttl;
        }
    }

}
