package com.app.oktpus.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyandeep on 15/12/16.
 */

public class EditSearchRangeAttributes implements Parcelable{

    @SerializedName("min")
    private String min;
    @SerializedName("max")
    private String max;
    @SerializedName("must_have")
    private boolean mustHave;
    @SerializedName("display_format")
    private String displayFormat;

    protected EditSearchRangeAttributes(Parcel in) {
        min = in.readString();
        max = in.readString();
        mustHave = in.readByte() != 0;
        displayFormat = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(min);
        dest.writeString(max);
        dest.writeByte((byte) (mustHave ? 1 : 0));
        dest.writeString(displayFormat);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EditSearchRangeAttributes> CREATOR = new Creator<EditSearchRangeAttributes>() {
        @Override
        public EditSearchRangeAttributes createFromParcel(Parcel in) {
            return new EditSearchRangeAttributes(in);
        }

        @Override
        public EditSearchRangeAttributes[] newArray(int size) {
            return new EditSearchRangeAttributes[size];
        }
    };

    public String getMin() { return min; }
    public String getMax() {
        return max;
    }
    public String getDisplayFormat() {
        return displayFormat;
    }

}