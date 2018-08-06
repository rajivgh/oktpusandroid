package com.app.oktpus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.app.oktpus.utils.Utility;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.app.oktpus.model.Default;
import com.app.oktpus.model.SignupResponse;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.constants.ResponseCode;
import com.app.oktpus.controller.AppController;
import com.app.oktpus.R;
import com.app.oktpus.responseModel.ResponseLogin;
import com.app.oktpus.utils.Client;
import com.app.oktpus.utils.SessionManagement;
import com.app.oktpus.utils.Validations;

/**
 * Created by Gyandeep on 26/12/16.
 */

public class SignupActivity extends AppCompatActivity{

    private TextInputLayout tilEmail, tilPwd;
    private AppCompatEditText mEmailView, mPasswordView;
    private ProgressBar progressBar;
    private SessionManagement mSession;
    private boolean mAuthTask = false;
    private RelativeLayout mSignUpButton;
    private ImageButton mCloseButton;
    private TextView tvLoginLink, responseMsg;
    public static final String TAG  = "Signup";
    private RelativeLayout bgImg, mainLayout;
    private Tracker mTracker;
    boolean show = false;
    private ImageView ivShow;
    private Client clientSignup, clientLogin;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mTracker = AppController.getInstance().getDefaultTracker();
        mTracker.setScreenName("Sign up");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        mSession = new SessionManagement(this);
        mCloseButton = (ImageButton) findViewById(R.id.btn_close);
        //mTitle = (TextView) findViewById(R.id.tv_not_logged_in_title);
        mEmailView = (AppCompatEditText) findViewById(R.id.email_signup);
        mPasswordView = (AppCompatEditText) findViewById(R.id.password_signup);
        tilEmail = (TextInputLayout) findViewById(R.id.til_email);
        tilPwd = (TextInputLayout) findViewById(R.id.til_pwd);
        tvLoginLink = (TextView) findViewById(R.id.login_link);
        //scrollContainer = (ScrollView) findViewById(R.id.scroll_container);
        //mPasswordView2 = (AppCompatEditText) findViewById(R.id.password2_signup);
        mSignUpButton = (RelativeLayout) findViewById(R.id.sign_up_button);
        progressBar = (ProgressBar) findViewById(R.id.pb_search);
        //mEmailErrorMsg = (TextView) findViewById(R.id.email_error_msg);
        /*mPasswordErrorMsg = (TextView) findViewById(R.id.pwd_error_msg);
        */

