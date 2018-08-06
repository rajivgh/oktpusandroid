package com.app.oktpus.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Gyandeep on 30/11/16.
 */

public class RequestModelAttributes {

    @SerializedName("attribute[city][must_have]")
    private String attributeCityMusthave;

    @SerializedName("attribute[make][must_have]")
    private String attributeMakeMusthave;

    @SerializedName("attribute[model][must_have]")
    private String attributeModelMusthave;

    @SerializedName("attribute[price][must_have]")
    private String attributePriceMusthave;

    @SerializedName("attribute[kilometers][must_have]")
    private String attributeKilometersMusthave;

    @SerializedName("attribute[year][must_have]")
    private String attributeYearMusthave;

    @SerializedName("attribute[body][must_have]")
    private String attributeBodyMusthave;

    @SerializedName("attribute[transmission][must_have]")
    private String attributeTransmissionMusthave;

    @SerializedName("attribute[colour][must_have]")
    private String attributeColourMusthave;

    @SerializedName("attribute[price][value][min]")
    private String attributePriceValueMin;

    @SerializedName("attribute[price][value][max]")
    private String attributePriceValueMax;

    @SerializedName("attribute[kilometers][value][min]")
    private String attributeKilometersValueMin;

    @SerializedName("attribute[kilometers][value][max]")
    private String attributeKilometersValueMax;

    @SerializedName("attribute[year][value][min]")
    private String attributeYearValueMin;

    @SerializedName("attribute[price][value][max]")
    private String attributeYearValueMax;

    @SerializedName("attribute[city][values][value][]")
    private String attributeCityValueList;
    @SerializedName("attribute[city][values][group][]")
    private List<String> attributeCityGroupList;

    @SerializedName("attribute[make][values][value][]")
    private String attributeMakeValueList;
    @SerializedName("attribute[make][values][group][]")
    private String attributeMakeGroupList;

    @SerializedName("attribute[model][values][value][]")
    private String attributeModelValueList;
    @SerializedName("attribute[model][values][group][]")
    private String attributeModelGroupList;

    @SerializedName("attribute[body][values][value][]")
    private String attributeBodyValueList;
    @SerializedName("attribute[body][values][group][]")
    private String attributeBodyGroupList;

    @SerializedName("attribute[transmission][values][value][]")
    private String attributeTransmissionValueList;
    @SerializedName("attribute[transmission][values][group][]")
    private String attributeTransmissionGroupList;

    @SerializedName("attribute[colour][values][value][]")
    private String attributeColourValueList;
    @SerializedName("attribute[colour][values][group][]")
    private String attributeColourGroupList;

    public void setAttributeModelValueList(String attributeModelValueList) {
        this.attributeModelValueList = attributeModelValueList;
    }

    public void setAttributeModelGroupList(String attributeModelGroupList) {
        this.attributeModelGroupList = attributeModelGroupList;
    }

    public void setAttributeBodyValueList(String attributeBodyValueList) {
        this.attributeBodyValueList = attributeBodyValueList;
    }

    public void setAttributeBodyGroupList(String attributeBodyGroupList) {
        this.attributeBodyGroupList = attributeBodyGroupList;
    }

    public void setAttributeTransmissionValueList(String attributeTransmissionValueList) {
        this.attributeTransmissionValueList = attributeTransmissionValueList;
    }

    public void setAttributeTransmissionGroupList(String attributeTransmissionGroupList) {
        this.attributeTransmissionGroupList = attributeTransmissionGroupList;
    }

    public void setAttributeColourValueList(String attributeColourValueList) {
        this.attributeColourValueList = attributeColourValueList;
    }

    public void setAttributeColourGroupList(String attributeColourGroupList) {
        this.attributeColourGroupList = attributeColourGroupList;
    }

    public void setAttributeCityMusthave(String attributeCityMusthave) {
        this.attributeCityMusthave = attributeCityMusthave;
    }

    public void setAttributeMakeMusthave(String attributeMakeMusthave) {
        this.attributeMakeMusthave = attributeMakeMusthave;
    }

    public void setAttributeModelMusthave(String attributeModelMusthave) {
        this.attributeModelMusthave = attributeModelMusthave;
    }

    public void setAttributePriceMusthave(String attributePriceMusthave) {
        this.attributePriceMusthave = attributePriceMusthave;
    }

    public void setAttributeKilometersMusthave(String attributeKilometersMusthave) {
        this.attributeKilometersMusthave = attributeKilometersMusthave;
    }

    public void setAttributeYearMusthave(String attributeYearMusthave) {
        this.attributeYearMusthave = attributeYearMusthave;
    }

    public void setAttributeBodyMusthave(String attributeBodyMusthave) {
        this.attributeBodyMusthave = attributeBodyMusthave;
    }

    public void setAttributeTransmissionMusthave(String attributeTransmissionMusthave) {
        this.attributeTransmissionMusthave = attributeTransmissionMusthave;
    }

    public void setAttributeColourMusthave(String attributeColourMusthave) {
        this.attributeColourMusthave = attributeColourMusthave;
    }

    public void setAttributePriceValueMin(String attributePriceValueMin) {
        this.attributePriceValueMin = attributePriceValueMin;
    }

    public void setAttributePriceValueMax(String attributePriceValueMax) {
        this.attributePriceValueMax = attributePriceValueMax;
    }

    public void setAttributeKilometersValueMin(String attributeKilometersValueMin) {
        this.attributeKilometersValueMin = attributeKilometersValueMin;
    }

    public void setAttributeKilometersValueMax(String attributeKilometersValueMax) {
        this.attributeKilometersValueMax = attributeKilometersValueMax;
    }

    public void setAttributeYearValueMin(String attributeYearValueMin) {
        this.attributeYearValueMin = attributeYearValueMin;
    }

    public void setAttributeYearValueMax(String attributeYearValueMax) {
        this.attributeYearValueMax = attributeYearValueMax;
    }

    public void setAttributeCityValueList(String attributeCityValueList) {
        this.attributeCityValueList = attributeCityValueList;
    }

    public void setAttributeCityGroupList(List<String> attributeCityGroupList) {
        this.attributeCityGroupList = attributeCityGroupList;
    }

    public void setAttributeMakeValueList(String attributeMakeValueList) {
        this.attributeMakeValueList = attributeMakeValueList;
    }

    public void setAttributeMakeGroupList(String attributeMakeGroupList) {
        this.attributeMakeGroupList = attributeMakeGroupList;
    }


}
