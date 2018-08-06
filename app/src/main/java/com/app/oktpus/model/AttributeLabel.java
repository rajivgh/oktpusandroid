package com.app.oktpus.model;

/**
 * Created by Gyandeep on 5/10/16.
 */

public class AttributeLabel {

    private KeyLabel kilometers;
    private KeyLabel price;
    private KeyLabel year;
    private KeyLabel make;
    private KeyLabel model;
    private KeyLabel colour;
    private KeyLabel transmission;
    private KeyLabel body;
    private KeyLabel city;
    private KeyLabel country;
    private KeyLabel user_type;
    private KeyLabel doors;
    private KeyLabel drivetrain;
    //private KeyLabel accessories;

    public void setKilometers(KeyLabel kilometers) { this.kilometers = kilometers;}
    public KeyLabel getKilometers() { return this.kilometers; }

    public void setPrice(KeyLabel price) { this.price = price;}
    public KeyLabel getPrice() { return this.price; }

    public void setYear(KeyLabel year) { this.year = year;}
    public KeyLabel getYear() { return this.year; }

    public void setMake(KeyLabel make) { this.make = make;}
    public KeyLabel getMake() { return this.make; }

    public void setModel(KeyLabel model) { this.model = model;}
    public KeyLabel getModel() { return this.model; }

    public void setColour(KeyLabel colour) { this.colour = colour;}
    public KeyLabel getColour() { return this.colour; }

    public void setTransmission(KeyLabel transmission) { this.transmission = transmission;}
    public KeyLabel getTransmission() { return this.transmission; }

    public void setBody(KeyLabel kilometers) { this.body = body;}
    public KeyLabel getBody() { return this.body; }

    public void setCity(KeyLabel city) { this.city = city;}
    public KeyLabel getCity() { return this.city; }

    public KeyLabel getUser_type() {    return user_type; }
    public void setUser_type(KeyLabel user_type) { this.user_type = user_type; }

    public KeyLabel getCountry() {
        return country;
    }
    public void setCountry(KeyLabel country) {
        this.country = country;
    }

    public KeyLabel getDoors() { return doors; }
    public void setDoors(KeyLabel doors) { this.doors = doors; }

    public KeyLabel getDrivetrain() { return drivetrain; }
    public void setDrivetrain(KeyLabel drivetrain) { this.drivetrain = drivetrain; }
}