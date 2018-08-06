package com.app.oktpus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.oktpus.utils.Utility;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.app.oktpus.adapter.SavedSearchAdapter;
import com.app.oktpus.model.AttrValSpinnerModel;
import com.app.oktpus.model.EditSearchRangeAttributes;
import com.app.oktpus.model.ItemRequestData;
import com.app.oktpus.model.SavedSearchItem;
import com.app.oktpus.model.SearchResponse;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.constants.ResponseCode;
import com.app.oktpus.constants.SavedSearchOperations;
import com.app.oktpus.controller.AppController;
import com.app.oktpus.listener.OnCallListener;
import com.app.oktpus.listener.OnClickSavedSearchItem;
import com.app.oktpus.R;
import com.app.oktpus.utils.Client;
import com.app.oktpus.utils.SessionManagement;

public class SavedSearchActivity extends BaseActivity implements OnCallListener, OnClickSavedSearchItem{

    private String responseString = "";
    private LinearLayout linearLayoutMain;
    private final static int RECEIVE_NOTIF = 0, DELETE_SEARCH = 1;
    private SessionManagement session;
    private RecyclerView savedSearchList;
    private SavedSearchAdapter savedSearchAdapter;
    private TextView pageTitle, noResultMsg;
    private ImageView backButton;
    private List<SavedSearchItem> itemsList;
    private RelativeLayout coordinatorLayout, noResultView;;
    private Tracker mTracker;
    private static final int OPERATION_GET_SEARCH = 0, OPERATION_GET_SEARCH_LIST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        includeNavigationMenu(this, R.layout.activity_saved_search);
        mTracker = AppController.getInstance().getDefaultTracker();
        mTracker.setScreenName("Saved Search");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        linearLayoutMain = (LinearLayout)findViewById(R.id.ll_saved_search);
        coordinatorLayout = (RelativeLayout) findViewById(R.id.root);
        pageTitle = (TextView) findViewById(R.id.page_title);
        backButton = (ImageView) findViewById(R.id.ss_back_arrow);
        noResultView = (RelativeLayout) findViewById(R.id.no_results_layout);
        noResultMsg = (TextView) findViewById(R.id.no_results_msg);
        noResultMsg.setTypeface(AppController.getFontType(getApplicationContext()));
        //nestedScrollView = (NestedScrollView) findViewById(R.id.nested_scroll_view);
        pageTitle.setTypeface(AppController.getFontType(this));
        pageTitle.setText("Saved Search");
        session = new SessionManagement(this);
        noResultMsg.setText("No saved searches found");
        savedSearchList = (RecyclerView) findViewById(R.id.rv_saved_search);

