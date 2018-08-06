package com.app.oktpus.model.wmcw;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.app.oktpus.constants.Flags;
import com.app.oktpus.constants.ListViewType;
import com.app.oktpus.constants.ModelStatus;
import com.app.oktpus.fragment.wmcw.WMCWVehicleFragment;
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
 * Created by Gyandeep on 13/3/18.
 */

public class WMCWVehicleModel extends Page {

    public Map<String, List<AttrValSpinnerModel>> mAttributeData;

    protected WMCWVehicleModel(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
        getDataFromNetwork();
        setModelData();
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
        return WMCWVehicleFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        ArrayList<String> rItems = new ArrayList<>();
        List<QuestionItems> qItems = new ArrayList<>();
        for(QuestionItems item: (ArrayList<QuestionItems>)mData.getSerializable(SIMPLE_DATA_KEY)) {
            if(item.getValue().length() > 0) {
                /*String valueToSet;
                if(item.getExtraValue() != null)
                    valueToSet = item.getValue() + " " + item.getExtraValue();
                else
                    valueToSet = item.getValue();*/
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

    public void setModelData() {
        ArrayList<QuestionItems> items = new ArrayList<QuestionItems>(){{
            add(new QuestionItems(){{
                setQuestion("Year");
                setParamKey(Flags.WMCWRequestParams.CAR_YEAR);
                setViewType(ListViewType.VIEW_EDIT_FIELD_NO_TITLE);
            }});
            add(new QuestionItems(){{
                setQuestion("Make");
                setParamKey(Flags.WMCWRequestParams.CAR_MAKE);
                setViewType(ListViewType.VIEW_POPUP);
            }});
            add(new QuestionItems(){{
                setQuestion("Model");
                setParamKey(Flags.WMCWRequestParams.CAR_MODEL);
                setViewType(ListViewType.VIEW_POPUP);
                setModelStatus(ModelStatus.CHOOSE_A_MAKE_FIRST.getValue());
            }});
            add(new QuestionItems(){{
                setQuestion("Transmission");
                setParamKey(Flags.WMCWRequestParams.CAR_TRANSMISSION);
                setViewType(ListViewType.VIEW_POPUP);
            }});
            add(new QuestionItems(){{
                setQuestion("Mileage");
                setExtraValue("kms");
                setParamKey(Flags.WMCWRequestParams.CAR_KILOMETERS);
                setViewType(ListViewType.VIEW_EDITTEXT_WITH_UNIT);
            }});
        }};

        mData.putSerializable(SIMPLE_DATA_KEY, items);
    }
}

