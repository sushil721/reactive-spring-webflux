package com.movie.service.client;

import com.movie.service.domain.MovieInfo;
import com.movie.service.exception.MoviesInfoClientException;
import com.movie.service.exception.MoviesInfoServerException;
import com.movie.service.util.RetryUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class MovieInfoRestClient {

    private WebClient webClient;

    @Value("${restClient.moviesInfoUrl}")
    private String moviesInfoUrl;

    public MovieInfoRestClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<MovieInfo> retriveMovieInfo(String movieId){

        var url = moviesInfoUrl.concat("/{id}");

        return webClient
                .get()
                .uri(url, movieId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    log.info("Status code is : {} ", clientResponse.statusCode().value());
                    if(clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {

                        return Mono.error(new MoviesInfoClientException(
                                "There is no MovieInfo available for the passed in id : "+ movieId,
                                clientResponse.statusCode().value()));
                    }
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(responseMessage -> Mono.just(new MoviesInfoClientException(
                                    responseMessage, clientResponse.statusCode().value()
                            )))
                            .flatMap(Mono::error);

                })
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    log.info("Status code is : {} ", clientResponse.statusCode().value());
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(responseMessage -> Mono.just(new MoviesInfoServerException(
                                    "Server Exception in MovieInfoService : "+ responseMessage
                            )))
                            .flatMap(Mono::error);
                })
                .bodyToMono(MovieInfo.class)
                //.retry(3) //retry 3 attempt -> at a time all attempt
                .retryWhen(RetryUtil.retrySpec()) // retry after 2 seconds
                .log();
    }

    public Flux<MovieInfo> retriveMovieInfoStream() {
        var url = moviesInfoUrl.concat("/stream");

        return webClient
                .get()
                .uri(url)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), clientResponse -> {

                    return clientResponse.bodyToMono(String.class)
                            .flatMap(responseMessage -> Mono.just(new MoviesInfoClientException(
                                    responseMessage, clientResponse.statusCode().value()
                            )))
                            .flatMap(Mono::error);
                })
                .onStatus(status -> status.is5xxServerError(), clientResponse -> {
                    log.info("Status code is : {} ", clientResponse.statusCode().value());
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(responseMessage -> Mono.just(new MoviesInfoServerException(
                                    "Server Exception in MovieInfoService : "+ responseMessage
                            )))
                            .flatMap(Mono::error);
                })
                .bodyToFlux(MovieInfo.class)
                //.retry(3) //retry 3 attempt -> at a time all attempt
                .retryWhen(RetryUtil.retrySpec()) // retry after 2 seconds
				.log();
    }

}