package com.app.oktpus.responseModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyandeep on 1/12/16.
 */

public class ResponseSearchResult extends CommonResponses {

    @SerializedName("result")
    private SearchResultFlagsResult result;

    public SearchResultFlagsResult getResult()  { return result; }
}
