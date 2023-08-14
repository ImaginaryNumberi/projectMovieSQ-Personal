package com.ixtx.projectmoviesq.entities;

import lombok.Getter;

import java.util.Date;
import java.util.Objects;

public class ReservationEntity {
    @Getter
    private int index;
    @Getter
    private String userEmail;
    @Getter
    private String ticketNumber;
    @Getter
    private int screenScheduleIndex;
    @Getter
    private int ticketTotal;
    @Getter
    private String reservedSeatName;

    private boolean isExpired;
    @Getter
    private int paymentAmount;
    @Getter
    private Date canceledAt;
    @Getter
    private String typeName;

    public ReservationEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public ReservationEntity setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public ReservationEntity setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
        return this;
    }

    public ReservationEntity setScreenScheduleIndex(int screenScheduleIndex) {
        this.screenScheduleIndex = screenScheduleIndex;
        return this;
    }

    public ReservationEntity setTicketTotal(int ticketTotal) {
        this.ticketTotal = ticketTotal;
        return this;
    }

    public ReservationEntity setReservedSeatName(String reservedSeatName) {
        this.reservedSeatName = reservedSeatName;
        return this;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public ReservationEntity setExpired(boolean expired) {
        isExpired = expired;
        return this;
    }

    public ReservationEntity setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
        return this;
    }

    public ReservationEntity setCanceledAt(Date canceledAt) {
        this.canceledAt = canceledAt;
        return this;
    }

    public ReservationEntity setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReservationEntity)) return false;
        ReservationEntity that = (ReservationEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}