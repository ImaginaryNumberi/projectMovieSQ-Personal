package com.ixtx.projectmoviesq.mappers;

import com.ixtx.projectmoviesq.entities.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminMapper {
    // * commercial *
    // 메인 - 영화 광고 insert
    int insertCommercial(CommercialEntity commercial);

    // 모든 광고가져오기 (관리자 모드 목록)
    CommercialEntity[] selectAllCommercial();

    // 계약 만료 전 광고 가져오기 (메인 화면 보이기)
    CommercialEntity[] selectCommercialNotDeleted();

    // 광고 이미지 가져오기
    CommercialEntity selectCommercialImageByIndex(@Param("index") int index);

    // 해당 광고 가져오기
    CommercialEntity selectCommercialByIndex(@Param("index") int index);

    // 광고 목록 삭제 수정 , 광고 내리기 올릴기 수정
    int updateCommercialByIndex(CommercialEntity commercial);

    // 광고 내용 수정
    int updateCommercialInformationByIndex(CommercialEntity commercial);

    /* movie */
    // 영화 정보 insert
    int insertMovie(MovieEntity movie);

    // 영화 첨부 파일 (포스터) insert
    int insertMovieImage(MovieImageEntity movieImage);

    // 상영예정 및 상영중 영화 모두 가져오기
    // deleted_flag false인 것만 가져오기
    MovieEntity[] selectMovies();

    MovieEntity[] selectMoviesByStatus();

    // 영화 첨부 파일 가져오기
    MovieImageEntity selectMovieImageByIndex(int index);

    // 영화 정보 update
    int updateMovie(MovieEntity movie);

    // 영화 첨부 파일 update
    int updateMovieImage(MovieImageEntity movieImage);

    /* support */
    // 공지사항 insert
    int insertNotice(NoticeEntity notice);

    // 공지사항 delete
    int deleteNoticeByIndex(@Param(value = "index") int index);

    // FAQ insert
    int insertFaq(NoticeEntity notice);

    // 영화 소식
    int insertMovieNews(NewsEntity news);
}