        //nestedScrollView.setFillViewport(true);
        //fab = ((BaseActivity) this).mFab;
        //appBarLayout = ((BaseActivity)this).mAppBarLayout;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        itemsList = new ArrayList<>();
        savedSearchAdapter = new SavedSearchAdapter(this, itemsList, SavedSearchActivity.this);
        savedSearchList.setAdapter(savedSearchAdapter);
        savedSearchList.setLayoutManager(new LinearLayoutManager(this));
        savedSearchAdapter.enableLoadingBar(true);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nestedScrollView.smoothScrollTo(0, 0);
                showToolbar();
                fab.hide();
            }
        });*/

        requestSearchApi(Request.Method.GET, Flags.URL.GET_SEARCH_LIST, SearchResponse.class, OPERATION_GET_SEARCH_LIST);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //nestedScrollView.smoothScrollTo(0, 0);
    }

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
    Client requestClient;
    private void requestSearchApi(final int methodType, final String url, final Class resClass, final int operation) {

        try {
            Log.d(Flags.ActivityTag.SAVED_SEARCH_ACTIVITY,"attrib url: " + url);
            requestClient = new Client(methodType, url, resClass, this, new Response.Listener<Object>() {
                @Override
                public void onResponse(Object response) {
                    SearchResponse res = (SearchResponse)response;
                    //System.out.println("Response found: "+ res.getStatus());
                    savedSearchAdapter.enableLoadingBar(false);
                    ResponseCode resCode = ResponseCode.fromValue(res.getStatus());
                    switch(resCode) {
                        case STATUS_SUCCESS: {
                            try {
                                responseRegulator(operation, responseString);
                            }
                            catch(Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                        case STATUS_FAILURE: {
                            Log.d(Flags.ActivityTag.SAVED_SEARCH_ACTIVITY,  "status 0");
                            break;
                        }
                        default : {
                            Log.d(Flags.ActivityTag.SAVED_SEARCH_ACTIVITY,  "default");
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    savedSearchAdapter.enableLoadingBar(false);
                    Utility.showErrorSnackbar(getApplicationContext(), coordinatorLayout, requestClient, error, Flags.NetworkRequestTags.SAVED_SEARCH_LIST);
                    Log.e(Flags.ActivityTag.SAVED_SEARCH_ACTIVITY, "Error: " + error);
                }
            });

            requestClient.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(requestClient, Flags.NetworkRequestTags.SAVED_SEARCH_LIST);
        }
        catch(Exception e) {
            Log.e(Flags.ActivityTag.SAVED_SEARCH_ACTIVITY, e.getMessage());
        }
    }

    private void launchEditSearchForm(String responseData){
        try {
            Bundle bundle = new Bundle();
            Bundle rangeBundle = new Bundle();
            int statusId = -1;
            String searchId = "";
            boolean includeNoImage = false;
            List<AttrValSpinnerModel> list;
            EditSearchRangeAttributes rangeAttributes;
            JsonObject jObj = new JsonParser().parse(responseString).getAsJsonObject();
            if(jObj.has(Flags.Keys.RESULT)) {
                JsonObject result = (JsonObject) jObj.get(Flags.Keys.RESULT);
                if(result.has(Flags.Keys.STATUS_ID)) { statusId = result.get(Flags.Keys.STATUS_ID).getAsInt(); }
                if(result.has(Flags.Keys.ID)) { searchId = result.get(Flags.Keys.ID).getAsString();}
                if(result.has(Flags.Keys.SERIALIZED_VALUES)) {
                    JsonObject serializedValues = (JsonObject) result.get(Flags.Keys.SERIALIZED_VALUES);
                    if(serializedValues.has(Flags.Keys.ATTRIBUTE) && serializedValues.get(Flags.Keys.ATTRIBUTE).isJsonObject()) {
                        JsonObject attributes = (JsonObject) serializedValues.get(Flags.Keys.ATTRIBUTE);

                        Set<Map.Entry<String, JsonElement>> entrySet = attributes.entrySet();
                        for (Map.Entry<String, JsonElement> entry : entrySet) {
                            if(entry.getValue() != null && entry.getValue().isJsonObject()) {
                                JsonObject entryValues = (JsonObject) entry.getValue();
                                switch(entry.getKey()) {
                                    case Flags.Keys.PRICE:
                                    case Flags.Keys.KILOMETERS:
                                    case Flags.Keys.YEAR:
                                    case Flags.Keys.BELOW_MARKET_AVG: {
                                        rangeAttributes = new Gson().fromJson(entryValues, new TypeToken<EditSearchRangeAttributes>(){}.getType());
                                        rangeBundle.putParcelable(entry.getKey(), rangeAttributes);
                                        break;
                                    }
                                    case Flags.Keys.COUNTRY:
                                    case Flags.Keys.CITY:
                                    case Flags.Keys.MAKE:
                                    case Flags.Keys.MODEL:
                                    case Flags.Keys.BODY:
                                    case Flags.Keys.DOORS:
                                    case Flags.Keys.DRIVETRAIN:
                                    case Flags.Keys.TRANSMISSION:
                                    case Flags.Keys.COLOUR:
                                    case Flags.Keys.USER_TYPE: {
                                        if(entryValues != null && entryValues.has(Flags.Keys.VALUES) && entryValues.isJsonObject()) {
                                            if(entryValues.get(Flags.Keys.VALUES).isJsonArray()) {
                                                list = new ArrayList<AttrValSpinnerModel>();
                                                JsonArray jArr = (JsonArray)entryValues.get(Flags.Keys.VALUES);
                                                list = new Gson().fromJson(jArr, new TypeToken<List<AttrValSpinnerModel>>(){}.getType());
                                                bundle.putParcelableArrayList(entry.getKey(), (ArrayList<? extends Parcelable>) list);
                                            }
                                        }

                                    }
                                    break;
                                    case Flags.Keys.IMAGE_URL:
                                        if(entryValues != null && entryValues.has(Flags.Keys.VALUES) && entryValues.get(Flags.Keys.VALUES).isJsonArray()) {
                                            JsonArray jArr = (JsonArray) entryValues.get(Flags.Keys.VALUES);
                                            Iterator itr = jArr.iterator();
                                            while(itr.hasNext()) {
                                                JsonObject jo = (JsonObject)itr.next();
                                                if(jo.has(Flags.Keys.ID)) {
                                                    if(jo.get(Flags.Keys.ID).getAsInt() == 1){
                                                        includeNoImage = true;
                                                    }
                                                }
                                            }
                                        }
                                        break;
                                }
                            }
                        }
                    }
                }
            }

            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            if(null != rangeBundle)  intent.putExtra(Flags.Bundle.Keys.ATTR_RANGE_PARCEL, rangeBundle);
            if(null != bundle) intent.putExtra(Flags.Bundle.Keys.ATTRIBUTE_PARCEL, bundle);
            intent.putExtra(Flags.Bundle.Keys.INCLUDE_NO_IMAGE_PARCEL, includeNoImage);
            intent.putExtra(Flags.Bundle.Keys.SEARCH_ID, searchId);
            intent.putExtra(Flags.Bundle.Keys.STATUS_ID, statusId);
            intent.putExtra(Flags.Bundle.Keys.SOURCE_PAGE, Flags.Bundle.Values.EDIT_SEARCH);
            intent.putExtra(Flags.Bundle.Keys.TO_SEARCH_FORM, true);
            /*intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);*/
            startActivity(intent);
            finishAffinity();
        }
        catch(Exception e) {
            Log.e(Flags.ActivityTag.SAVED_SEARCH_ACTIVITY, e.getMessage());
            e.printStackTrace();
        }
        /*{"status":1,"result":
        {"id":"1296","serialized_values":
        {"header":{"domain_id":"1"},
        "attribute":{"city":{"values":[{"is_group":0,"id":"22","value":"Barrie, ON, CA"},
        {"is_group":0,"id":"5","value":"Boston, MA, US"}],"must_have":true},
        "make":{"values":[{"is_group":0,"id":"80802408","value":"Acura"}],"must_have":true}}},"domain_id":"1","created_on":"2016-11-28 06:37:38",
        "updated_on":"2016-11-30 19:15:10","status_id":"1","status_date":"2016-11-30 19:15:10","user_id":"137",
        "search_values":["Barrie, ON, CA, Boston, MA, US","Acura"]}}*/

    }

    private void responseRegulator(int operation, String responseString) {
        try{
            switch(operation) {
                case OPERATION_GET_SEARCH_LIST:
                    loadDataForDisplay(responseString);
                    break;
                case OPERATION_GET_SEARCH:
                    launchEditSearchForm(responseString);
                    break;
            }
        }
        catch(Exception e) {
            Log.e(Flags.ActivityTag.SAVED_SEARCH_ACTIVITY, e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadDataForDisplay(String responseString) {
        try {
            JsonObject jObj = new JsonParser().parse(responseString).getAsJsonObject();
            if(jObj.has(Flags.Keys.RESULT)) {
                SavedSearchItem item;
                if(jObj.get(Flags.Keys.RESULT).isJsonArray()) {
                    JsonArray jArr = (JsonArray)jObj.get(Flags.Keys.RESULT);
                    if(jArr.size() == 0) {
                        noResultView.setVisibility(View.VISIBLE);
                        savedSearchList.setVisibility(View.GONE);
                    }
                }
                else {
                    noResultView.setVisibility(View.GONE);
                    savedSearchList.setVisibility(View.VISIBLE);
                    JsonObject inJo = (JsonObject) jObj.get(Flags.Keys.RESULT);
                    Set<Map.Entry<String, JsonElement>> entrySet = inJo.entrySet();

                    for (Map.Entry<String, JsonElement> entry : entrySet) {
                        //Log.d("KeyValue: ", "Key: " + entry.getKey() + ", value: " + entry.getValue() + "");
                        JsonObject jId = (JsonObject) entry.getValue();
                        Set<Map.Entry<String, JsonElement>> entrySetInner = jId.entrySet();
                        item = new SavedSearchItem();
                        for(Map.Entry<String, JsonElement> entryInner : entrySetInner) {
                            //Log.d("KeyValue: ", "KeyInner: " + entryInner.getKey() + ", valueInner: " + entryInner.getValue());
                            if (Flags.Keys.SEARCH_VALUES.equals(entryInner.getKey())) {
                                JsonArray valuesArr = entryInner.getValue().getAsJsonArray();
                                item.setTitleHeader(headerParser(valuesArr));
                                item.setSearchValues(headerFormatter(valuesArr));
                            }
                            if (Flags.Keys.STATUS_ID.equals(entryInner.getKey()))
                                item.setStatusID(entryInner.getValue().getAsInt());

                            if (Flags.Keys.ID.equals(entryInner.getKey())) {
                                item.setSearchID(entryInner.getValue().getAsString());
                            }

                            if (Flags.Keys.SERIALIZED_VALUES.equals(entryInner.getKey())) {
                                item.setSerializedValues(entryInner.getValue());
                            }
                        }

                        itemsList.add(item);

                    /*headerList.add(item.getSearchID());
                    childMap.put(item.getSearchID(), item);*/
                    }
                }


                savedSearchAdapter.notifyDataSetChanged();
                //if (headerList.size() > 0) {
                    /*savedSearchExpandableAdapter = new SavedSearchExpandableAdapter(this, headerList,
                            childMap, SavedSearchActivity.this);*/
                //savedSearchExList.setAdapter(savedSearchExpandableAdapter);
                //}
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            Log.e(Flags.ActivityTag.SAVED_SEARCH_ACTIVITY, e.getMessage());
        }
    }

    public String headerParser(JsonArray valuesArr) {
        StringBuilder result = new StringBuilder();
        int count = 0;
        for(JsonElement je : valuesArr) {
            String str = je.getAsString();
            if(str.contains(",") && !str.matches(".*\\d+.*")) {
                str = str.substring(0, str.indexOf(","));
            }
            ++count;
            if(count > 2) break;
            result.append(str +" ");
        }
        //Log.d(Flags.ActivityTag.SAVED_SEARCH_ACTIVITY,"Result: " + result.toString());

        return result.toString();
    }

    public String headerFormatter(JsonArray values) {
        List<String> list = new ArrayList<String>();
        for(JsonElement je : values) {
            list.add(String.valueOf(je.getAsString()));
        }
        return android.text.TextUtils.join(" • ", list);
    }

    @Override
    public void nwResponseData(String responseString) {
        this.responseString = responseString;
        Log.d(Flags.ActivityTag.SAVED_SEARCH_ACTIVITY, "partialData loaded in local: " + responseString.toString());
    }

    public void requestUpdateSavedSearch(int methodType, String url, final Class clazz, final String searchId,
                                         final int enableNotification, final Object serializedValues, final int disableSearch, final int reqType) {
        try {
            Client jsObjRequest = new Client(methodType, url, clazz, this, new Response.Listener<Object>() {
                @Override
                public void onResponse(Object response) {

                    SearchResponse res = (SearchResponse)response;
                    if(res.getStatus() == 1) {

                        String receiveNotifTxt = "";

                        if(enableNotification == 1) {
                            receiveNotifTxt = "You will now receive notifications";
                        }
                        else {
                            receiveNotifTxt = "Notifications turned off";
                        }

                        Snackbar snackbar = Snackbar.make(coordinatorLayout, (reqType == RECEIVE_NOTIF) ? receiveNotifTxt :
                                "Saved search deleted successfully", Snackbar.LENGTH_LONG);
                        /*View view = snackbar.getView();
                        view.bringToFront();
                        view.invalidate();*/
                        /*FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
                        view.setLayoutParams(params);*/
                        snackbar.setActionTextColor(getResources().getColor(R.color.successRes));
                        snackbar.show();
                        Log.d(Flags.ActivityTag.SAVED_SEARCH_ACTIVITY, "status:  " + res.getStatus());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse response = error.networkResponse;
                    if(response != null && response.data != null){
                        switch(response.statusCode){
                            case 400:
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, (reqType == RECEIVE_NOTIF) ? "Failed to update changes" :
                                        "Failed to delete saved search", Snackbar.LENGTH_LONG);
                                snackbar.setActionTextColor(getResources().getColor(R.color.successRes));
                                snackbar.show();
                                break;
                        }
                        //Additional cases
                    }
                    Log.d(Flags.ActivityTag.SAVED_SEARCH_ACTIVITY, "Error: " + error);
                }
            }){
                @Override
                public byte[] getBody() throws AuthFailureError {
                    //return request;
                    //return getRequestData(searchId, statusID);
                    return requestData(searchId, enableNotification, serializedValues, disableSearch);
                }
            };
            String tag_json_obj = "json_obj_req";
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsObjRequest, tag_json_obj);
        }
        catch(Exception e) {
            e.printStackTrace();
            Log.e(Flags.ActivityTag.SAVED_SEARCH_ACTIVITY, e.getMessage());
        }
    }

    public byte[] requestData(String searchID, int statusID, Object serializedValues, int disableSearch) {
        ItemRequestData.Builder builder = new ItemRequestData.Builder();

        JsonObject jObj = (JsonObject)serializedValues;
        String mKmsMinVal="", mKmsMaxVal="",
                mYearMinVal="", mYearMaxVal="",
                mPriceMinVal="", mPriceMaxVal="",
                mBmaMinVal="", mBmaMaxVal="";
        String priceFormat = Flags.DefaultValues.PRICE_FORMAT_DEFAULT, kilometersFormat = Flags.DefaultValues.KMS_FORMAT_DEFAULT;
        ArrayList<String> doorsValues, drivetrainValues, colourValues, countryValues, cityValues, bodyValues, transmissionValues, makeValues, modelValues, userTypeValues,
                doorsGroup, drivetrainGroup, countryGroup, cityGroup, makeGroup, modelGroup, bodyGroup, colourGroup, transmissionGroup, userTypeGroup;

        if(jObj.has(Flags.Keys.ATTRIBUTE) && jObj.get(Flags.Keys.ATTRIBUTE).isJsonObject()){
            JsonObject valueObj = (JsonObject)jObj.get(Flags.Keys.ATTRIBUTE);
            for(Map.Entry<String, JsonElement> entryInner : valueObj.entrySet()) {
                JsonObject values = (JsonObject) entryInner.getValue();

                if(Flags.Keys.COUNTRY.equals(entryInner.getKey())) {
                    countryValues = new ArrayList<>(); countryGroup = new ArrayList<>();
                    parseAndFill(countryValues, countryGroup, values);
                    if(countryValues != null && countryValues.size() > 0) builder.countryValueList(countryValues);
                    if(countryGroup != null && countryGroup.size() > 0) builder.countryGroupList(countryGroup);
                }
                else if(Flags.Keys.CITY.equals(entryInner.getKey())) {
                    cityValues = new ArrayList<>(); cityGroup = new ArrayList<>();
                    parseAndFill(cityValues, cityGroup, values);
                    if(cityValues != null && cityValues.size() > 0) builder.cityValueList(cityValues);
                    if(cityGroup != null && cityGroup.size() > 0) builder.cityGroupList(cityGroup);
                }
                else if(Flags.Keys.MAKE.equals(entryInner.getKey())){
                    makeValues = new ArrayList<>(); makeGroup = new ArrayList<>();
                    parseAndFill(makeValues, makeGroup, values);
                    if(makeValues != null && makeValues.size() > 0) builder.makeValueList(makeValues);
                    if(makeGroup != null && makeGroup.size() > 0) builder.makeGroupList(makeGroup);
                }
                else if(Flags.Keys.MODEL.equals(entryInner.getKey())){
                    modelValues = new ArrayList<>(); modelGroup = new ArrayList<>();
                    parseAndFill(modelValues, modelGroup, values);
                    if(modelValues != null && modelValues.size() > 0) builder.modelValueList(modelValues);
                    if(modelGroup != null && modelGroup.size() > 0) builder.modelGroupList(modelGroup);
                }
                else if(Flags.Keys.BODY.equals(entryInner.getKey())){
                    bodyValues = new ArrayList<>(); bodyGroup = new ArrayList<>();
                    parseAndFill(bodyValues, bodyGroup, values);
                    if(bodyValues != null && bodyValues.size() > 0) builder.bodyValueList(bodyValues);
                    if(bodyGroup != null && bodyGroup.size() > 0) builder.bodyGroupList(bodyGroup);
                }
                else if(Flags.Keys.USER_TYPE.equals(entryInner.getKey())){
                    userTypeValues = new ArrayList<>(); userTypeGroup = new ArrayList<>();
                    parseAndFill(userTypeValues, userTypeGroup, values);
                    if(userTypeValues != null && userTypeValues.size() > 0) builder.userTypeValueList(userTypeValues);
                    if(userTypeGroup != null && userTypeGroup.size() > 0) builder.userTypeGroupList(userTypeGroup);
                }
                else if(Flags.Keys.TRANSMISSION.equals(entryInner.getKey())){
                    transmissionValues = new ArrayList<>(); transmissionGroup = new ArrayList<>();
                    parseAndFill(transmissionValues, transmissionGroup, values);
                    if(transmissionValues != null && transmissionValues.size() > 0) builder.transmissionValueList(transmissionValues);
                    if(transmissionGroup != null && transmissionGroup.size() > 0) builder.transmissionGroupList(transmissionGroup);
                }
                else if(Flags.Keys.DOORS.equals(entryInner.getKey())){
                    doorsValues = new ArrayList<>(); doorsGroup = new ArrayList<>();
                    parseAndFill(doorsValues, doorsGroup, values);
                    if(doorsValues != null && doorsValues.size() > 0) builder.doorsValueList(doorsValues);
                    if(doorsGroup != null && doorsGroup.size() > 0) builder.doorsGroupList(doorsGroup);
                }
                else if(Flags.Keys.DRIVETRAIN.equals(entryInner.getKey())){
                    drivetrainValues = new ArrayList<>(); drivetrainGroup = new ArrayList<>();
                    parseAndFill(drivetrainValues, drivetrainGroup, values);
                    if(drivetrainValues != null && drivetrainValues.size() > 0) builder.drivetrainValueList(drivetrainValues);
                    if(drivetrainGroup != null && drivetrainGroup.size() > 0) builder.drivetrainGroupList(drivetrainGroup);
                }
                else if(Flags.Keys.COLOUR.equals(entryInner.getKey())){
                    colourValues = new ArrayList<>(); colourGroup = new ArrayList<>();
                    parseAndFill(colourValues, colourGroup, values);
                    if(colourValues != null && colourValues.size() > 0) builder.colourValueList(colourValues);
                    if(colourGroup != null && colourGroup.size() > 0) builder.colourGroupList(colourGroup);
                }
                else if(Flags.Keys.PRICE.equals(entryInner.getKey())) {
                    if(values.has(Flags.Keys.MIN)) mPriceMinVal = values.get(Flags.Keys.MIN).getAsString();
                    if(values.has(Flags.Keys.MAX)) mPriceMaxVal = values.get(Flags.Keys.MAX).getAsString();
                    if(values.has(Flags.Keys.DISPLAY_FORMAT)) priceFormat = values.get(Flags.Keys.DISPLAY_FORMAT).getAsString();
                }
                else if(Flags.Keys.YEAR.equals(entryInner.getKey())) {
                    if(values.has(Flags.Keys.MIN)) mYearMinVal = values.get(Flags.Keys.MIN).getAsString();
                    if(values.has(Flags.Keys.MAX)) mYearMaxVal = values.get(Flags.Keys.MAX).getAsString();
                }
                else if(Flags.Keys.KILOMETERS.equals(entryInner.getKey())) {
                    if(values.has(Flags.Keys.MIN)) mKmsMinVal = values.get(Flags.Keys.MIN).getAsString();
                    if(values.has(Flags.Keys.MAX)) mKmsMaxVal = values.get(Flags.Keys.MAX).getAsString();
                    if(values.has(Flags.Keys.DISPLAY_FORMAT)) kilometersFormat = values.get(Flags.Keys.DISPLAY_FORMAT).getAsString();
                }
                else if(Flags.Keys.BELOW_MARKET_AVG.equals(entryInner.getKey())) {
                    if(values.has(Flags.Keys.MIN)) mBmaMinVal = values.get(Flags.Keys.MIN).getAsString();
                    if(values.has(Flags.Keys.MAX)) mBmaMaxVal = values.get(Flags.Keys.MAX).getAsString();
                }
            }
        }

        builder.priceFormat(priceFormat).kilometersFormat(kilometersFormat);
        builder.sort(Flags.Sort.Keys.NEWEST_ARRIVALS);
        builder.distanceMinRaw(mKmsMinVal).distanceMaxRaw(mKmsMaxVal)
                .priceMinRaw(mPriceMinVal).priceMaxRaw(mPriceMaxVal)
                .yearMinRaw(mYearMinVal).yearMaxRaw(mYearMaxVal)
                .bmaMinRaw(mBmaMinVal).bmaMaxRaw(mBmaMaxVal);

        //if(priceFormat.equals("CAD") || priceFormat.equals("CDN"))
        if(mPriceMinVal.isEmpty()) builder.priceMinVal("CDN$ 0"); else builder.priceMinVal("CDN$ "+mPriceMinVal);
        if(mPriceMaxVal.isEmpty()) builder.priceMaxVal("above CDN$ 100,000"); else builder.priceMaxVal("CDN$ "+mPriceMaxVal);
        if(mKmsMinVal.isEmpty()) builder.distanceMinVal("0 km"); else builder.distanceMinVal(mKmsMinVal+" km");
        if(mKmsMaxVal.isEmpty()) builder.distanceMaxVal("above 300,000 km"); else builder.distanceMaxVal(mKmsMaxVal+" km");
        if(mYearMinVal.isEmpty()) builder.yearMinVal(Flags.DefaultValues.YEAR_MIN_DEFAULT); else builder.yearMinVal(mYearMinVal);
        if(mYearMaxVal.isEmpty()) builder.yearMaxVal(Flags.DefaultValues.YEAR_MAX_DEFAULT); else builder.yearMaxVal(mYearMaxVal);
        if(mBmaMinVal.isEmpty()) builder.bmaMinVal(Flags.DefaultValues.BMA_MIN_DEFAULT); else builder.bmaMinVal(mBmaMinVal+"%");
        if(mBmaMaxVal.isEmpty()) builder.bmaMaxVal(Flags.DefaultValues.BMA_MAX_DEFAULT); else builder.bmaMaxVal(mBmaMaxVal+"%");

        if(searchID != null) builder.searchID(searchID);

        if(disableSearch != 0) builder.disableSearch(disableSearch);
        builder.enableNotification(statusID);
        builder.searchID(searchID);
        builder.userID(String.valueOf(session.getUserID()));
        builder.build();
        ItemRequestData data = builder.build();

        return data.paramsByteFormat(data.getRequestData());
    }

    private void parseAndFill(ArrayList<String> valueList, ArrayList<String> groupList, JsonObject values) {
        if(values.get(Flags.Keys.VALUES).isJsonArray()) {
            JsonArray valuesArray = (JsonArray)values.get(Flags.Keys.VALUES);
            for(JsonElement value : valuesArray) {
                JsonObject jo = value.getAsJsonObject();
                int group = 0; String id ="";
                for(Map.Entry<String, JsonElement> item : jo.entrySet()) {
                    if(Flags.Keys.IS_GROUP.equals(item.getKey())) {
                        if(item.getValue().getAsInt() != 0) {
                            group = item.getValue().getAsInt();
                        }
                    }
                    if(Flags.Keys.ID.equals(item.getKey())){
                        id = item.getValue().getAsString();
                    }
                }
                if(group > 0) groupList.add(id);
                else valueList.add(id);
            }
        }
    }

    @Override
    public void onItemClick(String searchID, int statusID, int operationType, Object serializedValues) {
        SavedSearchOperations opType = SavedSearchOperations.fromValue(operationType);
        switch(opType) {
            case RECEIVE_NOTIF:
                int enableNotif = (statusID == 1) ? 0 : 1;          //Enable notif 0 = switchOFF, 1= switchON
                requestUpdateSavedSearch(Request.Method.POST, Flags.URL.UPDATE_SAVED_SEARCH+searchID+"?domain_id=1",
                        SearchResponse.class, searchID, enableNotif, serializedValues, 0, RECEIVE_NOTIF);
                break;
            case LAUNCH_SEARCH:
                requestSearchApi(Request.Method.GET, Flags.URL.GET_SEARCH+searchID, SearchResponse.class, OPERATION_GET_SEARCH);
                break;
            case DELETE_SEARCH:
                requestUpdateSavedSearch(Request.Method.POST, Flags.URL.UPDATE_SAVED_SEARCH+searchID+"?domain_id=1",
                        SearchResponse.class, searchID, 0, serializedValues, 1, DELETE_SEARCH);
                break;
            default :
                //TODO ...
        }
    }






    /*public class VolleyErrorHelper {
        *//**
     * Returns appropriate message which is to be displayed to the user
     * against the specified error object.
     *
     * @param error
     * @param context
     * @return
     *//*

    public static String getMessage (Object error , Context context){
        if(error instanceof TimeoutError){
            return context.getResources().getString(R.string.timeout);
        }else if (isServerProblem(error)){
            return handleServerError(error ,context);

        }else if(isNetworkProblem(error)){
            return context.getResources().getString(R.string.nointernet);
        }
        return context.getResources().getString(R.string.generic_error);

    }

    private static String handleServerError(Object error, Context context) {

        VolleyError er = (VolleyError)error;
        NetworkResponse response = er.networkResponse;
        if(response != null){
            switch (response.statusCode){

                case 404:
                case 422:
                case 401:
                    try {
                        // server might return error like this { "error": "Some error occured" }
                        // Use "Gson" to parse the result
                        HashMap<String, String> result = new Gson().fromJson(new String(response.data),
                                new TypeToken<Map<String, String>>() {
                                }.getType());

                        if (result != null && result.containsKey("error")) {
                            return result.get("error");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // invalid request
                    return ((VolleyError) error).getMessage();

                default:
                    return context.getResources().getString(R.string.timeout);
            }
        }

        return context.getResources().getString(R.string.generic_error);
    }

    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError || error instanceof AuthFailureError);
    }

    private static boolean isNetworkProblem (Object error){
        return (error instanceof NetworkError || error instanceof NoConnectionError);
    }*/

}