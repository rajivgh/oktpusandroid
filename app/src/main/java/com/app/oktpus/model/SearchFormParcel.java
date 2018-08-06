package com.app.oktpus.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gyandeep on 6/12/16.
 */

public class SearchFormParcel implements Parcelable{

    public Bundle attributeKeyValues;
    /*public ArrayList<String> cityValues, makeValues, modelValues,
            cityGroup, makeGroup, modelGroup;*/

    public String mGroup = "0", mId = "0";
    public int idCount = 0, groupCount = 0;


    protected SearchFormParcel(Parcel in) {
        attributeKeyValues = in.readBundle();
        mGroup = in.readString();
        mId = in.readString();
        idCount = in.readInt();
        groupCount = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(attributeKeyValues);
        dest.writeString(mGroup);
        dest.writeString(mId);
        dest.writeInt(idCount);
        dest.writeInt(groupCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SearchFormParcel> CREATOR = new Creator<SearchFormParcel>() {
        @Override
        public SearchFormParcel createFromParcel(Parcel in) {
            return new SearchFormParcel(in);
        }

        @Override
        public SearchFormParcel[] newArray(int size) {
            return new SearchFormParcel[size];
        }
    };
}
