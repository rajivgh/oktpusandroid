package com.app.oktpus.requestModel;

/**
 * Created by Gyandeep on 30/11/16.
 */

public class AttributeValue extends FlagsMustHave {
    @CustomSerializedName("values")
    private AttrVal values;

    public void setValues(AttrVal values) {
        this.values = values;
    }
}
