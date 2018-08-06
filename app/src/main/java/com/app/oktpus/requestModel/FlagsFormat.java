package com.app.oktpus.requestModel;

/**
 * Created by Gyandeep on 30/11/16.
 */

public class FlagsFormat extends FlagsValue {
    @CustomSerializedName("display_format")
    private String displayFormat;

    public void setDisplayFormat(String displayFormat) {
        this.displayFormat = displayFormat;
    }
}
