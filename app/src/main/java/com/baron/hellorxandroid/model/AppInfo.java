package com.baron.hellorxandroid.model;

import android.graphics.drawable.Drawable;

/**
 * @author baron (baronzhang[at]anjuke[dot]com)
 */
public class AppInfo {

    private String appName;
    private Drawable appIcon;
    private long lastUpdateTime;

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public String toString() {
        return appName;
    }
}
