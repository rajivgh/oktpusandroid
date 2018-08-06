package com.app.oktpus.listener;

/**
 * Created by Gyandeep on 28/11/16.
 */

public interface OnClickSavedSearchItem {

    void onItemClick(String searchID, int statusID, int operationType, Object serializedValues);
}