package com.app.oktpus.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyandeep on 7/12/16.
 */

public class AttributeModelEditSearch implements Parcelable{

    @SerializedName("is_group")
    private int isGroup;
    @SerializedName("id")
    private int id;
    @SerializedName("value")
    private String value;

    protected AttributeModelEditSearch(Parcel in) {
        isGroup = in.readInt();
        id = in.readInt();
        value = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(isGroup);
        dest.writeInt(id);
        dest.writeString(value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AttributeModelEditSearch> CREATOR = new Creator<AttributeModelEditSearch>() {
        @Override
        public AttributeModelEditSearch createFromParcel(Parcel in) {
            return new AttributeModelEditSearch(in);
        }

        @Override
        public AttributeModelEditSearch[] newArray(int size) {
            return new AttributeModelEditSearch[size];
        }
    };

    public int getIsGroup() { return isGroup; }
    public void setIsGroup(int isGroup) { this.isGroup = isGroup; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}