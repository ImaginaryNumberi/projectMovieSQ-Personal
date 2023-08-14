package com.ixtx.projectmoviesq.mappers;

import com.ixtx.projectmoviesq.entities.NewsEntity;
import com.ixtx.projectmoviesq.entities.NoticeEntity;
import com.ixtx.projectmoviesq.models.PagingModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SupportMapper {
    /* Notice */
    // notice DB의 전체 레코드 갯수 select
    int selectNoticeCount(@Param(value = "searchCriterion") String searchCriterion,
                          @Param(value = "searchQuery") String searchQuery);

    // 보여줄 페이지의 레코드 배열로 select
    NoticeEntity[] selectNoticeByPage(@Param(value = "pagingModel") PagingModel pagingModel,
                                      @Param(value = "searchCriterion") String searchCriterion,
                                      @Param(value = "searchQuery") String searchQuery);

    // index로 레코드 하나만 가져오기
    NoticeEntity selectByIndex(@Param(value = "index") int index);

    // notice update : view++을 위해
    int updateNotice(NoticeEntity notice);

    /* FAQ */
    // faq DB의 전체 레코드 갯수 select
    int selectFaqCount(@Param(value = "searchCriterion") String searchCriterion,
                       @Param(value = "searchQuery") String searchQuery);

    // 보여줄 페이지의 레코드 배열로 select
    NoticeEntity[] selectFaqByPage(@Param(value = "pagingModel") PagingModel pagingModel,
                                   @Param(value = "searchCriterion") String searchCriterion,
                                   @Param(value = "searchQuery") String searchQuery);


    // 최근 등록된 3개만 게시물 가져오기 (메인화면 보이기)
    NoticeEntity[] selectByRecent();

    /* 영화 소식 : News */
    // 가장 최근 뉴스 하나만 불러오기
    NewsEntity[] selectLatestNews();

    // 해당 index 뉴스 불러오기
    NewsEntity selectNewsByIndex(@Param("index") int index);

    // 뉴스 전체 불러오기
    NewsEntity[] selectNewsList();

    // 보여줄 페이지만큼만 뉴스 불러오기
    NewsEntity[] selectNewsByPage(@Param(value = "pagingModel") PagingModel pagingModel);

    // 뉴스 DB의 전체 레코드 갯수 select
    int selectNewsCount();
}
