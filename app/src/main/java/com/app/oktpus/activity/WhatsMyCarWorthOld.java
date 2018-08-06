package com.app.oktpus.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.oktpus.adapter.WMCWAdapter;
import com.app.oktpus.model.AttrValSpinnerModel;
import com.app.oktpus.model.Default;
import com.app.oktpus.model.SearchResponse;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.constants.ResponseCode;
import com.app.oktpus.controller.AppController;
import com.app.oktpus.listener.OnCallListener;
import com.app.oktpus.R;
import com.app.oktpus.service.FetchAddress;
import com.app.oktpus.utils.Client;
import com.app.oktpus.utils.SessionManagement;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.app.oktpus.adapter.WMCWAdapter.FIELD_CITY;
import static com.app.oktpus.adapter.WMCWAdapter.FIELD_COUNTRY;
import static com.app.oktpus.adapter.WMCWAdapter.FIELD_EMAIL;
import static com.app.oktpus.adapter.WMCWAdapter.FIELD_FNAME;
import static com.app.oktpus.adapter.WMCWAdapter.KMS_UNIT;
import static com.app.oktpus.adapter.WMCWAdapter.FIELD_MAKE;
import static com.app.oktpus.adapter.WMCWAdapter.FIELD_MILEAGE;
import static com.app.oktpus.adapter.WMCWAdapter.FIELD_MODEL;
import static com.app.oktpus.adapter.WMCWAdapter.FIELD_STATE;
import static com.app.oktpus.adapter.WMCWAdapter.FIELD_TRANSMISSION;
import static com.app.oktpus.adapter.WMCWAdapter.FIELD_YEAR;
import static com.app.oktpus.adapter.WMCWAdapter.FIELD_LNAME;

/**
 * Created by Gyandeep on 18/7/17.
 */

public class WhatsMyCarWorthOld extends BaseActivity {

    // note that these credentials will differ between live & sandbox environments.
    /**
     * cad-merchant@mca140.com              12345678
     * gyandeep-buyer@gmail.com             Personal abcABC123
     * oktpus.sales-facilitator@gmail.com   Business abcABC123
     *
     * Sandbox Client ID AQnQTCwskbXrVBt5kJK9IwOlqwkpH-6Rrj8A7-f_dmAqazUT6zs_j9PToNXsoPmbCTthv02dC4tuzGCL
     * Production Client ID AYgNnoqAtQdPHBehT7YC7qZqyfQzW4gihhL8MhTKQ0I_io8OPt1pb1d-Cedsk7bHT9byDTQRLmjKjDWO
     **/

    private static final String CURRENCY = "USD";
    //private static final String CONFIG_CLIENT = "AQnQTCwskbXrVBt5kJK9IwOlqwkpH-6Rrj8A7-f_dmAqazUT6zs_j9PToNXsoPmbCTthv02dC4tuzGCL";

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(AppController.getInstance().getResources().getString(R.string.paypal_config_env))
            .clientId(AppController.getInstance().getResources().getString(R.string.paypal_client_id));
    // The following are only used in PayPalFuturePaymentActivity.
    //.merchantName("Example Merchant");
    //.merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
    //.merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

    private TextView tvPageTitle;
    private ImageView ivBack;
    private RecyclerView rvWMCW;
    private Button successReturn;
    private WMCWAdapter adapterWMCW;
    private Map<String, List<AttrValSpinnerModel>> listData;
    private RelativeLayout successLayout;
    private ImageView progressBarLoading;
    private AnimationDrawable frameAnimation;
    private Map<Integer, String> mFieldData = null;
    private LinearLayout formContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        includeNavigationMenu(this, R.layout.activity_whats_my_car_worth);
        tvPageTitle = (TextView) findViewById(R.id.page_title);
        ivBack = (ImageView) findViewById(R.id.ss_back_arrow);
        successLayout = (RelativeLayout) findViewById(R.id.layout_success);
        successReturn = (Button) findViewById(R.id.btn_done_success);
        formContainer = (LinearLayout) findViewById(R.id.ll_form_container);
        progressBarLoading = (ImageView) findViewById(R.id.pb_wmcw_loading_bar);

        frameAnimation = (AnimationDrawable) progressBarLoading.getBackground();
        frameAnimation.start();

