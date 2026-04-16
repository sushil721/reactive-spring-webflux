package com.movie.info.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MovieInfoRedisCacheConfig {

    public static final String MOVIE_INFO_CACHE_MANAGER = "movieInfoCacheManager";
    public static final String MOVIE_INFO_CACHE_CONFIGURATION = "movieInfoCacheConfiguration";

    @Value("${spring.redis.cache-expire-time.cache-manager.movies-info-configuration}")
    private String getMovieInfoCacheExpiryTime;

    @Primary
    @Bean(name = MOVIE_INFO_CACHE_MANAGER)
    public RedisCacheManager configureMovieInfoCacheManager(LettuceConnectionFactory lettuceConnectionFactory) {
        Map<String, RedisCacheConfiguration> cacheConfigurationsMap = new HashMap<>();

        cacheConfigurationsMap.put(MOVIE_INFO_CACHE_CONFIGURATION,
                RedisCacheConfiguration
                        .defaultCacheConfig()
                        .disableCachingNullValues()
                        .entryTtl(Duration.ofMinutes(Long.parseLong(getMovieInfoCacheExpiryTime))));

        return RedisCacheManager
                .RedisCacheManagerBuilder
                .fromConnectionFactory(lettuceConnectionFactory)
                .withInitialCacheConfigurations(cacheConfigurationsMap)
                .build();
    }

}
