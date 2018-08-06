package com.app.oktpus.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.app.oktpus.R;

/**
 * Created by Gyandeep on 10/5/18.
 */

public class PostAd extends Fragment {
    private EditText etTitle, etPrice, etDesc;
    public PostAd() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ad_post, null);
        etTitle = (EditText) view.findViewById(R.id.et_title);
        etPrice = (EditText) view.findViewById(R.id.et_price);
        etDesc = (EditText) view.findViewById(R.id.et_desc);


        return view;
    }
}

