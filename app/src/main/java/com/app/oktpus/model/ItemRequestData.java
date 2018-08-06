package com.app.oktpus.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.app.oktpus.constants.Flags;
import com.app.oktpus.requestModel.AttrVal;
import com.app.oktpus.requestModel.AttributeValue;
import com.app.oktpus.requestModel.Attributes;
import com.app.oktpus.requestModel.FlagsFormat;
import com.app.oktpus.requestModel.FlagsLimit;
import com.app.oktpus.requestModel.FlagsValue;
import com.app.oktpus.requestModel.PropertyValues;
import com.app.oktpus.requestModel.Request;
import com.app.oktpus.requestModel.SortAttributes;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gyandeep on 13/11/16.
 */

public class ItemRequestData {


    private final int enableNotification, disableSearch, sort;
    private final boolean saveSubmit, image0;
    private final String kmsMaxVal, kmsMinVal, priceMaxVal, priceMinVal, yearMaxVal, yearMinVal, bmaMinVal, bmaMaxVal, bmaMinRaw, bmaMaxRaw,
            kmsMaxRaw, kmsMinRaw, priceMinRaw, priceMaxRaw, yearMinRaw, yearMaxRaw, searchID, userID, kilometersFormat, priceFormat;

    private final ArrayList<String> countryValues, countryGroups, cityValues, cityGroups, makeValues, makeGroups, modelValues, modelGroups,
            doorsValues, doorsGroups, drivetrainValues, drivetrainGroups, bodyValues, bodyGroups, transmissionValues, transmissionGroups, colourValues, colourGroups, userTypeValues, userTypeGroups;

    public ItemRequestData(Builder builder) {
        this.kmsMinVal = builder.kmsMinVal;        this.kmsMaxVal = builder.kmsMaxVal;
        this.kmsMinRaw = builder.kmsMinRaw;        this.kmsMaxRaw = builder.kmsMaxRaw;
        this.priceMinVal = builder.priceMinVal;    this.priceMaxVal = builder.priceMaxVal;
        this.priceMinRaw = builder.priceMinRaw;    this.priceMaxRaw = builder.priceMaxRaw;
        this.yearMinVal = builder.yearMinVal;      this.yearMaxVal = builder.yearMaxVal;
        this.yearMinRaw = builder.yearMinRaw;      this.yearMaxRaw = builder.yearMaxRaw;
        this.bmaMinVal = builder.bmaMinVal;        this.bmaMaxVal = builder.bmaMaxVal;
        this.bmaMinRaw = builder.bmaMinRaw;        this.bmaMaxRaw = builder.bmaMaxRaw;

        this.sort = builder.sort;
        this.saveSubmit = builder.saveSubmit;
        this.image0 = builder.image0;
        this.searchID = builder.searchID;
        this.enableNotification = builder.enableNotification;
        this.disableSearch = builder.disableSearch;
        this.userID = builder.userID;
        this.kilometersFormat = builder.kilometersFormat;
        this.priceFormat = builder.priceFormat;

        this.countryValues = builder.countryValues;     this.countryGroups = builder.countryGroups;
        this.cityValues = builder.cityValues;           this.cityGroups = builder.cityGroups;
        this.makeValues = builder.makeValues;           this.makeGroups = builder.makeGroups;
        this.modelValues = builder.modelValues;         this.modelGroups = builder.modelGroups;
        this.bodyValues = builder.bodyValues;           this.bodyGroups = builder.bodyGroups;
        this.transmissionValues = builder.transmissionValues;   this.transmissionGroups = builder.transmissionGroups;
        this.colourValues = builder.colourValues;       this.colourGroups = builder.colourGroups;
        this.userTypeValues = builder.userTypeValues;           this.userTypeGroups = builder.userTypeGroups;
        this.doorsValues = builder.doorsValues;           this.doorsGroups = builder.doorsGroups;
        this.drivetrainValues = builder.drivetrainValues;           this.drivetrainGroups = builder.drivetrainGroups;
    }

