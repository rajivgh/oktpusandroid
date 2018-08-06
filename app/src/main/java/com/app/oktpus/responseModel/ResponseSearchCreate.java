package com.app.oktpus.responseModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyandeep on 1/12/16.
 */

public class ResponseSearchCreate {
    /*{"result":{"search_id":1294},"status":1}*/
    @SerializedName("status")
    private int status;

    @SerializedName("result")
    private SearchCreateResult result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public SearchCreateResult getResult() {
        return result;
    }

    public void setResult(SearchCreateResult result) {
        this.result = result;
    }
}
