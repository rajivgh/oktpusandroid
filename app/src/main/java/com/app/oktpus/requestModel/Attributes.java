package com.app.oktpus.requestModel;

/**
 * Created by Gyandeep on 30/11/16.
 */

public class Attributes {
    @CustomSerializedName("city")
    private FlagsMustHave city;
    @CustomSerializedName("make")
    private FlagsMustHave make;
    @CustomSerializedName("country")
    private FlagsMustHave country;
    @CustomSerializedName("model")
    private FlagsMustHave model;
    @CustomSerializedName("price")
    private FlagsMustHave price;
    @CustomSerializedName("year")
    private FlagsMustHave year;
    @CustomSerializedName("kilometers")
    private FlagsMustHave kilometers;
    @CustomSerializedName("doors")
    private FlagsMustHave doors;
    @CustomSerializedName("drivetrain")
    private FlagsMustHave drivetrain;
    @CustomSerializedName("body")
    private FlagsMustHave body;
    @CustomSerializedName("transmission")
    private FlagsMustHave transmission;
    @CustomSerializedName("colour")
    private FlagsMustHave colour;
    @CustomSerializedName("below_market_average_percent")
    private FlagsMustHave bmaPercent;
    @CustomSerializedName("image0")
    private FlagsMustHave img0;
    @CustomSerializedName("user_type")
    private FlagsMustHave userType;

    public void setUserType(FlagsMustHave userType) { this.userType = userType;}

    public void setCountry(FlagsMustHave country) {this.country = country; }

    public void setCity(FlagsMustHave city) {
        this.city = city;
    }

    public void setMake(FlagsMustHave make) {
        this.make = make;
    }

    public void setModel(FlagsMustHave model) {
        this.model = model;
    }

    public void setPrice(FlagsMustHave price) {
        this.price = price;
    }

    public void setYear(FlagsMustHave year) {
        this.year = year;
    }

    public void setKilometers(FlagsMustHave kilometers) {
        this.kilometers = kilometers;
    }

    public void setBody(FlagsMustHave body) {
        this.body = body;
    }

    public void setTransmission(FlagsMustHave transmission) {
        this.transmission = transmission;
    }

    public void setBmaPercent(FlagsMustHave bmaPercent) { this.bmaPercent = bmaPercent; }

    public void setImage0(FlagsMustHave img0) { this.img0 = img0; }

    public void setColour(FlagsMustHave colour) {
        this.colour = colour;
    }

    public void setDoors(FlagsMustHave doors) {
        this.doors = doors;
    }

    public void setDrivetrain(FlagsMustHave drivetrain) {
        this.drivetrain = drivetrain;
    }
}
