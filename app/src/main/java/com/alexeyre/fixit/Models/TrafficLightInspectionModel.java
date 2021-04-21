package com.alexeyre.fixit.Models;

import java.io.Serializable;

public class TrafficLightInspectionModel implements Serializable {

    private String inspection_by, last_inspection, next_due_inpection, status, timestamp;

    public TrafficLightInspectionModel() {
    }

    public String getinspection_by() {
        return inspection_by;
    }

    public void setinspection_by(String inspection_by) {
        this.inspection_by = inspection_by;
    }

    public String getlast_inspection() {
        return last_inspection;
    }

    public void setlast_inspection(String last_inspection) {
        this.last_inspection = last_inspection;
    }

    public String getnext_due_inpection() {
        return next_due_inpection;
    }

    public void setnext_due_inpection(String next_due_inpection) {
        this.next_due_inpection = next_due_inpection;
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
