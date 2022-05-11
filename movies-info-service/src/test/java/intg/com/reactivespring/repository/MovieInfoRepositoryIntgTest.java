package com.reactivespring.repository;

import com.reactivespring.domain.MovieInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import org.junit.jupiter.api.Assertions;
import java.time.LocalDate;
import java.util.List;

@DataMongoTest
@ActiveProfiles("test")
//@SpringBootTest
class MovieInfoRepositoryIntgTest {

    @Autowired
    MovieInfoRepository movieInfoRepository;

    @BeforeEach
    void setUp() {
        var movieinfos = List.of(
                new MovieInfo(null, "Batman Begins", 2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight", 2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises", 2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        movieInfoRepository.saveAll(movieinfos).blockLast();
    }

    @AfterEach
    void tearDown() {
        movieInfoRepository.deleteAll().block();
    }

    @Test
    void findAll() {
        //Given

        //When
        Flux<MovieInfo> movieInfoFlux = movieInfoRepository.findAll();

        //Then
        StepVerifier.create(movieInfoFlux).expectNextCount(3).verifyComplete();

    }

    @Test
    void findById() {
        //Given

        //When
        Mono<MovieInfo> movieInfoFlux = movieInfoRepository.findById("abc");

        //Then
        StepVerifier.create(movieInfoFlux)
                //.expectNextCount(1)
                .assertNext(movieInfo -> {
                    Assertions.assertEquals("Dark Knight Rises", movieInfo.getName());
                })
                .verifyComplete();

    }

    @Test
    void saveMovieInfo() {
        //Given
        MovieInfo movieInfo = new MovieInfo(null, "Batman Begins1", 2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        //When
        Mono<MovieInfo> movieInfoFlux = movieInfoRepository.save(movieInfo).log();

        //Then
        StepVerifier.create(movieInfoFlux)
                //.expectNextCount(1)
                .assertNext(movieInfo1 -> {
                    Assertions.assertNotNull(movieInfo1.getMovieInfoId());
                    Assertions.assertEquals("Batman Begins1", movieInfo.getName());
                })
                .verifyComplete();

    }

    @Test
    void updateMovieInfo() {
        //Given
        MovieInfo movieInfo = movieInfoRepository.findById("abc").block();
        movieInfo.setYear(2021);

        //When
        Mono<MovieInfo> movieInfoFlux = movieInfoRepository.save(movieInfo).log();

        //Then
        StepVerifier.create(movieInfoFlux)
                //.expectNextCount(1)
                .assertNext(movieInfo1 -> {
                    Assertions.assertNotNull(movieInfo1.getMovieInfoId());
                    Assertions.assertEquals(2021, movieInfo.getYear());
                })
                .verifyComplete();

    }

    @Test
    void deleteMovieInfo() {
        //Given

        //When
        movieInfoRepository
                .deleteById("abc")
                .block();

        var movieInfoFlux = movieInfoRepository.findAll().log();

        //Then
        StepVerifier.create(movieInfoFlux)
                .expectNextCount(2)
                .verifyComplete();

    }


}