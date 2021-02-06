package com.alexeyre.fixit.Helpers;

import android.app.Activity;

public class HomePageWidgetModel {

    private int drawable;
    private String title;
    private Class classToOpen;


    public HomePageWidgetModel(int drawable, String title, Class classToOpen) {
        this.drawable = drawable;
        this.title = title;
        this.classToOpen = classToOpen;
    }

    public int getdrawable() {
        return drawable;
    }

    public void setdrawable(int drawable) {
        this.drawable = drawable;
    }

    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public Class getclassToOpen() {
        return classToOpen;
    }

    public void setclassToOpen(Class classToOpen) {
        this.classToOpen = classToOpen;
    }
}
