package com.alexeyre.fixit.Models;

public class MaintenanceModel {

    private String key, timestamp;

    public MaintenanceModel() {
    }

    public MaintenanceModel(String key, String timestamp) {
        this.key = key;
        this.timestamp = timestamp;
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
}
