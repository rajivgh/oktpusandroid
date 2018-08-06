package com.app.oktpus.utils;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.app.oktpus.BuildConfig;
import com.app.oktpus.constants.Flags;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.app.oktpus.controller.AppController;
import com.app.oktpus.listener.OnCallListener;

/**
 * Created by Gyandeep on 3/10/16.
 */

public class Client<T> extends Request<T> {

    //TODO One constructor for GET and one for POST
    private Response.Listener<T> listener;
    private Map<String, String> params;
    private byte[] paramsInBytes;
    private final Class<T> resClass;
    private boolean stringResponse = false;
    private Priority priority = Priority.NORMAL;
    private OnCallListener nwResponseData;
    public Client(int method, String url, Class<T> resClass, Map<String, String> params,
                  Response.Listener<T> responseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = responseListener;
        this.params = params;
        this.resClass = resClass;
    }

    //For GET methods
    public Client(int method, String url, Class<T> resClass, OnCallListener nwResponseData, Response.Listener<T> responseListener,
                  Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = responseListener;
        this.resClass = resClass;
        this.nwResponseData = nwResponseData;
    }

    //Prioritized GET Requests
    public Client(int method, String url, Class<T> resClass, OnCallListener nwResponseData, Response.Listener<T> responseListener,
                  Response.ErrorListener errorListener, Priority priority) {
        super(method, url, errorListener);
        this.listener = responseListener;
        this.resClass = resClass;
        this.nwResponseData = nwResponseData;
        this.priority = priority;
    }

    public Client(int method, String url, Class<T> resClass, Map<String, String> params, OnCallListener nwResponseData,
                  Response.Listener<T> reponseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
        this.resClass = resClass;
        this.nwResponseData = nwResponseData;
    }

    public Client(int method, String url, Class<T> resClass, byte[] params, OnCallListener nwResponseData,
                  Response.Listener<T> reponseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.paramsInBytes = params;
        this.resClass = resClass;
        this.nwResponseData = nwResponseData;
    }

    /*copy of above with PRIORITY*/
    public Client(int method, String url, Class<T> resClass, byte[] params, OnCallListener nwResponseData,
                  Response.Listener<T> reponseListener, Response.ErrorListener errorListener, Priority priority) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.paramsInBytes = params;
        this.resClass = resClass;
        this.nwResponseData = nwResponseData;
        this.priority = priority;
    }

    public Client(int method, String url, Class<T> resClass,
                  Response.Listener<T> reponseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.resClass = resClass;
        this.nwResponseData = nwResponseData;
    }

    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            AppController.getInstance().checkSessionCookie(response.headers);
            if(nwResponseData != null) {
                nwResponseData.nwResponseData(json);
                if(BuildConfig.DEBUG) {
                    Log.d("RESPONSE_HEADERS:" , "headers: "+ response.headers.toString());
                    for(Map.Entry<String, String> map : response.headers.entrySet()) {
                        Log.d("RES_HEADER", "response header:  " + map.getKey() + " -> " + map.getValue());
                    }
                    int maxLogSize = 500;
                    for(int i = 0; i <= json.length() / maxLogSize; i++) {
                        int start = i * maxLogSize;
                        int end = (i+1) * maxLogSize;
                        end = end > json.length() ? json.length() : end;
                        Log.v("ResponseData", json.substring(start, end));
                    }
                }
            }
            return Response.success(
                    new Gson().fromJson(json, resClass),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        } catch(Exception e) {
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
        Log.d("Client","request headers: "+headers.toString());
        return headers;
    }

    @Override
    protected void deliverResponse(T response) {
        // TODO Auto-generated method stub
        listener.onResponse(response);
    }
}