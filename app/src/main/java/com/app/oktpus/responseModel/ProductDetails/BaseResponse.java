package com.app.oktpus.responseModel.ProductDetails;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Gyandeep on 3/7/17.
 */

public class BaseResponse {

    @SerializedName("status")
    private int status;
    @SerializedName("result") @NonNull
    private List<Result> result;

    public int getStatus() { return status;  }
    public List<Result> getResult() { return result; }
}
