package com.app.oktpus.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.app.oktpus.R;
import com.app.oktpus.constants.Helper;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.math.BigDecimal;


/**
 * Created by remees on 6/7/18.
 */

public class CheckoutActivity extends AppCompatActivity {


    ProgressDialog pDialog;
    Button btn_placeorder;

    private static final String CURRENCY = "USD";
    //private static final String CONFIG_CLIENT = "AQnQTCwskbXrVBt5kJK9IwOlqwkpH-6Rrj8A7-f_dmAqazUT6zs_j9PToNXsoPmbCTthv02dC4tuzGCL";

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(Helper.paypal_config_env)
            .clientId(Helper.paypal_client_id);
    // The following are only used in PayPalFuturePaymentActivity.
    //.merchantName("Example Merchant");
    //.merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
    //.merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

    public static final int REQUEST_CODE_PAYMENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checout);

        pDialog = new ProgressDialog(CheckoutActivity.this);
        pDialog.setMessage("Loading...");
        Log.d("oncreate view", "parts oncreate veiw called ");
        btn_placeorder = (Button) findViewById(R.id.btn_placeorder);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        btn_placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePayment();
            }
        });
    }

    public void makePayment() {
        try {
            PayPalPayment payment = new PayPalPayment(new BigDecimal(5.00), CURRENCY,
                    getResources().getString(R.string.wmcw_paypal_order_msg), PayPalPayment.PAYMENT_INTENT_SALE);
            //payment.custom(getRequestParam(fieldData, "", false).toString());
//            String customData = adapterWMCW.prepareConfirm(fieldData, ",");
//            payment.custom(customData.length() < 256 ? customData : "");
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
            startActivityForResult(intent, REQUEST_CODE_PAYMENT);
        }
        catch(Exception e) {
            Log.e("error", e.getMessage());
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


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("checkout", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("checkout", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("FuturePaymentExample", "The user canceled.");
        } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("FuturePaymentExample",
                    "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
        }
    }





}
