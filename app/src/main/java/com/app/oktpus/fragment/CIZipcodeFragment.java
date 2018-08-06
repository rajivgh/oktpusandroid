package com.app.oktpus.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.oktpus.R;
import com.app.oktpus.activity.CarInsurance;
import com.app.oktpus.controller.AppController;
import com.app.oktpus.utils.Utility;

/**
 * Created by Gyandeep on 16/11/17.
 */

public class CIZipcodeFragment extends Fragment {

    public CIZipcodeFragment() {}

    private EditText mEditText;
    private RelativeLayout mButton;
    private Spinner mSpinner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ci_start_page, container, false);
        TextView pageTitle = (TextView) rootView.findViewById(R.id.page_title);
        ImageView backButton = (ImageView) rootView.findViewById(R.id.ss_back_arrow);
        mEditText = (EditText) rootView.findViewById(R.id.et_ci_zipcode);
        mButton = (RelativeLayout) rootView.findViewById(R.id.btn_ci_start_page);
        mSpinner = (Spinner) rootView.findViewById(R.id.spinner_select_country);
        mSpinner.setPadding(0,0,0,0);

        //mButton.setEnabled(false);

        /*mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>5) {
                    mButton.setEnabled(true);
                }
                else {
                    mButton.setEnabled(false);
                }
            }
        });*/


        pageTitle.setTypeface(AppController.getFontType(getContext()));
        pageTitle.setText(getResources().getString(R.string.nav_item_car_insurance));
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (getActivity()).finish();
            }
        });
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), R.layout.wmcw_spinner_item, getContext().getResources().getStringArray(R.array.allowed_countries_2));
        mSpinner.setAdapter(dataAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();
                ((CarInsurance)getActivity()).zipAndCountry.get(0).setValue(value);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CarInsurance)getActivity()).zipAndCountry.get(1).setValue(mEditText.getText().toString());
                Utility.hideKeyboard(getContext(), mEditText);
                ((CarInsurance)getActivity()).onStartButtonClicked();
            }
        });

        return rootView;
    }

}
