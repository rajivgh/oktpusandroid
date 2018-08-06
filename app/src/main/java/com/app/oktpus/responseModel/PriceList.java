package com.app.oktpus.responseModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyandeep on 23/12/16.
 */

public class PriceList {

    @SerializedName("CAD")
    private String CAD;

    @SerializedName("USD")
    private String USD;
    @SerializedName("EUR")
    private String EUR;

    public void setCAD(String CAD) {
        this.CAD = CAD;
    }

    public void setUSD(String USD) {
        this.USD = USD;
    }

    public void setEUR(String EUR) {
        this.EUR = EUR;
    }
}
