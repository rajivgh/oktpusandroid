package com.app.oktpus.activity;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.app.oktpus.adapter.NotificationsListAdapter;
import com.app.oktpus.model.SearchResponse;
import com.app.oktpus.model.SearchResultItem;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.controller.AppController;
import com.app.oktpus.listener.OnCallListener;
import com.app.oktpus.R;
import com.app.oktpus.utils.Client;
import com.app.oktpus.utils.SessionManagement;
import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class NotificationHistory extends BaseActivity{

    private RecyclerView recyclerView;
    private NotificationsListAdapter adapter;
    private List<SearchResultItem> notificationsList;
    private TextView pageTitle, noResultMsg;
    private ImageView backButton;
    private RelativeLayout noResultView;
    private Tracker mTracker;
    //private FloatingActionButton fab;
    //private AppBarLayout appBarLayout;
    private NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        includeNavigationMenu(this, R.layout.activity_notification_history);
        mTracker = AppController.getInstance().getDefaultTracker();
        mTracker.setScreenName("Notification History screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        nestedScrollView = (NestedScrollView) findViewById(R.id.nested_scroll_view);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_notif);
        pageTitle = (TextView) findViewById(R.id.page_title);
        backButton = (ImageView) findViewById(R.id.ss_back_arrow);
        noResultView = (RelativeLayout) findViewById(R.id.no_results_layout);
        noResultMsg = (TextView) findViewById(R.id.no_results_msg);
        noResultMsg.setTypeface(AppController.getFontType(getApplicationContext()));

        pageTitle.setTypeface(AppController.getFontType(getApplicationContext()));
        pageTitle.setText(getResources().getString(R.string.nav_item_notif_history));

        notificationsList = new ArrayList<>();
        adapter = new NotificationsListAdapter(NotificationHistory.this, notificationsList, new SessionManagement(this));
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

        nestedScrollView.setFillViewport(true);
        /*fab = ((BaseActivity) this).mFab;
        appBarLayout = ((BaseActivity)this).mAppBarLayout;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nestedScrollView.smoothScrollTo(0, 0);
                showToolbar();
                fab.hide();
            }
        });*/

        apiRequest(Request.Method.GET, Flags.URL.GET_NOTIFICATION_LIST, SearchResponse.class);
    }

    /**
     * Converting dp to pixel
     */
    /*private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }*/

    /*public void showToolbar(){
        try {
            if(appBarLayout != null) {
                appBarLayout.setExpanded(true, true);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }*/

    public String responseData;
    public com.app.oktpus.fragment.SearchResult.ProductFlags products = null;
    private void apiRequest(final int methodType, final String url, final Class resClass) {

        try {
            Log.d(Flags.ActivityTag.NOTIFICATIONS_HISTORY,"attrib url: " + url);

            Client jsObjRequest = new Client(methodType, url, resClass, new OnCallListener() {
                @Override
                public void nwResponseData(String pData) {
                    responseData = pData;
                }
            }, new Response.Listener<Object>() {
                @Override
                public void onResponse(Object response) {
                    //ResponseNotificationHistory res = (ResponseNotificationHistory) response;
                    //ResponseCode resCode = ResponseCode.fromValue(res.getStatus());
                    SearchResponse res = (SearchResponse)response;
                    if(res.getStatus() == 1) {
                        try {
                            adapter.enableLoadingBar(false);
                            JsonObject jObj = new JsonParser().parse(responseData).getAsJsonObject();
                            //JsonObject resultObj = (JsonObject)jObj.get(Flags.Keys.RESULT);

                            /*JsonArray searchResult = (JsonArray) res.get(Flags.Keys.SEARCH_RESULT);
                            List<NotificationHistoryFlagsResult> resultList = res.getNotifListResult();*/

                            if(jObj.has(Flags.Keys.USER_HAS_PRODUCT_FLAG) && jObj.get(Flags.Keys.USER_HAS_PRODUCT_FLAG).isJsonObject()) {
                                Gson gson = new GsonBuilder().create();
                                products = gson.fromJson(jObj, com.app.oktpus.fragment.SearchResult.ProductFlags.class);
                            }

                            if(jObj.get(Flags.Keys.RESULT).isJsonArray()) {
                                JsonArray searchResult = (JsonArray) jObj.get(Flags.Keys.RESULT);

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
                                        boolean isFav = false;
                                        int productID = item.get(Flags.Keys.PRODUCT_ID).getAsInt();

                                        if(null != products && (products.getUser_has_product_flag()).containsKey(item.get(Flags.Keys.PRODUCT_ID).getAsString())) {
                                            isFav = ((com.app.oktpus.fragment.SearchResult.ProductFlagItem)((products.getUser_has_product_flag()).get(item.get(Flags.Keys.PRODUCT_ID).getAsString()))).getFavorite();
                                        }

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
                                                maShow,
                                                String.valueOf(item.get(Flags.Keys.SENT_DATE) == null ? "" : item.get(Flags.Keys.SENT_DATE).getAsString()),
                                                isFav, productID, String.valueOf((item.get(Flags.Keys.HREF_URL) == null)? "":item.get(Flags.Keys.HREF_URL).getAsString()));
                                        notificationsList.add(searchResultItem);
                                        count++;

                                        if (count == 30)
                                            break;
                                    }

                                    ////////////////////


                                    /*for (NotificationHistoryFlagsResult resultItem : resultList) {
                                    *//*Log.d("RESULT: ", "title: "+resultItem.getItemTitle() +
                                            ", "+ resultItem.getPrice() );*//*
                                        SearchResultItem notifItem = new SearchResultItem();

                                        if (currencyFormat.equals("USD")) {
                                            notifItem.setPrice(notifItem.getCurrency_usd().replaceAll("\"", ""));
                                        } else if (currencyFormat.equals("CAD")) {
                                            notifItem.setPrice(notifItem.getCurrency_cad().replaceAll("\"", ""));
                                        }

                                        if (kilometerFormat.equals("kilometers")) {
                                            ((SearchResultAdapter.TextViewHolder) holder).distance.setText(item.getKilometers_kilometers().replaceAll("\"", ""));
                                        } else if (kilometerFormat.equals("miles")) {
                                            ((SearchResultAdapter.TextViewHolder) holder).distance.setText(item.getKilometers_miles().replaceAll("\"", ""));
                                        }

                                        notifItem.setItemTitle(resultItem.getItemTitle());

                                        notifItem.setImg(resultItem.getImgURL());
                                        notifItem.setSentDate(resultItem.getSentDate());
                                        notifItem.setKilometers(resultItem.getKilometers());
                                        notifItem.setUrl(resultItem.getUrl());
                                        notificationsList.add(notifItem);
                                        count++;

                                        if (count == 30)
                                            break;
                                    }*/
                                    //adapter.enableFooter(true);
                                }
                            }
                            else{
                                recyclerView.setVisibility(View.GONE);
                                noResultView.setVisibility(View.VISIBLE);
                                /*for(int i = 0; i< 5 ; i++) {
                                    NotificationsItem notifItem = new NotificationsItem();
                                    notifItem.setItemTitle("title"+i);
                                    notifItem.setCurrency("CAD");
                                    notifItem.setPrice("100"+i);
                                    notifItem.setImage0("");
                                    notifItem.setSentDate("10 February 2017");
                                    notifItem.setKilometers("1111"+i);
                                    notifItem.setUrl("");
                                    notificationsList.add(notifItem);
                                }*/
                                //adapter.enableFooter(true);

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
                        Log.d(Flags.ActivityTag.NOTIFICATIONS_HISTORY, "status 0");
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(null != adapter) {
                        adapter.enableLoadingBar(false);
                        adapter.notifyDataSetChanged();
                    }
                    Log.d(Flags.ActivityTag.NOTIFICATIONS_HISTORY, "Error: " + error);
                }
            });
            String tag_json_obj = "json_obj_req";
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsObjRequest, tag_json_obj);
        }
        catch(Exception e) {
            Log.d(Flags.ActivityTag.NOTIFICATIONS_HISTORY, e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.with(this).onDestroy();
    }
}