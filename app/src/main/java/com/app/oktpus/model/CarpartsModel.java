package com.app.oktpus.model;

/**
 * Created by remees on 25/6/18.
 */

public class CarpartsModel {
    private String title, description, price, image, id , qty ;

    public CarpartsModel() {
    }

    public CarpartsModel(String title, String description, String price) {
        this.title = title;
        this.description = description;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }


    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setimage(String image){

        this.image = image;

    }
    public String getimage() {
        return image;
    }

    public void setid(String id){

        this.id = id;

    }
    public String getid() {
        return id;
    }

    public void setQty(String qty){

        this.qty = qty;

    }
    public String getQty() {
        return qty;
    }

}
