package com.app.oktpus.requestModel;

/**
 * Created by Gyandeep on 30/11/16.
 */

public class Request {

    @CustomSerializedName("attribute")
    private Attributes attribute;
    @CustomSerializedName("sort")
    private SortAttributes sort;
    @CustomSerializedName("property")
    private PropertyValues property;
    @CustomSerializedName("save_submit")
    private Boolean saveSubmit;
    @CustomSerializedName("enable_notification")
    private Integer enableNotification;
    @CustomSerializedName("disable_search")
    private Integer disableSearch;
    @CustomSerializedName("edit_first_load")
    private Integer editFirstLoad;

    public void setDisableSearch(Integer disableSearch) {
        this.disableSearch = disableSearch;
    }

    public void setEnableNotification(int enableNotification) {
        this.enableNotification = enableNotification;
    }

    public void setSaveSubmit(Boolean saveSubmit) {
        this.saveSubmit = saveSubmit;
    }

    public void setProperty(PropertyValues property) {
        this.property = property;
    }

    public void setSort(SortAttributes sort) {
        this.sort = sort;
    }

    public void setAttribute(Attributes attribute) {
        this.attribute = attribute;
    }

    public void setEditFirstLoad(Integer editFirstLoad) {
        this.editFirstLoad = editFirstLoad;
    }

    @Override
    public String toString() {
        return new CustomDataType().encode(this);
    }
}