    public static class Builder implements Parcelable{
        private String kmsMaxVal, kmsMinVal, priceMaxVal, priceMinVal, yearMaxVal, yearMinVal, bmaMinVal, bmaMaxVal, bmaMinRaw, bmaMaxRaw,
                kmsMaxRaw, kmsMinRaw, priceMinRaw, priceMaxRaw, yearMinRaw, yearMaxRaw, searchID, userID, kilometersFormat, priceFormat;

        private ArrayList<String> countryValues, countryGroups, cityValues, cityGroups, makeValues, makeGroups, modelValues, modelGroups,
                bodyValues, bodyGroups, transmissionValues, transmissionGroups, colourValues, colourGroups, userTypeValues, userTypeGroups,
                doorsValues, doorsGroups, drivetrainValues, drivetrainGroups;
        private boolean saveSubmit, image0;
        private int enableNotification, disableSearch, sort;

        public Builder(){}
        protected Builder(Parcel in) {
            kmsMaxVal = in.readString();
            kmsMinVal = in.readString();
            priceMaxVal = in.readString();
            priceMinVal = in.readString();
            yearMaxVal = in.readString();
            yearMinVal = in.readString();
            kmsMaxRaw = in.readString();
            kmsMinRaw = in.readString();
            priceMinRaw = in.readString();
            priceMaxRaw = in.readString();
            yearMinRaw = in.readString();
            yearMaxRaw = in.readString();
            sort = in.readInt();
            searchID = in.readString();
            userID = in.readString();
            kilometersFormat = in.readString();
            priceFormat = in.readString();
            countryValues = in.createStringArrayList();
            countryGroups = in.createStringArrayList();
            cityValues = in.createStringArrayList();
            cityGroups = in.createStringArrayList();
            makeValues = in.createStringArrayList();
            makeGroups = in.createStringArrayList();
            modelValues = in.createStringArrayList();
            modelGroups = in.createStringArrayList();
            doorsValues = in.createStringArrayList();
            doorsGroups = in.createStringArrayList();
            drivetrainValues = in.createStringArrayList();
            drivetrainGroups = in.createStringArrayList();
            bodyValues = in.createStringArrayList();
            bodyGroups = in.createStringArrayList();
            transmissionValues = in.createStringArrayList();
            transmissionGroups = in.createStringArrayList();
            colourValues = in.createStringArrayList();
            colourGroups = in.createStringArrayList();
            userTypeValues = in.createStringArrayList();
            userTypeGroups = in.createStringArrayList();
            saveSubmit = in.readByte() != 0;
            image0 = in.readByte() != 0;
            enableNotification = in.readInt();
            disableSearch = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(kmsMaxVal);
            dest.writeString(kmsMinVal);
            dest.writeString(priceMaxVal);
            dest.writeString(priceMinVal);
            dest.writeString(yearMaxVal);
            dest.writeString(yearMinVal);
            dest.writeString(bmaMaxVal);
            dest.writeString(bmaMinVal);
            dest.writeString(kmsMaxRaw);
            dest.writeString(kmsMinRaw);
            dest.writeString(priceMinRaw);
            dest.writeString(priceMaxRaw);
            dest.writeString(yearMinRaw);
            dest.writeString(yearMaxRaw);
            dest.writeString(bmaMinRaw);
            dest.writeString(bmaMaxRaw);
            dest.writeInt(sort);
            dest.writeString(searchID);
            dest.writeString(userID);
            dest.writeString(kilometersFormat);
            dest.writeString(priceFormat);
            dest.writeStringList(countryValues);
            dest.writeStringList(countryGroups);
            dest.writeStringList(cityValues);
            dest.writeStringList(cityGroups);
            dest.writeStringList(makeValues);
            dest.writeStringList(makeGroups);
            dest.writeStringList(modelValues);
            dest.writeStringList(modelGroups);
            dest.writeStringList(doorsValues);
            dest.writeStringList(doorsGroups);
            dest.writeStringList(drivetrainValues);
            dest.writeStringList(drivetrainGroups);
            dest.writeStringList(bodyValues);
            dest.writeStringList(bodyGroups);
            dest.writeStringList(transmissionValues);
            dest.writeStringList(transmissionGroups);
            dest.writeStringList(colourValues);
            dest.writeStringList(colourGroups);
            dest.writeStringList(userTypeValues);
            dest.writeStringList(userTypeGroups);
            dest.writeByte((byte) (saveSubmit ? 1 : 0));
            dest.writeByte((byte) (image0 ? 1 : 0));
            dest.writeInt(enableNotification);
            dest.writeInt(disableSearch);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Builder> CREATOR = new Creator<Builder>() {
            @Override
            public Builder createFromParcel(Parcel in) {
                return new Builder(in);
            }

            @Override
            public Builder[] newArray(int size) {
                return new Builder[size];
            }
        };

        public Builder distanceMinVal(String kmsMinVal) {this.kmsMinVal = kmsMinVal; return this;}
        public Builder distanceMaxVal(String kmsMaxVal) {this.kmsMaxVal = kmsMaxVal; return this;}
        public Builder distanceMaxRaw(String kmsMaxRaw) {this.kmsMaxRaw = kmsMaxRaw; return this;}
        public Builder distanceMinRaw(String kmsMinRaw) {this.kmsMinRaw = kmsMinRaw; return this;}
        public Builder priceMinVal(String priceMinVal) {this.priceMinVal = priceMinVal; return this;}
        public Builder priceMaxVal(String priceMaxVal) {this.priceMaxVal = priceMaxVal; return this;}
        public Builder priceMinRaw(String priceMinRaw) {this.priceMinRaw = priceMinRaw; return this;}
        public Builder priceMaxRaw(String priceMaxRaw) {this.priceMaxRaw = priceMaxRaw; return this;}
        public Builder yearMinVal(String yearMinVal) {this.yearMinVal = yearMinVal; return this;}
        public Builder yearMaxVal(String yearMaxVal) {this.yearMaxVal = yearMaxVal; return this;}
        public Builder yearMinRaw(String yearMinRaw) {this.yearMinRaw = yearMinRaw; return this;}
        public Builder yearMaxRaw(String yearMaxRaw) {this.yearMaxRaw = yearMaxRaw; return this;}
        public Builder bmaMinVal(String bmaMinVal) {this.bmaMinVal = bmaMinVal; return this;}
        public Builder bmaMaxVal(String bmaMaxVal) {this.bmaMaxVal = bmaMaxVal; return this;}
        public Builder bmaMinRaw(String bmaMinRaw) {this.bmaMinRaw = bmaMinRaw; return this;}
        public Builder bmaMaxRaw(String bmaMaxRaw) {this.bmaMaxRaw = bmaMaxRaw; return this;}

        public Builder sort(int sort) {this.sort = sort; return this;}
        public Builder saveSubmit(boolean saveSubmit) {this.saveSubmit = saveSubmit; return this;}
        public Builder searchID(String searchID) {this.searchID = searchID; return this;}
        public Builder enableNotification(int enableNotification) {this.enableNotification = enableNotification; return this;}
        public Builder disableSearch(int disableSearch) { this.disableSearch = disableSearch; return this; }
        public Builder userID(String userID) {this.userID = userID; return this;}
        public Builder kilometersFormat(String kilometersFormat) {this.kilometersFormat = kilometersFormat; return this; }
        public Builder priceFormat(String priceFormat) { this.priceFormat = priceFormat; return this; }

        public Builder countryValueList(ArrayList<String> countryValues) {this.countryValues = countryValues; return this;}
        public Builder countryGroupList(ArrayList<String> countryGroups) {this.countryGroups = countryGroups; return this;}
        public Builder cityValueList(ArrayList<String> cityValues) {this.cityValues = cityValues; return this;}
        public Builder cityGroupList(ArrayList<String> cityGroups) {this.cityGroups = cityGroups; return this;}
        public Builder makeValueList(ArrayList<String> makeValues) {this.makeValues = makeValues; return this;}
        public Builder makeGroupList(ArrayList<String> makeGroups) {this.makeGroups = makeGroups; return this;}
        public Builder modelValueList(ArrayList<String> modelValues) {this.modelValues = modelValues; return this;}
        public Builder modelGroupList(ArrayList<String> modelGroups) {this.modelGroups = modelGroups; return this;}


        public Builder doorsValueList(ArrayList<String> doorsValues) {this.doorsValues = doorsValues; return this;}
        public Builder doorsGroupList(ArrayList<String> doorsGroups) {this.doorsGroups = doorsGroups; return this;}

        public Builder drivetrainValueList(ArrayList<String> drivetrainValues) {this.drivetrainValues = drivetrainValues; return this;}
        public Builder drivetrainGroupList(ArrayList<String> drivetrainGroups) {this.drivetrainGroups = drivetrainGroups; return this;}

        public Builder bodyValueList(ArrayList<String> bodyValues) {this.bodyValues = bodyValues; return this;}
        public Builder bodyGroupList(ArrayList<String> bodyGroups) {this.bodyGroups = bodyGroups; return this;}
        public Builder transmissionValueList(ArrayList<String> transmissionValues) {this.transmissionValues = transmissionValues; return this;}
        public Builder transmissionGroupList(ArrayList<String> transmissionGroups) {this.transmissionGroups = transmissionGroups; return this;}
        public Builder colourValueList(ArrayList<String> colourValues) {this.colourValues = colourValues; return this;}
        public Builder colourGroupList(ArrayList<String> colourGroups) {this.colourGroups = colourGroups; return this;}
        public Builder userTypeValueList(ArrayList<String> userTypeValues) {this.userTypeValues = userTypeValues; return this;}
        public Builder userTypeGroupList(ArrayList<String> userTypeGroups) {this.userTypeGroups = userTypeGroups; return this;}

        public Builder image0(boolean image0) { this.image0 = image0; return this;}

        public ItemRequestData build() {
            return new ItemRequestData(this);
        }
    }

