package com.app.oktpus.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.oktpus.R;
import com.app.oktpus.constants.Helper;
import com.app.oktpus.controller.AppController;
import com.app.oktpus.controller.CartRecyclerController;
import com.app.oktpus.model.CarpartsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by remees on 6/7/18.
 */

public class CartActivity extends AppCompatActivity {


    ProgressDialog pDialog;
    public List<CarpartsModel> carpartsList = new ArrayList<>();
    private CartRecyclerController mAdapter;
    LinearLayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    Button btn_checkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        pDialog = new ProgressDialog(CartActivity.this);
        pDialog.setMessage("Loading...");
        Log.d("oncreate view", "parts oncreate veiw called ");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_cart);
        btn_checkout = (Button) findViewById(R.id.btn_checkout);
        mAdapter = new CartRecyclerController(carpartsList);
        mLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
//                mLayoutManager.getOrientation());
//        recyclerView.addItemDecoration(dividerItemDecoration);

        callCartApi();

        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goMainActivity = new Intent(CartActivity.this, CheckoutActivity.class);
                startActivity(goMainActivity);
            }
        });

    }

    public void callCartApi(){

        pDialog.show();
        //data must be like  {"matri_ids":["SJ30500","SJ30501","SJ30502"]}
        // No need to check payment for this, the user must can view these profiles without payment
        HashMap<String, Object> params = new HashMap<String, Object>();

        Log.d("cart url", Helper.CAR_PARTS_GET_CART_URL+Helper.USER_ID);
        Log.d("search input", String.valueOf(new JSONObject(params)));
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Helper.CAR_PARTS_GET_CART_URL+Helper.USER_ID, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("search criteria", response.toString());
                        pDialog.hide();
                        try {
                            JSONArray arrayData = response.getJSONArray("result");

                            if(response.getInt("status") == 1) {

                                for (int i = 0;i <  arrayData.length();i++){

                                    JSONObject objectData = arrayData.getJSONObject(i);
                                    CarpartsModel model = new CarpartsModel();
                                    model.setTitle(objectData.getString("product_name"));
                                    model.setPrice(objectData.getString("product_description"));
                                    model.setDescription(objectData.getString("product_price"));
                                    model.setimage(objectData.getString("image_path"));
                                    model.setid(objectData.getString("product_id"));
                                    model.setQty(objectData.getString("product_quantity"));

                                    carpartsList.add(model);
                                    mAdapter.notifyDataSetChanged();
                                    Log.d("notifyDataSetChanged called", String.valueOf(i));

                                }


                            }else{
                                Toast.makeText(getApplicationContext(),"Opsss.. Something went wrong", Toast.LENGTH_LONG).show();
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
