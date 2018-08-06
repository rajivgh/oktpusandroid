package com.app.oktpus.fragment.carfinance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.oktpus.R;
import com.app.oktpus.activity.CarFinance;
import com.app.oktpus.controller.AppController;

/**
 * Created by Gyandeep on 9/1/18.
 */

public class GettingStartedPage extends Fragment {

    public GettingStartedPage() {}

    private RelativeLayout mButton;
    private Spinner mSpinnerCountry;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cf_start_page, container, false);
        TextView pageTitle = (TextView) rootView.findViewById(R.id.page_title);
        ImageView backButton = (ImageView) rootView.findViewById(R.id.ss_back_arrow);

        mButton = (RelativeLayout) rootView.findViewById(R.id.btn_ci_start_page);
        mSpinnerCountry = (Spinner) rootView.findViewById(R.id.spinner_select_country);
        mSpinnerCountry.setPadding(0,0,0,0);

        pageTitle.setTypeface(AppController.getFontType(getContext()));
        pageTitle.setText(getResources().getString(R.string.nav_item_car_finance));
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (getActivity()).finish();
            }
        });
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), R.layout.wmcw_spinner_item, getContext().getResources().getStringArray(R.array.allowed_countries_2));
        mSpinnerCountry.setAdapter(dataAdapter);
        mSpinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();
                ((CarFinance)getActivity()).countryPageItem.get(0).setValue(value);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((CarFinance)getActivity()).countryPageItem.get(1).setValue(mEditText.getText().toString());
                //Utility.hideKeyboard(getContext(), mEditText);
                ((CarFinance)getActivity()).onStartButtonClicked();
            }
        });

        return rootView;
    }
}
