package com.app.oktpus.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.oktpus.adapter.GarageAdapter;
import com.app.oktpus.model.SearchResponse;
import com.app.oktpus.model.SearchResultItem;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.controller.AppController;
import com.app.oktpus.listener.OnCallListener;
import com.app.oktpus.R;
import com.app.oktpus.utils.Client;
import com.app.oktpus.utils.SessionManagement;
import com.app.oktpus.utils.Utility;
import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Gyandeep on 29/5/17.
 */

public class GarageActivity extends BaseActivity{

    private RecyclerView recyclerView;
    private GarageAdapter adapter;
    private List<SearchResultItem> itemsList;
    private TextView pageTitle, noResultMsg;
    private ImageView backButton;
    private RelativeLayout noResultView, rootLayout;
    private Tracker mTracker;
    private SessionManagement session;
    private String responseData;
    private Client clientRequestObj;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        includeNavigationMenu(this, R.layout.activity_garage);
        session = new SessionManagement(getApplicationContext());
        /*mTracker = AppController.getInstance().getDefaultTracker();
        mTracker.setScreenName("Notification History screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());*/

        rootLayout = (RelativeLayout) findViewById(R.id.root);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_garage);
        pageTitle = (TextView) findViewById(R.id.page_title);
        backButton = (ImageView) findViewById(R.id.ss_back_arrow);
        noResultView = (RelativeLayout) findViewById(R.id.no_results_layout);
        noResultMsg = (TextView) findViewById(R.id.no_results_msg);
        noResultMsg.setTypeface(AppController.getFontType(getApplicationContext()));

        pageTitle.setTypeface(AppController.getFontType(getApplicationContext()));
        pageTitle.setText(getResources().getString(R.string.nav_item_garage));

        itemsList = new ArrayList<>();
        adapter = new GarageAdapter(GarageActivity.this, itemsList, session);
        recyclerView.setAdapter(adapter);

        if(getResources().getBoolean(R.bool.isTablet)) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(gridLayoutManager);
        }
        else
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.enableLoadingBar(true);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        apiRequest(Request.Method.GET, Flags.URL.GET_GARAGE, SearchResponse.class);
    }

    private void apiRequest(final int methodType, final String url, final Class resClass) {
        try {
            Log.d(Flags.ActivityTag.GARAGE,"attrib url: " + url);
            clientRequestObj = new Client(methodType, url, resClass, new OnCallListener() {
                @Override
                public void nwResponseData(String pData) {
                    responseData = pData;
                }
            }, new Response.Listener<Object>() {
                @Override
                public void onResponse(Object response) {
                    SearchResponse res = (SearchResponse)response;
                    if(res.getStatus() == 1) {
                        try {
                            adapter.enableLoadingBar(false);
                            JsonObject jObj = new JsonParser().parse(responseData).getAsJsonObject();
                            if(jObj.get(Flags.Keys.DATA).isJsonObject()) {
                                JsonObject data = (JsonObject) jObj.get(Flags.Keys.DATA);
                                if(data.get(Flags.Keys.PRODUCTS).isJsonArray()) {
                                    JsonArray searchResult = (JsonArray) data.get(Flags.Keys.PRODUCTS);
                                    if (searchResult.size() > 0) {
                                        noResultView.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        int count = 0;

                                        Iterator iterator = searchResult.iterator();
                                        SearchResultItem searchResultItem;
                                        while (iterator.hasNext()) {
                                            JsonObject item = (JsonObject) iterator.next();
                                            JsonObject priceList = null, kmsList = null, maDiffList = null;
                                            String priceUSD = "", priceCAD = "", priceEUR = "", maDiffUSD = "", maDiffCAD = "", kms = "", miles = "";

                                            if (item.has(Flags.Keys.PRICE_LIST)) {
                                                priceList = item.getAsJsonObject(Flags.Keys.PRICE_LIST);
                                                if (null != priceList && priceList.has(Flags.Keys.PRICE_LIST_USD))
                                                    priceUSD = priceList.get(Flags.Keys.PRICE_LIST_USD).getAsString();
                                                if (null != priceList && priceList.has(Flags.Keys.PRICE_LIST_CAD))
                                                    priceCAD = priceList.get(Flags.Keys.PRICE_LIST_CAD).getAsString();
                                            }

                                            if (item.has(Flags.Keys.KILOMETERS_LIST)) {
                                                kmsList = item.getAsJsonObject(Flags.Keys.KILOMETERS_LIST);
                                                if (null != kmsList && kmsList.has(Flags.Keys.KILOMETERS))
                                                    kms = kmsList.get(Flags.Keys.KILOMETERS).getAsString();

                                                if (null != kmsList && kmsList.has(Flags.Keys.MILES))
                                                    miles = kmsList.get(Flags.Keys.MILES).getAsString();
                                            }

                                            if (item.has("ma_difference_list")) {
                                                maDiffList = item.getAsJsonObject("ma_difference_list");
                                                if (null != maDiffList && maDiffList.has(Flags.Keys.PRICE_LIST_USD))
                                                    maDiffUSD = maDiffList.get(Flags.Keys.PRICE_LIST_USD).getAsString();
                                                if (null != maDiffList && maDiffList.has(Flags.Keys.PRICE_LIST_CAD))
                                                    maDiffCAD = maDiffList.get(Flags.Keys.PRICE_LIST_CAD).getAsString();
                                            }
                                            boolean maShow = item.get(Flags.Keys.MA_SHOW).getAsBoolean();
                                            searchResultItem = new SearchResultItem(String.valueOf(item.get(Flags.Keys.ITEM_TITLE) == null ? "" : item.get(Flags.Keys.ITEM_TITLE).getAsString()),
                                                    item.get(Flags.Keys.IMAGE_URL) == null ? "" : item.get(Flags.Keys.IMAGE_URL).getAsString(),
                                                    String.valueOf(item.get(Flags.Keys.POST_DATE) == null ? "" : item.get(Flags.Keys.POST_DATE).getAsString()),
                                                    String.valueOf(item.get(Flags.Keys.CITY) == null ? "" : item.get(Flags.Keys.CITY).getAsString()),
                                                    priceUSD, priceCAD, kms, miles,
                                                    String.valueOf((item.get(Flags.Keys.PRICE) == null) ? "" : item.get(Flags.Keys.PRICE).getAsString()),
                                                    maDiffUSD, maDiffCAD,
                                                    String.valueOf((item.get(Flags.Keys.URL) == null) ? "" : item.get(Flags.Keys.URL).getAsString()),
                                                    String.valueOf((item.get(Flags.Keys.MA_KEYWORD) == null) ? "" : item.get(Flags.Keys.MA_KEYWORD).getAsString()),
                                                    maShow, String.valueOf((item.get(Flags.Keys.SENT_DATE) == null) ? "" : item.get(Flags.Keys.SENT_DATE).getAsString()),
                                                    true, item.get(Flags.Keys.PRODUCT_ID).getAsInt(),
                                                    String.valueOf((item.get(Flags.Keys.HREF_URL) == null)? "":item.get(Flags.Keys.HREF_URL).getAsString()));
                                            itemsList.add(searchResultItem);
                                            count++;

                                            if (count == 150)
                                                break;
                                        }
                                    }
                                }
                            }
                            else{
                                recyclerView.setVisibility(View.GONE);
                                noResultView.setVisibility(View.VISIBLE);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        if(null != adapter) {
                            adapter.enableLoadingBar(false);
                            adapter.notifyDataSetChanged();
                        }
                        Log.d(Flags.ActivityTag.GARAGE, "status 0");
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(null != adapter) {
                        adapter.enableLoadingBar(false);
                        adapter.notifyDataSetChanged();
                        Utility.showErrorSnackbar(getApplicationContext(), rootLayout, clientRequestObj, error, Flags.NetworkRequestTags.GARAGE_LIST);
                    }
                    Log.d(Flags.ActivityTag.GARAGE, "Error: " + error);
                }
            });

            clientRequestObj.setRetryPolicy(new DefaultRetryPolicy(50000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(clientRequestObj, Flags.NetworkRequestTags.GARAGE_LIST);
        }
        catch(Exception e) {
            Log.d(Flags.ActivityTag.GARAGE, e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.with(getApplicationContext()).onDestroy();
    }
}