package com.alexeyre.dccmaintenance;

public class TrafficLightModel {

    private String name;
    private String latitude;
    private String longitude;

    //empty constructor to invoke on object creation of  the respective class in MainActivity.java
    public TrafficLightModel() {
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getlatitude() {
        return latitude;
    }

    public void setlatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getlongitude() {
        return longitude;
    }

    public void setlongitude(String longitude) {
        this.longitude = longitude;
    }
}