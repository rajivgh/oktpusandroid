package com.app.oktpus.responseModel.ProductDetails;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyandeep on 3/7/17.
 */

public class KilometersList {
    @SerializedName("kilometers")
    private String kilometers;
    @SerializedName("miles")
    private String miles;

    public String getKilometers() { return kilometers; }
    public String getMiles() {  return miles; }
}
