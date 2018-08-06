package com.app.oktpus.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.app.oktpus.R;
import com.app.oktpus.constants.Helper;
import com.app.oktpus.controller.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by remees on 29/6/18.
 */

public class ProductDetailActivity extends AppCompatActivity {

    ProgressDialog pDialog;
    String product_id;
    NetworkImageView product_image;
    TextView product_name ,product_desc ,product_price ;
    Spinner product_qty;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Button btn_add_cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carparts);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            product_id = extras.getString("product_id");
        }

        product_image = (NetworkImageView) findViewById(R.id.product_image);
        product_name = (TextView) findViewById(R.id.product_name);
        product_desc = (TextView) findViewById(R.id.product_desc);

        product_price = (TextView) findViewById(R.id.product_price);
        product_qty = (Spinner) findViewById(R.id.product_qty);

        btn_add_cart = (Button) findViewById(R.id.btn_add_cart);


        callCarpartsApi();

        btn_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callCartApi();
            }
        });

    }

    public void callCarpartsApi(){

        pDialog.show();
        //data must be like  {"matri_ids":["SJ30500","SJ30501","SJ30502"]}
        // No need to check payment for this, the user must can view these profiles without payment
        HashMap<String, Object> params = new HashMap<String, Object>();

        Log.d("search url", Helper.CAR_PARTS_URL);
        Log.d("search input", String.valueOf(new JSONObject(params)));
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Helper.CAR_PARTS_DETAILS_URL + product_id, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("search criteria", response.toString());
                        pDialog.hide();
                        try {
                            JSONArray arrayData = response.getJSONArray("result");
                            JSONArray arrayImageData = response.getJSONArray("imagePaths");


                            if(response.getInt("status") == 1) {


                                JSONObject objectData = arrayData.getJSONObject(0);
                                product_name.setText(objectData.getString("product_name"));
                                product_desc.setText(objectData.getString("product_description"));

                                product_price.setText(objectData.getString("product_price"));

                                if(arrayImageData.length()>0){
                                    product_image.setImageUrl(arrayImageData.getJSONObject(0).getString("image_path"), imageLoader);
                                }

                            }else{
                                Toast.makeText(ProductDetailActivity.this,"Opsss.. Something went wrong", Toast.LENGTH_LONG).show();
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
    public void callCartApi(){

        pDialog.show();
//        Request URL: http://mall140.com/oktpusApi/public/shoppingCart
//        Request Method:POST
//        Form Data:
//[user_id]: 125,
//[items]: {
//[product_id]:55,
//[quantity]: 5
//        },
//[mode] : 1

        HashMap<String, Object> params = new HashMap<String, Object>();
        HashMap<String, Object> items = new HashMap<String, Object>();

        params.put("user_id",Helper.USER_ID);
        params.put("mode",1);

        items.put("product_id",product_id);
        items.put("quantity",1);

        params.put("items",items);

        Log.d("search url", Helper.CAR_PARTS_ADD_CART_URL);
        Log.d("search input", String.valueOf(new JSONObject(params)));
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Helper.CAR_PARTS_ADD_CART_URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("search criteria", response.toString());
                        pDialog.hide();
                        try {

                            if(response.getInt("status") == 1) {



                            }else{
                                Toast.makeText(ProductDetailActivity.this,"Opsss.. Something went wrong", Toast.LENGTH_LONG).show();
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
