package com.app.oktpus.listener;

import com.app.oktpus.model.AttrValSpinnerModel;

import java.util.Map;

/**
 * Created by Gyandeep on 18/10/16.
 */

public interface OnCheckBoxClickListener {

    void onCheckBoxItemClicked(String keyName, boolean checked, String intRep, String value,
                               String group, String id, int position);
    void onCheckBoxItemClicked(boolean checked, String value);

    void onCheckBoxItemClicked(String keyname, Map<Integer, AttrValSpinnerModel> attrMap, int intRep, AttrValSpinnerModel tmpAttr);
}