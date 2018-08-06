package com.app.oktpus.service;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.constants.ResponseCode;
import com.app.oktpus.controller.AppController;
import com.app.oktpus.listener.OnCallListener;
import com.app.oktpus.listener.SearchFilterAPIListener;
import com.app.oktpus.model.AttrValSpinnerModel;
import com.app.oktpus.model.SearchResponse;
import com.app.oktpus.utils.Client;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Gyandeep on 13/11/17.
 */

public class SearchFilterAPI  {

    private String mResponseData = "";
    private SearchFilterAPIListener mCallbackListener;
    public SearchFilterAPI(SearchFilterAPIListener callbackListener) {
        mCallbackListener = callbackListener;
        attributeApiRequest(Request.Method.GET, Flags.URL.ITEM_GET_ATTRIBUTE_VALUE, SearchResponse.class);
    }

    private void attributeApiRequest(final int methodType, final String url, final Class resClass) {
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("domain_id", "1");

            Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY,"attrib url: " + url);
            Client jsObjRequest = new Client(methodType, url, resClass, params, new OnCallListener() {
                @Override
                public void nwResponseData(String resData) throws IOException {
                    mResponseData = resData;
                }
            }, new Response.Listener<Object>() {
                @Override
                public void onResponse(Object response) {
                    SearchResponse res = (SearchResponse) response;
                    ResponseCode resCode = ResponseCode.fromValue(res.getStatus());
                    switch (resCode) {
                        case STATUS_SUCCESS: {
                            try {
                                String json = new Gson().toJson(res);
                                Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, "json: " + json);

                                JsonObject jObj = new JsonParser().parse(mResponseData).getAsJsonObject();

                                if(jObj.has(Flags.Keys.ATTRIBUTE_VALUE)) {
                                    JsonObject o = (JsonObject) jObj.get(Flags.Keys.ATTRIBUTE_VALUE);

                                    Map<String, List<AttrValSpinnerModel>> listToReturn = new HashMap<>();
                                    listToReturn.put(Flags.Keys.MAKE, setFormValues(o, Flags.Keys.MAKE));
                                    listToReturn.put(Flags.Keys.TRANSMISSION, setFormValues(o, Flags.Keys.TRANSMISSION));

                                    mCallbackListener.getFilterAttributeDataFromNetwork(listToReturn);
                                }

                                Map<String, Map<String, Object>> attributeLabelList = new HashMap<>();
                                if (jObj.has("attribute_label")) {
                                    JsonObject attributeLabel = (JsonObject) jObj.get("attribute_label");
                                    Set<Map.Entry<String, JsonElement>> entryAttribLabel = attributeLabel.entrySet();

                                    Map<String, Object> labelListMap;// = new HashMap<>();
                                    for (Map.Entry<String, JsonElement> child : entryAttribLabel) {
                                        JsonObject childObj = (JsonObject) attributeLabel.get(child.getKey());

                                        if (childObj.has("label_list")) {
                                            labelListMap = new HashMap<>();
                                            Set<Map.Entry<String, JsonElement>> childValues = childObj.entrySet();
                                            for (Map.Entry<String, JsonElement> ecv : childValues) {
                                                if (ecv.getKey().equals("label_list")) {
                                                    JsonElement labelList = ecv.getValue();
                                                    //Set<Map.Entry<String, JsonElement>> setLabelList = labelList.entrySet();
                                                    labelListMap.put(ecv.getKey(), new Gson().fromJson(labelList, new TypeToken<Map<String, String>>() {
                                                    }.getType()));
                                                    //for(Map.Entry<String, JsonElement> element : labelList.entrySet()){}
                                                } else
                                                    labelListMap.put(ecv.getKey(), ecv.getValue());
                                            }
                                            attributeLabelList.put(child.getKey(), labelListMap);
                                        }
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            break;
                        }
                        case STATUS_FAILURE: {
                            Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, "status 0");
                            break;
                        }
                        default: {
                            Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, "default");
                        }
                    }
                    //Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, "Request Attribute Total time taken: " + timeTaken);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, "Error: " + error);
                }
            });
            String tag_json_obj = "search_form_attribute";
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToCacheRequestQueue(jsObjRequest, tag_json_obj);
        }
        catch(Exception e) {
            Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, e.getMessage());
        }
    }

    public List<AttrValSpinnerModel> setFormValues(JsonObject attribData, String keyName) {
        JsonArray jarr = new JsonArray();
        List<AttrValSpinnerModel> list = null;
        Map<String, List<AttrValSpinnerModel>> map = new HashMap<>();
        if(attribData.get(keyName) != null && attribData.get(keyName).isJsonObject()) {
            JsonObject jo = (JsonObject) attribData.get(keyName);
            Set<Map.Entry<String, JsonElement>> entrySet = jo.entrySet();
            for(Map.Entry<String,JsonElement> entry : entrySet){
                jarr.add(jo.get(entry.getKey()));
            }

            list = (List<AttrValSpinnerModel>)new Gson().fromJson(jarr,
                    new TypeToken<List<AttrValSpinnerModel>>(){}.getType());

            map.put(keyName, list);
        }



        return list;
    }

}
