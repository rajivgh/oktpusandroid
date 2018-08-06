package com.app.oktpus.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.oktpus.R;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.controller.AppController;

/**
 * Created by Gyandeep on 19/3/18.
 */

public class AboutActivity extends BaseActivity {

    private LinearLayout layoutTermsAndConditions, layoutWriteus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        includeNavigationMenu(this, R.layout.activity_about_screen);
        TextView pageTitle = (TextView) findViewById(R.id.page_title);
        ImageView backButton = (ImageView) findViewById(R.id.ss_back_arrow);

        layoutTermsAndConditions = (LinearLayout) findViewById(R.id.ll_tnc);
        layoutWriteus = (LinearLayout) findViewById(R.id.ll_write_us);

        pageTitle.setTypeface(AppController.getFontType(getApplicationContext()));
        pageTitle.setText(getResources().getString(R.string.nav_item_about));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        layoutTermsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(Flags.URL.TERMS));
                browse.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(browse);
            }
        });

        layoutWriteus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getResources().getString(R.string.mailto))); //+"?subject=" + "subject" + "&body=body");
                startActivity(intent);
            }
        });
    }
}
