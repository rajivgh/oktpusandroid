package com.app.oktpus.responseModel.Config;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyandeep on 8/12/16.
 */

public abstract class CommonFields {

    @SerializedName("key_name")
    private String keyName;

    @SerializedName("field")
    private Field field;

    @SerializedName("default_value")
    private DefaultValue defaultValue;

    @SerializedName("default_value")
    private DefaultValue values;

    @SerializedName("value_format_type")
    private String valueFormatType;

    @SerializedName("mask")
    private Mask mask;

    private boolean isExpanded = false;
    private String activeUnit = "";
    private String minFormatted = "", maxFormatted = "";
    private int tagId;
    private boolean isTagCreated = false;
    private int viewPos;

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyName() {
        return keyName;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public void setMask(Mask mask) {
        this.mask = mask;
    }

    public void setDefaultValue(DefaultValue defaultValue) {
        this.defaultValue = defaultValue;
    }

    public DefaultValue getDefaultValue() {
        return defaultValue;
    }

    public DefaultValue getValues() {
        return values;
    }

    public String getValueFormatType() {
        return valueFormatType;
    }

    public void setValueFormatType(String vft) {
        this.valueFormatType = vft;
    }

    public Mask getMask() {
        return mask;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean value) {
        this.isExpanded = value;
    }

    public String getMinFormatted() {
        return minFormatted;
    }

    public void setMinFormatted(String minFormatted) {
        this.minFormatted = minFormatted;
    }

    public String getMaxFormatted() {
        return maxFormatted;
    }

    public void setMaxFormatted(String maxFormatted) {
        this.maxFormatted = maxFormatted;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public boolean isTagCreated() {
        return isTagCreated;
    }

    public void setTagCreated(boolean tagCreated) {
        isTagCreated = tagCreated;
    }

    public int getViewPos() {
        return viewPos;
    }

    public void setViewPos(int viewPos) {
        this.viewPos = viewPos;
    }
}
