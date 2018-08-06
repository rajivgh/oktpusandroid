package com.app.oktpus.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gyandeep on 17/7/17.
 */

public class CarPartsResponseList implements Parcelable{

    public CarPartsResponseList(){};

    protected CarPartsResponseList(Parcel in) {}

    public static final Creator<CarPartsResponseList> CREATOR = new Creator<CarPartsResponseList>() {
        @Override
        public CarPartsResponseList createFromParcel(Parcel in) {
            return new CarPartsResponseList(in);
        }

        @Override
        public CarPartsResponseList[] newArray(int size) {
            return new CarPartsResponseList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}