package com.app.oktpus.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyandeep on 20/7/17.
 */

public class WMCWFormData {

    @SerializedName("make")
    private String make;

    @SerializedName("model")
    private String model;

    @SerializedName("email")
    private String email;

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getEmail() {
        return email;
    }
}
