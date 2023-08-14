package com.ixtx.projectmoviesq.dtos;

import java.sql.Date;
import java.util.Objects;

public class MovieDto {
    private int index;
    private String titleKo;
    private String titleEn;
    private String rating;
    private String rating_title;
    private Date releaseDate;
    private String genre;
    private String runningTime;
    private String synopsis;
    private String director;
    private String cast;
    private String agency;
    private String trailerUrl;
    private String status;
    private byte[] imageData;
    private String category;
    private double reservationRate;
    // 예매율 (특정 영화, 공연 등에서 전체 티켓(좌석) 중 사전에 판매된 티켓(좌석)의 비율)

    public int getIndex() {
        return index;
    }

    public MovieDto setIndex(int index) {
        this.index = index;
        return this;
    }

    public String getTitleKo() {
        return titleKo;
    }

    public MovieDto setTitleKo(String titleKo) {
        this.titleKo = titleKo;
        return this;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public MovieDto setTitleEn(String titleEn) {
        this.titleEn = titleEn;
        return this;
    }

    public String getRating() {
        return rating;
    }

    public MovieDto setRating(String rating) {
        this.rating = rating;
        return this;
    }

    public String getRating_title() {
        return rating_title;
    }

    public MovieDto setRating_title(String rating_title) {
        this.rating_title = rating_title;
        return this;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public MovieDto setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public String getGenre() {
        return genre;
    }

    public MovieDto setGenre(String genre) {
        this.genre = genre;
        return this;
    }

    public String getRunningTime() {
        return runningTime;
    }

    public MovieDto setRunningTime(String runningTime) {
        this.runningTime = runningTime;
        return this;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public MovieDto setSynopsis(String synopsis) {
        this.synopsis = synopsis;
        return this;
    }

    public String getDirector() {
        return director;
    }

    public MovieDto setDirector(String director) {
        this.director = director;
        return this;
    }

    public String getCast() {
        return cast;
    }

    public MovieDto setCast(String cast) {
        this.cast = cast;
        return this;
    }

    public String getAgency() {
        return agency;
    }

    public MovieDto setAgency(String agency) {
        this.agency = agency;
        return this;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public MovieDto setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public MovieDto setStatus(String status) {
        this.status = status;
        return this;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public MovieDto setImageData(byte[] imageData) {
        this.imageData = imageData;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public MovieDto setCategory(String category) {
        this.category = category;
        return this;
    }

    public double getReservationRate() {
        return reservationRate;
    }

    public MovieDto setReservationRate(double reservationRate) {
        this.reservationRate = reservationRate;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieDto)) return false;
        MovieDto movieDto = (MovieDto) o;
        return index == movieDto.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
