package com.app.oktpus.listener;

import com.app.oktpus.constants.ModelStatus;
import com.app.oktpus.model.AttrValSpinnerModel;

import java.util.List;

/**
 * Created by Gyandeep on 13/11/17.
 */

public interface SearchFilterModelListener {
    public void modelDataFromNetworkListener(ModelStatus status, List<AttrValSpinnerModel> data);
    public void modelStatusListner(ModelStatus status);
}
