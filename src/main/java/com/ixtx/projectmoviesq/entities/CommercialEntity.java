package com.ixtx.projectmoviesq.entities;


import java.util.Date;
import java.util.Objects;

public class CommercialEntity {
    private int index;
    private int movieIndex;
    private String commercialName;
    private String imageName;
    private byte[] imageData;
    private int imageSize;
    private Date createdAt;
    private Date startedAt;
    private Date finishedAt;
    private boolean isHidden;
    private boolean isDeleted;

    public int getIndex() {
        return index;
    }

    public CommercialEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public int getMovieIndex() {
        return movieIndex;
    }

    public CommercialEntity setMovieIndex(int movieIndex) {
        this.movieIndex = movieIndex;
        return this;
    }

    public String getCommercialName() {
        return commercialName;
    }

    public CommercialEntity setCommercialName(String commercialName) {
        this.commercialName = commercialName;
        return this;
    }

    public String getImageName() {
        return imageName;
    }

    public CommercialEntity setImageName(String imageName) {
        this.imageName = imageName;
        return this;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public CommercialEntity setImageData(byte[] imageData) {
        this.imageData = imageData;
        return this;
    }

    public int getImageSize() {
        return imageSize;
    }

    public CommercialEntity setImageSize(int imageSize) {
        this.imageSize = imageSize;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public CommercialEntity setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public CommercialEntity setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
        return this;
    }

    public Date getFinishedAt() {
        return finishedAt;
    }

    public CommercialEntity setFinishedAt(Date finishedAt) {
        this.finishedAt = finishedAt;
        return this;
    }


    public boolean isHidden() {
        return isHidden;
    }

    public CommercialEntity setHidden(boolean hidden) {
        isHidden = hidden;
        return this;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public CommercialEntity setDeleted(boolean deleted) {
        isDeleted = deleted;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommercialEntity that = (CommercialEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
