package com.app.oktpus.model.wmcw;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.app.oktpus.constants.Flags;
import com.app.oktpus.constants.ListViewType;
import com.app.oktpus.fragment.wmcw.WMCWContactFragment;
import com.app.oktpus.model.carinsurance.QuestionItems;

import java.util.ArrayList;
import java.util.List;

import wizardlib.model.ModelCallbacks;
import wizardlib.model.Page;
import wizardlib.model.ReviewItem;

/**
 * Created by Gyandeep on 13/3/18.
 */

public class WMCWContactModel extends Page {

    protected WMCWContactModel(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
        setData();
    }

    @Override
    public Fragment createFragment() {
        return WMCWContactFragment.create(getKey());
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
                setQuestion("Email");
                setViewType(ListViewType.VIEW_EDIT_FIELD);
                setParamKey(Flags.WMCWRequestParams.PERSON_EMAIL);
            }});
            add(new QuestionItems(){{
                setQuestion("First Name");
                setViewType(ListViewType.VIEW_EDIT_FIELD);
                setParamKey(Flags.WMCWRequestParams.PERSON_FNAME);
            }});
            add(new QuestionItems(){{
                setQuestion("Last Name");
                setViewType(ListViewType.VIEW_EDIT_FIELD);
                setParamKey(Flags.WMCWRequestParams.PERSON_LNAME);
            }});
        }};

        mData.putSerializable(SIMPLE_DATA_KEY, items);
    }
}