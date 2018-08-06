package com.app.oktpus.responseModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyandeep on 1/12/16.
 */

public class SearchCreateResult {
    @SerializedName("search_id")
    private String searchID;

    public String getSearchID() {return searchID; }
    public void setSearchID(String searchID) { this.searchID = searchID; }
}
