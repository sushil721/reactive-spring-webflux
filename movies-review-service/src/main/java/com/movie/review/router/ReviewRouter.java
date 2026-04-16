package com.movie.review.router;

import com.movie.review.handler.ReviewHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@Slf4j
public class ReviewRouter {

    @Bean
    public RouterFunction<ServerResponse> reviewsRoute(ReviewHandler reviewHandler){
        log.info("In reviewsRoute");

//nest router
        return route()  //RouterFunctions.route()
                .nest(RequestPredicates.path("/v1/reviews"), builder -> {
                    builder
                            .POST("", request -> reviewHandler.addReview(request))
                            .GET("", request -> reviewHandler.getReviews(request))
                            .GET("/{id}", request -> reviewHandler.getReviewsById(request))
                            .PUT("/{id}",  request -> reviewHandler.updateReviews(request))
                            .DELETE("/{id}",  request -> reviewHandler.deleteReviews(request))
                            .GET("/stream",  request -> reviewHandler.getReviewsStream(request));
                })
                .GET("/v1/helloworld", (request -> ServerResponse.ok().bodyValue("Hello World")))
                .build();

/* // Simple router
        return route()  //RouterFunctions.route()
            .GET("/v1/helloworld", (request -> ServerResponse.ok().bodyValue("Hello World")))
            .POST("/v1/reviews", request -> reviewHandler.addReview(request))
            .GET("/v1/reviews", request -> reviewHandler.getReviews(request))
        .build();
*/
    }

}
