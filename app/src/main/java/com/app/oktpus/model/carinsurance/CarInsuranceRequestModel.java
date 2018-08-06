package com.app.oktpus.model.carinsurance;

/**
 * Created by Gyandeep on 21/11/17.
 */

public class CarInsuranceRequestModel {

    private String key;
    private String item;
    private Object value;

    public CarInsuranceRequestModel(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /*public CarInsuranceRequestModel(String item, String value, String key) {
        this.item = item;
        this.value = value;
        this.key = key;
    }*/

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
