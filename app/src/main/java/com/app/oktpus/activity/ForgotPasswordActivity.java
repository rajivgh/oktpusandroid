package com.app.oktpus.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import com.app.oktpus.constants.Flags;
import com.app.oktpus.constants.ResponseCode;
import com.app.oktpus.controller.AppController;
import com.app.oktpus.R;
import com.app.oktpus.responseModel.ResponseLogin;
import com.app.oktpus.utils.Client;
import com.app.oktpus.utils.NetworkErrors;
import com.app.oktpus.utils.Utility;
import com.app.oktpus.utils.Validations;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by Gyandeep on 26/12/16.
 */

public class ForgotPasswordActivity extends AppCompatActivity {
    private AppCompatEditText emailField;
    private RelativeLayout actionSend, bgImg, mainLayout;
    private ImageButton closeButton;
    private TextView responseMsg;
    private boolean mAuthTask = false;
    private TextInputLayout tilEmail;
    public AlertDialog.Builder builder;
    public AlertDialog dialog;
    public View convertView;
    public LayoutInflater layoutInflater;
    private Tracker mTracker;
    private Client requestClient = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        mTracker = AppController.getInstance().getDefaultTracker();
        mTracker.setScreenName("Forgot Password");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        mainLayout = (RelativeLayout) findViewById(R.id.forgot_pwd_layout);
        //title = (TextView) findViewById(R.id.tv_not_logged_in_title);
        emailField = (AppCompatEditText) findViewById(R.id.email_forgot_pwd);
        closeButton = (ImageButton) findViewById(R.id.btn_close);
        actionSend = (RelativeLayout) findViewById(R.id.action_send_forgot_pwd);
        //emailErrorMsg = (TextView) findViewById(R.id.email_error_msg);
        responseMsg = (TextView) findViewById(R.id.res_msg);
        bgImg = (RelativeLayout) findViewById(R.id.bg_img);
        bgImg.setAlpha(Flags.bgOpacity);
        tilEmail = (TextInputLayout) findViewById(R.id.til_email);

        closeButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        actionSend.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuthTask = true;
                attemptSendRequest();
            }
        });

        builder = new AlertDialog.Builder(this);
        convertView = null;
        layoutInflater = LayoutInflater.from(this);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        emailField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_SEND) {
                    mAuthTask = true;
                    attemptSendRequest();
                    return true;
                }
                return false;
            }
        });

    }

    private void attemptSendRequest(){
        if (!mAuthTask)
            return;

        responseMsg.setVisibility(View.GONE);
        String email = emailField.getText().toString();
        boolean cancel = false, visibleEmailErr = false;

        if (TextUtils.isEmpty(email)) {
            emailField.setError(getString(R.string.error_field_required));
            cancel = true;
        } else if (!Validations.isEmailValid(email) || !Validations.isEmailValid2(email)) {
            emailField.setError(getString(R.string.error_invalid_email));
            cancel = true;
        }

        if(cancel) {
            mAuthTask = false;
        }
        else {
            actionSend.setEnabled(false);
            tilEmail.setErrorEnabled(false);
            Utility.hideKeyboard(getApplicationContext(), emailField);
            requestForgotPwdApi(email);
        }
    }

    public void actionLogin(View v) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    public void actionSignup(View v) {
        startActivity(new Intent(getApplicationContext(), SignupActivity.class));
    }


    private void requestForgotPwdApi(final String email) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(Flags.Keys.USR_EMAIL, email);
        requestClient = new Client(Request.Method.POST, Flags.URL.FORGOT_PWD, ResponseLogin.class, params, new Response.Listener<Object>() {
            @Override
            public void onResponse(Object response) {
                ResponseLogin loginRes = (ResponseLogin)response;
                ResponseCode resCode = ResponseCode.fromValue(loginRes.getStatus());
                switch(resCode) {
                    case STATUS_SUCCESS: {
                        /*Snackbar snackbar = Snackbar.make(mainLayout, "", Snackbar.LENGTH_LONG);

                        snackbar.setCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                super.onDismissed(snackbar, event);
                                emailField.setEnabled(true);
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            }
                        });
                        snackbar.setActionTextColor(getResources().getColor(R.color.successRes));
                        snackbar.show();*/
                        actionSend.setEnabled(true);
                        convertView = layoutInflater.inflate(R.layout.reset_pwd_dialog, null);

                        RelativeLayout btnOk = (RelativeLayout) convertView.findViewById(R.id.btn_ok);
                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                finish();
                            }
                        });

                        ImageButton ib = (ImageButton) convertView.findViewById(R.id.btn_close);
                        ib.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                finish();
                            }
                        });

                        TextView tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                        tvContent.append(" " + email);

                        builder.setView(convertView);
                        dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();

                        /*responseMsg.setText("An email has been sent");
                        responseMsg.setTextColor(getResources().getColor(R.color.successRes));*/
                        break;
                    }
                    case STATUS_FAILURE: {
                        Log.d("LoginFragment", "status 0");
                        responseMsg.setText(getApplicationContext().getResources().getString(R.string.forgot_pwd_err_no_acc_linked));
                        responseMsg.setVisibility(View.VISIBLE);
                        actionSend.setEnabled(true);
                        break;
                    }
                    default : {
                        Log.d("LoginFragment", "default");
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("LoginFragment", "Error: " + error);
                actionSend.setEnabled(true);
                Utility.showErrorSnackbar(getApplicationContext(), mainLayout, requestClient, error, Flags.NetworkRequestTags.FORGOT_PASSWORD);
            }
        });
        AppController.getInstance().addToRequestQueue(requestClient, Flags.NetworkRequestTags.FORGOT_PASSWORD);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            AppController.getInstance().cancelPendingRequests(Flags.NetworkRequestTags.FORGOT_PASSWORD);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}