package com.app.oktpus.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.app.oktpus.activity.SearchActivity;
import com.app.oktpus.adapter.SortRecyclerAdapter;
import com.app.oktpus.R;

/**
 * Created by Gyandeep on 23/4/17.
 */

public class SortDialogFragment extends DialogFragment{

    public SortDialogFragment() {}

    public SortRecyclerAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.item_sort_spinner_close_btn, container);
        ImageButton ib = (ImageButton)view.findViewById(R.id.close_tag);
        RecyclerView sortRecycler = (RecyclerView) view.findViewById(R.id.sort_recycler);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        /*if(null == adapter) {
            adapter = new SortRecyclerAdapter(optionsList((SearchActivity) getActivity()), new OptionSelectListener() {
                @Override
                public void sortSelected(int pos) {
                    ((SearchActivity)getActivity()).onSortParamSetTriggerRequest(pos);
                    getDialog().dismiss();
                }
            });
        }*/

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager((SearchActivity)getActivity());
        sortRecycler.setLayoutManager(mLayoutManager);
        if(null != adapter)sortRecycler.setAdapter(adapter);


        //getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        /*dataAdapter = new SortSpinnerAdapter(mContext, R.layout.item_sort_spinner, optionsList(mContext), new SearchResult.AdapterCallback(){
            @Override
            public void optionSelected(int pos) {
                if(null != callbackListener) callbackListener.optionSelected(pos);
            }
            @Override
            public void searchBarKeyword(String keyword) { }
        });*/
        /*mEditText = (EditText) view.findViewById(R.id.username);

        // set this instance as callback for editor action
        mEditText.setOnEditorActionListener(this);
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setTitle("Please enter username");
*/
        return view;
    }

    public String getCurrentSortOption() {
        return adapter.getCurrentOption();
    }

    public interface OptionSelectListener {
        void sortSelected(int pos);
    }

    public static SortDialogFragment newInstance() {
        SortDialogFragment f = new SortDialogFragment();
        return f;
    }

    public void setSortAdapter(SortRecyclerAdapter adapter) {
        this.adapter = adapter;
    }
}