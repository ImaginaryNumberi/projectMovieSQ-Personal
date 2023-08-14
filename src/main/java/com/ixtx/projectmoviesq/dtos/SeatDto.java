package com.ixtx.projectmoviesq.dtos;

public class SeatDto {

    private String seatName;
    private String reservedSeatName;

    public String getSeatName() {
        return seatName;
    }

    public SeatDto setSeatName(String seatName) {
        this.seatName = seatName;
        return this;
    }

    public String getReservedSeatName() {
        return reservedSeatName;
    }

    public SeatDto setReservedSeatName(String reservedSeatName) {
        this.reservedSeatName = reservedSeatName;
        return this;
    }


}
