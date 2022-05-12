package com.reactivespring.controller;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.repository.MovieInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class MoviesInfoControllerTest {

    @Autowired
    MovieInfoRepository movieInfoRepository;

    @Autowired
    WebTestClient webTestClient;
    static String MOVIES_INFO_URL = "/v1/movieinfos";

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

    }

    @Test
    void addMovieInfo() {
        //Given
        var movieInfo = new MovieInfo(null, "Batman Begins1", 2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));


        //When
        webTestClient.post()
                .uri(MOVIES_INFO_URL)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus().isCreated()
        .expectBody(MovieInfo.class)
        .consumeWith(movieInfoEntityExchangeResult -> {
            var savedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
            assert savedMovieInfo != null;
            assert savedMovieInfo.getMovieInfoId() != null;
        });

        //Then
    }

    @Test
    void getAllMovieInfos() {
        //Given

        //When
        webTestClient.get()
                .uri(MOVIES_INFO_URL)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .hasSize(3);

        //Then

    }


    @Test
    void getMovieInfoById() {
        //Given
        String movieInfoId = "abc";

        webTestClient.get()
                .uri(MOVIES_INFO_URL +"/{id}", movieInfoId)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Dark Knight Rises")
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var movieInfo = movieInfoEntityExchangeResult.getResponseBody();
                    assertNotNull(movieInfo);
                });

    }


    @Test
    void updateMovieInfo() {
        //Given
        var movieInfo = new MovieInfo(null, "new movie name", 1990, List.of("asdasdasd Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));
        String movieInfoId = "abc";

        //When
        webTestClient.put()
                .uri(MOVIES_INFO_URL +"/{id}", movieInfoId)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var updatedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
                    assert updatedMovieInfo != null;
                    assert updatedMovieInfo.getMovieInfoId() != null;
                    assertEquals("new movie name", updatedMovieInfo.getName());
                });

        //Then
    }

    @Test
    void deleteMovieInfo() {

        //Given
        String movieInfoId = "to_delete";
        var movieInfo = new MovieInfo(movieInfoId, "new movie name", 1990, List.of("asdasdasd Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));


        //When
        webTestClient.delete()
                .uri(MOVIES_INFO_URL +"/{id}", movieInfoId)
                .exchange()
                .expectStatus().isNoContent();

        //Then

    }
}