package com.app.oktpus.listener;

import com.app.oktpus.adapter.MultiSelectItemSearchViewAdapter;

/**
 * Created by Gyandeep on 16/11/16.
 */

public interface ListViewNotifier {

    void fetchAdapter(String keyName, MultiSelectItemSearchViewAdapter adapter);
    void fetchMinMax(String keyName, int minRawValue, int maxRawValue, String minValue, String maxValue, String displayFormat);
}