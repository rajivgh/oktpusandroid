package com.app.oktpus.responseModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.app.oktpus.model.AttrValSpinnerModel;

/**
 * Created by Gyandeep on 16/12/16.
 */

public class SearchByKeywordsResponse extends CommonResponses{

    @SerializedName("attribute_value")
    private List<AttrValSpinnerModel> attrValues;

    public List<AttrValSpinnerModel> getAttrValues() {
        return attrValues;
    }
}
