package com.app.oktpus.requestModel;

/**
 * Created by Gyandeep on 30/11/16.
 */

public class FlagsMustHave {
    @CustomSerializedName("must_have")
    private int mustHave;

    public void setMustHave(int mustHave) {
        this.mustHave = mustHave;
    }
}
