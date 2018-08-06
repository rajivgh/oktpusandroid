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
import com.app.oktpus.adapter.carfinance.CFLoanInfoAdapter;
import com.app.oktpus.model.carfinance.CFLoanInfoModel;
import com.app.oktpus.model.carinsurance.QuestionItems;

import java.util.ArrayList;

import wizardlib.model.Page;
import wizardlib.model.ui.PageFragmentCallbacks;

/**
 * Created by Gyandeep on 15/1/18.
 */

public class CFLoanInfo extends Fragment {

    private static final String ARG_KEY = "key";
    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private Page mPage;
    private ArrayList<QuestionItems> dataItems;
    private RecyclerView recyclerView;
    private CFLoanInfoAdapter adapter;
    public CFLoanInfo() {}

    public static CFLoanInfo create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        CFLoanInfo fragment = new CFLoanInfo();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = mCallbacks.onGetPage(mKey);
        CFLoanInfoModel pageModel = (CFLoanInfoModel) mPage;
        dataItems = pageModel.getModelData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_item_insurance, container, false);
        ((TextView) rootView.findViewById(R.id.page_title)).setText(mPage.getTitle());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_ci_page);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CFLoanInfoAdapter(getActivity(), getContext(), mPage, dataItems, new CFLoanInfo.AdapterListener() {
            @Override
            public void scrollNext(int pos) {
                //recyclerView.smoothScrollToPosition(pos);
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

    public interface AdapterListener{
        void scrollNext(int pos);
    }
}
