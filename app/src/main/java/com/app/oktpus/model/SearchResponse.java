package com.app.oktpus.model;

import com.app.oktpus.requestModel.AttributeValue;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyandeep on 5/10/16.
 */

public class SearchResponse{
    /**NR***/
    @SerializedName("status")
    private int status;
    @SerializedName("attribute_label")
    private AttributeLabel attribute_label;
    @SerializedName("search_result")
    private SearchResult search_result;
    @SerializedName("total_count")
    private int total_count;
    @SerializedName("attribute_value")
    private AttributeValue attributeValue;

    //private TypeToken attribute_value;
    /*private String kilometers;
    private String key_name;
    private String label;
    private String label_list;*/

    public void setStatus(int status) { this.status = status; }
    public int getStatus() { return this.status;}

    public int getTotal_count() {return this.total_count;}

    public AttributeLabel getAttribute_label(){ return this.attribute_label;}

    //public void setAttribute_value(TypeToken attribute_value) { this.attribute_value = attribute_value; }
    //public TypeToken getAttribute_value() {return this.attribute_value;}

    public SearchResult getSearch_result(){ return this.search_result;}

}
