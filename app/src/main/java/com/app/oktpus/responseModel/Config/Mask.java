package com.app.oktpus.responseModel.Config;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyandeep on 8/12/16.
 */

public class Mask {

    @SerializedName("regular")
    private String regular;
    @SerializedName("verbose")
    private String verbose;
    @SerializedName("min")
    private String min;
    @SerializedName("max")
    private String max;
    @SerializedName("min_verbose")
    private String minVerbose;
    @SerializedName("max_verbose")
    private String maxVerbose;

    public String getRegular() {
        return regular;
    }

    public String getVerbose() {
        return verbose;
    }

    public String getMin() {
        return min;
    }

    public String getMax() {
        return max;
    }

    public String getMinVerbose() {
        return minVerbose;
    }

    public String getMaxVerbose() {
        return maxVerbose;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }

    public void setVerbose(String verbose) {
        this.verbose = verbose;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public void setMinVerbose(String minVerbose) {
        this.minVerbose = minVerbose;
    }

    public void setMaxVerbose(String maxVerbose) {
        this.maxVerbose = maxVerbose;
    }
}
