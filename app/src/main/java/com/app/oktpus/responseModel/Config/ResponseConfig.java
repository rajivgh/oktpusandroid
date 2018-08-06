package com.app.oktpus.responseModel.Config;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;

import com.app.oktpus.responseModel.CommonResponses;

/**
 * Created by Gyandeep on 8/12/16.
 */

public class ResponseConfig extends CommonResponses {

    @SerializedName("user_config")
    private UserConfig userConfig;

    @SerializedName("display_format")
    private DisplayFormat displayFormat;

    @SerializedName("new_values")
    private LinkedList<Double> newValues;

    public LinkedList<Double> getNewValues() {
        return newValues;
    }

    public DisplayFormat getDisplayFormat() {
        return displayFormat;
    }

    public void setDisplayFormat(DisplayFormat displayFormat) {
        this.displayFormat = displayFormat;
    }

    public UserConfig getUserConfig() {
        return userConfig;
    }
}
