package com.alexeyre.fixit.Models;

import java.io.Serializable;

public class InspectionReceiptModel implements Serializable {

    private String timestamp;
    private String path;
    private String created_by;

    public InspectionReceiptModel() {
    }

    public String gettimestamp() {
        return timestamp;
    }

    public void settimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getpath() {
        return path;
    }

    public void setpath(String path) {
        this.path = path;
    }

    public String getcreated_by() {
        return created_by;
    }

    public void setcreated_by(String created_by) {
        this.created_by = created_by;
    }
}
