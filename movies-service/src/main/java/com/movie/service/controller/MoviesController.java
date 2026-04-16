package com.movie.service.controller;

import com.movie.service.client.MovieInfoRestClient;
import com.movie.service.config.ReviewsRestClient;
import com.movie.service.domain.Movie;
import com.movie.service.domain.MovieInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/movies")
public class MoviesController {

    private MovieInfoRestClient movieInfoRestClient;
    private ReviewsRestClient reviewsRestClient;


    public MoviesController(MovieInfoRestClient movieInfoRestClient,
                            ReviewsRestClient reviewsRestClient) {

        this.movieInfoRestClient = movieInfoRestClient;
        this.reviewsRestClient = reviewsRestClient;
    }



    @GetMapping("/{id}")
    public Mono<Movie> retriveMovieById(@PathVariable("id") String movieId){
        return movieInfoRestClient.retriveMovieInfo(movieId)
                .flatMap(movieInfo -> {
                    var reviewListMono = reviewsRestClient.retrivesReviews(movieId)
                            .collectList();
                    return reviewListMono.map(reviews -> new Movie(movieInfo, reviews));
                });
    }

    @GetMapping(value = "/stream", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<MovieInfo> retriveMovieStram(){
        return movieInfoRestClient.retriveMovieInfoStream();
    }

}
