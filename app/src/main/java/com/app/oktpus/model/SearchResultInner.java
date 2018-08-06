package com.app.oktpus.model;

/**
 * Created by Gyandeep on 27/10/16.
 */

public class SearchResultInner {

    private String item_title, kilometers_list, price_list, post_date, product_id, image0;
    private SearchResultRawValues raw_values;

    public void setSearchResultRawValues(SearchResultRawValues raw_values) { this.raw_values = raw_values; }
    public SearchResultRawValues getSearchResultRawValues() {return this.raw_values; }

    public void setItem_title(String item_title) { this.item_title = item_title; }
    public String getItem_title() { return this.item_title; }

    public void setKilometers_list(String kilometers_list) { this.kilometers_list = kilometers_list; }
    public String getKilometers_list() { return this.kilometers_list; }

    public void setPrice_list(String price_list) { this.price_list = price_list; }
    public String getPrice_list() { return this.price_list; }

    public void setPost_date(String post_date) { this.post_date = post_date; }
    public String getPost_date() { return this.post_date; }

    public void setProduct_id(String product_id) { this.product_id = product_id; }
    public String getProduct_id() { return this.product_id; }

    public void setImage0(String image0) { this.image0 = image0; }
    public String getImage0() { return this.image0; }

}
