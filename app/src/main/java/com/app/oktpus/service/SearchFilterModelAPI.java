package com.app.oktpus.service;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.constants.ModelStatus;
import com.app.oktpus.controller.AppController;
import com.app.oktpus.listener.OnCallListener;
import com.app.oktpus.listener.SearchFilterModelListener;
import com.app.oktpus.model.AttrValSpinnerModel;
import com.app.oktpus.model.SearchResponse;
import com.app.oktpus.utils.Client;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by Gyandeep on 13/11/17.
 */

public class SearchFilterModelAPI {

    private SearchFilterModelListener mCallbackListener;
    private String mResponseData = "";

    public SearchFilterModelAPI(SearchFilterModelListener listener, String url) {
        mCallbackListener = listener;
        searchFilterModelRequest(url);
    }

    private void searchFilterModelRequest(String url) {
        mCallbackListener.modelStatusListner(ModelStatus.LOADING);
        Client jsObjRequest = new Client(Request.Method.GET, url, SearchResponse.class, new OnCallListener() {
            @Override
            public void nwResponseData(String pData) {
                mResponseData = pData;
            }
        }, new Response.Listener<Object>() {
            @Override
            public void onResponse(Object response) {
                boolean isResultAvailable = true;
                JsonObject jObj = new JsonParser().parse(mResponseData).getAsJsonObject();
                Log.d("ClientResponse", "Response MODEL data on MAKE select");
                if (jObj.has(Flags.Keys.STATUS)) {
                    JsonPrimitive statu = (JsonPrimitive) jObj.get(Flags.Keys.STATUS);

                    if (statu.getAsInt() == 0) {
                        isResultAvailable = false;
                        mCallbackListener.modelStatusListner(ModelStatus.CHOOSE_A_MAKE_FIRST);

                    } else if (statu.getAsInt() == 1) {
                        if (jObj.has(Flags.Keys.RESULT)) {
                            if (jObj.get(Flags.Keys.RESULT).isJsonArray()) {
                                JsonArray jArr = (JsonArray) jObj.get(Flags.Keys.RESULT);
                                if (jArr.size() == 0) {
                                    mCallbackListener.modelStatusListner(ModelStatus.NO_MODELS_FOUND);
                                }

                                List<AttrValSpinnerModel> tmpList = new Gson().fromJson(jArr, new TypeToken<List<AttrValSpinnerModel>>() {}.getType());
                                mCallbackListener.modelDataFromNetworkListener(ModelStatus.SELECT, tmpList);
                            }
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, "Error: " + error);
                //Need to handle error for network failure cases
            }
        }, Request.Priority.IMMEDIATE);

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsObjRequest, "modelRequest");
    }
}
