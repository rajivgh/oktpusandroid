package com.app.oktpus.model.carinsurance;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import wizardlib.model.ModelCallbacks;
import wizardlib.model.Page;
import wizardlib.model.ReviewItem;

/**
 * Created by Gyandeep on 8/11/17.
 */

public class SingleChoiceItemModel extends Page {
    protected Map<String, ArrayList<String>> mQuestionItem = new HashMap<>();

    public SingleChoiceItemModel(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return SingleChoiceFragment.create(getKey());
    }

    public ArrayList<String> getOptionsAt(String itemKey) {
        return mQuestionItem.get(itemKey);
    }

    public int getQuestionsCount() {
        return mQuestionItem.size();
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem(getTitle(), mData.getString(SIMPLE_DATA_KEY), null, getKey()));
    }

    @Override
    public boolean isCompleted() {
        return !TextUtils.isEmpty(mData.getString(SIMPLE_DATA_KEY));
    }

    public SingleChoiceItemModel setQuestionItems(Map<String, ArrayList<String>> items) {
        mQuestionItem.putAll(items);
        return this;
    }

    public SingleChoiceItemModel setValue(String value) {
        mData.putString(SIMPLE_DATA_KEY, value);
        return this;
    }
}

