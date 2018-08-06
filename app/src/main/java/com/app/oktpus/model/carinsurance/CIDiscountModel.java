package com.app.oktpus.model.carinsurance;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.app.oktpus.constants.Flags;
import com.app.oktpus.constants.ListViewType;
import com.app.oktpus.fragment.CIDiscountFragment;

import java.util.ArrayList;
import java.util.List;

import wizardlib.model.ModelCallbacks;
import wizardlib.model.Page;
import wizardlib.model.ReviewItem;

/**
 * Created by Gyandeep on 20/11/17.
 */

public class CIDiscountModel extends Page {

    protected CIDiscountModel(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
        setData();
    }

    @Override
    public Fragment createFragment() {
        return CIDiscountFragment.create(getKey());
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
                setQuestion("Are you currently employed full time?");
                setOptions(new ArrayList<String>(){{
                    add("Yes");
                    add("No");
                }});
                setViewType(ListViewType.VIEW_SINGLE_CHOICE);
                setParamKey(Flags.CarInsurance.Keys.DISCOUNT_EMPLOYED_FULL_TIME);
            }});
            add(new QuestionItems(){{
                setQuestion("Are you active duty military or a veteran?");
                setOptions(new ArrayList<String>(){{
                    add("Yes");
                    add("No");
                }});
                setViewType(ListViewType.VIEW_SINGLE_CHOICE);
                setParamKey(Flags.CarInsurance.Keys.DISCOUNT_ACTIVE_MILITARY_VETERAN);
            }});
            add(new QuestionItems(){{
                setQuestion("Do you plan to pay in full at the start of your policy?");
                setOptions(new ArrayList<String>(){{
                    add("Yes");
                    add("No");
                }});
                setViewType(ListViewType.VIEW_SINGLE_CHOICE);
                setParamKey(Flags.CarInsurance.Keys.DISCOUNT_PAY_IN_FULL_AT_START);
            }});
        }};

        mData.putSerializable(SIMPLE_DATA_KEY, items);
    }
}
