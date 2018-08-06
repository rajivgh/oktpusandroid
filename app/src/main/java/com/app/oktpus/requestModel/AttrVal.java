package com.app.oktpus.requestModel;

import java.util.List;

/**
 * Created by Gyandeep on 30/11/16.
 */

public class AttrVal {
    @CustomSerializedName("value")
    private List<String> value;

    @CustomSerializedName("value")
    private List<Integer> valueInt;

    @CustomSerializedName("group")
    private List<String> group;



    public void setGroup(List<String> group) { this.group = group; }

    public void setValue(List<String> value) { this.value = value; }

    public void setValueInt(List<Integer> valueInt) {this.valueInt = valueInt;}
}
