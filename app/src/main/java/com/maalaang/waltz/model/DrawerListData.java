package com.maalaang.waltz.model;

import android.graphics.drawable.Drawable;

/**
 * Created by User on 2015-02-03.
 */
public class DrawerListData {
    private String title;
    private Drawable image;

    public DrawerListData(String title, Drawable image) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public Drawable getImage() {
        return image;
    }

}