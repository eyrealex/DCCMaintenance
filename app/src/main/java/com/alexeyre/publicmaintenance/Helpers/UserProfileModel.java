package com.alexeyre.publicmaintenance.Helpers;

import java.io.Serializable;

public class UserProfileModel implements Serializable {
    String name, email, phone;
    private Boolean admin;

    public UserProfileModel() {
    }

    public UserProfileModel(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public String getphone() {
        return phone;
    }

    public void setphone(String phone) {
        this.phone = phone;
    }

    public Boolean getadmin() {
        return admin;
    }

    public void setadmin(Boolean admin) {
        this.admin = admin;
    }
}
