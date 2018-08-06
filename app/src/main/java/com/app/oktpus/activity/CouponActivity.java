package com.app.oktpus.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.oktpus.R;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.constants.Helper;
import com.app.oktpus.controller.AppController;
import com.app.oktpus.controller.CouponRecyclerController;
import com.app.oktpus.model.CouponModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by remees on 4/8/18.
 */

public class CouponActivity extends BaseActivity {

    ProgressDialog pDialog;
    public List<CouponModel> couponList = new ArrayList<>();
    private CouponRecyclerController mAdapter;
    LinearLayoutManager mLayoutManager;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        includeNavigationMenu(this, R.layout.fragment_coupon);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_coupon);

        mAdapter = new CouponRecyclerController(couponList);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
//                mLayoutManager.getOrientation());
//        recyclerView.addItemDecoration(dividerItemDecoration);

        callCouponApi();

    }

    public void callCouponApi(){

        pDialog.show();
        //data must be like  {"matri_ids":["SJ30500","SJ30501","SJ30502"]}
        // No need to check payment for this, the user must can view these profiles without payment
        HashMap<String, Object> params = new HashMap<String, Object>();

        Log.d("search url", Helper.COUPON_URL);
        Log.d("search input", String.valueOf(new JSONObject(params)));
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Helper.COUPON_URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("search criteria", response.toString());
                        pDialog.hide();
                        try {
                            JSONArray arrayData = response.getJSONArray("result");

                            if(response.getInt("status") == 1) {

                                for (int i = 0;i<arrayData.length();i++){

                                    JSONObject objectData = arrayData.getJSONObject(i);

                                    CouponModel model = new CouponModel();
                                    model.setTitle(objectData.getString("coupon_title"));
                                    model.setDescription(objectData.getString("description"));
                                    model.setendDate(objectData.getString("end_date"));
                                    couponList.add(model);
                                    mAdapter.notifyDataSetChanged();
                                    Log.d("notifyDataSetChanged called", String.valueOf(i));


                                }


                            }else{
                                Toast.makeText(CouponActivity.this
                                        ,"Opsss.. Something went wrong",Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("search criteria", "Error: " + error.getMessage());
                // hide the progress dialog
                pDialog.hide();
            }
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type","application/json");
                headers.put("Accept", "application/josn");
//				HashMap<String, String> headers = new HashMap<String, String>();
//
//				headers.put("Accept", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }
        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, "coupon");
    }

}
