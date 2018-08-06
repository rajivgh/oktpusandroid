package com.app.oktpus.responseModel.ProductDetails;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyandeep on 3/7/17.
 */

public class Result {
    @SerializedName("product_id")
    private String productId;
    @SerializedName("make")
    private String make;
    @SerializedName("model")
    private String model;
    @SerializedName("kilometers")
    private String kilometers;
    @SerializedName("body")
    private String body;
    @SerializedName("accessories")
    private String accessories;
    @SerializedName("engine")
    private String engine;
    @SerializedName("cylinders")
    private String cylinders;
    @SerializedName("stock_number")
    private String stock_number;
    @SerializedName("drivetrain")
    private String drivetrain;
    @SerializedName("safety_rating")
    private String safetyRating;
    @SerializedName("transmission")
    private String transmission;
    @SerializedName("colour")
    private String colour;
    @SerializedName("interior_colour")
    private String interiorColor;
    @SerializedName("passengers")
    private String passengers;
    @SerializedName("doors")
    private String doors;
    @SerializedName("fuel")
    private String fuel;
    @SerializedName("city_fuel_economy")
    private String cityFuelEcon;
    @SerializedName("hwy_fuel_economy")
    private String hwyFuelEconomy;
    @SerializedName("rating")
    private String rating;
    @SerializedName("city")
    private String city;
    @SerializedName("country")
    private String country;
    @SerializedName("post_date")
    private String postDate;
    @SerializedName("image0")
    private String imageUrl;
    @SerializedName("heuristic")
    private String heuristic;
    @SerializedName("price_list")
    private PriceModel priceList;
    @SerializedName("item_title")
    private String itemTitle;
    @SerializedName("sent_date")
    private String sentDate;
    @SerializedName("ma_difference_list")
    private PriceModel maDifference;
    @SerializedName("ma_show")
    private boolean maShow;
    @SerializedName("ma_keyword")
    private String ma_keyword;
    @SerializedName("href_url")
    private String hrefUrl;


    @SerializedName("kilometers_list")
    private KilometersList kmsList;

    /*@SerializedName("raw_values")
    private RawValues rawValues;*/

    public String getProductId() { return productId; }
    public String getMake() {  return make; }
    public String getModel() { return model; }
    public String getKilometers() { return kilometers; }
    public String getBody() { return body; }
    public String getAccessories() { return accessories; }
    public String getEngine() { return engine; }
    public String getCylinders() { return cylinders; }
    public String getStock_number() { return stock_number; }
    public String getDrivetrain() { return drivetrain; }
    public String getSafetyRating() { return safetyRating; }
    public String getTransmission() { return transmission; }
    public String getColour() { return colour; }
    public String getInteriorColor() { return interiorColor; }
    public String getPassengers() { return passengers; }
    public String getDoors() { return doors; }
    public String getFuel() { return fuel; }
    public String getCityFuelEcon() { return cityFuelEcon; }
    public String getHwyFuelEconomy() { return hwyFuelEconomy; }
    public String getCity() {  return city;  }
    public String getCountry() { return country; }
    public String getPostDate() { return postDate; }
    public String getImageUrl() { return imageUrl; }
    public String getHeuristic() { return heuristic; }
    public String getRating() { return rating; }
    public PriceModel getPriceList() { return priceList; }
    public String getItemTitle() { return itemTitle; }
    public String getSentDate() {  return sentDate; }
    public PriceModel getMaDifference() { return maDifference; }
    public KilometersList getKmsList() {  return kmsList; }
    public boolean getMaShow() { return maShow;}
    public String getMaKeyword() { return ma_keyword; }
    public String getHrefUrl() { return hrefUrl;  }
    //public RawValues getRawValues() { return rawValues; }
}
