package com.app.oktpus.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.oktpus.activity.BaseActivity;
import com.app.oktpus.activity.SearchActivity;
import com.app.oktpus.adapter.SearchResultAdapter;
import com.app.oktpus.constants.SearchResultViewType;
import com.app.oktpus.model.AttrValSpinnerModel;
import com.app.oktpus.model.SearchResponse;
import com.app.oktpus.model.SearchResultItem;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.controller.AppController;
import com.app.oktpus.listener.OnCallListener;
import com.app.oktpus.listener.OnSearchActivityCalls;
import com.app.oktpus.R;
import com.app.oktpus.responseModel.Config.CommonFields;
import com.app.oktpus.utils.Client;
import com.app.oktpus.utils.EndlessScrollListener;
import com.app.oktpus.utils.FlingControlRecyclerView;
import com.app.oktpus.utils.LinearLayoutManagerWithSmoothScroll;
import com.app.oktpus.utils.SessionManagement;
import com.app.oktpus.utils.Utility;
import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpStatus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Gyandeep on 17/11/16.
 */

public class SearchResult extends Fragment implements OnCallListener, OnSearchActivityCalls, AdapterView.OnItemSelectedListener{

    private SearchActivity mContext;
    public static final String TAG  = "SearchResultActivity";
    public String responseData;
    private int mPageNumber = 0, mPerPage = 0;
    private FlingControlRecyclerView recyclerView;
    private SearchResultAdapter adapter;
    private EndlessScrollListener scrollListener;
    private byte[] requestData = null;
    private int countItems;
    private int sortParam = 0;
    private Bundle bundle;
    private SessionManagement mSession;
    private FloatingActionButton scrollToTopFAB, backToTopFAB;
    private AppBarLayout appBarLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Tracker mTracker;
    boolean isTablet = false;
    private OnSearchActivityCalls searchActivityListener;
    public List<SearchResultItem> getResultList() {
        return ((SearchActivity)getActivity()).getResultList();
    }

    @Override
    public void setRequestData(Bundle requestBundle) {
        if(requestBundle != null)
            bundle = requestBundle;

        if(requestBundle.containsKey(Flags.Bundle.Keys.KEYWORD)) {
            adapter.keywordSearchAction(requestBundle.getString(Flags.Bundle.Keys.KEYWORD));
        }

        if(requestBundle.containsKey("ResultAvailable")) {
            boolean resultAvailable = requestBundle.getBoolean("ResultAvailable", false);
            if(!resultAvailable) {
                adapter.totalCount = 0;
                ((SearchActivity) getActivity()).getResultList().clear();
                if(null != adapter) {
                    adapter.setSearchkeywords(requestBundle.getString(Flags.Bundle.Keys.KEYWORD));
                    adapter.enableLoadingFooter(false);
                    adapter.isNoResult(true);
                    adapter.notifyDataSetChanged();
                }
            }
        }
        else {
            requestData = requestBundle.getByteArray(Flags.Bundle.Keys.REQUEST_DATA);           //get builder instance
            initCall();
        }
        countItems = 0;
    }

    private void initCall() {
        try {
            ((SearchActivity)getActivity()).getResultList().clear();
            if(null != adapter) {
                adapter.enableLoadingFooter(true);
                adapter.isNoResult(false);
                adapter.enableLoadingResult(true);
                adapter.notifyDataSetChanged();
            }

            if(null != scrollListener) scrollListener.resetState();
            hideRefreshNotifier();
            mPageNumber = 1;

            adapter.isLoadingCount = true;
            adapter.setNetworkStatus(true);
            makeCountRequest(Request.Method.POST, Flags.URL.GET_ITEM_LIST, SearchResponse.class, mPerPage, false, requestData);
            makeRequest(Request.Method.POST, Flags.URL.GET_ITEM_LIST, SearchResponse.class, mPerPage, false, requestData);
        }
        catch(Exception e){
            e.getMessage();
        }
    }