    //Request data for count api calls and search result
    /*public String getParams(){

        StringBuilder builder = new StringBuilder();
        builder.append(Flags.Keys.ATTRIBUTE_CITY+"=1");
        builder.append("&"+Flags.Keys.ATTRIBUTE_MAKE+"=1");
        builder.append("&"+Flags.Keys.ATTRIBUTE_MODEL+"=1");
        builder.append("&"+Flags.Keys.ATTRIBUTE_PRICE+"=1");
        builder.append("&"+Flags.Keys.ATTRIBUTE_YEAR+"=1");
        builder.append("&"+Flags.Keys.ATTRIBUTE_KILOMETERS+"=1");
        builder.append("&"+Flags.Keys.ATTRIBUTE_BODY+"=1");
        builder.append("&"+Flags.Keys.ATTRIBUTE_TRANSMISSION+"=1");
        builder.append("&"+Flags.Keys.ATTRIBUTE_KMS_DISP_FORMAT+"=kilometers"); //variable
        builder.append("&"+Flags.Keys.ATTRIBUTE_PRICE_DISP_FORMAT+"=CAD");      //variable
        builder.append("&"+Flags.Keys.ATTRIBUTE_COLOUR+"=1");

        if(null != distanceMaxVal)   builder.append("&"+Flags.Keys.ATTRIBUTE_KMS_MAX+"="+ distanceMaxVal);        //variableNumber km
        if(null != distanceMinVal)   builder.append("&"+Flags.Keys.ATTRIBUTE_KMS_MIN+"="+distanceMinVal);         //above variableNumber km
        if(null != priceMaxVal) builder.append("&"+Flags.Keys.ATTRIBUTE_PRICE_MAX+"="+priceMaxVal);     //above CDN$ variableNumber
        if(null != priceMinVal) builder.append("&"+Flags.Keys.ATTRIBUTE_PRICE_MIN+"="+priceMinVal);     //CDN$ variableNumber
        if(null != yearMaxVal)  builder.append("&"+Flags.Keys.ATTRIBUTE_YEAR_MAX+"="+yearMaxVal);       //variableNumber
        if(null != yearMinVal)  builder.append("&"+Flags.Keys.ATTRIBUTE_YEAR_MIN+"="+yearMinVal);       //variableNumber and lower

        builder.append("&"+Flags.Keys.SORT_POST_DATE+"=post_date "+postDateOrder);

        if(saveSubmit == true) {
            builder.append("&"+Flags.Keys.SAVE_SUBMIT+"=true");
            builder.append("&"+Flags.Keys.PROPERTY_EDIT_SEARCH+"=Save"); //propEditSearch
            builder.append("&"+Flags.Keys.PROPERTY_SEARCH_ID+"="+searchID);  //searchID
        }

        if(null != searchID && -1 != enableNotification) {
            builder.append("&enable_notification="+enableNotification);
        }


        builder.append("&"+Flags.Keys.ATTRIBUTE_KMS_MAX+"="+ distanceMaxRaw);
        builder.append("&"+Flags.Keys.ATTRIBUTE_KMS_MIN+"="+distanceMinRaw);
        builder.append("&"+Flags.Keys.ATTRIBUTE_PRICE_MAX+"="+priceMaxRaw);
        builder.append("&"+Flags.Keys.ATTRIBUTE_PRICE_MIN+"="+priceMinRaw);
        builder.append("&"+Flags.Keys.ATTRIBUTE_YEAR_MAX+"="+yearMaxRaw);
        builder.append("&"+Flags.Keys.ATTRIBUTE_YEAR_MIN+"="+yearMinRaw);
        builder.append("&searchId="+searchID);
        builder.append("&userId="+userID);

        if(null != cityValues && (cityValues.size() > 0))
            for(String value : cityValues) {builder.append("&attribute[city][values][value][]="+value);}
        if(null != cityGroups && (cityGroups.size() > 0))
            for(String group : cityGroups) {builder.append("&attribute[city][values][group][]="+group);}

        if(null != makeValues && (makeValues.size() > 0))
            for(String value : makeValues) {builder.append("&attribute[make][values][value][]="+value);}
        if(null != makeGroups && (makeGroups.size() > 0))
            for(String group : makeGroups) {builder.append("&attribute[make][values][group][]="+group);}

        if(null != modelValues && (modelValues.size() > 0))
            for(String value : modelValues) {builder.append("&attribute[model][values][value][]="+value);}
        if(null != modelGroups && (modelGroups.size() > 0))
            for(String group : modelGroups) {builder.append("&attribute[model][values][group][]="+group);}

        if(null != bodyValues && (bodyValues.size() > 0))
            for(String value : bodyValues) {builder.append("&attribute[body][values][value][]="+value);}
        if(null != bodyGroups && (bodyGroups.size() > 0))
            for(String group : bodyGroups) {builder.append("&attribute[body][values][group][]="+group);}

        if(null != transmissionValues && (transmissionValues.size() > 0))
            for(String value : transmissionValues) {builder.append("&attribute[transmission][values][value][]="+value);}
        if(null != transmissionGroups && (transmissionGroups.size() > 0))
            for(String group : transmissionGroups) {builder.append("&attribute[transmission][values][group][]="+group);}

        if(null != colourValues && (colourValues.size() > 0))
            for(String value : colourValues) {builder.append("&attribute[colour][values][value][]="+value);}
        if(null != colourGroups && (colourGroups.size() > 0))
            for(String group : colourGroups) {builder.append("&attribute[colour][values][group][]="+group);}

        Log.d("_REQUESTDATA", builder.toString());
        return builder.toString();
    }*/

