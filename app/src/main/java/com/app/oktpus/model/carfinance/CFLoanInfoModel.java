package com.app.oktpus.model.carfinance;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.app.oktpus.constants.ListViewType;
import com.app.oktpus.constants.carfinance.CarFinanceCompanyList;
import com.app.oktpus.constants.carfinance.FinanceConstants;
import com.app.oktpus.fragment.carfinance.CFLoanInfo;
import com.app.oktpus.model.carinsurance.QuestionItems;

import java.util.ArrayList;
import java.util.List;

import wizardlib.model.ModelCallbacks;
import wizardlib.model.Page;
import wizardlib.model.ReviewItem;

/**
 * Created by Gyandeep on 15/1/18.
 */

public class CFLoanInfoModel extends Page{

    private String mCountry = null;

    protected CFLoanInfoModel(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
        setData(FinanceConstants.LoanOrFinanceType.NEW_OR_USED_AUTO);
    }

    @Override
    public Fragment createFragment() {
        return CFLoanInfo.create(getKey());
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

    public void setCountry(String country) {
        mCountry = country;
    }

    public void setData(String type) {
        ArrayList<QuestionItems> items = null;
        switch(type) {
            case FinanceConstants.LoanOrFinanceType.NEW_OR_USED_AUTO:
                items = dataNewOrUsedAuto();
                break;
            case FinanceConstants.LoanOrFinanceType.REFINANCE_AUTO:
                items = dataRefinanceAuto();
                break;
            case FinanceConstants.LoanOrFinanceType.PRIVATE_PARTY:
                items = dataPrivateParty();
                break;
            case FinanceConstants.LoanOrFinanceType.AUTO_LEASE_BUYOUT:
                items = dataAutoLeaseBuyout();
                break;
        }
        mData.putSerializable(SIMPLE_DATA_KEY, items);
    }

    public ArrayList<QuestionItems> dataNewOrUsedAuto() {
        return new ArrayList<QuestionItems>(){{
            add(new QuestionItems(){{
                setQuestion("Total Sales Price ($) ");
                setViewType(ListViewType.VIEW_EDIT_FIELD);
                setParamKey(FinanceConstants.APIKeys.LOF_TOTAL_SALES_PRICE);
            }});
            add(new QuestionItems(){{
                setQuestion("Requested term");
                setOptions(new ArrayList<String>(){{
                    add("24 months");
                    add("36 months");
                    add("48 months");
                    add("54 months");
                    add("60 months");
                    add("72 months");
                    add("84 months");
                }});
                setViewType(ListViewType.VIEW_SINGLE_CHOICE);
                setParamKey(FinanceConstants.APIKeys.LOF_REQUESTED_TERM);
            }});
            add(new QuestionItems(){{
                setQuestion("Cash Down Payment ($) ");
                setViewType(ListViewType.VIEW_EDIT_FIELD);
                setParamKey(FinanceConstants.APIKeys.LOF_CASH_DOWNPAYMENT);
            }});
        }};
    }

    public ArrayList<QuestionItems> dataRefinanceAuto() {
        return new ArrayList<QuestionItems>(){{
            add(new QuestionItems(){{
                setQuestion("Remaining Balance ($) ");
                setViewType(ListViewType.VIEW_EDIT_FIELD);
                setParamKey(FinanceConstants.APIKeys.LOF_REMAINING_BALANCE);
            }});
            add(new QuestionItems(){{
                setQuestion("Interest Rate (%)");
                setViewType(ListViewType.VIEW_EDIT_FIELD);
                setParamKey(FinanceConstants.APIKeys.LOF_INTEREST_RATE);
            }});
            add(new QuestionItems(){{
                setQuestion("Monthly Payment ($)");
                setViewType(ListViewType.VIEW_EDIT_FIELD);
                setParamKey(FinanceConstants.APIKeys.LOF_MONTHLY_PAYMENT);
            }});

            if(mCountry!= null && mCountry.equals("United States")) {
                add(new QuestionItems(){{
                    setQuestion("Lien Holder");
                    setViewType(ListViewType.VIEW_POPUP);
                    setmPopupOptions(new CarFinanceCompanyList().getList());
                    setParamKey(FinanceConstants.APIKeys.LOF_LIEN_HOLDER);
                }});
            }
            else {
                add(new QuestionItems(){{
                    setQuestion("Lien Holder");
                    setViewType(ListViewType.VIEW_EDIT_FIELD);
                    setParamKey(FinanceConstants.APIKeys.LOF_LIEN_HOLDER);
                }});
            }

            add(new QuestionItems(){{
                setQuestion("Next Payment Date");
                setViewType(ListViewType.VIEW_DATE_PICKER_EDIT_FIELD);
                setParamKey(FinanceConstants.APIKeys.LOF_NEXT_PAYMENT_DATE);
            }});
            add(new QuestionItems(){{
                setQuestion("New Loan Term");
                setOptions(new ArrayList<String>(){{
                    add("24 months");
                    add("36 months");
                    add("48 months");
                    add("54 months");
                    add("60 months");
                    add("72 months");
                    add("84 months");
                }});
                setViewType(ListViewType.VIEW_SINGLE_CHOICE);
                setParamKey(FinanceConstants.APIKeys.LOF_NEW_LOAN_TERM);
            }});
        }};
    }

    public ArrayList<QuestionItems> dataPrivateParty() {
        return new ArrayList<QuestionItems>(){{
            add(new QuestionItems(){{
                setQuestion("Loan Amount ($)");
                setViewType(ListViewType.VIEW_EDIT_FIELD);
                setParamKey(FinanceConstants.APIKeys.LOF_LOAN_AMOUNT);
            }});
            add(new QuestionItems(){{
                setQuestion("Term");
                setOptions(new ArrayList<String>(){{
                    add("24 months");
                    add("36 months");
                    add("48 months");
                    add("54 months");
                    add("60 months");
                    add("72 months");
                    add("84 months");
                }});
                setViewType(ListViewType.VIEW_SINGLE_CHOICE);
                setParamKey(FinanceConstants.APIKeys.LOF_TERM);
            }});
            add(new QuestionItems(){{
                setQuestion("Seller's Information");
                setViewType(ListViewType.VIEW_SIMPLE_TEXT);
            }});
            add(new QuestionItems(){{
                setQuestion("Seller's First Name");
                setViewType(ListViewType.VIEW_EDIT_FIELD);
                setParamKey(FinanceConstants.APIKeys.SELLER_FIRST_NAME);
            }});
            add(new QuestionItems(){{
                setQuestion("Seller's Last Name");
                setViewType(ListViewType.VIEW_EDIT_FIELD);
                setParamKey(FinanceConstants.APIKeys.SELLER_LAST_NAME);
            }});
            add(new QuestionItems(){{
                setQuestion("Seller's Phone");
                setViewType(ListViewType.VIEW_EDIT_FIELD);
                setParamKey(FinanceConstants.APIKeys.SELLER_PHONE);
            }});
        }};
    }

    public ArrayList<QuestionItems> dataAutoLeaseBuyout() {
        return new ArrayList<QuestionItems>(){{
            add(new QuestionItems(){{
                setQuestion("Lease Buyout Amount ($)");
                setViewType(ListViewType.VIEW_EDIT_FIELD);
                setParamKey(FinanceConstants.APIKeys.LOF_LEASE_BUYOUT_AMOUNT);
            }});
            add(new QuestionItems(){{
                setQuestion("Lease Expiry Date");
                setViewType(ListViewType.VIEW_DATE_PICKER_EDIT_FIELD);
                setParamKey(FinanceConstants.APIKeys.LOF_LEASE_EXPIRY_DATE);
            }});
            add(new QuestionItems(){{
                setQuestion("Lease Holder (Current Lease Company)");
                setViewType(ListViewType.VIEW_EDIT_FIELD);
                setParamKey(FinanceConstants.APIKeys.LOF_LEASE_HOLDER);
            }});
            add(new QuestionItems(){{
                setQuestion("New Loan Term");
                setOptions(new ArrayList<String>(){{
                    add("24 months");
                    add("36 months");
                    add("48 months");
                    add("54 months");
                    add("60 months");
                    add("72 months");
                    add("84 months");
                }});
                setViewType(ListViewType.VIEW_SINGLE_CHOICE);
                setParamKey(FinanceConstants.APIKeys.LOF_NEW_LOAN_TERM);
            }});

        }};
    }
}
