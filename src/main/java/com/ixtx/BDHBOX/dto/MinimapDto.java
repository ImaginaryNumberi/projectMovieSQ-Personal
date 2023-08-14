package com.ixtx.BDHBOX.dto;

public class MinimapDto {

    private byte[] seatsMap;
    private String mapType;

    public byte[] getSeatsMap() {
        return seatsMap;
    }

    public MinimapDto setSeatsMap(byte[] seatsMap) {
        this.seatsMap = seatsMap;
        return this;
    }

    public String getMapType() {
        return mapType;
    }

    public MinimapDto setMapType(String mapType) {
        this.mapType = mapType;
        return this;
    }
}
