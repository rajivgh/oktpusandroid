package com.app.oktpus.listener;

import com.app.oktpus.model.AttrValSpinnerModel;

import java.util.List;
import java.util.Map;

/**
 * Created by Gyandeep on 13/11/17.
 */

public interface SearchFilterAPIListener {

    public void getFilterAttributeDataFromNetwork(Map<String, List<AttrValSpinnerModel>> data);
}
