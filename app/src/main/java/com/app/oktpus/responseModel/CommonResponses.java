package com.app.oktpus.responseModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyandeep on 1/12/16.
 */

public class CommonResponses {

    @SerializedName("status")
    private int status;

    public int getStatus() { return status; }
}
