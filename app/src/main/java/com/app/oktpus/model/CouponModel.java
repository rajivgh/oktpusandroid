package com.app.oktpus.model;

/**
 * Created by remees on 25/6/18.
 */

public class CouponModel {
    private String title, description, endDate;

    public CouponModel() {
    }

    public CouponModel(String title, String genre, String year) {
        this.title = title;
        this.description = genre;
        this.endDate = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getendDate() {
        return endDate;
    }

    public void setendDate(String endDate) {
        this.endDate = endDate;
    }
}
