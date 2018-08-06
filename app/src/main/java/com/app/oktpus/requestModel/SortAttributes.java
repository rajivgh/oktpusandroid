package com.app.oktpus.requestModel;

/**
 * Created by Gyandeep on 30/11/16.
 */

public class SortAttributes {
    @CustomSerializedName("post_date")
    private String postDate;
    @CustomSerializedName("heuristic")
    private String heuristic;
    @CustomSerializedName("price")
    private String price;
    @CustomSerializedName("year")
    private String year;

    public void setPostDate(String postDate) { this.postDate = postDate; }
    public void setHeuristic(String heuristic) { this.heuristic = heuristic; }
    public void setPrice(String price) { this.price = price; }
    public void setYear(String year) {  this.year = year; }

}
