package com.app.oktpus.responseModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Gyandeep on 1/12/16.
 */

public class SearchResultFlagsResult {

    @SerializedName("total_count")
    private int totalCount;

    @SerializedName("search_result")
    private List<FlagsSearchResultListItem> resultItemList;

    public int getTotalCount() { return totalCount; }

    public List<FlagsSearchResultListItem> getResultItemList() {return resultItemList; }
}
