package com.app.oktpus.requestModel;

/**
 * Created by Gyandeep on 30/11/16.
 */

public class FlagsLimit {
    @CustomSerializedName("max")
    private String max;
    @CustomSerializedName("min")
    private String min;

    public void setMax(String max) {
        this.max = max;
    }

    public void setMin(String min) {
        this.min = min;
    }
}
