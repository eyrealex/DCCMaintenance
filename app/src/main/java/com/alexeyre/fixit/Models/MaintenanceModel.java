package com.alexeyre.fixit.Models;

public class MaintenanceModel {

    private String key, timestamp, name, created_by;

    public MaintenanceModel() {
    }

    public MaintenanceModel(String key, String timestamp, String name, String created_by) {
        this.key = key;
        this.timestamp = timestamp;
        this.name = name;
        this.created_by = created_by;
    }

    public String getkey() {
        return key;
    }

    public void setkey(String key) {
        this.key = key;
    }

    public String gettimestamp() {
        return timestamp;
    }

    public void settimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getcreated_by() {
        return created_by;
    }

    public void setcreated_by(String created_by) {
        this.created_by = created_by;
    }
}
