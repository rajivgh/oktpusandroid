package com.app.oktpus.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Gyandeep on 16/12/16.
 */

public class AttributeRequestForm implements Parcelable{
    /***NR***/
    private String kmsMinVal;
    private String kmsMaxVal;
    private String yearMinVal;
    private String yearMaxVal;
    private String priceMinVal;
    private String priceMaxVal;
    private String kmsMinRawValue="", kmsMaxRawValue="", yearMinRawValue="", yearMaxRawValue="", priceMinRawValue="", priceMaxRawValue="";
    private ArrayList<String> colourValues, cityValues, bodyValues, transmissionValues, makeValues, modelValues,
            cityGroup, makeGroup, modelGroup, bodyGroup, colourGroup, transmissionGroup;
    private String kilometersFormat, priceFormat;

    protected AttributeRequestForm(Parcel in) {
        kmsMinVal = in.readString();
        kmsMaxVal = in.readString();
        yearMinVal = in.readString();
        yearMaxVal = in.readString();
        priceMinVal = in.readString();
        priceMaxVal = in.readString();
        kmsMinRawValue = in.readString();
        kmsMaxRawValue = in.readString();
        yearMinRawValue = in.readString();
        yearMaxRawValue = in.readString();
        priceMinRawValue = in.readString();
        priceMaxRawValue = in.readString();
        colourValues = in.createStringArrayList();
        cityValues = in.createStringArrayList();
        bodyValues = in.createStringArrayList();
        transmissionValues = in.createStringArrayList();
        makeValues = in.createStringArrayList();
        modelValues = in.createStringArrayList();
        cityGroup = in.createStringArrayList();
        makeGroup = in.createStringArrayList();
        modelGroup = in.createStringArrayList();
        bodyGroup = in.createStringArrayList();
        colourGroup = in.createStringArrayList();
        transmissionGroup = in.createStringArrayList();
        kilometersFormat = in.readString();
        priceFormat = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(kmsMinVal);
        dest.writeString(kmsMaxVal);
        dest.writeString(yearMinVal);
        dest.writeString(yearMaxVal);
        dest.writeString(priceMinVal);
        dest.writeString(priceMaxVal);
        dest.writeString(kmsMinRawValue);
        dest.writeString(kmsMaxRawValue);
        dest.writeString(yearMinRawValue);
        dest.writeString(yearMaxRawValue);
        dest.writeString(priceMinRawValue);
        dest.writeString(priceMaxRawValue);
        dest.writeStringList(colourValues);
        dest.writeStringList(cityValues);
        dest.writeStringList(bodyValues);
        dest.writeStringList(transmissionValues);
        dest.writeStringList(makeValues);
        dest.writeStringList(modelValues);
        dest.writeStringList(cityGroup);
        dest.writeStringList(makeGroup);
        dest.writeStringList(modelGroup);
        dest.writeStringList(bodyGroup);
        dest.writeStringList(colourGroup);
        dest.writeStringList(transmissionGroup);
        dest.writeString(kilometersFormat);
        dest.writeString(priceFormat);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AttributeRequestForm> CREATOR = new Creator<AttributeRequestForm>() {
        @Override
        public AttributeRequestForm createFromParcel(Parcel in) {
            return new AttributeRequestForm(in);
        }

        @Override
        public AttributeRequestForm[] newArray(int size) {
            return new AttributeRequestForm[size];
        }
    };

    public String getPriceMaxVal() {
        return priceMaxVal;
    }

    public void setPriceMaxVal(String priceMaxVal) {
        this.priceMaxVal = priceMaxVal;
    }

    public String getKmsMinRawValue() {
        return kmsMinRawValue;
    }

    public void setKmsMinRawValue(String kmsMinRawValue) {
        this.kmsMinRawValue = kmsMinRawValue;
    }

    public String getKmsMaxRawValue() {
        return kmsMaxRawValue;
    }

    public void setKmsMaxRawValue(String kmsMaxRawValue) {
        this.kmsMaxRawValue = kmsMaxRawValue;
    }

    public String getYearMinRawValue() {
        return yearMinRawValue;
    }

    public void setYearMinRawValue(String yearMinRawValue) {
        this.yearMinRawValue = yearMinRawValue;
    }

    public String getYearMaxRawValue() {
        return yearMaxRawValue;
    }

    public void setYearMaxRawValue(String yearMaxRawValue) {
        this.yearMaxRawValue = yearMaxRawValue;
    }

    public String getPriceMinRawValue() {
        return priceMinRawValue;
    }

    public void setPriceMinRawValue(String priceMinRawValue) {
        this.priceMinRawValue = priceMinRawValue;
    }

    public String getPriceMaxRawValue() {
        return priceMaxRawValue;
    }

    public void setPriceMaxRawValue(String priceMaxRawValue) {
        this.priceMaxRawValue = priceMaxRawValue;
    }

    public ArrayList<String> getColourValues() {
        return colourValues;
    }

    public void setColourValues(ArrayList<String> colourValues) {
        this.colourValues = colourValues;
    }

    public ArrayList<String> getCityValues() {
        return cityValues;
    }

    public void setCityValues(ArrayList<String> cityValues) {
        this.cityValues = cityValues;
    }

    public ArrayList<String> getBodyValues() {
        return bodyValues;
    }

    public void setBodyValues(ArrayList<String> bodyValues) {
        this.bodyValues = bodyValues;
    }

    public ArrayList<String> getTransmissionValues() {
        return transmissionValues;
    }

    public void setTransmissionValues(ArrayList<String> transmissionValues) {
        this.transmissionValues = transmissionValues;
    }

    public ArrayList<String> getMakeValues() {
        return makeValues;
    }

    public void setMakeValues(ArrayList<String> makeValues) {
        this.makeValues = makeValues;
    }

    public ArrayList<String> getModelValues() {
        return modelValues;
    }

    public void setModelValues(ArrayList<String> modelValues) {
        this.modelValues = modelValues;
    }

    public ArrayList<String> getCityGroup() {
        return cityGroup;
    }

    public void setCityGroup(ArrayList<String> cityGroup) {
        this.cityGroup = cityGroup;
    }

    public ArrayList<String> getMakeGroup() {
        return makeGroup;
    }

    public void setMakeGroup(ArrayList<String> makeGroup) {
        this.makeGroup = makeGroup;
    }

    public ArrayList<String> getModelGroup() {
        return modelGroup;
    }

    public void setModelGroup(ArrayList<String> modelGroup) {
        this.modelGroup = modelGroup;
    }

    public ArrayList<String> getBodyGroup() {
        return bodyGroup;
    }

    public void setBodyGroup(ArrayList<String> bodyGroup) {
        this.bodyGroup = bodyGroup;
    }

    public ArrayList<String> getColourGroup() {
        return colourGroup;
    }

    public void setColourGroup(ArrayList<String> colourGroup) {
        this.colourGroup = colourGroup;
    }

    public ArrayList<String> getTransmissionGroup() {
        return transmissionGroup;
    }

    public void setTransmissionGroup(ArrayList<String> transmissionGroup) {
        this.transmissionGroup = transmissionGroup;
    }

    public String getKilometersFormat() {
        return kilometersFormat;
    }

    public void setKilometersFormat(String kilometersFormat) {
        this.kilometersFormat = kilometersFormat;
    }

    public String getPriceFormat() {
        return priceFormat;
    }

    public void setPriceFormat(String priceFormat) {
        this.priceFormat = priceFormat;
    }
}