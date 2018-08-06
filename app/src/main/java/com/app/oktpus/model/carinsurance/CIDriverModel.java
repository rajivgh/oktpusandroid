package com.app.oktpus.model.carinsurance;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.app.oktpus.constants.CarInsurance;
import com.app.oktpus.constants.CarInsuranceCompanyList;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.constants.ListViewType;
import com.app.oktpus.fragment.CIDriverFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wizardlib.model.ModelCallbacks;
import wizardlib.model.Page;
import wizardlib.model.ReviewItem;

/**
 * Created by Gyandeep on 15/11/17.
 */

public class CIDriverModel extends Page {

    public CIDriverModel(ModelCallbacks callbacks, String key) {
        super(callbacks, key);
        setModelData(CarInsurance.DONT_HAVE_INSURANCE);
    }

    @Override
    public Fragment createFragment() {
        return CIDriverFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        ArrayList<String> rItems = new ArrayList<>();
        List<QuestionItems> qItems = new ArrayList<>();

        for(QuestionItems item: (ArrayList<QuestionItems>)mData.getSerializable(SIMPLE_DATA_KEY)) {
            if(item.getValue().length() > 0 && !item.isExtension()) {
                rItems.add(item.getValue());
                qItems.add(item);
            }

            if(item.getMultiChoiceValues() != null && item.getMultiChoiceValues().size() > 0) {
                List<String> multiChoiceValues = new ArrayList<>();
                for(Map.Entry<String, Boolean> mapItem : item.getMultiChoiceValues().entrySet()) {
                    if(mapItem.getValue().booleanValue()) {
                        rItems.add(mapItem.getKey());
                        multiChoiceValues.add(mapItem.getKey());
                    }
                }
                if(multiChoiceValues.size() > 0) {
                    item.setMCValueList(multiChoiceValues);
                    qItems.add(item);
                }
            }
        }

        if(rItems.size() > 0)
            dest.add(new ReviewItem(getTitle(), TextUtils.join(",",rItems), qItems, getKey()));
    }



    @Override
    public boolean isCompleted() {
        return !TextUtils.isEmpty(mData.getString(SIMPLE_DATA_KEY));
    }

