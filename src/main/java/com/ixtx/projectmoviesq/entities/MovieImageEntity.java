package com.ixtx.projectmoviesq.entities;

import java.util.Date;
import java.util.Objects;

public class MovieImageEntity {
    private int index;
    private int movieIndex;
    private String imageName;
    private int imageSize;
    private String imageType;
    private byte[] imageData;
    private String category;
    private Date createdAt;
    private boolean isDeleted;

    public int getIndex() {
        return index;
    }

    public MovieImageEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public int getMovieIndex() {
        return movieIndex;
    }

    public MovieImageEntity setMovieIndex(int movieIndex) {
        this.movieIndex = movieIndex;
        return this;
    }

    public String getImageName() {
        return imageName;
    }

    public MovieImageEntity setImageName(String imageName) {
        this.imageName = imageName;
        return this;
    }

    public int getImageSize() {
        return imageSize;
    }

    public MovieImageEntity setImageSize(int imageSize) {
        this.imageSize = imageSize;
        return this;
    }

    public String getImageType() {
        return imageType;
    }

    public MovieImageEntity setImageType(String imageType) {
        this.imageType = imageType;
        return this;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public MovieImageEntity setImageData(byte[] imageData) {
        this.imageData = imageData;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public MovieImageEntity setCategory(String category) {
        this.category = category;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public MovieImageEntity setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public MovieImageEntity setDeleted(boolean deleted) {
        isDeleted = deleted;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieImageEntity that = (MovieImageEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
