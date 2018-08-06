package com.app.oktpus.model.carfinance;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.app.oktpus.constants.ListViewType;
import com.app.oktpus.constants.ModelStatus;
import com.app.oktpus.constants.carfinance.FinanceConstants;
import com.app.oktpus.fragment.carfinance.CFVehicleInfo;
import com.app.oktpus.listener.SearchFilterAPIListener;
import com.app.oktpus.model.AttrValSpinnerModel;
import com.app.oktpus.model.carinsurance.QuestionItems;
import com.app.oktpus.service.SearchFilterAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import wizardlib.model.ModelCallbacks;
import wizardlib.model.Page;
import wizardlib.model.ReviewItem;

/**
 * Created by Gyandeep on 15/1/18.
 */

public class CFVehicleInfoModel extends Page {
    public Map<String, List<AttrValSpinnerModel>> mAttributeData;
    protected CFVehicleInfoModel(ModelCallbacks callbacks, String key) {
        super(callbacks, key);
        getDataFromNetwork();
        setData();
    }

    public void getDataFromNetwork() {
        new SearchFilterAPI(new SearchFilterAPIListener() {
            @Override
            public void getFilterAttributeDataFromNetwork(Map<String, List<AttrValSpinnerModel>> data) {
                mAttributeData = data;
            }
        });
    }

    @Override
    public Fragment createFragment() {
        return CFVehicleInfo.create(getKey());
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
                setQuestion("Year");
                setParamKey(FinanceConstants.APIKeys.VEHICLE_YEAR);
                setViewType(ListViewType.VIEW_EDIT_FIELD_NO_TITLE);
            }});
            add(new QuestionItems(){{
                setQuestion("Make");
                setParamKey(FinanceConstants.APIKeys.VEHICLE_MAKE);
                setViewType(ListViewType.VIEW_POPUP);
            }});
            add(new QuestionItems(){{
                setQuestion("Model");
                setParamKey(FinanceConstants.APIKeys.VEHICLE_MODEL);
                setViewType(ListViewType.VIEW_POPUP);
                setModelStatus(ModelStatus.CHOOSE_A_MAKE_FIRST.getValue());
            }});
            add(new QuestionItems(){{
                setQuestion("Vehicle type");
                setParamKey(FinanceConstants.APIKeys.VEHICLE_TYPE);
                setViewType(ListViewType.VIEW_SINGLE_CHOICE);
                setOptions(new ArrayList<String>(){{
                    add("New");
                    add("Used");
                }});
            }});
            add(new QuestionItems(){{
                setQuestion("Trading in current car?");
                setParamKey(FinanceConstants.APIKeys.VEHICLE_TRADING_IN_CURRENT_CAR);
                setViewType(ListViewType.VIEW_SINGLE_CHOICE);
                setOptions(new ArrayList<String>(){{
                    add("Yes");
                    add("No");
                }});
            }});
        }};

        mData.putSerializable(SIMPLE_DATA_KEY, items);
    }
}
