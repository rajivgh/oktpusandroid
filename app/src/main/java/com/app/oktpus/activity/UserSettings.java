package com.app.oktpus.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import com.app.oktpus.model.SignupResponse;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.constants.ResponseCode;
import com.app.oktpus.controller.AppController;
import com.app.oktpus.R;
import com.app.oktpus.utils.Client;
import com.app.oktpus.utils.SessionManagement;
import com.app.oktpus.utils.Utility;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class UserSettings extends BaseActivity {
    private AppCompatEditText newPassword, newPasswordConfirm;
    private TextView txtmailid,txtheading,txt_label_changepassword,
            emailError,pwdError,pwdError2, responseMsg;//, footerAbout, footerContact, footerPrivacy, footerTerms;
    private RelativeLayout btnUpdateUser;
    private SessionManagement mSession;
    private String newPwd, newPwdConfirm, newEmail;
    private ImageView backButton;
    private Tracker mTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSession = new SessionManagement(this);
        includeNavigationMenu(this, R.layout.activity_user_settings);
        txtmailid = (TextView) findViewById(R.id.txtmailid);
        emailError = (TextView) findViewById(R.id.email_error_msg);
        pwdError = (TextView) findViewById(R.id.pwd_error_msg);
        pwdError2 = (TextView) findViewById(R.id.pwd_error_msg2);
        backButton = (ImageView) findViewById(R.id.ss_back_arrow);
        responseMsg = (TextView) findViewById(R.id.response_msg);
        /*footerAbout = (TextView) findViewById(R.id.txt_footer_about);
        footerContact = (TextView) findViewById(R.id.txt_footer_contact);
        footerPrivacy = (TextView) findViewById(R.id.txt_footer_privacy);
        footerTerms = (TextView) findViewById(R.id.txt_footer_terms);*/

        mTracker = AppController.getInstance().getDefaultTracker();
        mTracker.setScreenName("User Settings");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        pwdError2.setVisibility(View.GONE);
        pwdError.setVisibility(View.GONE);
        emailError.setVisibility(View.GONE);
        txtmailid.setText(mSession.getUsername());

        newPassword = (AppCompatEditText) findViewById(R.id.new_pwd);
        newPasswordConfirm = (AppCompatEditText) findViewById(R.id.new_pwd_confirm);
        //msg_proper_pass = (TextView) findViewById(R.id.msg_proper_pass);

        Typeface fontRobotoLight = AppController.getFontType(getApplicationContext());
        newPassword.setTypeface(fontRobotoLight);
        newPasswordConfirm.setTypeface(fontRobotoLight);
        //msg_proper_pass.setTypeface(fontRobotoLight);
        txtmailid.setTypeface(fontRobotoLight);

        txtheading = (TextView) findViewById(R.id.page_title);
        txt_label_changepassword= (TextView) findViewById(R.id.txt_label_changepassword);
        txtheading.setText(getResources().getString(R.string.nav_item_settings));
        txtheading.setTypeface(AppController.getFontType(getApplicationContext()));
        txt_label_changepassword.setTypeface(AppController.getFontType(getApplicationContext()));

        btnUpdateUser = (RelativeLayout) findViewById(R.id.btn_update_user);
        //btnUpdateUser.setTypeface(fontRobotoLight);

        btnUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptUpdate();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void attemptUpdate() {
        int count=0;
        newPwd = String.valueOf(newPassword.getText());
        newPwdConfirm = String.valueOf(newPasswordConfirm.getText());
        newEmail = mSession.getUsername();

        responseMsg.setText("");
        /*if(confirm_emailid.getText().toString().equals("")) {
            emailError.setText("This field is required, with correct email id");
            emailError.setVisibility(View.VISIBLE);
        }else{
            emailError.setVisibility(View.GONE);
        }*/

        if(newPwd.equals("")) {
            count++;
            pwdError.setText("This field is required");
            pwdError.setVisibility(View.VISIBLE);
        }else{
            pwdError.setVisibility(View.GONE);
        }

        if(newPwdConfirm.equals("")) {
            count++;
            pwdError2.setText("This field is required");
            pwdError2.setVisibility(View.VISIBLE);
        }else{
            pwdError2.setVisibility(View.GONE);
        }

        if(count == 0){
            if(newPwd.equals(newPwdConfirm) && newPwd.length() > 5) {
                Utility.hideKeyboard(getApplicationContext(), pwdError2);
                makeRequest(newEmail, Flags.URL.UPDATE_USER+mSession.getUserID(),newPwd, newPwdConfirm);
            }else{
                if(!newPwd.equals(newPwdConfirm)) {
                    pwdError.setText("Passwords not identical");
                }
                else {
                    pwdError.setText("Password must be 6 characters minimum");
                }
                pwdError.setVisibility(View.VISIBLE);
            }
        }
    }

    public void makeRequest(final String email, final String url, final String pass, final String pass2) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(Flags.Keys.NEW_EMAIL, email);
        params.put(Flags.Keys.NEW_PASSWORD, pass);
        params.put(Flags.Keys.NEW_PASSWORD2, pass2);
        Log.d(Flags.ActivityTag.USER_SETTINGS, "url: "+ url);

        Client jsObjRequest = new Client(Request.Method.POST, url, SignupResponse.class, params, new Response.Listener<Object>() {
            @Override
            public void onResponse(Object response) {
                SignupResponse lRes = (SignupResponse)response;
                //System.out.println("Response found: "+ lRes.getStatus() + ", " + lRes.getCode());

                ResponseCode resCode = ResponseCode.fromValue(lRes.getStatus());
                switch(resCode) {
                    case STATUS_SUCCESS: {
                        responseMsg.setText("Settings successfully updated. Please allow few minutes for the changes to take effect.");
                        responseMsg.setTextColor(getResources().getColor(R.color.successRes));
                        break;
                    }
                    case STATUS_FAILURE: {
                        Log.d("UserSettingsActivity", "status 0");
                        responseMsg.setText("Failed to update");
                        responseMsg.setTextColor(getResources().getColor(R.color.errorTextMsg));
                        break;
                    }
                    default : {
                        Log.d("UserSettingsActivity", "default");
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("UserSettings", "Error: " + error);
            }
        });
        String tag_json_obj = "json_obj_req";
        AppController.getInstance().addToRequestQueue(jsObjRequest, tag_json_obj);
    }



}