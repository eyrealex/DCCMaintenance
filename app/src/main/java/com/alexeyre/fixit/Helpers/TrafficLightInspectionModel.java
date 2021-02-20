package com.alexeyre.fixit.Helpers;

import java.io.Serializable;

public class TrafficLightInspectionModel implements Serializable {

    private String inspection_by, last_inspection, next_due_inpection, status, timestamp;

    public TrafficLightInspectionModel() {
    }

    public String getInspection_by() {
        return inspection_by;
    }

    public void setInspection_by(String inspection_by) {
        this.inspection_by = inspection_by;
    }

    public String getLast_inspection() {
        return last_inspection;
    }

    public void setLast_inspection(String last_inspection) {
        this.last_inspection = last_inspection;
    }

    public String getNext_due_inpection() {
        return next_due_inpection;
    }

    public void setNext_due_inpection(String next_due_inpection) {
        this.next_due_inpection = next_due_inpection;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
