package com.app.oktpus.fragment;

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
import com.app.oktpus.adapter.CIDiscountAdapter;
import com.app.oktpus.model.carinsurance.CIDiscountModel;
import com.app.oktpus.model.carinsurance.QuestionItems;

import java.util.ArrayList;

import wizardlib.model.Page;
import wizardlib.model.ui.PageFragmentCallbacks;

/**
 * Created by Gyandeep on 20/11/17.
 */

public class CIDiscountFragment extends Fragment {

    public CIDiscountFragment() {}
    private String mKey;
    private Page mPage;

    private PageFragmentCallbacks mCallbacks;

    private ArrayList<QuestionItems> dataItems;
    private RecyclerView recyclerView;
    private CIDiscountAdapter adapter;

    private static final String ARG_KEY = "key";

    public static CIDiscountFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        CIDiscountFragment fragment = new CIDiscountFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = mCallbacks.onGetPage(mKey);
        CIDiscountModel model = (CIDiscountModel) mPage;

        dataItems = model.getModelData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_item_insurance, container, false);
        ((TextView) rootView.findViewById(R.id.page_title)).setText(mPage.getTitle());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_ci_page);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CIDiscountAdapter(getActivity(), getContext(), mPage, dataItems);
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
