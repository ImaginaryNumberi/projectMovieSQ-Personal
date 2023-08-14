package com.ixtx.projectmoviesq.dtos;


import java.util.Date;

public class ScheduleDto {

    private int index;
    private int movieIndex;
    private int theaterIndex;
    private int screenIndex;
    private Date timeStart;
    private Date timeEnd;

    public int getIndex() {
        return index;
    }

    public ScheduleDto setIndex(int index) {
        this.index = index;
        return this;
    }

    public int getMovieIndex() {
        return movieIndex;
    }

    public ScheduleDto setMovieIndex(int movieIndex) {
        this.movieIndex = movieIndex;
        return this;
    }

    public int getTheaterIndex() {
        return theaterIndex;
    }

    public ScheduleDto setTheaterIndex(int theaterIndex) {
        this.theaterIndex = theaterIndex;
        return this;
    }

    public int getScreenIndex() {
        return screenIndex;
    }

    public ScheduleDto setScreenIndex(int screenIndex) {
        this.screenIndex = screenIndex;
        return this;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public ScheduleDto setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
        return this;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public ScheduleDto setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
        return this;
    }
}
