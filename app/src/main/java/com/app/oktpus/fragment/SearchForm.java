package com.app.oktpus.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.speech.RecognizerIntent;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.oktpus.activity.BaseActivity;
import com.app.oktpus.activity.SearchActivity;
import com.app.oktpus.adapter.SearchFormAdapter;
import com.app.oktpus.adapter.MultiSelectItemSearchViewAdapter;
import com.app.oktpus.model.AttrValSpinnerModel;
import com.app.oktpus.model.SearchResponse;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.constants.ResponseCode;
import com.app.oktpus.controller.AppController;
import com.app.oktpus.listener.ListViewNotifier;
import com.app.oktpus.listener.OnCallListener;
import com.app.oktpus.listener.OnCheckBoxClickListener;
import com.app.oktpus.listener.OnSearchActivityCalls;
import com.app.oktpus.listener.SearchActivityEvents;
import com.app.oktpus.R;
import com.app.oktpus.responseModel.Config.BMAPercent;
import com.app.oktpus.responseModel.Config.CommonFields;
import com.app.oktpus.responseModel.Config.Kilometers;
import com.app.oktpus.responseModel.Config.Price;
import com.app.oktpus.responseModel.Config.ResponseConfig;
import com.app.oktpus.responseModel.Config.Year;
import com.app.oktpus.utils.Client;
import com.app.oktpus.utils.LinearLayoutManagerWithSmoothScroll;
import com.app.oktpus.utils.SessionManagement;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.app.Activity.RESULT_OK;
import static com.app.oktpus.constants.Flags.ActivityTag;
import static com.app.oktpus.constants.Flags.Keys;
import static com.app.oktpus.constants.Flags.URL;
import static com.app.oktpus.constants.Flags.getConfigUrl;

/**
 * Created by Gyandeep on 10/11/16.
 */

