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
import com.app.oktpus.adapter.carfinance.CFStartPageAdapter;
import com.app.oktpus.model.carfinance.CFLoanInfoModel;
import com.app.oktpus.model.carfinance.CFPersonalInfoModel;
import com.app.oktpus.model.carfinance.CFStartModel;
import com.app.oktpus.model.carinsurance.QuestionItems;

import java.util.ArrayList;

import wizardlib.model.Page;
import wizardlib.model.ui.PageFragmentCallbacks;

import static com.app.oktpus.constants.carfinance.FinanceConstants.PageTitle.CO_APPLICANT_PERSONAL_INFO;
import static com.app.oktpus.constants.carfinance.FinanceConstants.PageTitle.LOAN_INFO_PAGE;
import static com.app.oktpus.constants.carfinance.FinanceConstants.PageTitle.PERSONAL_AND_RESIDENTIAL_PAGE;

/**
 * Created by Gyandeep on 15/1/18.
 */

public class CFStartFragment extends Fragment {

    public CFStartFragment() {}
    private String mKey;
    private Page mPage;
    private CFLoanInfoModel mLoanInfoModel;
    private CFPersonalInfoModel mPersonalInfoModel, mCoApplicantPersonalInfoModel;
    private PageFragmentCallbacks mCallbacks;

    private ArrayList<QuestionItems> dataItems;
    private RecyclerView recyclerView;
    private CFStartPageAdapter adapter;
    private static final String ARG_KEY = "key";

    public static CFStartFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        CFStartFragment fragment = new CFStartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = mCallbacks.onGetPage(mKey);
        CFStartModel model = (CFStartModel) mPage;
        dataItems = model.getModelData();
        mLoanInfoModel = (CFLoanInfoModel) ((CarFinance)getActivity()).onGetPage(LOAN_INFO_PAGE);
        mPersonalInfoModel = (CFPersonalInfoModel) ((CarFinance)getActivity()).onGetPage(PERSONAL_AND_RESIDENTIAL_PAGE);
        mCoApplicantPersonalInfoModel = (CFPersonalInfoModel) ((CarFinance)getActivity()).onGetPage(CO_APPLICANT_PERSONAL_INFO);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_item_insurance, container, false);
        ((TextView) rootView.findViewById(R.id.page_title)).setText(mPage.getTitle());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_ci_page);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CFStartPageAdapter(getActivity(), getContext(), mPage, dataItems, new CFStartFragment.AdapterListener() {
            @Override
            public void scrollNext(int pos) {
                //recyclerView.smoothScrollToPosition(pos);
            }
        }, mLoanInfoModel, mPersonalInfoModel, mCoApplicantPersonalInfoModel);
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

    public interface AdapterListener{
        void scrollNext(int pos);
    }
}