    public String getRequestData() {
        Attributes attributes = new Attributes();

        AttributeValue avCity = new AttributeValue();
        AttributeValue avMake = new AttributeValue();
        AttributeValue avModel = new AttributeValue();
        AttributeValue avBody = new AttributeValue();
        AttributeValue avTransmission = new AttributeValue();
        AttributeValue avColour = new AttributeValue();
        AttributeValue avImage0 = new AttributeValue();
        AttributeValue avUserType = new AttributeValue();
        AttributeValue avCountry = new AttributeValue();
        AttributeValue avDoors = new AttributeValue();
        AttributeValue avDriveTrain = new AttributeValue();

        avCountry.setValues(getAttrVal(countryValues, countryGroups));
        avCity.setValues(getAttrVal(cityValues, cityGroups));
        avMake.setValues(getAttrVal(makeValues, makeGroups));
        avModel.setValues(getAttrVal(modelValues, modelGroups));
        avBody.setValues(getAttrVal(bodyValues, bodyGroups));
        avTransmission.setValues(getAttrVal(transmissionValues, transmissionGroups));
        avColour.setValues(getAttrVal(colourValues, colourGroups));
        avUserType.setValues(getAttrVal(userTypeValues, userTypeGroups));
        avDoors.setValues(getAttrVal(doorsValues, doorsGroups));
        avDriveTrain.setValues(getAttrVal(drivetrainValues, drivetrainGroups));
        if(image0) avImage0.setValues(getAttrVal(image0));


        avCity.setMustHave(1); attributes.setCity(avCity);
        avMake.setMustHave(1); attributes.setMake(avMake);
        avModel.setMustHave(1); attributes.setModel(avModel);
        avBody.setMustHave(1); attributes.setBody(avBody);
        avTransmission.setMustHave(1); attributes.setTransmission(avTransmission);
        avColour.setMustHave(1); attributes.setColour(avColour);
        avImage0.setMustHave(1); attributes.setImage0(avImage0);
        avUserType.setMustHave(1); attributes.setUserType(avUserType);
        avCountry.setMustHave(1); attributes.setCountry(avCountry);
        avDoors.setMustHave(1); attributes.setDoors(avDoors);
        avDriveTrain.setMustHave(1); attributes.setDrivetrain(avDriveTrain);

        //Price
        FlagsLimit priceLimit1 = new FlagsLimit();
        if(null != priceMinVal) priceLimit1.setMin(priceMinVal);
        if(null != priceMaxVal) priceLimit1.setMax(priceMaxVal);
        FlagsLimit priceLimit2 = new FlagsLimit();
        priceLimit2.setMax(priceMaxRaw);
        priceLimit2.setMin(priceMinRaw);
        List<FlagsLimit> priceLimitList = new ArrayList<>();
        priceLimitList.add(priceLimit1);
        priceLimitList.add(priceLimit2);
        FlagsFormat priceFormat = new FlagsFormat();
        priceFormat.setDisplayFormat(this.priceFormat);
        priceFormat.setValue(priceLimitList);
        priceFormat.setMustHave(1); attributes.setPrice(priceFormat);

        FlagsLimit kmLimit = new FlagsLimit();
        if(null != kmsMaxVal) kmLimit.setMax(kmsMaxVal);
        if(null != kmsMinVal) kmLimit.setMin(kmsMinVal);
        FlagsLimit kmLimitRaw = new FlagsLimit();
        kmLimitRaw.setMax(kmsMaxRaw);
        kmLimitRaw.setMin(kmsMinRaw);
        List<FlagsLimit> kmLimitList = new ArrayList<>();
        kmLimitList.add(kmLimit);
        kmLimitList.add(kmLimitRaw);
        FlagsFormat kmFormat = new FlagsFormat();
        kmFormat.setDisplayFormat(kilometersFormat);
        kmFormat.setValue(kmLimitList);
        kmFormat.setMustHave(1);
        attributes.setKilometers(kmFormat);

        FlagsLimit yearLimit = new FlagsLimit();
        if(null != yearMaxVal)yearLimit.setMax(yearMaxVal);
        if(null != yearMinVal)yearLimit.setMin(yearMinVal);
        FlagsLimit yearLimitRaw = new FlagsLimit();
        yearLimitRaw.setMax(yearMaxRaw);
        yearLimitRaw.setMin(yearMinRaw);
        List<FlagsLimit> yearLimitList = new ArrayList<>();
        yearLimitList.add(yearLimit);
        yearLimitList.add(yearLimitRaw);
        FlagsValue yearValue = new FlagsValue();
        yearValue.setValue(yearLimitList);
        yearValue.setMustHave(1);
        attributes.setYear(yearValue);

        FlagsLimit bmaLimit = new FlagsLimit();
        if(null != bmaMaxVal)bmaLimit.setMax(bmaMaxVal);
        if(null != bmaMinVal)bmaLimit.setMin(bmaMinVal);
        FlagsLimit bmaLimitRaw = new FlagsLimit();
        bmaLimitRaw.setMax(bmaMaxRaw);
        bmaLimitRaw.setMin(bmaMinRaw);
        List<FlagsLimit> bmaLimitList = new ArrayList<>();
        bmaLimitList.add(bmaLimit);
        bmaLimitList.add(bmaLimitRaw);
        FlagsValue bmaValue = new FlagsValue();
        bmaValue.setValue(bmaLimitList);
        bmaValue.setMustHave(1);
        attributes.setBmaPercent(bmaValue);

        SortAttributes sortAttributes = new SortAttributes();

        switch(sort) {
            case Flags.Sort.Keys.NEWEST_ARRIVALS:   sortAttributes.setPostDate(Flags.Sort.Values.NEWEST_ARRIVALS);  break;
            case Flags.Sort.Keys.MOST_RELEVANT:     sortAttributes.setHeuristic(Flags.Sort.Values.MOST_RELEVANT);  break;
            case Flags.Sort.Keys.PRICE_HIGH_TO_LOW: sortAttributes.setPrice(Flags.Sort.Values.PRICE_HIGH_TO_LOW);  break;
            case Flags.Sort.Keys.PRICE_LOW_TO_HIGH: sortAttributes.setPrice(Flags.Sort.Values.PRICE_LOW_TO_HIGH);  break;
            case Flags.Sort.Keys.YEAR_NEW_TO_OLD:   sortAttributes.setYear(Flags.Sort.Values.YEAR_NEW_TO_OLD);  break;
            case Flags.Sort.Keys.YEAR_OLD_TO_NEW:   sortAttributes.setYear(Flags.Sort.Values.YEAR_OLD_TO_NEW);  break;

        }

        Request request = new Request();
        request.setAttribute(attributes);
        request.setEnableNotification(enableNotification);
        if(disableSearch != 0) request.setDisableSearch(disableSearch);
        request.setSort(sortAttributes);
        if(saveSubmit) {
            request.setSaveSubmit(saveSubmit);

            if(searchID != null) {
                PropertyValues pv = new PropertyValues("Save", searchID);
                /*pv.setSearchID(searchID);
                pv.setEditSearch("Save");*/
                request.setProperty(pv);
                request.setEditFirstLoad(1);
            }
            else {
                PropertyValues pv = new PropertyValues(1);
                //pv.setSaveSearch(1);
                request.setProperty(pv);
            }


        }

        Log.d("ItemRequestData", "Request data: "+request.toString());
        return request.toString();
    }

    private AttrVal getAttrVal(List<String> listValue, List<String> listGroup) {
        AttrVal valueGroup = null;
        if(null != listValue && listValue.size() > 0) {
            valueGroup = new AttrVal();
            List<String> valueList = new ArrayList<>();
            for(String value : listValue) { valueList.add(value); }
            valueGroup.setValue(valueList);
        }
        if(null != listGroup && listGroup.size() > 0) {
            if (null == valueGroup)
                valueGroup = new AttrVal();
            List<String> groupList = new ArrayList<>();
            for(String group : listGroup) { groupList.add(group); }
            valueGroup.setGroup(groupList);
        }
        return valueGroup;
    }

    private AttrVal getAttrVal(Boolean val) {
        AttrVal value = null;
        if(val != null) {
            value = new AttrVal();
            List<Integer> param = new ArrayList<>();
            param.add(1);
            value.setValueInt(param);
        }
        return value;
    }

    public byte[] paramsByteFormat(String params) {
        Log.d("Request JSON", params);
        return params.getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] stringToByteFormat(String params) {
        Log.d("Request JSON", params);
        return params.getBytes(StandardCharsets.UTF_8);
    }
}