package com.app.oktpus.utils;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.app.oktpus.controller.AppController;

/**
 * Created by Gyandeep on 30/11/16.
 */

public class ApiRequest <T> extends JsonRequest<T> {

    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Response.Listener<T> mListener;

    public ApiRequest(int method, String url, Class<T> clazz, String jsonRequest,
                      Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener,
                errorListener);
        setRetryPolicy(new DefaultRetryPolicy(60000, 0, 1f));
        this.clazz = clazz;
        mListener = listener;
    }
    public ApiRequest(int method, String url, Class<T> clazz, String jsonRequest,
                      Response.Listener<T> listener, Response.ErrorListener errorListener,int volleyTimeout) {
        super(method, url, jsonRequest, listener,
                errorListener);
        setRetryPolicy(new DefaultRetryPolicy(volleyTimeout, 0, 1f));
        this.clazz = clazz;
        mListener = listener;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String json = new String(
                    networkResponse.data,
                    HttpHeaderParser.parseCharset(networkResponse.headers));

            int maxLogSize = 500;
            for(int i = 0; i <= json.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i+1) * maxLogSize;
                end = end > json.length() ? json.length() : end;
                Log.v("ResponseData", json.substring(start, end));
            }

            return Response.success(
                    gson.fromJson(json, clazz),
                    HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    /* (non-Javadoc)
    * @see com.android.volley.Request#getHeaders()
    */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();
        if (headers == null || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }

        AppController.getInstance().addSessionCookie(headers);
        Log.d("Client","getHeadrs methods: "+headers.toString());
        return headers;
    }

    @Override
    public String getBodyContentType() {
        return "application/x-www-form-urlencoded; charset=UTF-8";
    }

    @Override
    protected void deliverResponse(T response) {
        // TODO Auto-generated method stub
        mListener.onResponse(response);
    }


}