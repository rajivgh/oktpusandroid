package com.app.oktpus.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyandeep on 30/11/16.
 */

public class RequestModel {

    @SerializedName("attribute[]")
    private RequestModelAttributes attributeModel;

    @SerializedName("enable_notification")
    private String enableNotification;

    public void setAttributeModel(RequestModelAttributes attributeModel) {
        this.attributeModel = attributeModel;
    }

    public void setEnableNotification(String enableNotification) {
        this.enableNotification = enableNotification;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
