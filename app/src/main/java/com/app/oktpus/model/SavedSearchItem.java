package com.app.oktpus.model;

/**
 * Created by Gyandeep on 23/11/16.
 */

public class SavedSearchItem {

    /*{"id":"1248","domain_id":"1","serialized_values":
    {"header":{"domain_id":"1"},"attribute":
    {"city":{"values":[{"is_group":0,"id":"10","value":"Abbotsford, BC, CA"},
    {"is_group":0,"id":"22","value":"Barrie, ON, CA"}],"must_have":true},
    "make":{"values":[{"is_group":0,"id":"80802408","value":"Acura"}],"must_have":true},
    "model":{"values":[{"is_group":0,"id":"36783856","value":"EL"}],"must_have":true}}},
    "status_id":"1",
    "search_values":["Abbotsford, BC, CA, Barrie, ON, CA","Acura","EL"]}*/

    private String id;
    private String domain_id;
    private int is_group;
    private String search_values;
    private int status_id;
    private String titleHeader;
    private Object serializedValues;

    public SavedSearchItem() {}

    public void setSearchID(String id) { this.id = id;}
    public String getSearchID() {return this.id;}

    public void setStatusID(int status_id) { this.status_id = status_id;}
    public int getStatusID(){ return this.status_id; }

    public void setSearchValues(String searchValues) {this.search_values = searchValues;}
    public String getSearchValues() {return this.search_values; }

    public void setTitleHeader(String titleHeader) {this.titleHeader = titleHeader;}
    public String getTitleHeader() {return this.titleHeader; }

    public void setSerializedValues(Object serializedValues) {this.serializedValues = serializedValues;}
    public Object getSerializedValues() {return this.serializedValues; }

}
