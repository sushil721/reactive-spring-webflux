package com.movie.info.cache;

import com.movie.info.configs.MovieInfoRedisCacheConfig;
import com.movie.info.model.MovieInfo;
import com.movie.info.service.MovieInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class MovieInfoServiceCacheHandler {

    @Autowired
    private MovieInfoService movieInfoService;

    @Cacheable(value = MovieInfoRedisCacheConfig.MOVIE_INFO_CACHE_CONFIGURATION,
            key = "MOVIE_INFO.concat('_').concat(#id)",
            cacheManager = MovieInfoRedisCacheConfig.MOVIE_INFO_CACHE_MANAGER)
    public MovieInfo getMovieInfoById(String id) {
        System.err.println("In Handler: ");
        return movieInfoService.getMovieInfoByIdOne(id);
    }

}
