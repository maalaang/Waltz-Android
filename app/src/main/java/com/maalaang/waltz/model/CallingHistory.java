package com.maalaang.waltz.model;

import android.graphics.drawable.Drawable;

import java.util.Date;

/**
 * Created by User on 2015-02-06.
 */
public class CallingHistory {
    private User user;
    private Drawable callingType;
    private Date lastCallingDate;
    private int callingTime;

    public CallingHistory(Drawable photo, Drawable callingType, String name, Date lastCallingDate, int callingTime) {
        user = new User(photo, name);
        this.callingType = callingType;
        this.lastCallingDate = lastCallingDate;
        this.callingTime = callingTime;
    }

    public Drawable getPhoto() {
        return user.getPhoto();
    }

    public String getUserName() { return user.getUserName(); }

    public Drawable getCallingType() {
        return callingType;
    }

    public Date getLastCallingDate() {
        return lastCallingDate;
    }

    public int getCallingTime() {
        return callingTime;
    }
}
