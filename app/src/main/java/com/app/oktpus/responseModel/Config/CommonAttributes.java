package com.app.oktpus.responseModel.Config;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyandeep on 10/12/16.
 */

public class CommonAttributes {

    @SerializedName("price")
    private Price price;
    @SerializedName("year")
    private Year year;
    @SerializedName("kilometers")
    private Kilometers kms;
    @SerializedName("below_market_average_percent")
    private BMAPercent bmaPercent;

    public Price getPrice() {
        return price;
    }

    public Year getYear() {
        return year;
    }

    public Kilometers getKms() {
        return kms;
    }

    public BMAPercent getBmaPercent() {
        return bmaPercent;
    }


}
