package com.app.oktpus.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.app.oktpus.R;
import com.app.oktpus.fragment.PostAd;

/**
 * Created by Gyandeep on 16/5/18.
 */

public class PostAdActivity extends BaseActivity {

    FrameLayout container;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        includeNavigationMenu(this, R.layout.activity_ad_post);
        container = (FrameLayout) findViewById(R.id.fragment_frame);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Fragment fragment = new PostAd();
        ft.add(container.getId(), fragment);
        ft.commit();
    }
}
