package com.alexeyre.fixit.Helpers;

import java.io.Serializable;

public class TrafficLightInspectionModel implements Serializable {

    private String inspection_by;
    private String status;
    private String timestamp;

    public TrafficLightInspectionModel() {
    }

    public String getinspection_by() {
        return inspection_by;
    }

    public void setinspection_by(String inspection_by) {
        this.inspection_by = inspection_by;
    }

    public String getstatus() {
        return status;
    }

    public void setstatus(String status) {
        this.status = status;
    }

    public String gettimestamp() {
        return timestamp;
    }

    public void settimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
