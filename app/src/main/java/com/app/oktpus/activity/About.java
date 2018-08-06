package com.app.oktpus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.oktpus.constants.Flags;
import com.app.oktpus.controller.AppController;
import com.app.oktpus.R;
import com.app.oktpus.utils.WallOfDeals;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by Gyandeep on 18/12/16.
 */

public class About extends AppCompatActivity {

    private Button btnClose, btnSearch, btnWallOfDeals;
    private RelativeLayout bgImg;
    private TextView loginLink, signupLink;
    private Tracker mTracker;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mTracker = AppController.getInstance().getDefaultTracker();
        mTracker.setScreenName("About");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        btnClose = (Button) findViewById(R.id.btn_not_logged_in_close);
        btnSearch = (Button) findViewById(R.id.btn_start_search);
        bgImg = (RelativeLayout) findViewById(R.id.bg_img);
        loginLink = (TextView) findViewById(R.id.about_login);
        signupLink = (TextView) findViewById(R.id.about_signup);
        btnWallOfDeals = (Button) findViewById(R.id.btn_wall_of_deals);

        bgImg.setAlpha(Flags.bgOpacity);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra(Flags.Bundle.Keys.SOURCE_PAGE, Flags.Bundle.Values.NEW_SEARCH);
                intent.putExtra(Flags.Bundle.Keys.TO_SEARCH_FORM, true);
                startActivity(intent);
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(i);
            }
        });

        btnWallOfDeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WallOfDeals.setDeals(About.this);
            }
        });
    }

}