package com.alexeyre.fixit.Models;

public class UserSingletonModel {
    private static UserSingletonModel instance = null;

    // variable of type String
    public String user_name;
    public String user_uid;

    public UserSingletonModel() {
    }

    public static UserSingletonModel getInstance() {
        if (instance == null)
            instance = new UserSingletonModel();

        return instance;
    }

    public static UserSingletonModel getinstance() {
        return instance;
    }

    public static void setinstance(UserSingletonModel instance) {
        UserSingletonModel.instance = instance;
    }

    public String getuser_name() {
        return user_name;
    }

    public void setuser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getuser_uid() {
        return user_uid;
    }

    public void setuser_uid(String user_uid) {
        this.user_uid = user_uid;
    }
}