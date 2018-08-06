package com.app.oktpus.fragment;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.oktpus.activity.MainActivity;
import com.app.oktpus.activity.SearchActivity;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.R;
import com.app.oktpus.utils.WallOfDeals;

/**
 * Created by Gyandeep on 24/4/17.
 */

public class AppStartModal extends DialogFragment {

    public AppStartModal() {}
    public ImageView ivClose;
    public RelativeLayout btnWallOfDeals, btnSearch;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_start_modal, container);
        ivClose = (ImageView) view.findViewById(R.id.iv_app_start_modal_close);

        btnWallOfDeals = (RelativeLayout) view.findViewById(R.id.btn_wall_of_deals);
        btnSearch = (RelativeLayout) view.findViewById(R.id.btn_search);

        btnWallOfDeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() instanceof MainActivity) {
                    ((MainActivity)getActivity()).mSession.setAppStartModalDisplayed(true);
                }
                WallOfDeals.setDeals(getActivity());
                getDialog().dismiss();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() instanceof MainActivity) {
                    ((MainActivity)getActivity()).mSession.setAppStartModalDisplayed(true);
                }
                Intent search = new Intent(getActivity().getApplicationContext(), SearchActivity.class);
                search.putExtra(Flags.Bundle.Keys.SOURCE_PAGE, Flags.Bundle.Values.REFINE_SEARCH);
                search.putExtra(Flags.Bundle.Keys.TO_SEARCH_FORM, true);
                getActivity().startActivity(search);
                getDialog().dismiss();
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() instanceof MainActivity) {
                    ((MainActivity)getActivity()).mSession.setAppStartModalDisplayed(true);
                }
                getDialog().dismiss();
            }
        });

        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        //((MainActivity)getActivity()).mSession.setAppStartModalDisplayed(true);
    }

    public static AppStartModal newInstance() {
        AppStartModal f = new AppStartModal();
        return f;
    }
}