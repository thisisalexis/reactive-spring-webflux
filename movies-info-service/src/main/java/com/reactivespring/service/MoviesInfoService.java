package com.reactivespring.service;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.repository.MovieInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MoviesInfoService {

    private final MovieInfoRepository movieInfoRepository;
    public Mono<MovieInfo> addMovieInfo(MovieInfo movieInfo) {

        return movieInfoRepository.save(movieInfo);

    }

    public Flux<MovieInfo> getAllMovieInfos() {

        return movieInfoRepository.findAll();

    }

    public Mono<MovieInfo> getMovieInfosById(String id) {
        return movieInfoRepository.findById(id);
    }

    public Mono<MovieInfo> updateMovieInfo(MovieInfo updatedMovieInfo, String id) {

        return movieInfoRepository.findById(id)
                .flatMap(movieInfo -> { //TODO IMPORTANTE
                    movieInfo.setCasts(updatedMovieInfo.getCasts());
                    movieInfo.setName(updatedMovieInfo.getName());
                    movieInfo.setRelease_date(updatedMovieInfo.getRelease_date());
                    movieInfo.setYear(updatedMovieInfo.getYear());
                    return movieInfoRepository.save(movieInfo);
                });

    }

    public Mono<Void> deleteMovieInfo(String id) {

        return movieInfoRepository.deleteById(id);

    }
}
