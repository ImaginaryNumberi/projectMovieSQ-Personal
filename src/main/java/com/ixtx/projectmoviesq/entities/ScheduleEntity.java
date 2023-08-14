package com.ixtx.projectmoviesq.entities;

import java.util.Date;
import java.util.Objects;

public class ScheduleEntity {
    private int index;
    private int movieIndex;
    private int theaterIndex;
    private int screenIndex;
    private Date timeStart;
    private Date timeEnd;

    public int getIndex() {
        return index;
    }

    public ScheduleEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public int getMovieIndex() {
        return movieIndex;
    }

    public ScheduleEntity setMovieIndex(int movieIndex) {
        this.movieIndex = movieIndex;
        return this;
    }

    public int getTheaterIndex() {
        return theaterIndex;
    }

    public ScheduleEntity setTheaterIndex(int theaterIndex) {
        this.theaterIndex = theaterIndex;
        return this;
    }

    public int getScreenIndex() {
        return screenIndex;
    }

    public ScheduleEntity setScreenIndex(int screenIndex) {
        this.screenIndex = screenIndex;
        return this;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public ScheduleEntity setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
        return this;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public ScheduleEntity setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScheduleEntity)) return false;
        ScheduleEntity that = (ScheduleEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
