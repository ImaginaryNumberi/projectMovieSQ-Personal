package com.ixtx.projectmoviesq.mappers;

import com.ixtx.projectmoviesq.dtos.MovieDto;
import com.ixtx.projectmoviesq.entities.MovieEntity;
import com.ixtx.projectmoviesq.entities.MovieImageEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MovieMapper {
    // 영화 모두 가져오기
    MovieDto[] selectMovies(@Param("sort") String sort);

    // 영화 이미지 가져오기
    MovieImageEntity selectMovieImage(@Param("index") int index, @Param("isPoster") boolean isPoster);

    // 인덱스에 해당하는 영화 가져오기
    MovieDto selectMovieByIndex(@Param("index") int index);

    // 상영중, 상영예정 영화 가져오기
    MovieDto[] selectTopTenMovies();
}