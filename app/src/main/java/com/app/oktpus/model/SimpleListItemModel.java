package com.app.oktpus.model;

import com.app.oktpus.constants.ListItemCheckType;

/**
 * Created by Gyandeep on 19/11/17.
 */

public class SimpleListItemModel extends Object{

    private String title;
    private boolean isSelected = false;
    private ListItemCheckType itemCheckType;

    public SimpleListItemModel(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public ListItemCheckType getItemCheckType() {
        return itemCheckType;
    }

    public void setItemCheckType(ListItemCheckType itemCheckType) {
        this.itemCheckType = itemCheckType;
    }
}
