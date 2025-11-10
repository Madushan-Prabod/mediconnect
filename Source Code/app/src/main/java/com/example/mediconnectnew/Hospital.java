package com.example.mediconnectnew;

public class Hospital {
    private String name;
    private String address;
    private String phone;
    private double latitude;
    private double longitude;
    private double distance; // Distance from user in KM

    public Hospital(String name, String address, String phone, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public double getDistance() { return distance; }

    public void setDistance(double distance) { this.distance = distance; }
}
