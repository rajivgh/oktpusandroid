package com.app.oktpus.model.carfinance;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.app.oktpus.constants.ListViewType;
import com.app.oktpus.constants.carfinance.FinanceConstants;
import com.app.oktpus.fragment.carfinance.CFPersonalInfo;
import com.app.oktpus.model.carinsurance.QuestionItems;

import java.util.ArrayList;
import java.util.List;

import wizardlib.model.ModelCallbacks;
import wizardlib.model.Page;
import wizardlib.model.ReviewItem;

/**
 * Created by Gyandeep on 15/1/18.
 */

public class CFPersonalInfoModel extends Page{

    private String mCountry = null;
    private String mPageTitle;
    public boolean isJointApplication = false;
    protected CFPersonalInfoModel(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
        this.mPageTitle = title;
        //setCountry("Canada");
        setData();
    }

    @Override
    public Fragment createFragment() {
        return CFPersonalInfo.create(getKey());
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

    public void setApplicationType(String type) {
        if(FinanceConstants.ApplicationType.JOINT.equals(type)) {
            isJointApplication = true;
        }
        else {
            isJointApplication = false;
        }
        setData();
    }

    public void setCountry(String country){
        mCountry = country;
    }

    public void setData() {
        ArrayList<QuestionItems> items = new ArrayList<QuestionItems>(){
            {
                if(isJointApplication && (mPageTitle.equals(FinanceConstants.PageTitle.CO_APPLICANT_PERSONAL_INFO))) {
                    add(new QuestionItems() {{
                        setQuestion("I acknowledge I am the Co-Applicant for this loan/financing");
                        setViewType(ListViewType.VIEW_CHECKBOX_ROW);
                        setParamKey((mPageTitle.equals(FinanceConstants.PageTitle.PERSONAL_AND_RESIDENTIAL_PAGE)?
                                FinanceConstants.APIKeys.PERSONAL_PRIMARY_APPLICANT :
                                FinanceConstants.APIKeys.CO_APPLICANT_PRIMARY_APPLICANT));
                    }});
                }
                else if(isJointApplication && (mPageTitle.equals(FinanceConstants.PageTitle.PERSONAL_AND_RESIDENTIAL_PAGE))) {
                    add(new QuestionItems() {{
                        setQuestion("I acknowledge I am the Primary Applicant for this loan/financing.");
                        setViewType(ListViewType.VIEW_CHECKBOX_ROW);
                        setParamKey((mPageTitle.equals(FinanceConstants.PageTitle.PERSONAL_AND_RESIDENTIAL_PAGE) ?
                                FinanceConstants.APIKeys.PERSONAL_PRIMARY_APPLICANT :
                                FinanceConstants.APIKeys.CO_APPLICANT_PRIMARY_APPLICANT));
                    }});
                }

                add(new QuestionItems() {{
                    setQuestion("First Name");
                    setViewType(ListViewType.VIEW_EDIT_FIELD);
                    setParamKey((mPageTitle.equals(FinanceConstants.PageTitle.PERSONAL_AND_RESIDENTIAL_PAGE)?
                            FinanceConstants.APIKeys.PERSONAL_FIRST_NAME :
                            FinanceConstants.APIKeys.CO_APPLICANT_FIRST_NAME));
                }});
                add(new QuestionItems() {{
                    setQuestion("Last Name");
                    setViewType(ListViewType.VIEW_EDIT_FIELD);
                    setParamKey((mPageTitle.equals(FinanceConstants.PageTitle.PERSONAL_AND_RESIDENTIAL_PAGE)?
                            FinanceConstants.APIKeys.PERSONAL_LAST_NAME :
                            FinanceConstants.APIKeys.CO_APPLICANT_LAST_NAME));
                }});
                add(new QuestionItems() {{
                    setQuestion("Birthdate");
                    setViewType(ListViewType.VIEW_DATE_PICKER_EDIT_FIELD);
                    setParamKey((mPageTitle.equals(FinanceConstants.PageTitle.PERSONAL_AND_RESIDENTIAL_PAGE)?
                            FinanceConstants.APIKeys.PERSONAL_DATE_OF_BIRTH :
                            FinanceConstants.APIKeys.CO_APPLICANT_DATE_OF_BIRTH));
                }});

                add(new QuestionItems() {{
                    setQuestion((mCountry != null && mCountry.equals("Canada")?"Social Insurance Number":"Social Security Number"));
                    setViewType(ListViewType.VIEW_EDIT_FIELD);
                    setParamKey((mPageTitle.equals(FinanceConstants.PageTitle.PERSONAL_AND_RESIDENTIAL_PAGE)?
                            FinanceConstants.APIKeys.PERSONAL_SOCIAL_SECURITY_NUMBER :
                            FinanceConstants.APIKeys.CO_APPLICANT_SOCIAL_SECURITY_NUMBER));
                }});
                add(new QuestionItems() {{
                    setQuestion("Primary Phone");
                    setViewType(ListViewType.VIEW_EDIT_FIELD);
                    setParamKey((mPageTitle.equals(FinanceConstants.PageTitle.PERSONAL_AND_RESIDENTIAL_PAGE)?
                            FinanceConstants.APIKeys.PERSONAL_PRIMARY_PHONE :
                            FinanceConstants.APIKeys.CO_APPLICANT_PRIMARY_PHONE));
                }});
                add(new QuestionItems() {{
                    setQuestion("Email");
                    setViewType(ListViewType.VIEW_EDIT_FIELD);
                    setParamKey((mPageTitle.equals(FinanceConstants.PageTitle.PERSONAL_AND_RESIDENTIAL_PAGE)?
                            FinanceConstants.APIKeys.PERSONAL_EMAIL_ADDRESS :
                            FinanceConstants.APIKeys.CO_APPLICANT_EMAIL_ADDRESS));
                }});

                if(isJointApplication && (mPageTitle.equals(FinanceConstants.PageTitle.CO_APPLICANT_PERSONAL_INFO))) {
                    add(new QuestionItems() {{
                        setQuestion("Check if home address is same as Applicant");
                        setViewType(ListViewType.VIEW_CHECKBOX_ROW);
                        setParamKey(FinanceConstants.APIKeys.CO_APPLICANT_IS_ADDRESS_SAME_APPLICANT);
                    }});
                }

                add(new QuestionItems() {{
                    setQuestion("Street Address");
                    setViewType(ListViewType.VIEW_EDIT_FIELD);
                    setParamKey((mPageTitle.equals(FinanceConstants.PageTitle.PERSONAL_AND_RESIDENTIAL_PAGE)?
                            FinanceConstants.APIKeys.PERSONAL_STREET_ADDRESS :
                            FinanceConstants.APIKeys.CO_APPLICANT_STREET_ADDRESS));
                }});
                add(new QuestionItems() {{
                    setQuestion("City");
                    setViewType(ListViewType.VIEW_EDIT_FIELD);
                    setParamKey((mPageTitle.equals(FinanceConstants.PageTitle.PERSONAL_AND_RESIDENTIAL_PAGE)?
                            FinanceConstants.APIKeys.PERSONAL_CITY :
                            FinanceConstants.APIKeys.CO_APPLICANT_CITY));
                }});
                add(new QuestionItems() {{
                    setQuestion("State/Province");
                    setViewType(ListViewType.VIEW_EDIT_FIELD);
                    setParamKey((mPageTitle.equals(FinanceConstants.PageTitle.PERSONAL_AND_RESIDENTIAL_PAGE)?
                            FinanceConstants.APIKeys.PERSONAL_STATE :
                            FinanceConstants.APIKeys.CO_APPLICANT_STATE));
                }});
                add(new QuestionItems() {{
                    setQuestion("Zip/Postal code");
                    setViewType(ListViewType.VIEW_EDIT_FIELD);
                    setParamKey((mPageTitle.equals(FinanceConstants.PageTitle.PERSONAL_AND_RESIDENTIAL_PAGE)?
                            FinanceConstants.APIKeys.PERSONAL_ZIPCODE :
                            FinanceConstants.APIKeys.CO_APPLICANT_ZIPCODE));
                }});
                add(new QuestionItems() {{
                    setQuestion("Time At Residence");
                    setViewType(ListViewType.VIEW_EDIT_FIELD);
                    setParamKey((mPageTitle.equals(FinanceConstants.PageTitle.PERSONAL_AND_RESIDENTIAL_PAGE)?
                            FinanceConstants.APIKeys.PERSONAL_TIME_AT_RESIDENCE :
                            FinanceConstants.APIKeys.CO_APPLICANT_TIME_AT_RESIDENCE));
                }});
                add(new QuestionItems() {{
                    setQuestion("Status of Residence?");
                    setViewType(ListViewType.VIEW_SINGLE_CHOICE);
                    setOptions(new ArrayList<String>() {{
                        add("Own");
                        add("Lease");
                        add("Rent");
                        add("Other");
                    }});
                    setParamKey((mPageTitle.equals(FinanceConstants.PageTitle.PERSONAL_AND_RESIDENTIAL_PAGE)?
                            FinanceConstants.APIKeys.PERSONAL_STATUS_OF_RESIDENCE :
                            FinanceConstants.APIKeys.CO_APPLICANT_STATUS_OF_RESIDENCE));
                }});
                add(new QuestionItems() {{
                    setQuestion("Monthly Housing Payment ($)");
                    setViewType(ListViewType.VIEW_EDIT_FIELD);
                    setParamKey((mPageTitle.equals(FinanceConstants.PageTitle.PERSONAL_AND_RESIDENTIAL_PAGE)?
                            FinanceConstants.APIKeys.PERSONAL_MONTHLY_HOUSING_PAYMENT :
                            FinanceConstants.APIKeys.CO_APPLICANT_MONTHLY_HOUSING_PAYMENT));
                }});

            }};

        mData.putSerializable(SIMPLE_DATA_KEY, items);
    }
}
