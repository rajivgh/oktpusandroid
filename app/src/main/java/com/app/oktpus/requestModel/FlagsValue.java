package com.app.oktpus.requestModel;

import java.util.List;

/**
 * Created by Gyandeep on 30/11/16.
 */

public class FlagsValue extends FlagsMustHave {
    @CustomSerializedName("value")
    private List<FlagsLimit> value;

    public void setValue(List<FlagsLimit> value) {
        this.value = value;
    }
}
