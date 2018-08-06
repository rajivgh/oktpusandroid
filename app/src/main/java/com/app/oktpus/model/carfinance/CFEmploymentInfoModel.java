package com.app.oktpus.model.carfinance;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.app.oktpus.constants.ListViewType;
import com.app.oktpus.constants.carfinance.FinanceConstants;
import com.app.oktpus.fragment.carfinance.CFEmploymentInfo;
import com.app.oktpus.model.carinsurance.QuestionItems;

import java.util.ArrayList;
import java.util.List;

import wizardlib.model.ModelCallbacks;
import wizardlib.model.Page;
import wizardlib.model.ReviewItem;

/**
 * Created by Gyandeep on 15/1/18.
 */

public class CFEmploymentInfoModel extends Page {

    private String mPageTitle;

    protected CFEmploymentInfoModel(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
        mPageTitle = title;
        setData();
    }

    @Override
    public Fragment createFragment() {
        return CFEmploymentInfo.create(getKey());
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
                setQuestion("Employment Status");
                setOptions(new ArrayList<String>(){{
                    add("Employed");
                    add("Unemployed");
                    add("Retired");
                    add("Military");
                    add("Self Employed");
                    add("Student");
                    add("Other");
                }});
                setViewType(ListViewType.VIEW_SINGLE_CHOICE);
                setParamKey((mPageTitle.equals(FinanceConstants.PageTitle.EMPLOYMENT_INFO_PAGE)?
                        FinanceConstants.APIKeys.EMPLOYMENT_STATUS :
                        FinanceConstants.APIKeys.CO_APPLICANT_EMPLOYMENT_STATUS));
            }});
            add(new QuestionItems(){{
                setQuestion("Occupation");
                setViewType(ListViewType.VIEW_EDIT_FIELD);
                setParamKey((mPageTitle.equals(FinanceConstants.PageTitle.EMPLOYMENT_INFO_PAGE)?
                        FinanceConstants.APIKeys.OCCUPATION :
                        FinanceConstants.APIKeys.CO_APPLICANT_OCCUPATION));
            }});
            add(new QuestionItems(){{
                setQuestion("Employer");
                setViewType(ListViewType.VIEW_EDIT_FIELD);
                setParamKey((mPageTitle.equals(FinanceConstants.PageTitle.EMPLOYMENT_INFO_PAGE)?
                        FinanceConstants.APIKeys.EMPLOYER :
                        FinanceConstants.APIKeys.CO_APPLICANT_EMPLOYER));
            }});
            add(new QuestionItems(){{
                setQuestion("Work Phone");
                setViewType(ListViewType.VIEW_EDIT_FIELD);
                setParamKey((mPageTitle.equals(FinanceConstants.PageTitle.EMPLOYMENT_INFO_PAGE)?
                        FinanceConstants.APIKeys.WORK_PHONE :
                        FinanceConstants.APIKeys.CO_APPLICANT_WORK_PHONE));
            }});
            add(new QuestionItems(){{
                setQuestion("Time at Employer");
                setViewType(ListViewType.VIEW_EDIT_FIELD);
                setParamKey((mPageTitle.equals(FinanceConstants.PageTitle.EMPLOYMENT_INFO_PAGE)?
                        FinanceConstants.APIKeys.TIME_AT_EMPLOYER :
                        FinanceConstants.APIKeys.CO_APPLICANT_TIME_AT_EMPLOYER));
            }});
            add(new QuestionItems(){{
                setQuestion("Gross Monthly Income ($)");
                setViewType(ListViewType.VIEW_EDIT_FIELD);
                setParamKey((mPageTitle.equals(FinanceConstants.PageTitle.EMPLOYMENT_INFO_PAGE)?
                        FinanceConstants.APIKeys.GROSS_MONTHLY_INCOME :
                        FinanceConstants.APIKeys.CO_APPLICANT_GROSS_MONTHLY_INCOME));
            }});
        }};

        mData.putSerializable(SIMPLE_DATA_KEY, items);
    }

}
