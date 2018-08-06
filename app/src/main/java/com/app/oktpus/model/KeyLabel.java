package com.app.oktpus.model;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyandeep on 5/10/16.
 */

public class KeyLabel {

    @SerializedName("key_name")
    private String key_name;
    @SerializedName("label")
    private String label;
    @SerializedName("label_list")
    private JsonObject label_list;

    public void setKey_name(String key_name) { this.key_name = key_name; }
    public String getKey_name() { return this.key_name; }

    public void setLabel(String label) { this.label = label; }
    public String getLabel() { return this.label; }

    public JsonObject getLabel_list() { return this.label_list; }

}