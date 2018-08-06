package com.app.oktpus.fragment.wmcw;

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
import com.app.oktpus.adapter.WizardPageViewAdapter;
import com.app.oktpus.model.AttrValSpinnerModel;
import com.app.oktpus.model.carinsurance.QuestionItems;
import com.app.oktpus.model.wmcw.WMCWVehicleModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wizardlib.model.Page;
import wizardlib.model.ui.PageFragmentCallbacks;

/**
 * Created by Gyandeep on 13/3/18.
 */

public class WMCWVehicleFragment extends Fragment {

    private static final String ARG_KEY = "key";

    private PageFragmentCallbacks mCallbacks;
    private List<String> mChoices;
    private String mKey;
    private Page mPage;
    private Bundle mBundleData;
    public WMCWVehicleFragment() {}

    public static WMCWVehicleFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        WMCWVehicleFragment fragment = new WMCWVehicleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static void parentActivity() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = mCallbacks.onGetPage(mKey);
        WMCWVehicleModel model = (WMCWVehicleModel) mPage;
        mBundleData = new Bundle();
        dataItems = model.getModelData();
        mFilterAttributeData = model.mAttributeData;
    }

    private ArrayList<QuestionItems> dataItems;
    private RecyclerView recyclerView;
    private WizardPageViewAdapter adapter;

    private Map<String, List<AttrValSpinnerModel>> mFilterAttributeData = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_wizard_fragment, container, false);
        ((TextView) rootView.findViewById(R.id.page_title)).setText(mPage.getTitle());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_wizard_page);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new WizardPageViewAdapter(getActivity(), getContext(), mPage, mFilterAttributeData, dataItems);
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
}