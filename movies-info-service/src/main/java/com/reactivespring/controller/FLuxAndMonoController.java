package com.reactivespring.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class FLuxAndMonoController {

    @GetMapping(value = "/flux")
    public Flux<Integer> flux() {
        return Flux.just(1, 2, 3)
                .delayElements(Duration.ofMillis(1000))
                .log();
    }

    @GetMapping(value = "/mono")
    public Mono<String> felloWorldMono() {
        return Mono.just("Hello, World!")
                .log();
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Long> stream() {
        return Flux.interval(Duration.ofSeconds(1))
                .log();
    }



}
