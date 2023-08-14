package com.ixtx.projectmoviesq.entities;

import java.util.Objects;

public class TheaterEntity {
    private int index;
    private String name;
    private String regionCategory;
    private String addressPrimary;
    private String addressSecondary;
    private double latitude;
    private double longitude;
    private String contact;
    private String image;

    public int getIndex() {
        return index;
    }

    public TheaterEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public String getName() {
        return name;
    }

    public TheaterEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getRegionCategory() {
        return regionCategory;
    }

    public TheaterEntity setRegionCategory(String regionCategory) {
        this.regionCategory = regionCategory;
        return this;
    }

    public String getAddressPrimary() {
        return addressPrimary;
    }

    public TheaterEntity setAddressPrimary(String addressPrimary) {
        this.addressPrimary = addressPrimary;
        return this;
    }

    public String getAddressSecondary() {
        return addressSecondary;
    }

    public TheaterEntity setAddressSecondary(String addressSecondary) {
        this.addressSecondary = addressSecondary;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public TheaterEntity setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public TheaterEntity setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getContact() {
        return contact;
    }

    public TheaterEntity setContact(String contact) {
        this.contact = contact;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TheaterEntity)) return false;
        TheaterEntity that = (TheaterEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }

    public String getImage() {
        return image;
    }

    public TheaterEntity setImage(String image) {
        this.image = image;
        return this;
    }
}
