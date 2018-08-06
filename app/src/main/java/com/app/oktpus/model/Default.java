package com.app.oktpus.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyandeep on 4/10/16.
 */

public class Default {

    @SerializedName("status")
    private String status;

    public void setStatus(String status) { this.status = status;}
    public String getStatus(){ return this.status;}
}
