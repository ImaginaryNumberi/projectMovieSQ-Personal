package com.ixtx.projectmoviesq.entities;

import lombok.Getter;

import java.util.Date;
import java.util.Objects;

public class NoticeEntity {
    @Getter
    private int index;
    @Getter
    private String title;
    @Getter
    private String content;
    @Getter
    private int view;
    @Getter
    private String writerEmail;
    @Getter
    private String clientIp;
    @Getter
    private Date createdAt;
    private boolean isDeleted;

    public NoticeEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public NoticeEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public NoticeEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public NoticeEntity setView(int view) {
        this.view = view;
        return this;
    }

    public NoticeEntity setWriterEmail(String writerEmail) {
        this.writerEmail = writerEmail;
        return this;
    }

    public NoticeEntity setClientIp(String clientIp) {
        this.clientIp = clientIp;
        return this;
    }

    public NoticeEntity setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public NoticeEntity setDeleted(boolean deleted) {
        isDeleted = deleted;
        return this;
    }
}
