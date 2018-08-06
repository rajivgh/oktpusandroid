package com.app.oktpus.responseModel.ProductDetails;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyandeep on 3/7/17.
 */

public class PriceModel {
    @SerializedName("USD")
    private String usd;
    @SerializedName("CAD")
    private String cad;

    public String getUsd() { return usd; }
    public String getCad() { return cad; }
}
