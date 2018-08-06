package com.app.oktpus.model.carfinance;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.app.oktpus.constants.ListViewType;
import com.app.oktpus.constants.carfinance.FinanceConstants;
import com.app.oktpus.fragment.carfinance.CFStartFragment;
import com.app.oktpus.model.carinsurance.QuestionItems;

import java.util.ArrayList;
import java.util.List;

import wizardlib.model.ModelCallbacks;
import wizardlib.model.Page;
import wizardlib.model.ReviewItem;

/**
 * Created by Gyandeep on 9/1/18.
 */

public class CFStartModel extends Page {

    protected CFStartModel(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
        setData();
    }

    @Override
    public Fragment createFragment() {
        return CFStartFragment.create(getKey());
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
                setQuestion("What is the loan or finance type you are applying for?");
                setOptions(new ArrayList<String>(){{
                    add(FinanceConstants.LoanOrFinanceType.NEW_OR_USED_AUTO);
                    add(FinanceConstants.LoanOrFinanceType.REFINANCE_AUTO);
                    add(FinanceConstants.LoanOrFinanceType.PRIVATE_PARTY);
                    add(FinanceConstants.LoanOrFinanceType.AUTO_LEASE_BUYOUT);
                }});
                setViewType(ListViewType.VIEW_SINGLE_CHOICE);
                setParamKey(FinanceConstants.APIKeys.LOAN_OR_FINANCE_TYPE);
            }});
            add(new QuestionItems(){{
                setQuestion("Is this application type individual or joint?");
                setOptions(new ArrayList<String>(){{
                    add(FinanceConstants.ApplicationType.INDIVIDUAL);
                    add(FinanceConstants.ApplicationType.JOINT);
                }});
                setViewType(ListViewType.VIEW_SINGLE_CHOICE);
                setParamKey(FinanceConstants.APIKeys.APPLICATION_TYPE);
            }});
        }};

        mData.putSerializable(SIMPLE_DATA_KEY, items);
    }
}
