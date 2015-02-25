package com.maalaang.waltz.model;

import android.graphics.drawable.Drawable;

/**
 * Created by User on 2015-02-06.
 */
public class User {
    private String pnum;
    private Drawable photo;
    private String userName;
    private String bio;
    private boolean isRegistered;

    public User(String pnum, Drawable photo, String name, String bio, boolean isRegistered) {
        this.pnum           = pnum;
        this.photo          = photo;
        this.userName       = name;
        this.bio            = bio;
        this.isRegistered   = isRegistered;
    }

    public User(String name, boolean isRegistered){
        this.userName       = name;
        this.isRegistered   = isRegistered;
    }

//    public User() {
////        photo = cursor.getString(cursor.getColumnIndex());
//        this.userName       = cursor.getString(2);
//        this.bio            = cursor.getString(4);
//        this.isRegistered   = (cursor.getInt(5)!=0);
//    }

    public User(Drawable photo, String name){
        this.photo      = photo;
        this.userName   = name;
    }



    public String getPnum() { return pnum; }

    public Drawable getPhoto() {
        return photo;
    }

    public String getUserName() {
        return userName;
    }

    public String getBio() {
        return bio;
    }

    public boolean isRegistered() {
        return isRegistered;
    }
}
