package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class FluxAndMonoGeneratorService {

    public static final String EXAMPLE = "abc";

    public static void main(String[] args) throws Exception {
        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
        fluxAndMonoGeneratorService.namesFlux().subscribe(name -> {
            System.out.println("Flux name is " + name);
        });

        fluxAndMonoGeneratorService.nameMono().subscribe(name -> {
            System.out.println("Mono name is " + name);
        });

        fluxAndMonoGeneratorService.explore_merge().subscribe(element -> {
            System.out.println("Element: " + element);
        });

        Thread.sleep(2000);

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

    //Mono FlatMap: uno mono que devuelve otro mono: Se usa cuando la transformación debe ser non blocking, por ejemplo, va a una BD, a un servicio externo o algo
    public Mono<List<String>> namesMono_flatMap(int stringLength) {

        return Mono.just("alex")
                .map(String::toUpperCase)               //Converting to uppercase
                .filter(s -> s.length() > stringLength) //Fill
                .flatMap(this::splitStringMono)
                .log(); //Moni<Lit of A, L, E, X>
    }

    //Mono flatToManu: Cuando un mono debe convertirse en un Flux
    public Flux<String> namesMono_flatMapMany(int stringLength) {

        return Mono.just("alex")
                .map(String::toUpperCase)               //Converting to uppercase
                .filter(s -> s.length() > stringLength) //Fill
                .flatMapMany(this::splitString)
                .log();
    }

    private Mono<List<String>> splitStringMono(String s) {

        String[] charArray = s.split(""); //Divido arreglo de strings por cada caracter
        List<String> charList = List.of(charArray); //Creo una lista
        return Mono.just(charList); //Creo un mono con la lista
    }

    public Flux<String> namesFlux_flatmap(int stringLenght) {

        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)               //Converting to uppercase
                .filter(s -> s.length() > stringLenght) //Filtering
                .flatMap(name -> splitString(name))// ALEX, CHLOE -> A, L, E, X, C, H, L, O , E
                .log();
    }

    //Transform es para guardar en una variable multiples funciones y pasarla como argumento
    //SE usa defaultIfEmpty cuando por alguna razón queremos que el flux o mono envíe un valor por defecto VACIO si está vacío
    public Flux<String> namesFlux_transform(int stringLenght) {

        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                .filter( s -> s.length() > stringLenght);

        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filterMap)
                .flatMap(name -> splitString(name))// ALEX, CHLOE -> A, L, E, X, C, H, L, O , E
                .defaultIfEmpty("default") //Si no está presente el Next no es llamado sino que se llama directamnete complete.
                .log();
    }
    //SE usa switchIfEmpty cuando por alguna razón queremos que el flux o mono envíe un valor por defecto tipo Flux o Mono si está vacío
    public Flux<String> namesFlux_transform_switchIfEmpty(int stringLenght) {

        Function<Flux<String>, Flux<String>> filterMap = name ->
                name.map(String::toUpperCase)
                .filter( s -> s.length() > stringLenght)
                .flatMap(s -> splitString(s));

        Flux<String> defaultFlux = Flux.just("default")
                .transform(filterMap); // "D", "E", "F", "A", "U", "L", "T"

        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filterMap)
                .switchIfEmpty(defaultFlux) //Si no está presente el Next no es llamado sino que se llama directamnete complete.
                .log();
    }



    //Importante entender que cuando el orden importa, NO se debe ussar flatMap, porque esta altera el orden d
    public Flux<String> namesFlux_flatmap_async(int stringLenght) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)               //Converting to uppercase
                .filter(s -> s.length() > stringLenght) //Filtering
                .flatMap(name -> splitString_withDelay(name))// ALEX, CHLOE -> A, L, E, X, C, H, L, O , E
                .log();
    }

    //Es como concatMap pero respeta el orden
    public Flux<String> namesFlux_concatmap(int stringLenght) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)               //Converting to uppercase
                .filter(s -> s.length() > stringLenght) //Filtering
                .concatMap(name -> splitString_withDelay(name))// ALEX, CHLOE -> A, L, E, X, C, H, L, O , E
                .log();
    }

    //ALEX -> Flux (A, L, E, X)
    public Flux<String> splitString(String name) {
        final String[] charArray = name.split("");
        return Flux.fromArray(charArray);
    }

    //ALEX -> Flux (A, L, E, X)
    public Flux<String> splitString_withDelay(String name) {
        final String[] charArray = name.split("");
        int delay = new Random().nextInt(1000);
        return Flux.fromArray(charArray).delayElements(Duration.ofMillis(delay));
    }


    //Concatenar dos FLux de forma secuencial, primero se suscribe a uno y cuando ese termina se suscribe al otro
    public Flux<String> explore_concat() {
        Flux<String> abcFlux = Flux.just("A", "B", "C");

        Flux<String> defFlux = Flux.just("D", "E", "F");

        return Flux.concat(abcFlux, defFlux)
                .log();

    }

    public Flux<String> explore_concatWith() {
        Flux<String> abcFlux = Flux.just("A", "B", "C");

        Flux<String> defFlux = Flux.just("D", "E", "F");

        return abcFlux.concatWith(defFlux)
                .log();
    }
    public Flux<String> explore_concatWith_Mono() {
        Mono<String> aMono = Mono.just("A");

        Mono<String> bMono = Mono.just("B");

        return aMono.concatWith(bMono)
                .log();

    }

    //Se subscribe a ambos Publishers simultaneamente  y los va devolviendo de forma intercalada (uno a la vez, el que venta, sin respetar orden)
    // Si quisieras respetar el orden deberías usar mergeWithSequential
    public Flux<String> explore_merge() {

        Flux<String> abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100)); //A, B, C

        Flux<String> defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125)); //D, E, F


        return Flux.merge(abcFlux, defFlux)
                .log();

    }

    public Flux<String> explore_mergeWith() {

        Flux<String> abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100)); //A, B, C

        Flux<String> defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125)); //D, E, F


        return abcFlux.mergeWith(defFlux).log();

    }

    public Flux<String> explore_mergeWith_Mono() {

        Mono<String> aMono = Mono.just("A");

        Mono<String> bMono = Mono.just("B");

        return aMono.mergeWith(bMono).log();

    }


}