    @Override
    public void searchBarEvent(String keywords, boolean isFromResult) {
        if(adapter != null)
            adapter.keywordSearchAction(keywords);
    }

    @Override
    public void clearAllTags() {
        keywordSearchClearText();
    }

    @Override
    public void locationUpdate() {}

    @Override
    public void updateGeoCities(String cities) {
        if(null != adapter) adapter.updateTVGeoCities(cities);
    }

    @Override
    public void syncTagLayout(boolean isFromResultScreen, boolean isCreateTagRequest,
                              AttrValSpinnerModel attr, CommonFields rangeField, boolean isMultiSelectView, String keyname) {
        if(isMultiSelectView) {
            if(isCreateTagRequest)
                adapter.addTagLayout(attr, keyname);
            else
                adapter.removeTagLayout(attr, keyname);
        }
        else {
            if(isCreateTagRequest) {
                if(rangeField.isTagCreated())
                    adapter.updateRangeTag(rangeField, keyname);
                else
                    adapter.addRangeTag(rangeField, keyname);
            }
            else {
                adapter.removeRangeTag(rangeField, keyname);
            }
        }
    }

    public void keywordSearchClearText() {
        if(null != adapter) {
            adapter.clearKeywordSearchField();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchActivity){
            this.mContext = (SearchActivity) context;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mContext = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Glide.with(AppController.getInstance().getApplicationContext()).onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        appBarLayout = ((BaseActivity) this.getActivity()).mAppBarLayout;
        mTracker = AppController.getInstance().getDefaultTracker();
        mTracker.setScreenName("Search filter");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        scrollToTopFAB = ((BaseActivity) this.getActivity()).mFabFilter;
        backToTopFAB = ((BaseActivity)this.getActivity()).mBackToTopFab;
    }

    public ImageView ivCloseRefreshNotifier;
    public NestedScrollView llRefreshNotifier;

    public boolean isRefreshNotifierVisible = false;
    public void showRefreshNotifier() {
        if(!isRefreshNotifierVisible) isRefreshNotifierVisible = true;
        if(((SearchActivity)getActivity()).mPager.getCurrentItem() == 1 && llRefreshNotifier.getVisibility() != View.VISIBLE)
            llRefreshNotifier.setVisibility(View.VISIBLE);
    }

    public void hideRefreshNotifier() {
        try {
            if(isRefreshNotifierVisible) isRefreshNotifierVisible = false;
            if(((SearchActivity)getActivity()).mPager.getCurrentItem() == 1 && llRefreshNotifier.getVisibility() == View.VISIBLE)
                llRefreshNotifier.setVisibility(View.GONE);
            /*llRefreshNotifier.animate()
                    .translationY(llRefreshNotifier.getHeight())
                    .alpha(0.0f)
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            llRefreshNotifier.setVisibility(View.GONE);
                        }
                    });*/
            //}
            //llRefreshNotifier.animate().translationY(llRefreshNotifier.getHeight());
            //llRefreshNotifier.setVisibility(View.GONE);
        }
        catch(Exception e){
            e.printStackTrace();
            startActivity(new Intent(getActivity(), SearchActivity.class));
        }
    }

    /*public void multiViewConfig() {
        if(getResources().getBoolean(R.bool.isTablet)) {
            //mPerPage = 8;
        }
        else {
            //mPerPage = 6;
        }
    }*/

    public FlingControlRecyclerView.LayoutManager getLayoutManager() {
        FlingControlRecyclerView.LayoutManager layoutManager;
        if(getResources().getBoolean(R.bool.isTablet)){
            GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch (adapter.getItemViewType(position)) {
                        case SearchResultViewType.VIEW_ITEM:
                            return 1;
                        case SearchResultViewType.VIEW_AD_CARD:
                            return 1;
                        default:
                            return 2;
                    }
                }
            });
            return gridLayoutManager;
        }
        else {
            layoutManager = new LinearLayoutManagerWithSmoothScroll(mContext);
            return layoutManager;
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search, container, false);
        try {
            ((SearchActivity)getActivity()).containerMsg.setVisibility(View.GONE);
            mSession = new SessionManagement(mContext);
            recyclerView = (FlingControlRecyclerView) v.findViewById(R.id.recycler_view);
            //ivCloseRefreshNotifier = (ImageView) v.findViewById(R.id.iv_cls_refresh);
            llRefreshNotifier = (NestedScrollView) v.findViewById(R.id.refresh_notifier);
            swipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swipe_refresh_layout);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
                @Override
                public void onRefresh() {
                    hideRefreshNotifier();
                    ((SearchActivity)getActivity()).onSortParamSetTriggerRequest(sortParam);
                    countItems = 0;
                }
            });

            mPageNumber = 1; mPerPage = 6;
            hideRefreshNotifier();
            adapter = new SearchResultAdapter(mContext, ((SearchActivity)getActivity()).getResultList(), new AdapterCallback(){
                @Override
                public void optionSelected(int pos) {
                    sortParam = pos;
                    ((SearchActivity)getActivity()).onSortParamSetTriggerRequest(pos);
                }

                @Override
                public void searchBarKeyword(String keywords) {
                    ((SearchActivity)getActivity()).searchBarEvent(keywords, true);
                }

                @Override
                public void triggerRetryNetworkRequests() {
                    retryRequests();
                }
            }, getActivity(), "", mSession);

            adapter.totalCount = 0;

            //final CustomLinearLayoutManager mLayoutManager = new CustomLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            //final LinearLayoutManagerWithSmoothScroll mLayoutManager = new LinearLayoutManagerWithSmoothScroll(mContext);
            //final LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            FlingControlRecyclerView.LayoutManager layoutManager = getLayoutManager();
            recyclerView.setLayoutManager(layoutManager);
            //recyclerView.addItemDecoration(new SearchResult.GridSpacingItemDecoration(2, dpToPx(10), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
            scrollListener = new EndlessScrollListener(layoutManager) {

                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    // Triggered only when new data needs to be appended to the list
                    //Log.d(TAG, "page number I: "+ mPageNumber + ", " + Flags.URL.GET_ITEM_LIST);
                /*keywordSearchBtn.setEnabled(false);
                searchField.setEnabled(false);
                voiceSearch.setEnabled(false);*/
                    //if(isLastItemDisplaying(recyclerView)) {

                    try {
                        if(null != requestData)
                            makeRequest(Request.Method.POST, Flags.URL.GET_ITEM_LIST, SearchResponse.class, mPerPage, true, requestData);   //Check condition total counts with modulus
                        else{
                            //TODO: Handle error
                        }
                    }
                    catch (Exception e) {
                        Log.d(Flags.ActivityTag.SEARCH_RESULT, e.getMessage());
                    }
                }

                @Override
                public void getItemCount(int itemPos) {
                    try {
                        if(itemPos > 4 && ((SearchActivity)getActivity()).mPager.getCurrentItem() == 1) {
                            if(!backToTopFAB.isShown()) {
                                backToTopFAB.show(new FloatingActionButton.OnVisibilityChangedListener() {
                                    @Override
                                    public void onShown(FloatingActionButton fab) {
                                        super.onShown(fab);
                                        fab.setAlpha(0.8f);
                                    }
                                });
                            }
                        }
                        else {
                            if(backToTopFAB.isShown()) {
                                backToTopFAB.hide();
                            }
                        }
                    }
                    catch(Exception e) {
                        Log.d(Flags.ActivityTag.SEARCH_RESULT, e.getMessage());
                    }
                }
            };

            recyclerView.addOnScrollListener(scrollListener);
            backToTopFAB.setOnClickListener(new FloatingActionButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //((LinearLayoutManagerWithSmoothScroll)recyclerView.getLayoutManager()).setMillisecondsPerInch(80f);
                    //recyclerView.smoothScrollToPosition(((LinearLayoutManagerWithSmoothScroll)recyclerView.getLayoutManager()).findFirstVisibleItemPosition() + 1);
                    recyclerView.scrollToPosition(0);
                    backToTopFAB.hide();
                    //recyclerView.fling(recyclerView.getMaxFlingVelocity(), 3);
                    //hideToolbar();
                    /*scrollToTopFAB.show(new FloatingActionButton.OnVisibilityChangedListener() {
                        @Override
                        public void onShown(FloatingActionButton fab) {
                            super.onShown(fab);
                            fab.setAlpha(0.8f);
                        }
                    });*/
                /*if(((LinearLayoutManagerWithSmoothScroll)recyclerView.getLayoutManager()).getItemCount() ==
                        ((LinearLayoutManagerWithSmoothScroll)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition()) {
                    hideFABs();
                }*/
                }
            });


            if(null != scrollToTopFAB) {
                scrollToTopFAB.setOnClickListener(new FloatingActionButton.OnClickListener() {          //Nullpointer exception coming
                    @Override
                    public void onClick(View v) {
                    /*((LinearLayoutManagerWithSmoothScroll)recyclerView.getLayoutManager()).setMillisecondsPerInch(5f);
                    //recyclerView.smoothScrollToPosition(0);
                    //showToolbar();*/

                        //New interface for New Search call
                        ((SearchActivity)getActivity()).startNewSearch();
                        recyclerView.scrollToPosition(0);
                    /*((SearchActivity) getActivity()).searchType = Flags.Bundle.Values.NEW_SEARCH;
                    scrollToTopFAB.hide();
                    swipeView();*/
                    }
                });
            }
        }
        catch (Exception e) {
            Log.e("SearchResultFragment", e.getMessage());
            startActivity(new Intent(getActivity(), SearchActivity.class));
        }

        return v;

    }

    /*private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }*/

    public static SearchResult newInstance(String text) {
        SearchResult f = new SearchResult();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    /*public void swipeView() {
        mContext.mPager.setCurrentItem(0, true);
    }*/

    public void resetGeoLocIcon() {
        if(adapter.isGeoLocUpdateInProgress && null != adapter.pbGeoLoc && null != adapter.btnGeoLocate) {
            adapter.pbGeoLoc.setVisibility(View.GONE);
            adapter.btnGeoLocate.setVisibility(View.VISIBLE);
            adapter.isGeoLocUpdateInProgress = false;
        }
    }

    public void showKeywordSearchButtonProgress(boolean constraint) {
        if(null != adapter) {
            adapter.setKeywordSearchProgress(constraint);
        }
        else {
            ((SearchActivity)getActivity()).startNewSearch();
        }
    }

    private Client resultCountRequestClient, resultListRequestClient;
    public void makeCountRequest(int methodType, String url, final Class clazz, final int mPerPage, boolean incrPageNumber, final byte[] requestData) {
        try{
            AppController.getInstance().cancelPendingRequests("searchResultCount");
            String tag = "searchResultCount";
            final String url2 = url + "&include_count=1";

            resultCountRequestClient = new Client(methodType, url2, clazz, requestData, new OnCallListener() {
                @Override
                public void nwResponseData(String resData) throws IOException {
                    JsonObject jObj = new JsonParser().parse(resData).getAsJsonObject();
                    if(jObj.get(Flags.Keys.STATUS).getAsInt() == 1 && jObj.has(Flags.Keys.RESULT)) {
                        JsonObject resultObj = (JsonObject) jObj.get(Flags.Keys.RESULT);
                        if (resultObj.has("total_count") && resultObj.get("total_count").isJsonPrimitive()) {
                            adapter.totalCount = resultObj.get("total_count").getAsInt();
                        }
                        else {
                            adapter.totalCount = 0;
                        }
                        adapter.isLoadingCount = false;
                    }
                }
            }, new Response.Listener<Object>() {
                @Override
                public void onResponse(Object response) {
                    try {
                        adapter.notifyDataSetChanged();
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        if (error.networkResponse != null && error.networkResponse.statusCode == HttpStatus.SC_GATEWAY_TIMEOUT) {
                            adapter.totalCount = 100000;
                        } else {
                            adapter.totalCount = 0;
                        }
                        adapter.isLoadingCount = false;
                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        Log.d(Flags.ActivityTag.SEARCH_RESULT, e.getMessage());
                    }
                }
            }){
                @Override
                public byte[] getBody() throws AuthFailureError {
                    Log.d(TAG, "custom request for search API only");
                    return requestData;
                }
            };
            resultCountRequestClient.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(resultCountRequestClient, tag);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }


    public ProductFlags products = null;
    public void makeRequest(int methodType, String url, final Class clazz, final int mPerPage, boolean incrPageNumber, final byte[] requestData) {
        try {
            ((BaseActivity)getActivity()).btnSearch.setEnabled(false);
            Utility.setViewAndChildrenEnabled(adapter.searchHeaderLayout, false);
            Utility.setViewAndChildrenEnabled(adapter.rlDropDown, false);

            if(incrPageNumber) ++mPageNumber;
            //Log.d(TAG, "page number II: "+ mPageNumber + ", " + ((SearchActivity)getActivity()).getResultList());
            final String url2 = url + Flags.URL.PAGE+ mPageNumber + Flags.URL.PER_PAGE + mPerPage;
            countItems = mPageNumber * mPerPage;
            String tag = Flags.NetworkRequestTags.SEARCH_RESULT;
            AppController.getInstance().getRequestQueue().cancelAll(tag);

            resultListRequestClient = new Client(methodType, url2, clazz, requestData, this, new Response.Listener<Object>() {
                @Override
                public void onResponse(Object response) {
                    try {
                        SearchResponse res = (SearchResponse)response;
                        swipeRefreshLayout.setRefreshing(false);
                        if(res.getStatus() == 1) {
                            JsonObject jObj = new JsonParser().parse(responseData).getAsJsonObject();
                            JsonObject resultObj = (JsonObject)jObj.get(Flags.Keys.RESULT);
                            /*if(resultObj.has("total_count") && resultObj.get("total_count").isJsonPrimitive()) {
                                adapter.totalCount = resultObj.get("total_count").getAsInt(); //totalCount.put("TotalItems", resultObj.get("total_count").getAsInt());

                                if(resultObj.has(Flags.Keys.USER_HAS_PRODUCT_FLAG) && resultObj.get(Flags.Keys.USER_HAS_PRODUCT_FLAG).isJsonObject()) {
                                    Gson gson = new GsonBuilder().create();
                                    products = gson.fromJson(resultObj, ProductFlags.class);
                                }
                            }*/
                            products = null;
                            if(resultObj.has(Flags.Keys.USER_HAS_PRODUCT_FLAG) && resultObj.get(Flags.Keys.USER_HAS_PRODUCT_FLAG).isJsonObject()) {
                                Gson gson = new GsonBuilder().create();
                                products = gson.fromJson(resultObj, ProductFlags.class);
                            }

                            if(resultObj.get(Flags.Keys.SEARCH_RESULT).isJsonArray()) {
                                JsonArray searchResult = (JsonArray)resultObj.get(Flags.Keys.SEARCH_RESULT);

                                /*AppController.getInstance().setCurrencyFormat(mSession.getCurrencyFormat());
                                AppController.getInstance().setKilometerFormat(mSession.getKilometerFormat());*/

                                if(searchResult.size() > 0) {
                                    Iterator iterator = searchResult.iterator();
                                    SearchResultItem searchResultItem;

                                    while(iterator.hasNext()) {
                                        JsonObject item = (JsonObject)iterator.next();

                                        //For ad cards
                                        if(item.has("card_type") && item.get("card_type").getAsString().equals("ad")) {
                                            if(item.has("exclude_from_device") && item.get("exclude_from_device").isJsonArray()
                                                    && item.get("exclude_from_device").getAsJsonArray().size() == 0) {

                                                String linkUrl = item.get("link_url").getAsString();
                                                /*if(linkUrl.contains("mailto:"))
                                                    linkUrl = linkUrl.replace("mailto:","");*/
                                                searchResultItem = new SearchResultItem(SearchResultViewType.VIEW_AD_CARD,
                                                        Flags.URL.HOST+item.get("image_url").getAsString(), linkUrl, false);
                                                ((SearchActivity)getActivity()).getResultList().add(searchResultItem);
                                            }
                                            else {
                                                searchResultItem = new SearchResultItem(SearchResultViewType.VIEW_RATE_THIS_APP,
                                                        Flags.URL.HOST+item.get("image_url").getAsString(), "", true);
                                                ((SearchActivity)getActivity()).getResultList().add(searchResultItem);
                                            }
                                            continue;
                                        }

                                        JsonObject priceList = null, kmsList = null, maDiffList = null;
                                        String priceUSD = "", priceCAD = "", priceEUR = "", maDiffUSD = "", maDiffCAD = "", kms = "", miles = "";
                                        int productID = item.get(Flags.Keys.PRODUCT_ID).getAsInt();
                                        boolean isFav = false;

                                        if(null != products && (products.getUser_has_product_flag()).containsKey(item.get(Flags.Keys.PRODUCT_ID).getAsString())) {
                                            isFav = ((ProductFlagItem)((products.getUser_has_product_flag()).get(item.get(Flags.Keys.PRODUCT_ID).getAsString()))).getFavorite();
                                        }

                                        if(item.has(Flags.Keys.PRICE_LIST)) {
                                            priceList = item.getAsJsonObject(Flags.Keys.PRICE_LIST);
                                            if(null != priceList && priceList.has(Flags.Keys.PRICE_LIST_USD))
                                                priceUSD = priceList.get(Flags.Keys.PRICE_LIST_USD).getAsString();
                                            if(null != priceList && priceList.has(Flags.Keys.PRICE_LIST_CAD))
                                                priceCAD = priceList.get(Flags.Keys.PRICE_LIST_CAD).getAsString();
                                        }

                                        if(item.has(Flags.Keys.KILOMETERS_LIST)) {
                                            kmsList = item.getAsJsonObject(Flags.Keys.KILOMETERS_LIST);
                                            if(null != kmsList && kmsList.has(Flags.Keys.KILOMETERS))
                                                kms = kmsList.get(Flags.Keys.KILOMETERS).getAsString();

                                            if(null != kmsList && kmsList.has(Flags.Keys.MILES))
                                                miles = kmsList.get(Flags.Keys.MILES).getAsString();
                                        }

                                        if(item.has("ma_difference_list")) {
                                            maDiffList = item.getAsJsonObject("ma_difference_list");
                                            if(null != maDiffList && maDiffList.has(Flags.Keys.PRICE_LIST_USD))
                                                maDiffUSD = maDiffList.get(Flags.Keys.PRICE_LIST_USD).getAsString();
                                            if(null != maDiffList && maDiffList.has(Flags.Keys.PRICE_LIST_CAD))
                                                maDiffCAD = maDiffList.get(Flags.Keys.PRICE_LIST_CAD).getAsString();
                                        }

                                        boolean maShow = item.get(Flags.Keys.MA_SHOW).getAsBoolean();
                                        searchResultItem = new SearchResultItem(String.valueOf(item.get(Flags.Keys.ITEM_TITLE)==null?"":item.get(Flags.Keys.ITEM_TITLE).getAsString()),
                                                item.get(Flags.Keys.IMAGE_URL) == null? "":item.get(Flags.Keys.IMAGE_URL).getAsString(),
                                                String.valueOf(item.get(Flags.Keys.POST_DATE)==null? "":item.get(Flags.Keys.POST_DATE).getAsString()),
                                                String.valueOf(item.get(Flags.Keys.CITY)==null? "":item.get(Flags.Keys.CITY).getAsString()),
                                                priceUSD, priceCAD, kms, miles,
                                                String.valueOf((item.get(Flags.Keys.PRICE) == null)?"":item.get(Flags.Keys.PRICE).getAsString()),
                                                maDiffUSD, maDiffCAD,
                                                String.valueOf((item.get(Flags.Keys.URL) == null)? "":item.get(Flags.Keys.URL).getAsString()),
                                                String.valueOf((item.get(Flags.Keys.MA_KEYWORD) == null)? "":item.get(Flags.Keys.MA_KEYWORD).getAsString()), maShow, "",
                                                isFav, productID, String.valueOf((item.get(Flags.Keys.HREF_URL) == null)? "":item.get(Flags.Keys.HREF_URL).getAsString()));

                                        ((SearchActivity)getActivity()).getResultList().add(searchResultItem);
                                    }

                                    //adapter.enableLoadingFooter(true);
                                    if(mPageNumber == 1) {
                                        if (searchResult.size() > 0) {
                                            adapter.enableLoadingResult(false);
                                            adapter.enableLoadingFooter(true);
                                            //adapter.enableSortOptions(true);
                                            //showFABs();
                                        } else {
                                            ((SearchActivity) getActivity()).getResultList().clear();
                                            adapter.enableLoadingFooter(false);
                                            //adapter.enableSortOptions(false);

                                            //hideFABs();
                                        }
                                    }
                                    //adapter.enableSortOptions(true);
                                }
                                else {
                                    if(mPageNumber == 1)
                                        ((SearchActivity) getActivity()).getResultList().clear();
                                    //adapter.enableSortOptions(false);
                                    adapter.isNoResult(true);
                                    /*if(adapter.totalCount < 1) {

                                        //hideFABs();
                                    }
                                    else {
                                        //adapter.enableSortOptions(true);
                                    }*/
                                    adapter.enableLoadingFooter(false);
                                }
                            }
                            adapter.notifyDataSetChanged();

                            /*((SearchActivity) getActivity()).splashImg.setVisibility(View.GONE);
                            ((SearchActivity) getActivity()).searchLoadingLayout.setVisibility(View.GONE);
                            ((SearchActivity)getActivity()).containerMsg.setVisibility(View.GONE);

                            ((SearchActivity)getActivity()).container.setVisibility(View.VISIBLE);
                            ((SearchActivity)getActivity()).checkIfEditSearch();*/
                        }
                        else if(res.getStatus() == 0) {
                            adapter.totalCount = 0;//.put("TotalItems", 0);
                            adapter.enableLoadingFooter(false);
                            //adapter.enableSortOptions(true);
                            adapter.notifyDataSetChanged();
                        }
                        ((BaseActivity)getActivity()).btnSearch.setEnabled(true);
                        Utility.setViewAndChildrenEnabled(adapter.rlDropDown, true);
                        Utility.setViewAndChildrenEnabled(adapter.searchHeaderLayout, true);
                    }
                    catch(Exception e) {
                        Log.d(Flags.ActivityTag.SEARCH_RESULT, e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        if (error instanceof NoConnectionError) {

                        }
                        ((BaseActivity)getActivity()).btnSearch.setEnabled(true);
                        Utility.setViewAndChildrenEnabled(adapter.rlDropDown, true);
                        Utility.setViewAndChildrenEnabled(adapter.searchHeaderLayout, true);
                        swipeRefreshLayout.setRefreshing(false);
                        adapter.enableLoadingFooter(false);
                        adapter.setNetworkStatus(false);
                        adapter.notifyDataSetChanged();

                        ((SearchActivity) getActivity()).splashImg.setVisibility(View.GONE);
                        ((SearchActivity) getActivity()).searchLoadingLayout.setVisibility(View.GONE);
                    }
                    catch(Exception e) {
                        Log.d(Flags.ActivityTag.SEARCH_RESULT, e.getMessage());
                    }
                }
            }){
                @Override
                public byte[] getBody() throws AuthFailureError {
                    Log.e(TAG, "custom request for search API only");
                    return requestData;
                }
            };

            resultListRequestClient.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(resultListRequestClient, tag);
        }
        catch(Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void retryRequests() {
        try {

            if(!AppController.getInstance().checkNetworkConn()) {
                Toast.makeText(getActivity(), getResources().getString(R.string.no_connection_error), Toast.LENGTH_LONG).show();
                return;
            }

            if(resultCountRequestClient != null && resultListRequestClient != null && adapter != null) {
                adapter.enableLoadingFooter(true);
                adapter.setNetworkStatus(true);
                adapter.notifyDataSetChanged();
                AppController.getInstance().addToRequestQueue(resultListRequestClient, Flags.NetworkRequestTags.SEARCH_RESULT);
                AppController.getInstance().addToRequestQueue(resultCountRequestClient, Flags.NetworkRequestTags.SEARCH_RESULT_COUNT);
            }
            else {
                ((SearchActivity)getActivity()).startNewSearch();
            }
        }
        catch (Exception e) {
            Log.e("SearchResultFragment", e.getMessage());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public void nwResponseData(String pData) {
        responseData = pData;
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            adapter.notifyDataSetChanged();

            if(((SearchActivity)getActivity()).mPager.getCurrentItem() == 1) {
                if(isRefreshNotifierVisible) {
                    //if(llRefreshNotifier.getVisibility() != View.VISIBLE)
                    llRefreshNotifier.setVisibility(View.VISIBLE);
                }
                else
                    llRefreshNotifier.setVisibility(View.GONE);
            }
        }
        catch(Exception e) {
            Log.d(Flags.ActivityTag.SEARCH_RESULT, e.getMessage());

        }

        /*if(null != scrollToTopFAB && mContext.mPager.getCurrentItem() == 1) {
            scrollToTopFAB.setOnClickListener(new FloatingActionButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((LinearLayoutManagerWithSmoothScroll)recyclerView.getLayoutManager()).setMillisecondsPerInch(5f);
                    recyclerView.smoothScrollToPosition(0);
                    showToolbar();
                    scrollToTopFAB.hide();
                }
            });
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mContext = null;
    }


    public void readyForRequest() {
        if(requestData != null && isVisible()) {
            makeRequest(Request.Method.POST, Flags.URL.GET_ITEM_LIST, SearchResponse.class, mPerPage, false, requestData);
        }
    }

    public interface AdapterCallback {
        void optionSelected(int pos);
        void searchBarKeyword(String keyword);
        void triggerRetryNetworkRequests();
    }

    public class ProductFlagItem{
        private boolean favorite;
        private String product_id;

        public boolean getFavorite() {
            return favorite;
        }

        /*public void setFavorite(boolean favorite) {
            this.favorite = favorite;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }*/
    }

    public class ProductFlags {
        private Map<String, ProductFlagItem> user_has_product_flag = new HashMap<String, ProductFlagItem>();

        public Map<String, ProductFlagItem> getUser_has_product_flag() {
            return user_has_product_flag;
        }

        /*public void setUser_has_product_flag(Map<String, ProductFlagItem> user_has_product_flag) {
            this.user_has_product_flag = user_has_product_flag;
        }*/
    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     *//*
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            *//*int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }*//*
        }
    }

    *//**
     * Converting dp to pixel
     *//*
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }*/

    /*public void showToolbar(){
        try {
            if(appBarLayout != null) {
                appBarLayout.setExpanded(true, true);
            }
            isToolbarVisible = true;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void hideToolbar(){
        try {
            if(appBarLayout != null) {
                appBarLayout.setExpanded(false, true);
            }
            isToolbarVisible = false;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }*/

    /*public void hideFABs() {
        nextItemFAB.hide();
        scrollToTopFAB.hide();
    }

    public void showFABs() {
        if(mContext.mPager.getCurrentItem() == 1) {
            nextItemFAB.show(new FloatingActionButton.OnVisibilityChangedListener() {
                @Override
                public void onShown(FloatingActionButton fab) {
                    super.onShown(fab);
                    fab.setAlpha(0.8f);
                }
            });
        }
    }*/

    /*public void hideFABs() {
        backToTopFAB.hide();
        scrollToTopFAB.hide();
    }*/



}