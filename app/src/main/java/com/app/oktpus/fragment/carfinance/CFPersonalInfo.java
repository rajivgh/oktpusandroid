package com.app.oktpus.fragment.carfinance;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.oktpus.R;
import com.app.oktpus.activity.CarFinance;
import com.app.oktpus.adapter.carfinance.CFPersonalInfoAdapter;
import com.app.oktpus.constants.carfinance.FinanceConstants;
import com.app.oktpus.model.carfinance.CFPersonalInfoModel;
import com.app.oktpus.model.carinsurance.QuestionItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import wizardlib.model.Page;
import wizardlib.model.ui.PageFragmentCallbacks;

import static com.app.oktpus.constants.carfinance.FinanceConstants.PageTitle.PERSONAL_AND_RESIDENTIAL_PAGE;

/**
 * Created by Gyandeep on 15/1/18.
 */

public class CFPersonalInfo extends Fragment {

    private static final String ARG_KEY = "key";
    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private Page mPage;
    private ArrayList<QuestionItems> dataItems;
    private RecyclerView recyclerView;
    private CFPersonalInfoAdapter adapter;

    public CFPersonalInfo() {}

    public static CFPersonalInfo create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        CFPersonalInfo fragment = new CFPersonalInfo();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = mCallbacks.onGetPage(mKey);
        CFPersonalInfoModel model = (CFPersonalInfoModel) mPage;
        dataItems = model.getModelData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_item_insurance, container, false);
        ((TextView) rootView.findViewById(R.id.page_title)).setText(mPage.getTitle());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_ci_page);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CFPersonalInfoAdapter(getActivity(), getContext(), mPage, dataItems, new AdapterListener() {
            @Override
            public void scrollNext(int pos) {
                //recyclerView.smoothScrollToPosition(pos);
            }

            @Override
            public void updateCoApplicantAddress() {
                if(mPage.getTitle().equals(FinanceConstants.PageTitle.CO_APPLICANT_PERSONAL_INFO)) {
                    copyApplicantAddress();
                }
            }
        });
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof PageFragmentCallbacks)) {
            throw new ClassCastException("Activity must implement PageFragmentCallbacks");
        }
        mCallbacks = (PageFragmentCallbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        mPage.notifyDataChanged();
    }

    public void copyApplicantAddress() {
        CFPersonalInfoModel personalInfoModel = (CFPersonalInfoModel) ((CarFinance)getActivity()).onGetPage(PERSONAL_AND_RESIDENTIAL_PAGE);

        Map<String, String> addressData = new HashMap<>();
        for(QuestionItems item : personalInfoModel.getModelData()) {
            switch(item.getParamKey()) {
                case FinanceConstants.APIKeys.PERSONAL_STREET_ADDRESS:
                    addressData.put(FinanceConstants.APIKeys.PERSONAL_STREET_ADDRESS, item.getValue());
                    break;
                case FinanceConstants.APIKeys.PERSONAL_CITY:
                    addressData.put(FinanceConstants.APIKeys.PERSONAL_CITY, item.getValue());
                    break;
                case FinanceConstants.APIKeys.PERSONAL_STATE:
                    addressData.put(FinanceConstants.APIKeys.PERSONAL_STATE, item.getValue());
                    break;
                case FinanceConstants.APIKeys.PERSONAL_ZIPCODE:
                    addressData.put(FinanceConstants.APIKeys.PERSONAL_ZIPCODE, item.getValue());
                    break;
                case FinanceConstants.APIKeys.PERSONAL_TIME_AT_RESIDENCE:
                    addressData.put(FinanceConstants.APIKeys.PERSONAL_TIME_AT_RESIDENCE, item.getValue());
                    break;
            }
        }

        for(QuestionItems item: dataItems) {
            switch(item.getParamKey()) {
                case FinanceConstants.APIKeys.CO_APPLICANT_STREET_ADDRESS:
                    item.setValue(addressData.get(FinanceConstants.APIKeys.PERSONAL_STREET_ADDRESS));
                    break;
                case FinanceConstants.APIKeys.CO_APPLICANT_CITY:
                    item.setValue(addressData.get(FinanceConstants.APIKeys.PERSONAL_CITY));
                    break;
                case FinanceConstants.APIKeys.CO_APPLICANT_STATE:
                    item.setValue(addressData.get(FinanceConstants.APIKeys.PERSONAL_STATE));
                    break;
                case FinanceConstants.APIKeys.CO_APPLICANT_ZIPCODE:
                    item.setValue(addressData.get(FinanceConstants.APIKeys.PERSONAL_ZIPCODE));
                    break;
                case FinanceConstants.APIKeys.CO_APPLICANT_TIME_AT_RESIDENCE:
                    item.setValue(addressData.get(FinanceConstants.APIKeys.PERSONAL_TIME_AT_RESIDENCE));
                    break;
            }
        }

        adapter.notifyDataSetChanged();
    }


    public interface AdapterListener{
        void scrollNext(int pos);
        void updateCoApplicantAddress();
    }
}
