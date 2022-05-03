package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class FluxAndMonoGeneratorService {

    public static final String EXAMPLE = "abc";
    public static void main(String[] args) {
        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
        fluxAndMonoGeneratorService.namesFlux().subscribe( name -> {
            System.out.println("Flux name is " + name);
        } );

        fluxAndMonoGeneratorService.nameMono().subscribe(name -> {
            System.out.println("Mono name is " + name);
        });

    }

    public Flux<String> namesFlux() {
        List<String> names = List.of("alex", "ben", "chloe"); //Let's asume this comes from a BD or an external resource
        return Flux.fromIterable(names).log();
    }

    public Mono<String> nameMono() {
        return Mono.just("Aleix").log();
    }

    public Flux<String> namesFlux_map(int stringLenght) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)               //Converting to uppercase
                .filter(s -> s.length() > stringLenght) //Filtering
                .map(s -> s.length() + " - " + s) //Converting to 4 - Alexis, 5 - Chloe
                .log();
    }


    public Flux<String> namesFlux_inmutability() {
        Flux<String> namesFlux = Flux.fromIterable(List.of("alex", "ben", "chloe"));
        namesFlux().map(String::toUpperCase);
        return namesFlux;
    }

}
