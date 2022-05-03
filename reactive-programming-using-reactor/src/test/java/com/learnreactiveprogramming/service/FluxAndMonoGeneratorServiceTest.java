package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService fluxAndMonoGeneratorService =
            new FluxAndMonoGeneratorService();

    @Test
    void namesFlux() {
        //Given

        //When
        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux();

        //Then
        StepVerifier.create(namesFlux)
                //.expectNext("alex", "ben", "chloe")
                //.expectNextCount(3)
                .expectNext("alex")
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void namesFlux_map() {
        //Given
        int stringLenght = 3;

        //When
        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux_map(stringLenght);

        //Then
        StepVerifier.create(namesFlux)
                .expectNext("4 - ALEX", "5 - CHLOE")
                .verifyComplete();

    }

    @Test
    void namesFlux_inmutability() {
        //Given

        //When
        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux_inmutability();


        //Then
        StepVerifier.create(namesFlux)
                .expectNext("alex", "ben", "chloe")
                .verifyComplete();
    }
}