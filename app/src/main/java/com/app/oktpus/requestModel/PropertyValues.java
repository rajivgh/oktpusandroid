package com.app.oktpus.requestModel;

/**
 * Created by Gyandeep on 1/12/16.
 */

public class PropertyValues {
    @CustomSerializedName("edit_search")
    private String editSearch;
    @CustomSerializedName("search_id")
    private String searchID;
    @CustomSerializedName("save_search")
    private Integer saveSearch;


    public PropertyValues(String editSearch, String searchID) {
        this.editSearch = editSearch;
        this.searchID = searchID;
    }

    public PropertyValues(Integer saveSearch){
        this.saveSearch = saveSearch;
    }

    /*public void setSaveSearch(int saveSearch) { this.saveSearch = saveSearch; }

    public void setEditSearch(String editSearch) {
        this.editSearch = editSearch;
    }

    public void setSearchID(String searchID) {
        this.searchID = searchID;
    }*/
}
