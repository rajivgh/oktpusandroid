package com.app.oktpus.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.app.oktpus.model.Default;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.constants.ResponseCode;
import com.app.oktpus.controller.AppController;
import com.app.oktpus.listener.OnCallListener;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Gyandeep on 3/10/16.
 */

public class GetSessionID {

    private Context context;
    private OnCallListener mListener;
    public GetSessionID(Context context) {
        this.context = context;
        //this.mListener = listener;
        //Log.d("GetSessionID", "listener in constructor: "+ listener);
    }

    public void getSessIDFromServer() {

        RequestQueue queue = Volley.newRequestQueue(this.context);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Flags.URL.ITEM_GET_ATTRIBUTE_VALUE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        //Default d = new Gson().fromJson(response, Default.class);
                        //mListener.onSuccessResponse(d);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if(error instanceof com.android.volley.NoConnectionError) {
                            //System.out.println("error: "+ error.getMessage() + ", statusCode: ");
                        }
                        if(response != null && response.data != null){
                            switch(response.statusCode){
                                case 400:
                                    Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
                            }
                        }
                        Log.d(Flags.ActivityTag.SAVED_SEARCH_ACTIVITY, "Error: " + error);
                    }
                }) {

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                // since we don't know which of the two underlying network vehicles
                // will Volley use, we have to handle and store session cookies manually
                try {
                    String json = new String( response.data, HttpHeaderParser.parseCharset(response.headers));
                    Default def = new Gson().fromJson(json, Default.class);
                    if (ResponseCode.STATUS_SUCCESS.getValue() == Integer.parseInt(def.getStatus())) {
                        //listener.responseHeader(response.headers);
                        for(Map.Entry<String, String> map : response.headers.entrySet()) {
                            Log.d(TAG, "networkResponse GetSessionID..  " + map.getKey() + " -> " + map.getValue());
                        }
                        AppController.getInstance().checkSessionCookie(response.headers);
                        Log.d(TAG, "storing cookie into shared pref");
                    }

                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JsonSyntaxException e) {
                    return Response.error(new ParseError(e));
                }
                return super.parseNetworkResponse(response);
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }

    }
