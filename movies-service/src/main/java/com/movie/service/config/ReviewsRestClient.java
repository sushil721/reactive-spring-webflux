package com.movie.service.config;

import com.movie.service.domain.Review;
import com.movie.service.exception.ReviewsClientException;
import com.movie.service.exception.ReviewsServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ReviewsRestClient {

    private WebClient webClient;

    @Value("${restClient.reviewsUrl}")
    private String moviesInfoUrl;

    public ReviewsRestClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<Review> retrivesReviews(String movieId){

        var url = UriComponentsBuilder.fromUriString(moviesInfoUrl)
                .queryParam("movieInfoId", movieId)
                .buildAndExpand().toUri();

        return webClient
                .get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    log.info("Status code is : {} ", clientResponse.statusCode().value());
                    if(clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {

                        return Mono.empty();
                    }
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(responseMessage -> Mono.just(new ReviewsClientException(
                                    responseMessage))).flatMap(Mono::error);
                })
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                    log.info("Status code is : {} ", clientResponse.statusCode().value());


                    return clientResponse.bodyToMono(String.class)
                            .flatMap(responseMessage -> Mono.just(new ReviewsServerException(
                                    "Server Exception in ReviewsService : "+ responseMessage
                            ))).flatMap(Mono::error);
                })
                .bodyToFlux(Review.class);

    }

}