    public void setModelData(final CarInsurance insuranceAvailable) {
        final Map<String, String> keyTitlePairs = Flags.CarInsurance.insuranceKeyTitlePairs();

        final ArrayList<QuestionItems> extension = new ArrayList<QuestionItems>(){{
            add(new QuestionItems(){{
                setViewType(ListViewType.VIEW_SIMPLE_TEXT);
                setQuestion("How many of each?");
            }});
            add(new QuestionItems(){{
                setViewType(ListViewType.VIEW_ITEM_COUNTER);
                setQuestion("Accidents that were my fault");
            }});
            add(new QuestionItems(){{
                setViewType(ListViewType.VIEW_ITEM_COUNTER);
                setQuestion("Accidents that were not my fault");
            }});
            add(new QuestionItems(){{
                setViewType(ListViewType.VIEW_ITEM_COUNTER);
                setQuestion("Claims \n (Weather, vandalism, uninsured motorist damage, etc)");
            }});
            add(new QuestionItems(){{
                setViewType(ListViewType.VIEW_ITEM_COUNTER);
                setQuestion("Tickets/Violations");
            }});
        }};

        ArrayList<QuestionItems> items = new ArrayList<QuestionItems>(){{
            add(new QuestionItems(){{
                setQuestion("First Name");
                setViewType(ListViewType.VIEW_EDIT_FIELD);
                setParamKey(Flags.CarInsurance.Keys.DRIVER_FIRST_NAME);
            }});
            add(new QuestionItems(){{
                setQuestion("Last Name");
                setViewType(ListViewType.VIEW_EDIT_FIELD);
                setParamKey(Flags.CarInsurance.Keys.DRIVER_LAST_NAME);
            }});
            add(new QuestionItems(){{
                setQuestion("Birthdate");
                setViewType(ListViewType.VIEW_DATE_PICKER_EDIT_FIELD);
                setParamKey(Flags.CarInsurance.Keys.DRIVER_BIRTH_DATE);
            }});
            add(new QuestionItems(){{
                setQuestion("Email");
                setViewType(ListViewType.VIEW_EDIT_FIELD);
                setParamKey(Flags.CarInsurance.Keys.DRIVER_EMAIL);
            }});
            add(new QuestionItems(){{
                setQuestion("Phone");
                setViewType(ListViewType.VIEW_EDIT_FIELD);
                setParamKey(Flags.CarInsurance.Keys.DRIVER_PHONE);
            }});

            add(new QuestionItems(){{
                setViewType(ListViewType.VIEW_SINGLE_CHOICE);
                setQuestion(keyTitlePairs.get(Flags.CarInsurance.Keys.DRIVER_GENDER));
                setOptions(new ArrayList<String>(){{
                    add("Male");
                    add("Female");
                }});
                setParamKey(Flags.CarInsurance.Keys.DRIVER_GENDER);
            }});

            add(new QuestionItems(){{
                setViewType(ListViewType.VIEW_SINGLE_CHOICE);
                setQuestion(keyTitlePairs.get(Flags.CarInsurance.Keys.DRIVER_MARITAL_STATUS));
                setOptions(new ArrayList<String>(){{
                    add("Single");
                    add("Married");
                    add("Divorced");
                    add("Widowed");
                }});
                setParamKey(Flags.CarInsurance.Keys.DRIVER_MARITAL_STATUS);
            }});

            add(new QuestionItems(){{
                setViewType(ListViewType.VIEW_SINGLE_CHOICE);
                setQuestion(keyTitlePairs.get(Flags.CarInsurance.Keys.DRIVER_CREDIT_SCORE));
                setOptions(new ArrayList<String>(){{
                    add("Poor(Below 580)");
                    add("Average(580-679)");
                    add("Good(680-719)");
                    add("Excellent(720+)");
                }});
                setParamKey(Flags.CarInsurance.Keys.DRIVER_CREDIT_SCORE);
            }});
            add(new QuestionItems(){{
                setViewType(ListViewType.VIEW_SINGLE_CHOICE);
                setQuestion(keyTitlePairs.get(Flags.CarInsurance.Keys.DRIVER_HIGHEST_EDUCATION_LEVEL));
                setOptions(new ArrayList<String>(){{
                    add("No Diploma");
                    add("High School Diploma/GED");
                    add("Bachelor's Degree");
                    add("Master's Degree");
                    add("Doctoral Degree");
                }});
                setParamKey(Flags.CarInsurance.Keys.DRIVER_HIGHEST_EDUCATION_LEVEL);
            }});
            add(new QuestionItems(){{
                setViewType(ListViewType.VIEW_SINGLE_CHOICE);
                setQuestion(keyTitlePairs.get(Flags.CarInsurance.Keys.DRIVER_HOUSE_OWNERSHIP));
                setOptions(new ArrayList<String>(){{
                    add("Own home");
                    add("Own condo");
                    add("Rent");
                    add("Other");
                }});
                setParamKey(Flags.CarInsurance.Keys.DRIVER_HOUSE_OWNERSHIP);
            }});
            add(new QuestionItems(){{
                setViewType(ListViewType.VIEW_SINGLE_CHOICE);
                setQuestion(keyTitlePairs.get(Flags.CarInsurance.Keys.DRIVER_ACCIDENTS_TICKET_CLAIM));
                setOptions(new ArrayList<String>(){{
                    add("Yes");
                    add("No");              // Have Extension
                }});
                setHasExtension(true);
                setSubQuestions(extension);
                setParamKey(Flags.CarInsurance.Keys.DRIVER_ACCIDENTS_TICKET_CLAIM);
            }});

            if(CarInsurance.HAVE_INSURANCE == insuranceAvailable) {
                add(new QuestionItems(){{
                    setViewType(ListViewType.VIEW_SINGLE_CHOICE);
                    setQuestion(keyTitlePairs.get(Flags.CarInsurance.Keys.DRIVER_INSURANCE_PERIOD));
                    setOptions(new ArrayList<String>(){{
                        add("Under 6 months");
                        add("6-11 months");
                        add("1-3 years");
                    }});
                }});
                add(new QuestionItems(){{
                    setViewType(ListViewType.VIEW_POPUP);
                    setQuestion(keyTitlePairs.get(Flags.CarInsurance.Keys.DRIVER_CURRENT_CAR_COMPANY));
                    setmPopupOptions(new CarInsuranceCompanyList().getList());
                    setParamKey(Flags.CarInsurance.Keys.DRIVER_CURRENT_CAR_COMPANY);
                }});
            }

            add(new QuestionItems(){{
                setViewType(ListViewType.VIEW_MULTI_CHOICE);
                setQuestion(keyTitlePairs.get(Flags.CarInsurance.Keys.DRIVER_ANY_OTHER));
                setOptions(getAnyOtherOptionsList());
                setMultiChoiceValues(new HashMap<String, Boolean>());
                setParamKey(Flags.CarInsurance.Keys.DRIVER_ANY_OTHER);
            }});
        }};
        mData.putSerializable(SIMPLE_DATA_KEY, items);
    }

    public ArrayList<QuestionItems> getSingleChoiceItemVehicleData() {
        return (ArrayList<QuestionItems>) mData.getSerializable(SIMPLE_DATA_KEY);
    }

    public List<String> getAnyOtherOptionsList() {
        final List<String> list =
                Collections.unmodifiableList(Arrays
                        .asList("Moved in past 2 months",
                                "Recently bought a car",
                                "Recently married, divorced or widowed",
                                "Need to file SR-22 form",
                                "New licensed or teen driver in policy",
                                "Would bundle with home/renters insurance"));
        return list;
    }
}
