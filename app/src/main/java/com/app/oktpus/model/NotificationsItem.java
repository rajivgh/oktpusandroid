package com.app.oktpus.model;

/**
 * Created by Gyandeep on 9/11/16.
 */

public class NotificationsItem {
    private String itemTitle, image0, sentDate, price, currency, kilometers, url;

    public NotificationsItem() {}

    public NotificationsItem(String itemTitle, String image0, String sentDate, String price,
                             String currency, String kilometers, String url) {
        this.itemTitle = itemTitle;
        this.image0 = image0;
        this.sentDate = sentDate;
        this.price = price;
        this.currency = currency;
        this.kilometers = kilometers;
        this.url = url;
    }

    public String getItemTitle() {
        return itemTitle;
    }
    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getImage0() {
        return image0;
    }
    public void setImage0(String image0) {
        this.image0 = image0;
    }

    public String getSentDate() {
        return sentDate;
    }
    public void setSentDate(String sentDate) {
        this.sentDate = sentDate;
    }

    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getKilometers() {
        return kilometers;
    }
    public void setKilometers(String kilometers) {
        this.kilometers = kilometers;
    }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}
