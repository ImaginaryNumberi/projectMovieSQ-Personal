package com.ixtx.projectmoviesq.entities;

import lombok.Getter;

import java.sql.Date;
import java.util.Objects;

public class NewsEntity {
    private int index;
    private String title;
    private String content;
    private String imageName;
    private int imageSize;
    private String imageType;
    private byte[] imageData;
    private String category;
    private java.sql.Date dateStart;
    private java.sql.Date dateEnd;
    @Getter
    private int view;
    @Getter
    private String writerEmail;
    @Getter
    private String clientIp;
    private Date createdAt;
    private boolean isExpired;

    public int getIndex() {
        return index;
    }

    public NewsEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public NewsEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public NewsEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public String getImageName() {
        return imageName;
    }

    public NewsEntity setImageName(String imageName) {
        this.imageName = imageName;
        return this;
    }

    public int getImageSize() {
        return imageSize;
    }

    public NewsEntity setImageSize(int imageSize) {
        this.imageSize = imageSize;
        return this;
    }

    public String getImageType() {
        return imageType;
    }

    public NewsEntity setImageType(String imageType) {
        this.imageType = imageType;
        return this;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public NewsEntity setImageData(byte[] imageData) {
        this.imageData = imageData;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public NewsEntity setCategory(String category) {
        this.category = category;
        return this;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public NewsEntity setDateStart(Date dateStart) {
        this.dateStart = dateStart;
        return this;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public NewsEntity setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
        return this;
    }

    public NewsEntity setView(int view) {
        this.view = view;
        return this;
    }

    public NewsEntity setWriterEmail(String writerEmail) {
        this.writerEmail = writerEmail;
        return this;
    }

    public NewsEntity setClientIp(String clientIp) {
        this.clientIp = clientIp;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public NewsEntity setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public NewsEntity setExpired(boolean expired) {
        isExpired = expired;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NewsEntity)) return false;
        NewsEntity that = (NewsEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}