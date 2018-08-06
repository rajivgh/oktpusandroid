package com.app.oktpus.model.carinsurance;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.app.oktpus.constants.Flags;
import com.app.oktpus.constants.ListViewType;
import com.app.oktpus.constants.ModelStatus;
import com.app.oktpus.fragment.CIVehicleFragment;
import com.app.oktpus.listener.SearchFilterAPIListener;
import com.app.oktpus.model.AttrValSpinnerModel;
import com.app.oktpus.service.SearchFilterAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import wizardlib.model.ModelCallbacks;
import wizardlib.model.Page;
import wizardlib.model.ReviewItem;

/**
 * Created by Gyandeep on 4/11/17.
 */

public class CIVehicleModel extends Page {

    public Map<String, List<AttrValSpinnerModel>> mAttributeData;

    public CIVehicleModel(ModelCallbacks callbacks, String key) {
        super(callbacks, key);
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
        return CIVehicleFragment.create(getKey());
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

    private void setModelData() {
        final Map<String, String> keyTitlePairs = Flags.CarInsurance.insuranceKeyTitlePairs();

        ArrayList<QuestionItems> items = new ArrayList<QuestionItems>(){{
            add(new QuestionItems(){{
                setQuestion("Year");
                setParamKey(Flags.CarInsurance.Keys.VEHICLE_YEAR);
                setViewType(ListViewType.VIEW_EDIT_FIELD_NO_TITLE);
            }});
            add(new QuestionItems(){{
                setQuestion("Make");
                setParamKey(Flags.CarInsurance.Keys.VEHICLE_MAKE);
                setViewType(ListViewType.VIEW_POPUP);
            }});
            add(new QuestionItems(){{
                setQuestion("Model");
                setParamKey(Flags.CarInsurance.Keys.VEHICLE_MODEL);
                setViewType(ListViewType.VIEW_POPUP);
                setModelStatus(ModelStatus.CHOOSE_A_MAKE_FIRST.getValue());
            }});
            add(new QuestionItems(){{
                setParamKey(Flags.CarInsurance.Keys.VEHICLE_PRIMARY_USAGE);
                setQuestion(keyTitlePairs.get(Flags.CarInsurance.Keys.VEHICLE_PRIMARY_USAGE));
                setOptions(new ArrayList<String>(){{
                    add("Personal/Commuting");
                    add("Pleasure");
                    add("Farm");
                    add("Business");
                }});
                setViewType(ListViewType.VIEW_SINGLE_CHOICE);
            }});

            add(new QuestionItems(){{
                setQuestion(keyTitlePairs.get(Flags.CarInsurance.Keys.VEHICLE_MILEAGE_COVERED_PER_YEAR));
                setOptions(new ArrayList<String>(){{
                    add("0-7,500");
                    add("7,501 - 10,000");
                    add("10,001-15,000 (average)");
                    add("More than 15,000");
                }});
                setParamKey(Flags.CarInsurance.Keys.VEHICLE_MILEAGE_COVERED_PER_YEAR);
                setViewType(ListViewType.VIEW_SINGLE_CHOICE);
            }});

            add(new QuestionItems(){{
                setQuestion(keyTitlePairs.get(Flags.CarInsurance.Keys.VEHICLE_OWNERSHIP));
                setParamKey(Flags.CarInsurance.Keys.VEHICLE_OWNERSHIP);
                setOptions(new ArrayList<String>(){{
                    add("Own - paid in full");
                    add("Own - making payments");
                    add("Lease");
                }});
                setViewType(ListViewType.VIEW_SINGLE_CHOICE);
            }});
            add(new QuestionItems(){{
                setParamKey(Flags.CarInsurance.Keys.VEHICLE_PRIMARY_PARKING);
                setQuestion(keyTitlePairs.get(Flags.CarInsurance.Keys.VEHICLE_PRIMARY_PARKING));
                setViewType(ListViewType.VIEW_EDIT_FIELD);
            }});
        }};
        mData.putSerializable(SIMPLE_DATA_KEY, items);
    }

    public ArrayList<QuestionItems> getSingleChoiceItemVehicleData() {
        return (ArrayList<QuestionItems>) mData.getSerializable(SIMPLE_DATA_KEY);
    }

}