package com.app.oktpus.model;

/**
 * Created by Gyandeep on 20/9/16.
 */
public class NavDrawerItem {

    private String title;
    private int icon;

    public NavDrawerItem() {}

    public NavDrawerItem(String title, int iconId) {
        this.title = title;
        this.icon = iconId;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }
    public void setIcon(int icon) {
        this.icon = icon;
    }
}