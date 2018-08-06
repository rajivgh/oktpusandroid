package com.app.oktpus.listener;

import android.os.Bundle;

import com.app.oktpus.model.AttrValSpinnerModel;
import com.app.oktpus.responseModel.Config.CommonFields;

/**
 * Created by Gyandeep on 14/2/17.
 */

public interface OnSearchActivityCalls {
    public void setRequestData(Bundle requestBundle);
    public void searchBarEvent(String keywords, boolean isFromResultScreen);
    public void locationUpdate();
    public void updateGeoCities(String cities);
    public void syncTagLayout(boolean isFromResultScreen, boolean isCreateTagRequest,
                              AttrValSpinnerModel attr, CommonFields rangeField, boolean isMultiSelectView, String keyname);
    public void clearAllTags();
}