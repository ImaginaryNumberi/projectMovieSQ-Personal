package com.ixtx.projectmoviesq.services;

import com.ixtx.projectmoviesq.dtos.MovieDto;
import com.ixtx.projectmoviesq.entities.MovieEntity;
import com.ixtx.projectmoviesq.entities.MovieImageEntity;
import com.ixtx.projectmoviesq.mappers.MovieMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {
    private final MovieMapper movieMapper;

    @Autowired
    public MovieService(MovieMapper movieMapper) {
        this.movieMapper = movieMapper;
    }

    public MovieDto[] getMovies(String sort) {
        MovieDto[] movies = this.movieMapper.selectMovies(sort);
        return movies;
    }

    public MovieImageEntity getImage(int index, boolean isPoster) {
        // 스틸컷 이미지 가지고 올 때 저장된 스틸컷이 없으면 포스터라도 가져올 수 있도록 함
        MovieImageEntity movieImage = this.movieMapper.selectMovieImage(index, isPoster);
        if (movieImage != null)
            return movieImage;
        else
            return this.movieMapper.selectMovieImage(index, true);
    }

    public MovieDto getMovieByIndex(int index) {
        return this.movieMapper.selectMovieByIndex(index);
    }

    // 상영중, 상영예정인 영화 예매율순 TOP 10 가져오기
    public List<MovieDto> getMoviesByReservationRate() {
        MovieDto[] movies = this.movieMapper.selectTopTenMovies();
        List<MovieDto> movieDtoList = new ArrayList<>();
        for (MovieDto movie : movies) {
            MovieDto movieDto = new MovieDto();
            movieDto.setIndex(movie.getIndex())
                    .setTitleKo(movie.getTitleKo())
                    .setTitleEn(movie.getTitleEn())
                    .setRating(movie.getRating())
                    .setTrailerUrl(movie.getTrailerUrl())
                    .setImageData(this.getImage(movie.getIndex(), true)
                            .getImageData())
                    .setReservationRate(movie.getReservationRate());
            movieDtoList.add(movieDto);
        }
        return movieDtoList;
    }

    public String getMovieUrl(@Param("index") int index) {
        return this.movieMapper.selectMovieByIndex(index).getTrailerUrl();
    }

}