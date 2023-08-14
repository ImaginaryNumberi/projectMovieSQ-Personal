package com.ixtx.projectmoviesq.dtos;

import java.util.Date;
import java.util.Objects;

public class TicketDto {
    // reservation
    private int index;
    private String ticketNumber; // 예매번호
    private String userEmail; // 예매자
    private int ticketTotal; // 관람인원
    private String reservedSeatName; // 관람좌석
    private String paymentAmount; // 결제금액
    private Date canceledAt; // 예매취소일

    // screenSchedules
    private Date timeStart; // 영화시작
    private Date timeEnd; // 영화종료
    // movie
    private int movieIndex; // 영화인덱스
    private String titleKo; // 영화제목
    // theaters
    private String theaterName; // 관람극장
    // screen
    private String screenName; // 상영관


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketDto that = (TicketDto) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }

    public int getIndex() {
        return index;
    }

    public TicketDto setIndex(int index) {
        this.index = index;
        return this;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public TicketDto setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
        return this;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public TicketDto setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public int getTicketTotal() {
        return ticketTotal;
    }

    public TicketDto setTicketTotal(int ticketTotal) {
        this.ticketTotal = ticketTotal;
        return this;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public TicketDto setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
        return this;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public TicketDto setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
        return this;
    }


    public int getMovieIndex() {
        return movieIndex;
    }

    public TicketDto setMovieIndex(int movieIndex) {
        this.movieIndex = movieIndex;
        return this;
    }

    public String getTheaterName() {
        return theaterName;
    }

    public TicketDto setTheaterName(String theaterName) {
        this.theaterName = theaterName;
        return this;
    }

    public String getScreenName() {
        return screenName;
    }

    public TicketDto setScreenName(String screenName) {
        this.screenName = screenName;
        return this;
    }

    public Date getCanceledAt() {
        return canceledAt;
    }

    public TicketDto setCanceledAt(Date canceledAt) {
        this.canceledAt = canceledAt;
        return this;
    }

    public String getTitleKo() {
        return titleKo;
    }

    public TicketDto setTitleKo(String titleKo) {
        this.titleKo = titleKo;
        return this;
    }

    public String getReservedSeatName() {
        return reservedSeatName;
    }

    public TicketDto setReservedSeatName(String reservedSeatName) {
        this.reservedSeatName = reservedSeatName;
        return this;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public TicketDto setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
        return this;
    }
}
