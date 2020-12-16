package com.alexeyre.dccmaintenance;

public class TrafficLightModel {

    String locationName, latitude, longitude;

    //empty constructor to invoke on object creation of  the respective class in MainActivity.java
    public TrafficLightModel() {
    }

    // constructor that will store the retrieved data from firebase
    public TrafficLightModel(String locationName, String latitude, String longitude) {
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}