public class SearchForm extends Fragment implements ListViewNotifier, OnCallListener, OnCheckBoxClickListener,
        OnSearchActivityCalls, SearchActivity.MultiSelectClickEvent, SearchActivityEvents, SearchActivity.EventListener {

    public ImageButton keywordSearchBtn, mVoiceSearch, searchResultSwipeBtn;
    public EditText searchField;

    //private Button searchButton, clearFormButton;
    private RelativeLayout saveSearchLayout, receiveNotificationLayout, includeNoImgCarsLayout;
    private SwitchButton saveSearchSwitch, receiveNotificationSwitch;
    private SessionManagement mSession;
    private TextView tvformBottomMsg, image0Tv;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;

    public OnSearchActivityCalls onSearchResultCallListener;
    private SearchActivity mContext;
    private String pData;
    public long attributeStartTime, multiUnitStartTime, totalStartTime;
    private String searchID = null;
    public String url;
    //public int searchType;
    public Bundle parcelBundle, rangeAttrParcel;
    public List<AttrValSpinnerModel> attributeParcel;
    private Map<String, Map<String, String>> labelList = new HashMap<>();
    private Bundle keywordSearchBundle;
    public Price price;
    public Year year;
    public Kilometers kms;
    public BMAPercent bma;
    public AppController appl;
    public Bundle variableCityBundle;

    public RecyclerView rvSearchForm;
    public SearchFormAdapter adapter;
    public Map<String, List<AttrValSpinnerModel>> msMap;
    public Map<String, CommonFields> rangeMap;
    public Map<String, String> attrLabels;
    private Tracker mTracker;
    public LinearLayoutManagerWithSmoothScroll mLayoutManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.mContext = (SearchActivity) context;
            mSession = new SessionManagement(mContext);
            parcelBundle = getActivity().getIntent().getBundleExtra(Flags.Bundle.Keys.ATTRIBUTE_PARCEL);
            rangeAttrParcel = getActivity().getIntent().getBundleExtra(Flags.Bundle.Keys.ATTR_RANGE_PARCEL);
            keywordSearchBundle = getActivity().getIntent().getBundleExtra(Flags.Bundle.Keys.ATTR_PARCEL_BY_KEYWORDS);
            if(keywordSearchBundle != null && keywordSearchBundle.containsKey("key")) {
                attributeParcel = (ArrayList<AttrValSpinnerModel>)keywordSearchBundle.get("key");
            }
            //searchType = getActivity().getIntent().getIntExtra(Flags.Bundle.Keys.SOURCE_PAGE, 0);//((SearchActivity)getActivity()).searchType;//
            onSearchResultCallListener = (OnSearchActivityCalls) getActivity();
        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mTracker = AppController.getInstance().getDefaultTracker();
        mTracker.setScreenName("Search filter");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("Search form fragment onSaveInstanceState");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mContext = null;
        System.out.println("Search form fragment onDestroy");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_search_form,
                container, false);
        //nestedScrollView = (NestedScrollView) v.findViewById(R.id.nestedScrollView);
        rvSearchForm = (RecyclerView) v.findViewById(R.id.rv_form);
        mVoiceSearch = (ImageButton) v.findViewById(R.id.ibtn_search_voice);
        keywordSearchBtn = (ImageButton)v.findViewById(R.id.ibtn_search);
        searchField = (EditText) v.findViewById(R.id.et_search);
        toolbar = ((BaseActivity) this.getActivity()).mToolbar;
        appBarLayout = ((BaseActivity) this.getActivity()).mAppBarLayout;

        msMap = new HashMap<String, List<AttrValSpinnerModel>>();
        rangeMap = new HashMap<String, CommonFields>();
        attrLabels = new HashMap<String, String>();

        adapter = new SearchFormAdapter((SearchActivity)getActivity(), mContext, msMap, rangeMap,
                attrLabels, labelList, this);

        //final LinearLayoutManagerWithSmoothScroll mLayoutManager = new LinearLayoutManagerWithSmoothScroll(mContext);
        mLayoutManager = new LinearLayoutManagerWithSmoothScroll(mContext);
        rvSearchForm.setLayoutManager(mLayoutManager);
        //rvSearchForm.setItemAnimator(new DefaultItemAnimator());
        rvSearchForm.setAdapter(adapter);

        ((SearchActivity)getActivity()).searchButton = ((BaseActivity)getActivity()).btnSearch;
        ((SearchActivity)getActivity()).tvSearchButton = ((BaseActivity)getActivity()).tvSearchButton;
        //((SearchActivity)getActivity()).pbSearchButton = ((BaseActivity)getActivity()).pbSearchLayout;
        saveSearchLayout = ((BaseActivity)getActivity()).saveSearchLayout;
        receiveNotificationLayout = ((BaseActivity)getActivity()).receiveNotificationLayout;
        saveSearchSwitch = ((BaseActivity)getActivity()).saveSearchSwitch;
        receiveNotificationSwitch = ((BaseActivity)getActivity()).receiveNotificationSwitch;

        ((SearchActivity)getActivity()).searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((SearchActivity)getActivity()).searchButton.setEnabled(false);
                //((SearchActivity)getActivity()).pbSearchButton.setVisibility(View.VISIBLE);
                adapter.attemptSearchResultNavigation();
                if(null != appl && null != variableCityBundle && null != adapter) {
                    variableCityBundle.clear();
                    variableCityBundle.putParcelableArrayList(Flags.Bundle.Keys.DEFAULT_CITY, (ArrayList<? extends Parcelable>) getSelectedCity());
                    appl.setVariableCityBundle(variableCityBundle);
                }
            }
        });

        ((BaseActivity)getActivity()).rootContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                ((BaseActivity)getActivity()).rootContainer.getWindowVisibleDisplayFrame(r);
                int screenHeight = ((BaseActivity)getActivity()).rootContainer.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;

                //Log.d(TAG, "keypadHeight = " + keypadHeight);

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                    if(((BaseActivity)getActivity()).mBottomLayout.getVisibility() == View.VISIBLE && ((SearchActivity) getActivity()).mPager.getCurrentItem() == 1) {
                        ((BaseActivity)getActivity()).mBottomLayout.setVisibility(View.GONE);
                    }
                    else if(((BaseActivity)getActivity()).mBottomLayout.getVisibility() == View.VISIBLE) {
                        ((BaseActivity)getActivity()).mBottomLayout.animate()
                                .translationY(((BaseActivity)getActivity()).mBottomLayout.getHeight());
                        ((BaseActivity)getActivity()).mBottomLayout.setVisibility(View.GONE);
                    }
                }
                else {
                    // keyboard is closed
                    if(((SearchActivity) getActivity()).container.getVisibility() == View.VISIBLE && ((SearchActivity) getActivity()).mPager.getCurrentItem() == 0) {
                        ((BaseActivity)getActivity()).mBottomLayout.animate().translationY(0).setDuration(200);
                        ((BaseActivity)getActivity()).mBottomLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        saveSearchLayout.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!saveSearchSwitch.isChecked()) {
                    setSwitchOn(saveSearchSwitch);
                    saveSearchSwitch.setThumbColorRes(R.color.switchActiveColor);
                }
                else {
                    setSwitchOff(saveSearchSwitch);
                    saveSearchSwitch.setThumbColorRes(R.color.white);
                    if(receiveNotificationSwitch.isChecked()) {
                        setSwitchOff(receiveNotificationSwitch);
                        setSwitchOff(saveSearchSwitch);
                    }
                }
                adapter.enableSaveSearch(saveSearchSwitch.isChecked());
            }
        });

        receiveNotificationLayout.setOnClickListener(new RelativeLayout.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!receiveNotificationSwitch.isChecked()) {
                    setSwitchOn(receiveNotificationSwitch);
                    receiveNotificationSwitch.setThumbColorRes(R.color.switchActiveColor);
                }
                else {
                    setSwitchOff(receiveNotificationSwitch);
                    receiveNotificationSwitch.setThumbColorRes(R.color.white);
                }
                adapter.enableReceiveNotification(receiveNotificationSwitch.isChecked());
                if(!saveSearchSwitch.isChecked()) {
                    adapter.enableSaveSearch(true);
                    setSwitchOn(saveSearchSwitch);
                }
            }
        });

        receiveNotificationSwitch.setBackColorRes(R.color.colorPrimary);
        saveSearchSwitch.setBackColorRes(R.color.colorPrimary);
        saveSearchSwitch.setThumbColorRes(R.color.white);
        receiveNotificationSwitch.setThumbColorRes(R.color.white);

        appl = (AppController) getActivity().getApplication();
        variableCityBundle = new Bundle();

        //image0Tv.setTypeface(AppController.getFontType(mContext));

        //Edit Search
        if(((SearchActivity)getActivity()).getSearchType() == Flags.Bundle.Values.EDIT_SEARCH) {
            saveSearchLayout.setVisibility(View.GONE);
            //searchButton.setText(getResources().getString(R.string.save_and_search));
            setSwitchOn(saveSearchSwitch);
            adapter.enableSaveSearch(true);
            adapter.setSearchID(getActivity().getIntent().getStringExtra(Flags.Bundle.Keys.SEARCH_ID));
            //((SearchActivity)getActivity()).setSearchType();
            //adapter.setSearchType(searchType);
            searchID = getActivity().getIntent().getStringExtra(Flags.Bundle.Keys.SEARCH_ID);
            ((SearchActivity)getActivity()).tvSearchButton.setText(getResources().getString(R.string.save_and_search));
        }
        else {
            ((SearchActivity)getActivity()).tvSearchButton.setText(getResources().getString(R.string.search_btn_new));
        }

        initData();

        return v;
    }

    public List<AttrValSpinnerModel> getSelectedCity() {
        List<AttrValSpinnerModel> selected = new ArrayList<AttrValSpinnerModel>();
        if(adapter.getMSCheckedValues(Keys.CITY).size() > 0 || adapter.getMSCheckedGroups(Keys.CITY).size() > 0) {
            for(AttrValSpinnerModel attr : adapter.getMultiSelectAttr(Keys.CITY)) {
                if(attr.isSelected()) {
                    selected.add(attr);
                }
            }
        }
        return selected;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Flags.REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(result.get(0).length() > 0) {
                        searchField.setText(result.get(0));
                    }
                }
                break;
            }
        }
    }

    public void setSwitchOn(SwitchButton switchButton) {
        switchButton.setChecked(true);
        switchButton.setThumbColorRes(R.color.switchActiveColor);
    }

    public void setSwitchOff(SwitchButton switchButton) {
        switchButton.setChecked(false);
        switchButton.setThumbColorRes(R.color.white);
    }

    /*public void setSaveSearchOn() {
        saveSearchSwitch.setChecked(true);
        saveSearchSwitch.setThumbColorRes(R.color.switchActiveColor);
    }

    public void setSaveSearchOff() {
        saveSearchSwitch.setChecked(false);
        saveSearchSwitch.setThumbColorRes(R.color.colorAccent);
    }*/

    public static SearchForm newInstance(String text) {

        SearchForm f = new SearchForm();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);

        return f;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mContext = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            //String link = bundle.getString("test");
            //Log.e("SearchForm", "link: "+link);
        }
    }

    private void initData() {
        //disableButtons();
        //Attribute API call to set labels and values
        attributeApiRequest(Request.Method.GET, URL.ITEM_GET_ATTRIBUTE_VALUE, SearchResponse.class);
    }

    private void multiUnitConfigApi(int methodType, String url, Class responseClass, final Map<String, Map<String, String>> labelList,
                                    final JsonObject jObj, final SearchResponse attrRes) {
        ///api/user/{user_id}/config
        /*GET Params:
        -- domain_id=1
        - POST Params:
        -- config[attribute][price][type]:USD // format we are switching to
        -- current_values[values][0]:0 // current min value in the form input field
        -- current_values[values][1]:100000 // current max value in the form input field
        -- current_values[from_format]:CAD // current format
        -- search_id:1154 // If we are switching within an edit search, need to pass the search ID, because we will update not only the user's*/
        try {
            multiUnitStartTime = System.currentTimeMillis();
            Log.d(ActivityTag.SEARCH_FORM_ACTIVITY,"Multiunit request Request start time: " + multiUnitStartTime);
            Map<String, String> params = new HashMap<String, String>();
            Log.d(ActivityTag.SEARCH_FORM_ACTIVITY,"attrib url: " + url);
            final Client jsObjRequest = new Client(methodType, url, responseClass, params, this, new Response.Listener<Object>() {
                @Override
                public void onResponse(Object response) {
                    ResponseConfig res = (ResponseConfig) response;
                    Log.d(ActivityTag.SEARCH_FORM_ACTIVITY, "multiunit call status "+ res.getStatus());

                    ResponseCode resCode = ResponseCode.fromValue(res.getStatus());
                    switch(resCode) {
                        case STATUS_SUCCESS: {
                            try {
                                Log.d(ActivityTag.SEARCH_FORM_ACTIVITY, "json multiunit: "+ pData);

                                price = res.getUserConfig().getPrice();
                                year = res.getUserConfig().getYear();
                                kms = res.getUserConfig().getKms();
                                bma = res.getUserConfig().getBmaPercent();
                                //setDefaultFields(res);

                                //AppController.getInstance().setCurrencyFormat(price.getKeyName());
                                //AppController.getInstance().setKilometerFormat(kms.getKeyName());

                                setRangeValues(Keys.PRICE, price.getField().getLabel(), price);
                                setRangeValues(Keys.YEAR, year.getField().getLabel(), year);
                                setRangeValues(Keys.KILOMETERS, kms.getField().getLabel(), kms);
                                setRangeValues(Keys.BELOW_MARKET_AVG, bma.getField().getLabel(), bma);

                                if(jObj.has(Keys.ATTRIBUTE_VALUE)) {
                                    JsonObject o = (JsonObject) jObj.get(Keys.ATTRIBUTE_VALUE);

                                    setFormValues(o, attrRes.getAttribute_label().getCountry().getKey_name(), attrRes.getAttribute_label().getCountry().getLabel());
                                    setFormValues(o, attrRes.getAttribute_label().getCity().getKey_name(), attrRes.getAttribute_label().getCity().getLabel());
                                    setFormValues(o, attrRes.getAttribute_label().getMake().getKey_name(), attrRes.getAttribute_label().getMake().getLabel());
                                    setFormValues(o, attrRes.getAttribute_label().getModel().getKey_name(), attrRes.getAttribute_label().getModel().getLabel());
                                    setFormValues(o, attrRes.getAttribute_label().getColour().getKey_name(), attrRes.getAttribute_label().getColour().getLabel());
                                    setFormValues(o, attrRes.getAttribute_label().getTransmission().getKey_name(), attrRes.getAttribute_label().getTransmission().getLabel());
                                    setFormValues(o, attrRes.getAttribute_label().getBody().getKey_name(), attrRes.getAttribute_label().getBody().getLabel());
                                    setFormValues(o, attrRes.getAttribute_label().getUser_type().getKey_name(), attrRes.getAttribute_label().getUser_type().getLabel());

                                    setFormValues(o, attrRes.getAttribute_label().getDoors().getKey_name(), attrRes.getAttribute_label().getDoors().getLabel());
                                    setFormValues(o, attrRes.getAttribute_label().getDrivetrain().getKey_name(), attrRes.getAttribute_label().getDrivetrain().getLabel());


                                    if(Flags.Bundle.Values.SEARCH_BY_KEYWORDS == ((SearchActivity)getActivity()).getSearchType()) {
                                        String keywords = getActivity().getIntent().getStringExtra(Flags.Bundle.Keys.KEYWORD);
                                        //if(keywords != null) searchField.setText(keywords);
                                        adapter.applyDefaultCity(true);
                                        ((SearchActivity)getActivity()).searchBarEvent(keywords, false);
                                        adapter.keywordSearchAction(keywords);
                                        /*if(getActivity().getIntent().getBooleanExtra(Flags.Bundle.Keys.NO_RESULT_STATUS, false)){
                                            displayNoResult(keywords);
                                            enableButtons();
                                        } else {

                                        }*/
                                    } else if (Flags.Bundle.Values.EDIT_SEARCH == ((SearchActivity)getActivity()).getSearchType() || Flags.Bundle.Values.WALL_OF_DEALS == ((SearchActivity)getActivity()).getSearchType()) {
                                        switch(getActivity().getIntent().getIntExtra(Flags.Bundle.Keys.STATUS_ID, 0)) {
                                            case 1 : setSwitchOff(receiveNotificationSwitch);
                                                break;
                                            case 2 : setSwitchOn(receiveNotificationSwitch);
                                                break;
                                        }
                                        adapter.enableReceiveNotification(receiveNotificationSwitch.isChecked());
                                        if(Flags.Bundle.Values.WALL_OF_DEALS == ((SearchActivity)getActivity()).getSearchType()) {
                                            adapter.applyDefaultCity(true);
                                            adapter.setWallOfDealsData();
                                        }
                                        else {
                                            adapter.setEditSearchParcelData();
                                        }
                                    }
                                    else if(Flags.Bundle.Values.NEW_SEARCH == ((SearchActivity)getActivity()).getSearchType() || Flags.Bundle.Values.REFINE_SEARCH == ((SearchActivity)getActivity()).getSearchType()){
                                        adapter.applyDefaultCity(true);
                                        adapter.triggerResult();
                                        //v1.2 adapter.attributeCountBatchRequest();
                                        //v1.2 adapter.requestItemGetCountAll(Request.Method.POST, URL.ITEM_GETCOUNT + URL.DOMAIN_ID, SearchResponse.class);
                                    }

                                    ((SearchActivity) getActivity()).splashImg.setVisibility(View.GONE);            //Possible memory leakage
                                    ((SearchActivity) getActivity()).searchLoadingLayout.setVisibility(View.GONE);
                                    ((SearchActivity)getActivity()).containerMsg.setVisibility(View.GONE);
                                    ((SearchActivity) getActivity()).container.setVisibility(View.VISIBLE);

                                    if(((((SearchActivity)getActivity()).getSearchType() == Flags.Bundle.Values.WALL_OF_DEALS || ((SearchActivity)getActivity()).getSearchType() == Flags.Bundle.Values.REFINE_SEARCH) && !mSession.isDemoChecked())
                                            && ((SearchActivity)getActivity()).mPager.getCurrentItem() == 0) {
                                        //mSession.setIsDemoChecked(true);
                                        adapter.initDemo();
                                    }
                                    if(mContext.mPager.getCurrentItem() == 0)
                                        ((BaseActivity) getActivity()).mBottomLayout.setVisibility(View.VISIBLE);
                                }
                            }
                            catch(Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                        case STATUS_FAILURE: {
                            /*((SearchActivity) getActivity()).splashImg.setVisibility(View.GONE);            //Possible memory leakage
                            ((SearchActivity) getActivity()).searchLoadingLayout.setVisibility(View.GONE);
                            ((SearchActivity) getActivity()).containerMsg.setVisibility(View.GONE);
                            ((SearchActivity) getActivity()).container.setVisibility(View.VISIBLE);*/

                            ///Show some error with retry screen

                            Log.d(ActivityTag.SEARCH_FORM_ACTIVITY,  "status 0");
                            break;
                        }
                        default : {
                            Log.d(ActivityTag.SEARCH_FORM_ACTIVITY,  "default");
                        }
                    }

                    long timeTaken = System.currentTimeMillis() - multiUnitStartTime;
                    Log.d(ActivityTag.SEARCH_FORM_ACTIVITY, "Multiunit request Total time taken: " + timeTaken +
                            ", total network time: " + (System.currentTimeMillis() - totalStartTime) );
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(ActivityTag.SEARCH_FORM_ACTIVITY, "Error: " + error);
                }
            })/*{
                @Override
                public byte[] getBody() throws AuthFailureError {
                    Log.e(ActivityTag.SEARCH_FORM_ACTIVITY, "custom request for search API only");
                    return request;
                }
            }*/;
            String tag_json_obj = "multi_unit_config";
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToCacheRequestQueue(jsObjRequest, tag_json_obj);
        }
        catch(Exception e) {
            Log.d(ActivityTag.SEARCH_FORM_ACTIVITY, e.getMessage());
        }
    }

    private void attributeApiRequest(final int methodType, final String url, final Class resClass) {
        try {
            attributeStartTime = System.currentTimeMillis();
            totalStartTime = attributeStartTime;
            Log.d(ActivityTag.SEARCH_FORM_ACTIVITY,"Attribute Request start time: " + attributeStartTime);
            Map<String, String> params = new HashMap<String, String>();
            params.put("domain_id", "1");

            Log.d(ActivityTag.SEARCH_FORM_ACTIVITY,"attrib url: " + url);
            Client jsObjRequest = new Client(methodType, url, resClass, params, this, new Response.Listener<Object>() {
                @Override
                public void onResponse(Object response) {
                    SearchResponse res = (SearchResponse)response;
                    Log.d(ActivityTag.SEARCH_FORM_ACTIVITY, "status: "+ res.getStatus());
                    Log.e("ClientResponse", "Attribute API Request");
                    ResponseCode resCode = ResponseCode.fromValue(res.getStatus());
                    switch(resCode) {
                        case STATUS_SUCCESS: {
                            try {
                                String json = new Gson().toJson(res);
                                Log.d(ActivityTag.SEARCH_FORM_ACTIVITY, "json: "+ json);

                                //Api call for Price, Year, Kilometer

                                JsonObject jObj = new JsonParser().parse(pData).getAsJsonObject();
                                Map<String, Map<String, Object>> attributeLabelList = new HashMap<>(); Map<String, String> labelListPrice = new HashMap<>(), labelListKms = new HashMap<>();
                                /*"price":{"key_name":"price","label":"Price","label_list":{"CAD":"Price (CAD)","USD":"Price (USD)","EUR":"Price (EUR)"}}*/
                                if(jObj.has("attribute_label")){
                                    JsonObject attributeLabel = (JsonObject) jObj.get("attribute_label");
                                    Set<Map.Entry<String, JsonElement>> entryAttribLabel = attributeLabel.entrySet();

                                    Map<String, Object> labelListMap;// = new HashMap<>();
                                    for(Map.Entry<String, JsonElement> child : entryAttribLabel) {
                                        JsonObject childObj = (JsonObject) attributeLabel.get(child.getKey());

                                        if(childObj.has("label_list")) {
                                            labelListMap = new HashMap<>();
                                            Set<Map.Entry<String, JsonElement>> childValues = childObj.entrySet();
                                            for(Map.Entry<String, JsonElement> ecv : childValues) {
                                                if(ecv.getKey().equals("label_list")) {
                                                    JsonElement labelList = ecv.getValue();
                                                    //Set<Map.Entry<String, JsonElement>> setLabelList = labelList.entrySet();
                                                    labelListMap.put(ecv.getKey(), new Gson().fromJson(labelList, new TypeToken<Map<String, String>>(){}.getType()));
                                                    //for(Map.Entry<String, JsonElement> element : labelList.entrySet()){}
                                                }
                                                else labelListMap.put(ecv.getKey(), ecv.getValue());
                                            }
                                            attributeLabelList.put(child.getKey(), labelListMap);
                                        }
                                    }
                                }

                                /*for(Map.Entry<String, Map<String, Object>> map : attributeLabelList.entrySet()) {
                                    Map<String, Object> labelList = map.getValue();
                                    for(Map.Entry<String, Object> entry : labelList.entrySet()){
                                        Log.d("LABEL_LIST ", entry.getKey() +" -> "+ map.getValue());
                                    }
                                }*/

                                Map<String, String> labelListPriceNew = new HashMap<>();
                                labelListPriceNew.put("CAD", "Price (CAD)");
                                labelListPriceNew.put("USD", "Price (USD)");
                                labelListPriceNew.put("EUR", "Price (EUR)");

                                Map<String, String> labelListKmsNew = new HashMap<>();
                                labelListKmsNew.put("kilometers", "Kilometers");
                                labelListKmsNew.put("miles","Mileage");

                                labelList.put("price", labelListPriceNew);
                                labelList.put("kilometers", labelListKmsNew);

                                mSession.setLabelList(new Gson().toJson(labelList));
                                multiUnitConfigApi(Request.Method.GET, getConfigUrl(mSession, ((SearchActivity)getActivity()).getSearchType(), searchID), ResponseConfig.class, labelList, jObj, res);
                            }
                            catch(Exception e) {
                                e.printStackTrace();
                            }

                            break;
                        }
                        case STATUS_FAILURE: {
                            Log.d(ActivityTag.SEARCH_FORM_ACTIVITY,  "status 0");
                            break;
                        }
                        default : {
                            Log.d(ActivityTag.SEARCH_FORM_ACTIVITY,  "default");
                        }
                    }

                    //showProgress(false);
                    //mAuthTask = true;

                    long timeTaken = System.currentTimeMillis() - attributeStartTime;
                    Log.d(ActivityTag.SEARCH_FORM_ACTIVITY, "Request Attribute Total time taken: " + timeTaken);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(ActivityTag.SEARCH_FORM_ACTIVITY, "Error: " + error);
                    //((SearchActivity)getActivity()).rootView.setBackground(null);
                    //((SearchActivity) getActivity()).splashImg.setBackground(null);
                    ((SearchActivity) getActivity()).splashImg.setVisibility(View.GONE);
                    ((SearchActivity) getActivity()).searchLoadingLayout.setVisibility(View.GONE);
                    ((SearchActivity)getActivity()).containerMsg.setVisibility(View.VISIBLE);
                }
            });
            String tag_json_obj = "search_form_attribute";
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToCacheRequestQueue(jsObjRequest, tag_json_obj);
        }
        catch(Exception e) {
            Log.d(ActivityTag.SEARCH_FORM_ACTIVITY, e.getMessage());
        }
    }

    private void setRangeValues(String keyName, String label, CommonFields fields) {
        String currFormat = "";
        switch(keyName) {
            case Keys.PRICE: rangeMap.put(keyName, fields);
                attrLabels.put(keyName, label);

                if(((SearchActivity)getActivity()).getSearchType() != Flags.Bundle.Values.EDIT_SEARCH) {
                    if(AppController.getInstance().getCurrencyFormat().equals("CAD")) {
                        currFormat = "USD";
                    }
                    else {
                        currFormat = "CAD";
                    }

                    ((SearchActivity)getActivity()).isFirstLoad = true;
                    adapter.multiUnitConfigApi(Request.Method.POST, Flags.getConfigUrl(mSession,
                            ((SearchActivity)getActivity()).getSearchType(), searchID), ResponseConfig.class,
                            adapter.getMultiUnitRequestData(Keys.PRICE, currFormat,
                                    fields.getDefaultValue().getMin(),
                                    fields.getDefaultValue().getMax(), AppController.getInstance().getCurrencyFormat()),
                            null, Keys.PRICE);
                }

                break;
            case Keys.YEAR: rangeMap.put(keyName, fields);
                attrLabels.put(keyName, label);
                break;
            case Keys.KILOMETERS: rangeMap.put(keyName, fields);
                attrLabels.put(keyName, label);

                if(((SearchActivity)getActivity()).getSearchType() != Flags.Bundle.Values.EDIT_SEARCH) {
                    if(AppController.getInstance().getKilometerFormat().equals("kilometers")) {
                        currFormat = "miles";
                    }
                    else {
                        currFormat = "kilometers";
                    }

                    adapter.multiUnitConfigApi(Request.Method.POST, Flags.getConfigUrl(mSession,
                            ((SearchActivity)getActivity()).getSearchType(), searchID), ResponseConfig.class,
                            adapter.getMultiUnitRequestData(Keys.KILOMETERS, currFormat,
                                    fields.getDefaultValue().getMin(),
                                    fields.getDefaultValue().getMax(), AppController.getInstance().getKilometerFormat()),
                            null, Keys.KILOMETERS);
                }

                break;
            case Keys.BELOW_MARKET_AVG: rangeMap.put(keyName, fields);
                attrLabels.put(keyName, label);
                break;
        }
        adapter.notifyDataSetChanged();
    }

    public void setFormValues(JsonObject attribData, String keyName, String label) {
        JsonArray jarr = new JsonArray();
        if(attribData.get(keyName) != null && attribData.get(keyName).isJsonObject()) {
            JsonObject jo = (JsonObject) attribData.get(keyName);
            Set<Map.Entry<String, JsonElement>> entrySet = jo.entrySet();
            for(Map.Entry<String,JsonElement> entry : entrySet){
                jarr.add(jo.get(entry.getKey()));
            }

            List<AttrValSpinnerModel> list = (List<AttrValSpinnerModel>)new Gson().fromJson(jarr,
                    new TypeToken<List<AttrValSpinnerModel>>(){}.getType());

            switch(keyName) {
                case Keys.COUNTRY :
                    msMap.put(keyName, list);
                    attrLabels.put(keyName, label);
                    adapter.notifyDataSetChanged();
                    break;
                case Keys.CITY :
                    msMap.put(keyName, list);
                    attrLabels.put(keyName, label);
                    adapter.notifyDataSetChanged();
                    break;
                case Keys.MAKE :
                    msMap.put(keyName, list);
                    attrLabels.put(keyName, label);
                    adapter.notifyDataSetChanged();
                    break;
                /*case Keys.MODEL :
                    msMap.put(keyName, (List<AttrValSpinnerModel>)new Gson().fromJson(jarr,
                            new TypeToken<List<AttrValSpinnerModel>>(){}.getType()));
                    attrLabels.put(keyName, label);
                    adapter.notifyDataSetChanged();
                    break;*/
                case Keys.BODY :
                    msMap.put(keyName, list);
                    attrLabels.put(keyName, label);
                    adapter.notifyDataSetChanged();
                    break;
                case Keys.USER_TYPE :
                    for(AttrValSpinnerModel attr : list) {
                        if(attr.getId() == 2061441) {
                            attr.setValue("Unclassified");
                        }
                    }

                    msMap.put(keyName, list);
                    attrLabels.put(keyName, label);
                    adapter.notifyDataSetChanged();
                    break;
                case Keys.TRANSMISSION :
                    msMap.put(keyName, list);
                    attrLabels.put(keyName, label);
                    adapter.notifyDataSetChanged();
                    break;
                case Keys.COLOUR :
                    msMap.put(keyName, list);
                    attrLabels.put(keyName, label);
                    adapter.notifyDataSetChanged();
                    break;
                case Keys.DOORS :
                    msMap.put(keyName, list);
                    attrLabels.put(keyName, label);
                    adapter.notifyDataSetChanged();
                    break;
                case Keys.DRIVETRAIN :
                    msMap.put(keyName, list);
                    attrLabels.put(keyName, label);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
        else if(Keys.MODEL.equals(keyName)){
            msMap.put(keyName, (List<AttrValSpinnerModel>)new Gson().fromJson(jarr, new TypeToken<List<AttrValSpinnerModel>>(){}.getType()));
            attrLabels.put(keyName, label);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void multiSelectEventTrigger(String keyname) {
        try {
            /*if(keyname.equals(Keys.MAKE))
                requestModelData(Request.Method.GET, getModelAttrURL(), SearchResponse.class);
            else
                requestItemGetCountAll(Request.Method.POST, Flags.URL.ITEM_GETCOUNT + Flags.URL.DOMAIN_ID, SearchResponse.class);*/
        }
        catch(Exception e){
            Log.d(ActivityTag.SEARCH_FORM_ACTIVITY, e.getMessage());
        }
    }

    @Override
    public void nwResponseData(String pData){
        this.pData = pData;
    }

    @Override
    public void onCheckBoxItemClicked(String keyname, Map<Integer, AttrValSpinnerModel> attrMap, int asd, AttrValSpinnerModel tmp) {}

    @Override
    public void onCheckBoxItemClicked(String keyName, boolean checked, String intRep, String value, String group, String id, int position) {}

    @Override
    public void onCheckBoxItemClicked(boolean checked, String value) {}

    @Override
    public void fetchAdapter(String attribName, MultiSelectItemSearchViewAdapter adapter) {}

    @Override
    public void fetchMinMax(String attribName, int minRawValue, int maxRawValue, String minValue, String maxValue, String displayFormat) {}

    @Override
    public void setRequestData(Bundle requestBundle) {}

    @Override
    public void searchBarEvent(String keywords, boolean isFromResultScreen) {
        //searchField.setText(keywords);
        adapter.keywordSearchAction(keywords);
    }

    @Override
    public void clearAllTags() {
        if(null != adapter) {
            adapter.clearAllTags();
        }
    }

    @Override
    public void locationUpdate() {
        if(null != adapter && adapter.applyDefaultCity(true))
            adapter.triggerResult();
    }

    @Override
    public void updateGeoCities(String cities) {}

    @Override
    public void syncTagLayout(boolean isFromResultScreen, boolean isCreateTagRequest,
                              AttrValSpinnerModel attr, CommonFields rangeField, boolean isMultiSelectView, String keyname) {
        if(!isCreateTagRequest) {
            if(isMultiSelectView){
                adapter.removeMultiSelectTagToken(attr.isSelected(), keyname, attr);
                adapter.notifyDataSetChanged();
            }
            else {
                adapter.removeRangeTagToken(rangeField, keyname);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onSearchTypeChange(int flag) {
        switch(flag) {
            case Flags.Bundle.Values.WALL_OF_DEALS: {
                if(((SearchActivity)getActivity()).getSearchType() == Flags.Bundle.Values.EDIT_SEARCH) {
                    applyNewSearchConstraints();
                    //adapter.setSearchType(flag);
                    ((SearchActivity)getActivity()).setSearchType(flag);
                }
                adapter.clearKeywordSearchField();
                //searchType = flag;
                adapter.clearAll(true);
                adapter.applyDefaultCity(true);
                ((SearchActivity)getActivity()).clearAllTagsForResult();
                adapter.setWallOfDealsData();
                break;
            }
            case Flags.Bundle.Values.NEW_SEARCH: {
                applyNewSearchConstraints();
                adapter.clearKeywordSearchField();
                //searchType = flag;
                if(adapter.clearAll(false))
                    adapter.triggerResult();

                break;
            }
        }
        ((SearchActivity)getActivity()).setSearchType(flag);
    }

    public void keywordSearchClearText() {
        if(null != adapter) {
            adapter.clearKeywordSearchField();
        }
    }

    /*********************/

    public void sortResultCaller(int param) {
        adapter.sortResult(param);
    }

    public void showKeywordSearchButtonProgress(boolean constraint) {
        if(null != adapter) {
            adapter.setKeywordSearchProgress(constraint);
        }
        else {
            ((SearchActivity)getActivity()).startNewSearch();
        }
    }

    public void applyNewSearchConstraints() {
        try {
            saveSearchLayout.setVisibility(View.VISIBLE);
            setSwitchOff(saveSearchSwitch);
            saveSearchSwitch.setThumbColorRes(R.color.white);
            if(receiveNotificationSwitch.isChecked()) {
                setSwitchOff(receiveNotificationSwitch);
                setSwitchOff(saveSearchSwitch);
            }
            adapter.enableSaveSearch(false);
            adapter.setSearchID(null);
            searchID = null;
            ((SearchActivity)getActivity()).tvSearchButton.setText(getResources().getString(R.string.search_btn_new));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void scrollToTopListener(int pos) {
        if(null != mLayoutManager)
            mLayoutManager.smoothScrollToPosition(rvSearchForm, null, pos);
    }
}