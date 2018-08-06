package com.app.oktpus.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyandeep on 6/10/16.
 */

public class AttrValSpinnerModel implements Parcelable{

    @SerializedName("id")
    private int id;
    @SerializedName("value")
    private String value;
    @SerializedName("integer_representation")
    private int integer_representation;
    @SerializedName("group")
    private int group;

    @SerializedName("attribute_name")
    private String attrbuteKey;
    @SerializedName("is_group")
    private int isGroup;

    private boolean selected, tmpSelected;
    private String label;
    private String count;

    public AttrValSpinnerModel(){}

    protected AttrValSpinnerModel(Parcel in) {
        id = in.readInt();
        value = in.readString();
        integer_representation = in.readInt();
        group = in.readInt();
        attrbuteKey = in.readString();
        isGroup = in.readInt();
        selected = in.readByte() != 0;
        tmpSelected = in.readByte() != 0;
        label = in.readString();
        count = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(value);
        dest.writeInt(integer_representation);
        dest.writeInt(group);
        dest.writeString(attrbuteKey);
        dest.writeInt(isGroup);
        dest.writeByte((byte) (selected ? 1 : 0));
        dest.writeByte((byte) (tmpSelected ? 1 : 0));
        dest.writeString(label);
        dest.writeString(count);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AttrValSpinnerModel> CREATOR = new Creator<AttrValSpinnerModel>() {
        @Override
        public AttrValSpinnerModel createFromParcel(Parcel in) {
            return new AttrValSpinnerModel(in);
        }

        @Override
        public AttrValSpinnerModel[] newArray(int size) {
            return new AttrValSpinnerModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getInteger_representation() {
        return integer_representation;
    }

    public void setInteger_representation(int integer_representation) {
        this.integer_representation = integer_representation;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public String getAttrbuteKey() {
        return attrbuteKey;
    }

    public void setAttrbuteKey(String attrbuteKey) {
        this.attrbuteKey = attrbuteKey;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public int getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(int isGroup) {
        this.isGroup = isGroup;
    }

    public boolean isTmpSelected() {
        return tmpSelected;
    }

    public void setTmpSelected(boolean tmpSelected) {
        this.tmpSelected = tmpSelected;
    }
}