        listData = new HashMap<>();
        rvWMCW = (RecyclerView) findViewById(R.id.rv_wmcw_content);
        rvWMCW.setLayoutManager(new LinearLayoutManager(this));

        adapterWMCW = new WMCWAdapter(this, new SessionManagement(this), this, listData, new AdapterCallbacks() {
            @Override
            public void initiatePayment(Map<Integer, String> fieldData) {
                mFieldData = fieldData;
                //displayThankyou(null);
                //makePayment(fieldData); //Uncomment for payment feature
                Map<String, String> requestData = getRequestParam(mFieldData,
                        "", true);
                wmcwRequest(Request.Method.POST, Flags.URL.WMCW, requestData, null, Default.class);
                /*formContainer.setVisibility(View.GONE);
                inflateResultView(null, null, null);*/
                //wmcwRequest(Request.Method.POST, Flags.URL.WMCW, getRequestParam(fieldData, "xxxxxxxx"), Default.class);
            }

            @Override
            public void displayError(int errorCode) {
                rvWMCW.smoothScrollToPosition(rvWMCW.getHeight());
            }
        });

        rvWMCW.setAdapter(adapterWMCW);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvPageTitle.setTypeface(AppController.getFontType(this));
        tvPageTitle.setText(getResources().getString(R.string.wmcw_title));

