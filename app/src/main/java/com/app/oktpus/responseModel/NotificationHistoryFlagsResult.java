package com.app.oktpus.responseModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyandeep on 1/12/16.
 */

public class NotificationHistoryFlagsResult {

    @SerializedName("product_id")
    private String productId;

    @SerializedName("image0")
    private String imgURL;

    @SerializedName("item_title")
    private String itemTitle;

    @SerializedName("currency")
    private String currency;

    @SerializedName("price")
    private String price;

    @SerializedName("sent_date")
    private String sentDate;

    @SerializedName("kilometers")
    private String kilometers;

    @SerializedName("url")
    private String url;

    public String getUrl() { return url; }
    public String getSentDate() { return sentDate; }
    public String getKilometers() { return kilometers; }
    public String getProductId() { return productId; }
    public String getImgURL() { return imgURL; }
    public String getItemTitle() {return itemTitle; }
    public String getCurrency() { return currency; }
    public String getPrice() { return price; }

}
