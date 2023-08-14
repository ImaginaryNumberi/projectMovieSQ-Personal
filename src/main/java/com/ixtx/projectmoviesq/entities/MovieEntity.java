package com.ixtx.projectmoviesq.entities;

import java.util.Date;
import java.util.Objects;

public class MovieEntity {
    private int index;
    private String titleKo;
    private String titleEn;
    private String rating;
    private java.sql.Date releaseDate;  // yyyy-mm-dd
    private String genre;
    private String runningTime;
    private String synopsis;
    private String director;
    private String cast;
    private String agency;
    private String trailerUrl;
    private String status;
    private Date createdAt;             // yyyy-mm-dd hh:mm:ss
    private boolean isDeleted;

    public int getIndex() {
        return index;
    }

    public MovieEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public String getTitleKo() {
        return titleKo;
    }

    public MovieEntity setTitleKo(String titleKo) {
        this.titleKo = titleKo;
        return this;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public MovieEntity setTitleEn(String titleEn) {
        this.titleEn = titleEn;
        return this;
    }

    public String getRating() {
        return rating;
    }

    public MovieEntity setRating(String rating) {
        this.rating = rating;
        return this;
    }

    public java.sql.Date getReleaseDate() {
        return releaseDate;
    }

    public MovieEntity setReleaseDate(java.sql.Date releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public String getGenre() {
        return genre;
    }

    public MovieEntity setGenre(String genre) {
        this.genre = genre;
        return this;
    }

    public String getRunningTime() {
        return runningTime;
    }

    public MovieEntity setRunningTime(String runningTime) {
        this.runningTime = runningTime;
        return this;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public MovieEntity setSynopsis(String synopsis) {
        this.synopsis = synopsis;
        return this;
    }

    public String getDirector() {
        return director;
    }

    public MovieEntity setDirector(String director) {
        this.director = director;
        return this;
    }

    public String getCast() {
        return cast;
    }

    public MovieEntity setCast(String cast) {
        this.cast = cast;
        return this;
    }

    public String getAgency() {
        return agency;
    }

    public MovieEntity setAgency(String agency) {
        this.agency = agency;
        return this;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public MovieEntity setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public MovieEntity setStatus(String status) {
        this.status = status;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public MovieEntity setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public MovieEntity setDeleted(boolean deleted) {
        isDeleted = deleted;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieEntity)) return false;
        MovieEntity that = (MovieEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
