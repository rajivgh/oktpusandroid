package com.app.oktpus.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.oktpus.adapter.CompareCarAdapter;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.controller.AppController;
import com.app.oktpus.listener.OnCallListener;
import com.app.oktpus.R;
import com.app.oktpus.responseModel.ProductDetails.BaseResponse;
import com.app.oktpus.responseModel.ProductDetails.Result;
import com.app.oktpus.utils.Client;
import com.app.oktpus.utils.RecyclerTouchHelper;
import com.app.oktpus.utils.SessionManagement;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Gyandeep on 19/6/17.
 */

public class CompareCar extends BaseActivity{
    RecyclerView rvItemHolder;
    DrawerLayout drawerLayout;
    RelativeLayout nestedScrollView;
    List<Result> dataList;
    CompareCarAdapter itemHolderAdapter;
    RelativeLayout clearAll, noItemsLayout;
    TextView tvTitle, tvCity, tvDate, tvPrice, tvKms, tvEngine, tvPassengers,
            tvColor, tvInteriorColor, tvDrivetrain, tvDoors, tvMake, tvModel, tvBody,
            tvFuel, tvMarketAvg, tvMarketAvgValue, tvTransmission, tvHeader, tvPageTitle, tvItemsinList;
    ImageView ivClose, ivBack;
    Animation slideLeft, slideRight;
    LinearLayout rootLayoutCompareCarItem;
    CardView btnShow;
    int totalItems = 0;
    //SlidingPaneLayout slidingPaneLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        includeNavigationMenu(this, R.layout.activity_compare_cars);

        rvItemHolder = (RecyclerView) findViewById(R.id.rv_compare_item_holder);
        clearAll = (RelativeLayout) findViewById(R.id.clear_all);
        tvItemsinList = (TextView) findViewById(R.id.tv_items_in_list);
        rootLayoutCompareCarItem = (LinearLayout) findViewById(R.id.root_layout_compare_car_item);
        btnShow = (CardView) findViewById(R.id.view_puller);
        noItemsLayout = (RelativeLayout) findViewById(R.id.no_items_layout);
        tvHeader = (TextView) findViewById(R.id.tv_header);
        tvTitle = (TextView)findViewById(R.id.tv_title);
        tvCity = (TextView)findViewById(R.id.tv_city);
        tvDate = (TextView)findViewById(R.id.tv_date);
        tvPrice = (TextView)findViewById(R.id.tv_price);
        tvKms = (TextView)findViewById(R.id.tv_kms);
        tvEngine = (TextView)findViewById(R.id.tv_engine);
        tvPassengers = (TextView)findViewById(R.id.tv_passengers);
        tvColor = (TextView)findViewById(R.id.tv_color);
        tvInteriorColor = (TextView)findViewById(R.id.tv_interior_color);
        tvDrivetrain = (TextView)findViewById(R.id.tv_drivetrain);
        tvTransmission = (TextView) findViewById(R.id.tv_transmission);
        tvDoors = (TextView)findViewById(R.id.tv_doors);
        tvMake = (TextView) findViewById(R.id.tv_make);
        tvModel = (TextView)findViewById(R.id.tv_model);
        tvBody = (TextView) findViewById(R.id.tv_body);
        tvMarketAvg = (TextView) findViewById(R.id.tv_ma);
        tvMarketAvgValue = (TextView) findViewById(R.id.tv_ma_value);
        //tvAccessories = (TextView) findViewById(R.id.tv_accessories);
        //tvCylinder = (TextView) findViewById(R.id.tv_cylinder);
        //tvStockNumber = (TextView) findViewById(R.id.tv_stock_number);
        tvFuel = (TextView) findViewById(R.id.tv_fuel);
        //tvRating = (TextView) findViewById(R.id.tv_rating);
        //tvSafetyRating = (TextView) findViewById(R.id.tv_safety_rating);

        tvPageTitle = (TextView) findViewById(R.id.page_title);
        ivBack = (ImageView) findViewById(R.id.ss_back_arrow);

        ivClose = (ImageView)findViewById(R.id.iv_close);
        ivClose.setVisibility(View.INVISIBLE);

        final SessionManagement session = new SessionManagement(this, this);
        dataList = new ArrayList<>();
        itemHolderAdapter = new CompareCarAdapter(dataList, this, session, this);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        rvItemHolder.setLayoutManager(layoutManager);
        rvItemHolder.setItemAnimator(new DefaultItemAnimator());
        rvItemHolder.setAdapter(itemHolderAdapter);

        ItemTouchHelper.Callback callback = new RecyclerTouchHelper(itemHolderAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rvItemHolder);

