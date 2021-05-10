package com.alexeyre.fixit.Models;

import java.io.Serializable;

public class InspectionReceiptModel implements Serializable {

    private String timestamp;
    private String path;
    private String created_by;
    private String id;
    private String location;

    public InspectionReceiptModel() {
    }

    public InspectionReceiptModel(String timestamp, String path, String created_by, String id, String location) {
        this.timestamp = timestamp;
        this.path = path;
        this.created_by = created_by;
        this.id = id;
        this.location = location;
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

    public String getid() {
        return id;
    }

    public void setid(String id) {
        this.id = id;
    }

    public String getlocation() {
        return location;
    }

    public void setlocation(String location) {
        this.location = location;
    }
}