        init();
    }

    public void init() {
        attributeApiRequest(Request.Method.GET, Flags.URL.ITEM_GET_ATTRIBUTE_VALUE, SearchResponse.class);
        mResultReceiver = new AddressResultReceiver(new Handler());
        startIntentService();
    }

    public static final int REQUEST_CODE_PAYMENT = 1;

    public void makePayment(Map<Integer, String> fieldData) {
        try {
            PayPalPayment payment = new PayPalPayment(new BigDecimal(Flags.WMCW_AMOUNT), CURRENCY,
                    getResources().getString(R.string.wmcw_paypal_order_msg), PayPalPayment.PAYMENT_INTENT_SALE);
            //payment.custom(getRequestParam(fieldData, "", false).toString());
            String customData = adapterWMCW.prepareConfirm(fieldData, ",");
            payment.custom(customData.length() < 256 ? customData : "");
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
            startActivityForResult(intent, REQUEST_CODE_PAYMENT);
        }
        catch(Exception e) {
            Log.e(Flags.ActivityTag.WMCW, e.getMessage());
        }
        //fieldData.get(FIELD_MAKE) + " " + fieldData.get(FIELD_MODEL)
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        /*System.out.println(confirm.toJSONObject().toString(4));
                        System.out.println(confirm.getPayment().toJSONObject()
                                .toString(4));*/

                        /*System.out.println("Result: " + confirm.toJSONObject().toString());

                        System.out.println("transaction id: "+confirm.getProofOfPayment().getTransactionId());
                        System.out.println("payment_id: "+ confirm.getProofOfPayment().getPaymentId());*/

                        formContainer.setVisibility(View.GONE);

                        Map<String, String> requestData = getRequestParam(mFieldData,
                                confirm.getProofOfPayment().getPaymentId(), true);

                        inflateResultView(confirm);
                        wmcwRequest(Request.Method.POST, Flags.URL.WMCW, requestData, confirm, Default.class);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("WMCW", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("WMCW", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("FuturePaymentExample", "The user canceled.");
        } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("FuturePaymentExample",
                    "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
        }
    }

    @Override
    public void onDestroy() {
        // Stop service when done
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    public String pData;
    private void attributeApiRequest(final int methodType, final String url, final Class resClass) {
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("domain_id", "1");

            Log.d(Flags.ActivityTag.WMCW, "attrib url: " + url);
            Client jsObjRequest = new Client(methodType, url, resClass, params, new OnCallListener() {
                @Override
                public void nwResponseData(String data) {
                    pData = data;
                }
            }, new Response.Listener<Object>() {
                @Override
                public void onResponse(Object response) {
                    SearchResponse res = (SearchResponse) response;
                    Log.d(Flags.ActivityTag.WMCW, "status: " + res.getStatus());
                    ResponseCode resCode = ResponseCode.fromValue(res.getStatus());
                    hideLoading();
                    switch (resCode) {
                        case STATUS_SUCCESS: {
                            try {
                                String json = new Gson().toJson(res);
                                Log.d(Flags.ActivityTag.WMCW, "json: " + json);
                                JsonObject jObj = new JsonParser().parse(pData).getAsJsonObject();
                                if(jObj.has("attribute_label")) {
                                    if(jObj.has(Flags.Keys.ATTRIBUTE_VALUE)) {
                                        JsonObject o = (JsonObject) jObj.get(Flags.Keys.ATTRIBUTE_VALUE);
                                        listData.put(Flags.Keys.MAKE, setFormValues(o, res.getAttribute_label().getMake().getKey_name()));
                                        listData.put(Flags.Keys.TRANSMISSION, setFormValues(o, res.getAttribute_label().getTransmission().getKey_name()));
                                        listData.put(Flags.Keys.COUNTRY, prepareCountryData());
                                        adapterWMCW.setListData(listData);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                        case STATUS_FAILURE: {
                            Log.d(Flags.ActivityTag.WMCW, "status 0");
                            break;
                        }
                        default: {
                            Log.d(Flags.ActivityTag.WMCW, "default");
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(Flags.ActivityTag.WMCW, "Error: " + error);
                    hideLoading();
                }
            });
            String tag_json_obj = "json_obj_req";
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsObjRequest, tag_json_obj);
        }
        catch(Exception e) {
            Log.e(Flags.ActivityTag.WMCW, e.getMessage());
        }
    }

    public AlertDialog dialog;
    public AlertDialog.Builder builder;
    public List<AttrValSpinnerModel> setFormValues(JsonObject attribData, String keyName) {
        JsonArray jarr = new JsonArray();
        List<AttrValSpinnerModel> list = new ArrayList<AttrValSpinnerModel>();
        if(attribData.get(keyName) != null && attribData.get(keyName).isJsonObject()) {
            JsonObject jo = (JsonObject) attribData.get(keyName);
            Set<Map.Entry<String, JsonElement>> entrySet = jo.entrySet();
            for(Map.Entry<String,JsonElement> entry : entrySet){
                jarr.add(jo.get(entry.getKey()));
            }
            list = (List<AttrValSpinnerModel>)new Gson().fromJson(jarr,
                    new TypeToken<List<AttrValSpinnerModel>>(){}.getType());
        }
        return list;
    }

    protected Location mLastLocation;
    private AddressResultReceiver mResultReceiver;

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if(isCountryAllowed(resultData.getString(FetchAddress.COUNTRY_CODE))) {
                adapterWMCW.fillLocationData(resultData.getString(FetchAddress.CITY),
                        resultData.getString(FetchAddress.STATE),
                        resultData.getString(FetchAddress.COUNTRY),
                        resultData.getString(FetchAddress.COUNTRY_CODE));
                adapterWMCW.notifyDataSetChanged();
            }
        }
    }

    /*private String paypalCustomField(Map<Integer, String> fieldData) {
        StringBuilder sb = new StringBuilder();
        String str = fieldData.values().toString();
        if(str.length() < 256) {
            return str;
        }
        else {
            if(fieldData.containsKey(FIELD_LNAME)) fieldData.remove(FIELD_LNAME);
            if(fieldData.containsKey(FIELD_STATE)) fieldData.remove(FIELD_STATE);
            if(fieldData.containsKey(FIELD_FNAME)) fieldData.remove(FIELD_FNAME);
            if(fieldData.containsKey(KMS_UNIT)) fieldData.remove(KMS_UNIT);
            return fieldData.values().toString();
        }
    }*/

    private Map<String, String> getRequestParam(Map<Integer, String> fieldData, String transactionID, boolean isCompleteDataRequired) {
        Map<String, String> request = new HashMap<>();
        request.put(Flags.WMCWRequestParams.CAR_MAKE, fieldData.get(FIELD_MAKE));
        request.put(Flags.WMCWRequestParams.CAR_MODEL, fieldData.get(FIELD_MODEL));
        request.put(Flags.WMCWRequestParams.CAR_YEAR, fieldData.get(FIELD_YEAR));
        request.put(Flags.WMCWRequestParams.PERSON_EMAIL, fieldData.get(FIELD_EMAIL));
        request.put(Flags.WMCWRequestParams.CAR_TRANSMISSION, fieldData.get(FIELD_TRANSMISSION));

        if(isCompleteDataRequired) {
            request.put(Flags.WMCWRequestParams.CAR_KILOMETERS, fieldData.get(FIELD_MILEAGE));
            request.put(Flags.WMCWRequestParams.CAR_KMS_TYPE, fieldData.get(KMS_UNIT));

            request.put(Flags.WMCWRequestParams.PERSON_CITY, fieldData.get(FIELD_CITY));
            request.put(Flags.WMCWRequestParams.PERSON_STATE, fieldData.get(FIELD_STATE));
            request.put(Flags.WMCWRequestParams.PERSON_COUNTRY, fieldData.get(FIELD_COUNTRY));
            request.put(Flags.WMCWRequestParams.PERSON_FNAME, fieldData.get(FIELD_FNAME));
            request.put(Flags.WMCWRequestParams.PERSON_LNAME, fieldData.get(FIELD_LNAME));
            if(!transactionID.isEmpty())
                request.put(Flags.WMCWRequestParams.PERSON_TXN_ID, transactionID);
        }

        return request;
    }

    private void hideLoading() {
        frameAnimation.stop();
        progressBarLoading.setVisibility(View.GONE);
    }

    private void inflateResultView(PaymentConfirmation paymentConfirmation) {
        try {
            successLayout.setVisibility(View.VISIBLE);
            TextView tvPayId, tvCreateDate;
            tvPayId = (TextView) findViewById(R.id.tv_wmcw_result_txn_id);
            tvCreateDate = (TextView) findViewById(R.id.tv_wmcw_result_create_date);

            tvPayId.setText(tvPayId.getText() + paymentConfirmation.getProofOfPayment().getPaymentId());
            tvCreateDate.setText(tvCreateDate.getText() + paymentConfirmation.getProofOfPayment().getCreateTime());

            /*tvPayId.setText(tvPayId.getText() + "xxxxxxxxxxx");
            tvCreateDate.setText(tvCreateDate.getText() + "xxxx-xxx-xxxx");*/

        }
        catch (Exception e) {
            Log.e(Flags.ActivityTag.WMCW, e.getMessage());
        }
    }

    private void wmcwRequest(final int methodType, final String url, final Map<String, String> requestData,
                             final PaymentConfirmation paymentDetails, final Class resClass) {
        try {
            Log.d(Flags.ActivityTag.WMCW, "attrib url: " + url);
            displayThankyou(null);
            /*Client jsObjRequest = new Client(methodType, url, resClass, requestData, new OnCallListener() {
                @Override
                public void nwResponseData(String data) {}
            }, new Response.Listener<Object>() {
                @Override
                public void onResponse(Object response) {
                    Default res = (Default) response;
                    Log.d(Flags.ActivityTag.WMCW, "status: " + res.getStatus());

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(Flags.ActivityTag.WMCW, "Error: " + error);
                }
            });
            String tag_json_obj = "json_obj_req";
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsObjRequest, tag_json_obj);*/
        }
        catch(Exception e) {
            Log.e(Flags.ActivityTag.WMCW, e.getMessage());
        }
    }

    protected void startIntentService() {
        Intent intent = new Intent(this, FetchAddress.class);
        intent.putExtra(FetchAddress.RECEIVER, new AddressResultReceiver(new Handler()));
        intent.putExtra(FetchAddress.LOCATION_DATA_EXTRA, mLastLocation);
        startService(intent);
    }

    public interface AdapterCallbacks {
        void initiatePayment(Map<Integer, String> fieldData);
        void displayError(int errorCode);
    }

    public boolean isCountryAllowed(String countryCode) {
        for(String itm : getResources().getStringArray(R.array.allowed_countries)) {
            if(itm.equals(countryCode))
                return true;
        }
        return false;
    }

    public List<AttrValSpinnerModel> prepareCountryData() {
        List<AttrValSpinnerModel> list = new ArrayList<>();
        for(final String item : getResources().getStringArray(R.array.allowed_countries)) {
            list.add(new AttrValSpinnerModel(){{
                setValue(item);
                setAttrbuteKey(Flags.Keys.COUNTRY);
            }});
        }
        return list;
    }

    public void displayThankyou(View v) {
        try {
            successLayout.setVisibility(View.GONE);
            formContainer.setVisibility(View.VISIBLE);
            adapterWMCW.displayThankyou();
            rvWMCW.scrollToPosition(0);
        }
        catch(Exception e) {
            Log.e(Flags.ActivityTag.WMCW, e.getMessage());
        }
    }
}