package com.app.oktpus.fragment.wmcw;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.oktpus.R;
import com.app.oktpus.activity.WhatsMyCarWorth;
import com.app.oktpus.controller.AppController;

/**
 * Created by Gyandeep on 13/3/18.
 */

public class WMCWGetStarted extends Fragment {

    public WMCWGetStarted() {}

    private RelativeLayout mButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_wmcw_getstarted, container, false);
        TextView pageTitle = (TextView) rootView.findViewById(R.id.page_title);
        ImageView backButton = (ImageView) rootView.findViewById(R.id.ss_back_arrow);
        pageTitle.setTypeface(AppController.getFontType(getActivity()));
        pageTitle.setText(getResources().getString(R.string.nav_item_wmcw));
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        mButton = (RelativeLayout) rootView.findViewById(R.id.btn_get_started);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((WhatsMyCarWorth)getActivity()).onStartButtonClicked();
            }
        });

        return rootView;
    }

}