        responseMsg = (TextView) findViewById(R.id.res_msg);
        bgImg = (RelativeLayout) findViewById(R.id.bg_img);
        mainLayout = (RelativeLayout) findViewById(R.id.signup_layout);
        ivShow = (ImageView) findViewById(R.id.iv_show);
        mPasswordView.setTypeface(null);
        bgImg.setAlpha(Flags.bgOpacity);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        /*mTitle.setTypeface(AppController.getFontType(getApplicationContext()));
        mTitle.setText(getResources().getString(R.string.action_sign_up));*/
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuthTask = true;
                attemptSignup();
            }
        });

        mCloseButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_SEND) {
                    mAuthTask = true;
                    attemptSignup();
                    Utility.hideKeyboard(getApplicationContext(), textView);
                    return true;
                }
                return false;
            }
        });
    }

    /*public void actionBackToTop(View view) {
        scrollContainer.smoothScrollTo(0,0);
    }*/

    public void hideShowPassword(View v) {
        if(!show) {
            show = true;
            ivShow.setBackground(getResources().getDrawable(R.drawable.ic_tick_blue));
            //mPasswordView.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mPasswordView.setInputType(InputType.TYPE_CLASS_TEXT);
            mPasswordView.setSelection(mPasswordView.getText().length());
            mPasswordView.setTypeface(null);
        }
        else {
            show = false;
            ivShow.setBackground(getResources().getDrawable(R.drawable.ic_show));
            //mPasswordView.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mPasswordView.setSelection(mPasswordView.getText().length());
            mPasswordView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mPasswordView.setTypeface(null);
        }
    }

    public void attemptSignup() {
        if (!mAuthTask) {
            return;
        }
        responseMsg.setVisibility(View.GONE);
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        //String password2 = mPasswordView2.getText().toString();
        //mEmailErrorMsg.setVisibility(View.INVISIBLE);
        //mPasswordErrorMsg.setVisibility(View.INVISIBLE);

        //Log.d(TAG, "pass 1: "+password + ", pass 2: " + password2);
        boolean cancel = false, visibleEmailErr = false, visiblePwdErr = false;
        //View focusView = null;

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            mEmailView.requestFocus();
            //focusView = mEmailView;
            //mEmailErrorMsg.setText(getString(R.string.error_field_required));
            cancel = true;
        } else if (!Validations.isEmailValid(email) || !Validations.isEmailValid2(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            mEmailView.requestFocus();
            //focusView = mEmailView;
            //mEmailErrorMsg.setText(getString(R.string.error_invalid_email));
            cancel = true;
        }

        //Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            mPasswordView.requestFocus();
            //focusView = mPasswordView;
            //mPasswordErrorMsg.setText(getString(R.string.error_field_required));
            cancel = true;
        } else if(!Validations.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.invalid_pwd));
            mPasswordView.requestFocus();
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            //focusView.requestFocus();
            //if(visiblePwdErr) mPasswordErrorMsg.setVisibility(View.VISIBLE);
            //if(visibleEmailErr) mEmailErrorMsg.setVisibility(View.VISIBLE);
            mAuthTask = false;
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            tilEmail.setErrorEnabled(false);
            tilPwd.setErrorEnabled(false);
            mSignUpButton.setEnabled(false);
            Utility.hideKeyboard(getApplicationContext(), mEmailView);
            signupRequest(email, password, password);
            //signupRequestDummy(email, password, password);
        }
    }

    /*public void signupRequestDummy(final String email, final String pass, final String pass2) {
        mSession.createLoginSession(email, 00001, false);
        Snackbar snackbar = Snackbar.make(mainLayout, "Signed up succcessfully! Logging in..", Snackbar.LENGTH_LONG);

        snackbar.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);
                onFinish();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.successRes));
        snackbar.show();
    }*/

    public void signupRequest(final String email, final String pass, final String pass2) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(Flags.Keys.USR_EMAIL, email);
        params.put(Flags.Keys.USR_PASSWORD, pass);
        params.put(Flags.Keys.USR_PASSWORD2, pass2);

        clientSignup = new Client(Request.Method.POST, Flags.URL.API_USER, SignupResponse.class, params, new Response.Listener<Object>() {
            @Override
            public void onResponse(Object response) {
                SignupResponse lRes = (SignupResponse)response;
                //System.out.println("Response found: "+ lRes.getStatus() + ", " + lRes.getCode());

                ResponseCode resCode = ResponseCode.fromValue(lRes.getStatus());
                switch(resCode) {
                    case STATUS_SUCCESS: {
                        loginRequest(email, pass);
                        break;
                    }
                    case STATUS_FAILURE: {
                        Log.d("Signup", "status 0");
                        responseMsg.setText("Email " + email + " already exists");
                        responseMsg.setVisibility(View.VISIBLE);
                        //responseMsg.setTextColor(getResources().getColor(R.color.errorTextMsg));
                        showProgress(false);
                        mSignUpButton.setEnabled(true);
                        break;
                    }
                    default : {
                        Log.d("Signup", "default");
                    }
                }
                mAuthTask = true;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error);
                showProgress(false);
                mSignUpButton.setEnabled(true);
                Utility.showErrorSnackbar(getApplicationContext(), mainLayout, clientSignup, error, Flags.NetworkRequestTags.SIGNUP);
            }
        });

        AppController.getInstance().addToRequestQueue(clientSignup, Flags.NetworkRequestTags.SIGNUP);
    }

    private void loginRequest(final String email, String password) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(Flags.Keys.USR_EMAIL, email);
        params.put(Flags.Keys.USR_PASSWORD, password);
        clientLogin = new Client(Request.Method.POST, Flags.URL.LOGIN, ResponseLogin.class, params, new Response.Listener<Object>() {
            @Override
            public void onResponse(Object response) {
                ResponseLogin loginRes = (ResponseLogin)response;
                //System.out.println("Response found: "+ loginRes.getStatus() + ", " + loginRes.getUserId());
                showProgress(false);
                mAuthTask = true;
                ResponseCode resCode = ResponseCode.fromValue(loginRes.getStatus());
                switch(resCode) {
                    case STATUS_SUCCESS: {
                        //String url = Flags.URL.GET_USER + loginRes.getUserId();
                        //getUserDetailsRequest(Request.Method.GET, url, ResponseLogin.class);
                        mSession.createLoginSession(email, loginRes.getUserId(), false);
                        Snackbar snackbar = Snackbar.make(mainLayout, getResources().getString(R.string.sign_up_success), Snackbar.LENGTH_SHORT);

                        snackbar.setCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                super.onDismissed(snackbar, event);
                                mSignUpButton.setEnabled(true);
                                onFinish();
                            }
                        });
                        snackbar.setActionTextColor(getResources().getColor(R.color.successRes));
                        snackbar.show();
                        break;
                    }
                    case STATUS_FAILURE: {
                        Log.d("LoginFragment", "status 0");
                        showProgress(false);
                        mSignUpButton.setEnabled(true);
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
                Log.d(Flags.ActivityTag.LOGIN_ACTIVITY, "Error: " + error);
                showProgress(false);
                mSignUpButton.setEnabled(true);
                Utility.showErrorSnackbar(getApplicationContext(), mainLayout, clientLogin, error, Flags.NetworkRequestTags.LOGIN);
            }
        })
        {   @Override
        protected Response<String> parseNetworkResponse(NetworkResponse response) {
            try {
                String json = new String( response.data, HttpHeaderParser.parseCharset(response.headers));
                Default def = new Gson().fromJson(json, Default.class);
                if (ResponseCode.STATUS_SUCCESS.getValue() == Integer.parseInt(def.getStatus())) {
                    AppController.getInstance().checkSessionCookie(response.headers);
                }

            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JsonSyntaxException e) {
                return Response.error(new ParseError(e));
            }
            return super.parseNetworkResponse(response);
        }
        };
        AppController.getInstance().addToRequestQueue(clientLogin, Flags.NetworkRequestTags.LOGIN);
    }

    private void onFinish() {
        try {
            Intent i = new Intent(getApplicationContext(), SearchActivity.class);
            i.putExtra(Flags.Bundle.Keys.SOURCE_PAGE, Flags.Bundle.Values.NEW_SEARCH);
            i.putExtra(Flags.Bundle.Keys.TO_SEARCH_FORM, true);
            startActivity(i);
            finishAffinity();
        }
        catch(Exception e) {
            Log.d(Flags.ActivityTag.LOGIN_ACTIVITY, e.getMessage());
        }
    }

    private void showProgress(boolean show) {
        if(show)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
    }
}