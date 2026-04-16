package com.movie.info.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class FluxMonoTestController {

    @GetMapping("/flux")
    public Flux<Integer> getFlux() {
       // return Flux.range(1, 5);
        return Flux
                .just(1,2,3,4,5)
                .log();
    }

    @GetMapping("/mono")
    public Mono<String> getMono() {
        return Mono
                .just("Hello, Mono!")
                .log();
    }

    @GetMapping("/stream")
    public Flux<Long> getStream() {
        return Flux
                .interval(Duration.ofSeconds(1))
                .log();
    }

    @GetMapping("/stream-delay")
    public Flux<Long> getFluxWithDelay() {
        return Flux
                .interval(Duration.ofSeconds(1))
                .take(5)
                .log();
    }
}
