package com.app.oktpus.responseModel.Config;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyandeep on 8/12/16.
 */

public class DefaultValue {

    @SerializedName("min")
    private int min;

    @SerializedName("max")
    private int max;

    public int getMax() {
        return max;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }
}