        if(null != session.getCompareCarProductIds() && session.getCompareCarProductIds().size() > 0)
            loadProductDetails(android.text.TextUtils.join(",",(Set<String>)session.getCompareCarProductIds()));
        else
            noItemsLayout.setVisibility(View.VISIBLE);

        updateTotalItems(totalItems);
        tvHeader.setText("Features");
        tvHeader.setTypeface(Typeface.DEFAULT_BOLD);

        tvTitle.setText("Item Name");
        tvCity.setText("City");
        tvDate.setText("Post Date");
        tvPrice.setText("Price");
        tvKms.setText("Distance");
        tvEngine.setText("Engine");
        tvPassengers.setText("Passengers");
        tvColor.setText("Colour");
        tvInteriorColor.setText("Interior Colour");
        tvDrivetrain.setText("Drivetrain");
        tvDoors.setText("Doors");
        tvTransmission.setText("Transmission");
        tvMake.setText("Make");
        tvModel.setText("Model");
        tvBody.setText("Body");
        tvMarketAvg.setText("Market Avg.");
        tvMarketAvgValue.setText("Value");
        //tvAccessories.setText("Accessories");
        //tvCylinder.setText("Cylinder");
        //tvStockNumber.setText("Stock Number");
        tvFuel.setText("Fuel");
        //tvRating.setText("Rating");

        slideLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left_anim);
        slideRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right_anim);

        btnShow.setVisibility(View.GONE);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootLayoutCompareCarItem.setVisibility(View.VISIBLE);
                Animation anim = new TranslateAnimation(0,0,0,0);
                anim.setDuration(100);
                anim.setFillAfter(true);
                rootLayoutCompareCarItem.startAnimation(anim);

                Animation animPuller = new TranslateAnimation(0,-btnShow.getWidth(),0,0);
                animPuller.setDuration(250);
                animPuller.setFillAfter(true);
                btnShow.startAnimation(animPuller);
                btnShow.setVisibility(View.GONE);
            }
        });
        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.removeAllcompareCarItems();
                dataList.clear();
                updateTotalItems(0);
                itemHolderAdapter.notifyDataSetChanged();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvPageTitle.setTypeface(AppController.getFontType(this));
        tvPageTitle.setText("Compare cars");

        rootLayoutCompareCarItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Animation animation = new TranslateAnimation(0, -rootLayoutCompareCarItem.getWidth(),0, 0);
                animation.setDuration(500);
                animation.setFillAfter(true);

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        *//*Animation anim = new TranslateAnimation(-rootLayoutCompareCarItem.getWidth(),0,0, 0);
                        anim.setDuration(500);
                        anim.setFillAfter(true);
                        rvItemHolder.startAnimation(anim);*//*
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        rootLayoutCompareCarItem.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                rootLayoutCompareCarItem.startAnimation(animation);*/


                Animation anim = new TranslateAnimation(0,-rootLayoutCompareCarItem.getWidth(),0, 0);
                anim.setDuration(200);
                anim.setFillAfter(true);

                Animation anim1 = new TranslateAnimation(0,0,0,0);
                anim1.setDuration(200);
                anim1.setFillAfter(true);

                final Animation animPuller = new TranslateAnimation(0,0,0,0);
                animPuller.setDuration(200);
                animPuller.setFillAfter(true);

                anim1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        rootLayoutCompareCarItem.setVisibility(View.GONE);
                        btnShow.startAnimation(animPuller);
                        btnShow.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });

                rootLayoutCompareCarItem.startAnimation(anim);
                rvItemHolder.startAnimation(anim1);
            }
        });
    }

    public void updateTotalItems(int totalItems) {
        tvItemsinList.setText(totalItems+"/5");
    }

    public void loadProductDetails(String requestParam) {
        try {
            Client jsonRequest = new Client(Request.Method.GET, Flags.URL.API_ITEM_DETAILS + requestParam, BaseResponse.class, new OnCallListener() {
                @Override
                public void nwResponseData(String pData) {
                    System.out.println("rsposne: " + pData);
                }
            }, new Response.Listener<Object>() {
                @Override
                public void onResponse(Object response) {
                    BaseResponse res = (BaseResponse)response;
                    dataList.clear();
                    dataList.addAll(res.getResult());
                    rvItemHolder.swapAdapter(itemHolderAdapter, false);
                    updateTotalItems(dataList.size());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("error: "+error);
                }
            });

            String tag_json_request = "json_obj_request";
            jsonRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsonRequest, tag_json_request);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.with(this).onDestroy();
    }
}