package com.movie.service.util;

import java.time.Duration;

import com.movie.service.exception.MoviesInfoServerException;
import com.movie.service.exception.ReviewsServerException;
import reactor.core.Exceptions;
import reactor.util.retry.Retry;

public class RetryUtil {

    public static Retry retrySpec() {

        return Retry.fixedDelay(3, Duration.ofSeconds(2))
                .filter(ex -> ex instanceof MoviesInfoServerException
                        || ex instanceof ReviewsServerException)
                .onRetryExhaustedThrow((retryBackoffSpec,
                                        retrySignal) -> Exceptions
                        .propagate(retrySignal.failure()));

    }
}