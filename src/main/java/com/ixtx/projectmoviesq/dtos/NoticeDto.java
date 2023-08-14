package com.ixtx.projectmoviesq.dtos;

import java.util.Objects;

public class NoticeDto {
    private int index;
    private String title;
    private String content;
    private int view;
    private String writerName;  // 작성인 이름
    private String createdOn;   // 작성 날짜

    public int getIndex() {
        return index;
    }

    public NoticeDto setIndex(int index) {
        this.index = index;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public NoticeDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getWriterName() {
        return writerName;
    }

    public NoticeDto setWriterName(String writerName) {
        this.writerName = writerName;
        return this;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public NoticeDto setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public int getView() {
        return view;
    }

    public NoticeDto setView(int view) {
        this.view = view;
        return this;
    }

    public String getContent() {
        return content;
    }

    public NoticeDto setContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NoticeDto)) return false;
        NoticeDto noticeDto = (NoticeDto) o;
        return index == noticeDto.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}