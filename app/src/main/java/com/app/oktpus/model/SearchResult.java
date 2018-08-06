package com.app.oktpus.model;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Gyandeep on 27/10/16.
 */

public class SearchResult {
    @SerializedName("attribute_label")
    private AttributeLabel attribute_label;
    private Type search_result = new TypeToken<List<SearchResultInner>>() {}.getType();// ;

    public AttributeLabel getAttribute_label(){ return this.attribute_label;}

    public void setSearch_result(Type search_result) { this.search_result = search_result; }
    public Type getSearch_result(){ return this.search_result;}

}
