package com.app.oktpus.fragment;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.app.oktpus.R;

/**
 * Created by Gyandeep on 19/5/17.
 */

public class ConfirmDisableLocationPermission extends DialogFragment {
    public ConfirmDisableLocationPermission() {}
    private Button btnGoToSettings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.location_disable_confirm_dialog, container);
        btnGoToSettings = (Button)view.findViewById(R.id.btn_go_to_settings);

        btnGoToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                getActivity().finish();
            }
        });
        return view;
    }

    public static ConfirmDisableLocationPermission newInstance() {
        ConfirmDisableLocationPermission f = new ConfirmDisableLocationPermission();
        return f;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        getActivity().finish();
    }
}