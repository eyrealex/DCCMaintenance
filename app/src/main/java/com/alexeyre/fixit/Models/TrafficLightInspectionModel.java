package com.alexeyre.fixit.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class TrafficLightInspectionModel extends InspectionReceiptModel implements Serializable {

    private String inspection_by, last_inspection, status, timestamp;
    private long next_due_inpection;
    private ArrayList<InspectionReceiptModel> receipt;

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

    public long getnext_due_inpection() {
        return next_due_inpection;
    }

    public void setnext_due_inpection(long next_due_inpection) {
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

    public ArrayList<InspectionReceiptModel> getreceipt() {
        return receipt;
    }

    public void setreceipt(ArrayList<InspectionReceiptModel> receipt) {
        this.receipt = receipt;
    }
}
