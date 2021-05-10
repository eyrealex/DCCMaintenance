package com.alexeyre.fixit.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class TrafficLightModel extends TrafficLightInspectionModel implements Serializable {

    private String name, latitude , longitude, key;
    private ArrayList<TrafficLightInspectionModel> inspection;

    //empty constructor to invoke on object creation of  the respective class in MainActivity.java
    public TrafficLightModel() {
    }

    public TrafficLightModel(String name, String latitude, String longitude, String key) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.key = key;
    }

    public String getkey() {
        return key;
    }

    public void setkey(String key) {
        this.key = key;
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

    public ArrayList<TrafficLightInspectionModel> getinspection() {
        return inspection;
    }

    public void setinspection(ArrayList<TrafficLightInspectionModel> inspection) {
        this.inspection = inspection;
    }
}