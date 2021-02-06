package com.alexeyre.fixit.Helpers;

import java.io.Serializable;

public class UserProfileModel implements Serializable {
    String name, email, phone,  profile_photo_url, uid;
    private Boolean admin;

    public UserProfileModel() {
    }

    public UserProfileModel(String name, String email, String phone, String uid) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.uid = uid;
    }

    public String getprofile_photo_url() {
        return profile_photo_url;
    }

    public void setprofile_photo_url(String profile_photo_url) {
        this.profile_photo_url = profile_photo_url;
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

    public String getuid() {
        return uid;
    }

    public void setuid(String uid) {
        this.uid = uid;
    }
}
