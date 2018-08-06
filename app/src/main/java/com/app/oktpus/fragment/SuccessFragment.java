package com.app.oktpus.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.oktpus.R;
import com.app.oktpus.activity.CarFinance;
import com.app.oktpus.activity.CarInsurance;
import com.app.oktpus.activity.WhatsMyCarWorth;

/**
 * Created by Gyandeep on 22/11/17.
 */

public class SuccessFragment extends Fragment {

    public SuccessFragment() {}

    public static SuccessFragment create() {
        return new SuccessFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_success, container, false);
        //TextView successMsg = (TextView) rootView.findViewById(R.id.tv_success_msg);
        //successMsg.setText("Form submitted successfully \nYou will receive an email shortly!");

        LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.ll_success_msg_center);
        ll.setVisibility(View.INVISIBLE);

        Button doneButton = (Button) rootView.findViewById(R.id.btn_done_success);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() instanceof CarInsurance) {
                    ((CarInsurance)getActivity()).dismissActivity();
                }
                else if(getActivity() instanceof CarFinance) {
                    ((CarFinance)getActivity()).dismissActivity();
                }
                else if(getActivity() instanceof WhatsMyCarWorth) {
                    ((WhatsMyCarWorth)getActivity()).dismissActivity();
                }

            }
        });

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
