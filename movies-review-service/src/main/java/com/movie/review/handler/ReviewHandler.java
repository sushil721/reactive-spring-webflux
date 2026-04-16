package com.movie.review.handler;

import com.movie.review.domain.Review;
import com.movie.review.exception.ReviewDataException;
import com.movie.review.exception.ReviewNotFoundException;
import com.movie.review.repository.ReviewReactiveRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.stream.Collectors;

@Component
@Slf4j
public class ReviewHandler {

    @Autowired
    private ReviewReactiveRepository repository;

    @Autowired
    private Validator validarot;

    Sinks.Many<Review> reviewSink = Sinks.many()
            .replay()
            .latest();

    public Mono<ServerResponse> addReview(ServerRequest request) {

		/* 1. Using Lambda Expression
		return request.bodyToMono(Review.class)
				.flatMap(review -> {
					return repository.save(review);
				})
				.flatMap(saveReview -> {
					return ServerResponse.status(HttpStatus.CREATED)
						.bodyValue(saveReview);
				});
		*/
        //2. Using Method Reference
		/*return request.bodyToMono(Review.class)
				.doOnNext(this::validate)
				.flatMap(repository::save)
				.flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue);
		*/
        //3. Using Sink to subscribe new added reviews
        return request.bodyToMono(Review.class)
                .doOnNext(this::validate)
                .flatMap(repository::save)
                .doOnNext(review -> {
                    reviewSink.tryEmitNext(review);
                })
                .flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue);

    }

    private void validate(Review review) {
        var constraintsViolations = validarot.validate(review);
        log.info("constraintsViolations : {}", constraintsViolations);
        if(constraintsViolations.size() > 0) {
            var errorMessage = constraintsViolations.stream()
                    .map(ConstraintViolation::getMessage)
                    .sorted()
                    .collect(Collectors.joining(","));
            log.info("errorMessage : {} ", errorMessage);
            throw new ReviewDataException(errorMessage);
        }
    }

    public Mono<ServerResponse> getReviews(ServerRequest request) {

        var movieInfoId = request.queryParam("movieInfoId");

        if(movieInfoId.isPresent()) {

            var reviewFlux = repository.findReviewsByMovieInfoId(Long.valueOf(movieInfoId.get()));
            return buildReviewResponse(reviewFlux);
        }else {
            var reviewFlux = repository.findAll();
            return buildReviewResponse(reviewFlux);
        }
    }

    private Mono<ServerResponse> buildReviewResponse(Flux<Review> reviewFlux) {
        return ServerResponse.ok().body(reviewFlux, Review.class);
    }


    public Flux<ServerResponse> getReviews2(ServerRequest request) {
        /* 1. Using Method Reference */
        return repository.findAll()
                .flatMap(ServerResponse.status(HttpStatus.OK)::bodyValue);
    }

    public Mono<ServerResponse> updateReviews(ServerRequest request) {
        // TODO Auto-generated method stub
        var reviewId = request.pathVariable("id");
        var existingReview = repository.findById(reviewId)
				  // Approach-1 to return not found by ID
					.switchIfEmpty(Mono.error(new ReviewNotFoundException("Review not found for the given review id : "+reviewId)))
        ;

        return existingReview.flatMap(review -> request.bodyToMono(Review.class)
                .map(requestReview -> {
                    review.setComment(requestReview.getComment());
                    review.setRating(requestReview.getRating());
                    return review;
                })
                .flatMap(repository::save)
                .flatMap(savedReview -> ServerResponse.ok().bodyValue(savedReview)))
                // Approach-2 to return not found by ID
                //.switchIfEmpty(ServerResponse.notFound().build())
                ;
    }

    public Mono<ServerResponse> deleteReviews(ServerRequest request) {
        var reviewId = request.pathVariable("id");
        var existingReview = repository.findById(reviewId);

        return existingReview
                .flatMap(review -> repository.deleteById(reviewId)
                        .then(ServerResponse.noContent()
                                .build()));

    }

    public Mono<ServerResponse> getReviewsById(ServerRequest request) {
        // TODO Auto-generated method stub
        var reviewId = request.pathVariable("id");
        var existingReview = repository.findById(reviewId);
        return ServerResponse.ok().body(existingReview, Review.class);
    }

    public Mono<ServerResponse> getReviewsStream(ServerRequest request) {

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_NDJSON)
                .body(reviewSink.asFlux(), Review.class)
                .log()
                ;
    }

}
