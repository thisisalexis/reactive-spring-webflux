package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    void namesFlux() {
        //Given

        //When
        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux();

        //Then
        StepVerifier.create(namesFlux)
                //.expectNext("alex", "ben", "chloe")
                //.expectNextCount(3)
                .expectNext("alex").expectNextCount(2).verifyComplete();
    }

    @Test
    void namesFlux_map() {
        //Given
        int stringLenght = 3;

        //When
        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux_map(stringLenght);

        //Then
        StepVerifier.create(namesFlux).expectNext("4 - ALEX", "5 - CHLOE").verifyComplete();

    }

    @Test
    void namesFlux_inmutability() {
        //Given

        //When
        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux_inmutability();


        //Then
        StepVerifier.create(namesFlux).expectNext("alex", "ben", "chloe").verifyComplete();
    }

    @Test
    void namesFlux_flatmap() {

        //Given
        int stringLenght = 3;

        //When
        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux_flatmap(stringLenght);

        //Then
        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();

    }

    @Test
    void namesFlux_flatmap_async() {

        //Given
        int stringLenght = 3;

        //When
        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux_flatmap_async(stringLenght);

        //Then
        StepVerifier.create(namesFlux).expectNextCount(9).verifyComplete();

    }

    @Test
    void namesFlux_concatmap() {

        //Given
        int stringLenght = 3;

        //When
        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux_concatmap(stringLenght);

        //Then
        StepVerifier.create(namesFlux)
                //.expectNextCount(9)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();

    }

    @Test
    void namesMono_flatMap() {
        //given
        int stringLenght = 3;

        //when
        Mono<List<String>> value = fluxAndMonoGeneratorService.namesMono_flatMap(stringLenght);

        //then
        StepVerifier.create(value)
                .expectNext(List.of("A", "L", "E", "X"))
                .verifyComplete();

    }

    @Test
    void namesMono_flatMapMany() {
        //given
        int stringLenght = 3;

        //when
        Flux<String> value = fluxAndMonoGeneratorService.namesMono_flatMapMany(stringLenght);

        //then
        StepVerifier.create(value)
                .expectNext("A", "L", "E", "X")
                .verifyComplete();

    }

    @Test
    void namesFlux_transform() {

        //Given
        int stringLenght = 3;

        //When
        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux_transform(stringLenght);

        //Then
        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();

    }

    @Test
    void namesFlux_transform_1() {

        //Given
        int stringLenght = 6; //Ningún nombre cumple la condición así que volverá un Flux vacío Empty

        //When
        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux_transform(stringLenght);

        //Then
        StepVerifier.create(namesFlux)
                .expectNext("default")
                .verifyComplete();

    }

    @Test
    void namesFlux_transform_switchIfEmpty() {

        //Given
        int stringLenght = 6; //Ningún nombre cumple la condición así que volverá un Flux vacío Empty

        //When
        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux_transform_switchIfEmpty(stringLenght);

        //Then
        StepVerifier.create(namesFlux)
                .expectNext("D", "E", "F", "A", "U", "L", "T")
                .verifyComplete();

    }

    @Test
    void main() {
    }

    @Test
    void nameMono() {
    }



    @Test
    void splitString() {
    }

    @Test
    void splitString_withDelay() {
    }

    @Test
    void explore_concat() {

        //given

        //when
        Flux<String> result = fluxAndMonoGeneratorService.explore_concat();

        //then
        StepVerifier.create(result)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();

    }

    @Test
    void explore_concatWith() {

        //given

        //when
        Flux<String> result = fluxAndMonoGeneratorService.explore_concatWith();

        //then
        StepVerifier.create(result)
                .expectNext("A", "B")
                .verifyComplete();

    }

    @Test
    void explore_merge() {

        //given

        //when
        Flux<String> result = fluxAndMonoGeneratorService.explore_merge();

        //then
        StepVerifier.create(result)
                .expectNext("A", "D", "B", "E", "C", "F")
                .verifyComplete();

    }


}