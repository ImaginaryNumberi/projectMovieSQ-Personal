package com.ixtx.projectmoviesq.services;

import com.ixtx.projectmoviesq.dtos.NoticeDto;
import com.ixtx.projectmoviesq.entities.NewsEntity;
import com.ixtx.projectmoviesq.entities.NoticeEntity;
import com.ixtx.projectmoviesq.mappers.SupportMapper;
import com.ixtx.projectmoviesq.mappers.UserMapper;
import com.ixtx.projectmoviesq.models.PagingModel;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class SupportService {
    public static final int PAGE_COUNT = 10;
    public static final int PAGE_COUNT_NEWS = 6;
    private final SupportMapper supportMapper;
    private final UserMapper userMapper;

    public SupportService(SupportMapper supportMapper, UserMapper userMapper) {
        this.supportMapper = supportMapper;
        this.userMapper = userMapper;
    }

    // notices DB의 전체 게시물 갯수 가져오기
    public int getNoticeCount(String searchCriterion, String searchQuery) {
        return this.supportMapper.selectNoticeCount(searchCriterion, searchQuery);
    }

    // faqs DB의 전체 게시물 갯수 가져오기
    public int getFaqCount(String searchCriterion, String searchQuery) {
        return this.supportMapper.selectFaqCount(searchCriterion, searchQuery);
    }

    // news DB의 전체 게시물 갯수 가져오기
    public int getNewsCount() {
        return this.supportMapper.selectNewsCount();
    }

    // 보여줄 페이지 만큼의 게시물 가져오기 - Notice
    public List<NoticeDto> getNoticeByPage(PagingModel pagingModel, String searchCriterion, String searchQuery) {
        NoticeEntity[] notices = this.supportMapper.selectNoticeByPage(pagingModel, searchCriterion, searchQuery);
        List<NoticeDto> noticeDtos = new ArrayList<>();
        for (NoticeEntity notice : notices) {
            noticeDtos.add(setNoticeDto(notice));
        }
        return noticeDtos;
    }

    // 보여줄 페이지 만큼의 게시물 가져오기 - Faq
    public List<NoticeDto> getFaqByPage(PagingModel pagingModel, String searchCriterion, String searchQuery) {
        NoticeEntity[] faqs = this.supportMapper.selectFaqByPage(pagingModel, searchCriterion, searchQuery);
        List<NoticeDto> faqDtos = new ArrayList<>();
        for (NoticeEntity notice : faqs) {
            faqDtos.add(setNoticeDto(notice));
        }
        return faqDtos;
    }

    // 게시판 게시물 읽기 : 게시물 가져오고 조회수 1 추가
    public NoticeDto getNotice(int index) {
        NoticeEntity notice = this.supportMapper.selectByIndex(index);
        // 조회수 +1
        notice.setView(notice.getView() + 1);
        return setNoticeDto(notice);
    }

    // NoticeEntity에 있는 정보를 NoticeDto로 옮겨담음
    // WriterName : NoticeEntity에는 작성자 email 주소만 있기 때문
    // CreatedOn : CreatedAt 정보를 String 형태로 바꾸어 넣어줌
    public NoticeDto setNoticeDto(NoticeEntity notice) {
        NoticeDto noticeDto = new NoticeDto();
        noticeDto.setIndex(notice.getIndex())
                .setTitle(notice.getTitle())
                .setWriterName(this.userMapper.selectUserByEmail(notice.getWriterEmail()).getName())
                .setCreatedOn(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(notice.getCreatedAt()))
                .setView(notice.getView())
                .setContent(notice.getContent());
        this.supportMapper.updateNotice(notice);    // 조회수가 +1 된 경우 update가 필요하기 때문
        return noticeDto;
    }

    // 최근 등록된 3개만 게시물 가져오기 (메인화면 보이기)
    public NoticeEntity[] getByRecent() {
        return this.supportMapper.selectByRecent();
    }

    // 영화 소식
    // 최근 영화 소식 1개 가져오기 - for Home
    public NewsEntity[] getMovieLatestNews() {
        return this.supportMapper.selectLatestNews();
    }

    public NewsEntity getMovieNewsByIndex(int index) {
        return this.supportMapper.selectNewsByIndex(index);
    }

    // 영화 소식 전체 리스트 가져오기
    public NewsEntity[] getNewsList() {
        return this.supportMapper.selectNewsList();
    }

    // 보여줄 페이지 만큼의 게시물 가져오기 - Faq
    public NewsEntity[] getNewsByPage(PagingModel pagingModel) {
        NewsEntity[] news = this.supportMapper.selectNewsByPage(pagingModel);
        return news;
    }
}
