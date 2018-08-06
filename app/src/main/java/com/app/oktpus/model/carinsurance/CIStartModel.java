package com.app.oktpus.model.carinsurance;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.app.oktpus.constants.Flags;
import com.app.oktpus.fragment.CIStartFragment;

import java.util.ArrayList;
import java.util.List;

import wizardlib.model.ModelCallbacks;
import wizardlib.model.Page;
import wizardlib.model.ReviewItem;

/**
 * Created by Gyandeep on 16/11/17.
 */

public class CIStartModel extends Page {

    protected CIStartModel(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
        setData();
    }

    @Override
    public Fragment createFragment() {
        return CIStartFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        ArrayList<String> rItems = new ArrayList<>();
        List<QuestionItems> qItems = new ArrayList<>();
        for(QuestionItems item: (ArrayList<QuestionItems>)mData.getSerializable(SIMPLE_DATA_KEY)) {
            if(item.getValue().length() > 0) {
                rItems.add(item.getValue());
                qItems.add(item);
            }
        }

        if(rItems.size() > 0)
            dest.add(new ReviewItem(getTitle(), TextUtils.join(",",rItems), qItems, getKey()));
    }

    @Override
    public boolean isCompleted() {
        return !TextUtils.isEmpty(mData.getString(SIMPLE_DATA_KEY));
    }

    public ArrayList<QuestionItems> getModelData() {
        return (ArrayList<QuestionItems>) mData.getSerializable(SIMPLE_DATA_KEY);
    }

    public void setData() {
        ArrayList<QuestionItems> items = new ArrayList<QuestionItems>(){{
            add(new QuestionItems(){{
                setQuestion("Do you currently have car insurance?");
                setOptions(new ArrayList<String>(){{
                    add("Yes, I have insurance");
                    add("No, I don't");
                }});
                setParamKey(Flags.CarInsurance.Keys.HAVE_INSURANCE);
            }});
        }};

        mData.putSerializable(SIMPLE_DATA_KEY, items);
    }

}
