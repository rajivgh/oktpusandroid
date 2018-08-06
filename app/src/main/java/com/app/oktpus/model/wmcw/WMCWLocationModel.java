package com.app.oktpus.model.wmcw;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.app.oktpus.constants.Countries;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.constants.ListViewType;

import com.app.oktpus.fragment.wmcw.WMCWLocationFragment;
import com.app.oktpus.model.carinsurance.QuestionItems;

import java.util.ArrayList;
import java.util.List;

import wizardlib.model.ModelCallbacks;
import wizardlib.model.Page;
import wizardlib.model.ReviewItem;

/**
 * Created by Gyandeep on 13/3/18.
 */

public class WMCWLocationModel extends Page {

    protected WMCWLocationModel(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
        setData();
    }

    @Override
    public Fragment createFragment() {
        return WMCWLocationFragment.create(getKey());
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
                setQuestion("City");
                setViewType(ListViewType.VIEW_EDIT_FIELD);
                setParamKey(Flags.WMCWRequestParams.PERSON_CITY);
            }});
            add(new QuestionItems(){{
                setQuestion("State/Province");
                setViewType(ListViewType.VIEW_EDIT_FIELD);
                setParamKey(Flags.WMCWRequestParams.PERSON_STATE);
            }});
            add(new QuestionItems(){{
                setViewType(ListViewType.VIEW_SIMPLE_POPUP);
                setQuestion("Country");
                setmPopupOptions(new Countries().getList());
                setParamKey(Flags.WMCWRequestParams.PERSON_COUNTRY);
            }});
        }};

        mData.putSerializable(SIMPLE_DATA_KEY, items);
    }
}
