package com.app.oktpus.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.app.oktpus.model.Default;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.constants.LoginResponseCode;
import com.app.oktpus.constants.LoginType;
import com.app.oktpus.constants.ResponseCode;
import com.app.oktpus.controller.AppController;
import com.app.oktpus.R;
import com.app.oktpus.responseModel.ResponseLogin;
import com.app.oktpus.utils.Client;
import com.app.oktpus.utils.SessionManagement;
import com.app.oktpus.utils.Utility;
import com.app.oktpus.utils.Validations;
import com.app.oktpus.utils.WallOfDeals;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener{
    private TextInputLayout tilEmail, tilPwd;
    private AppCompatEditText mEmailView, mPasswordView;
    private ImageButton btnClose;
    private ProgressBar mProgressView;
    private SessionManagement mSession;
    private boolean mAuthTask = false;
    private RelativeLayout mEmailSignInButton;
    private TextView mForgotPasswordLink, mSignupLink, tvErr;
    private RelativeLayout bgImg;
    //Google Plus
    private SignInButton btnSignIn;
    private static final int RC_SIGN_IN = 007;
    private GoogleApiClient mGoogleApiClient;

    //Facebook
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private boolean isSocialLogin = false;
    private Tracker mTracker;
    private Client requestClient;

    //Facebook login callback
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Profile profile = Profile.getCurrentProfile();
            nextActivity(profile);
        }
        @Override
        public void onCancel() {}
        @Override
        public void onError(FacebookException e) {}
    };

    private RelativeLayout loginLayout;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Facebook SDK initialisation
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        mTracker = AppController.getInstance().getDefaultTracker();
        mTracker.setScreenName("Log in");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        mSession = new SessionManagement(this);
        loginLayout = (RelativeLayout) findViewById(R.id.login_layout);
        mEmailView = (AppCompatEditText) findViewById(R.id.email);
        mPasswordView = (AppCompatEditText) findViewById(R.id.password);
        mEmailSignInButton = (RelativeLayout) findViewById(R.id.email_sign_in_button);
        tvErr = (TextView) findViewById(R.id.err_msg);
        mProgressView = (ProgressBar) findViewById(R.id.pb_search);
        btnClose = (ImageButton)findViewById(R.id.btn_close);
        mForgotPasswordLink = (TextView) findViewById(R.id.forgot_pwd_link);
        mSignupLink = (TextView) findViewById(R.id.signup_link);
        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);      //Google plus
        bgImg = (RelativeLayout) findViewById(R.id.bg_img);

        tilEmail = (TextInputLayout) findViewById(R.id.til_email);
        tilPwd = (TextInputLayout) findViewById(R.id.til_pwd);

        bgImg.setAlpha(Flags.bgOpacity);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_ACTION_SEND) {
                    mAuthTask = true;
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mPasswordView.setTypeface(null);

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuthTask = true;
                attemptLogin();
            }
        });

        btnClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            /*if(isFromLogout)
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            else*/
                finish();
            }
        });

        mForgotPasswordLink.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), ForgotPasswordActivity.class), 3);
                finish();
                //overridePendingTransition(0, 0);
                //LoginActivity.this.overridePendingTransition(R.anim.frag_slide_in_from_bottom,R.anim.frag_slide_in_from_bottom);
            }
        });

        mSignupLink.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignupActivity.class));
                finish();
            }
        });

        //Facebook
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {}
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                nextActivity(newProfile);
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        final LoginButton loginButton = (LoginButton)findViewById(R.id.fb_login_button);
        callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final AccessToken accessToken = loginResult.getAccessToken();
                Log.i("LoginActivity", "fb_app_id: "+getResources().getString(R.string.facebook_app_id)+" fb token: " + accessToken.getToken());
                GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("LoginActivity", response.toString());
                        // Get facebook data from login
                        Bundle bFacebookData = getFacebookData(object);
                        showProgress(true);
                        loginRequest(bFacebookData.get("email").toString(), "", true, LoginType.MODE_FB.getValue(), accessToken.getToken());
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d("LoginActivity", "Fb login cancelled");
            }

            @Override
            public void onError(FacebookException e) {
                e.printStackTrace();
            }
        };
        loginButton.setReadPermissions(Arrays.asList("email"));
        loginButton.registerCallback(callbackManager, callback);

        //Google plus
        btnSignIn.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestScopes(new Scope(Scopes.PLUS_LOGIN))
                //.requestScopes(new Scope(Scopes.PLUS_ME))
                .requestEmail()
                .requestEmail()
                .requestIdToken(getResources().getString(R.string.gplus_client_id)) //1094579423391-tniia5u3hvpduusse98ocln0ancim704.apps.googleusercontent.com
                //.requestServerAuthCode("797229729484-757h3jhjp96img2aa5ehtrh2ea02mhqh.apps.googleusercontent.com")
                .build();
        Log.i("LoginActivity", "gplus clientId: "+getResources().getString(R.string.gplus_client_id));
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Customizing G+ button
        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        btnSignIn.setScopes(gso.getScopeArray());
    }

    private Bundle getFacebookData(JSONObject object) {

        try {
            Bundle bundle = new Bundle();
            /*String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));*/
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            /*if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));*/

            return bundle;
        }
        catch(JSONException e) {
            Log.d("LoginActivity","Error parsing JSON");
        }
        return null;
    }

    private void attemptLogin() {
        if (!mAuthTask) {
            return;
        }
        tvErr.setVisibility(View.GONE);
        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false, visibleEmailErr = false, visiblePwdErr = false;

        // Check for a valid email address.
        if(TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            mEmailView.requestFocus();
            cancel = true;
        } else if (!Validations.isEmailValid(email) || !Validations.isEmailValid2(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            mEmailView.requestFocus();
            cancel = true;
        }

        if(TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            mPasswordView.requestFocus();
            cancel = true;
        }
        else if(!Validations.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.invalid_pwd));
            mPasswordView.requestFocus();
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            mAuthTask = false;
        } else {
            showProgress(true);
            tilEmail.setErrorEnabled(false);
            tilPwd.setErrorEnabled(false);
            mEmailSignInButton.setEnabled(false);
            Utility.hideKeyboard(getApplicationContext(), mPasswordView);
            Utility.hideKeyboard(getApplicationContext(), mEmailView);
            loginRequest(email, password, false, LoginType.MODE_STANDARD_FORM.getValue(), "");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void loginRequest(final String email, String password, final boolean isSocialLogin, final int loginType,
                              final String token) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(Flags.Keys.USR_EMAIL, email);
        params.put(Flags.Keys.LOGIN_MODE, String.valueOf(loginType));
        if(loginType == LoginType.MODE_STANDARD_FORM.getValue())
            params.put(Flags.Keys.USR_PASSWORD, password);
        else
            params.put(Flags.Keys.SOCIAL_LOGIN_TOKEN, token);
        System.out.println("request "+ params);
        requestClient = new Client(Request.Method.POST, Flags.URL.LOGIN, ResponseLogin.class, params, new Response.Listener<Object>() {
            @Override
            public void onResponse(Object response) {
                ResponseLogin loginRes = (ResponseLogin)response;
                Log.d(Flags.ActivityTag.LOGIN_ACTIVITY, "Response found: "+ loginRes.getStatus() + ", " + loginRes.getUserId());
                showProgress(false);
                mAuthTask = true;
                LoginResponseCode resCode = LoginResponseCode.fromValue(loginRes.getStatus());
                switch(resCode) {
                    case SUCCESS: {
                        mSession.createLoginSession(email, loginRes.getUserId(), isSocialLogin);
                        SpannableString ss = new SpannableString(getResources().getString(R.string.success_login));
                        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.successRes)), 0, ss.length(), 0);
                        Snackbar snackbar = Snackbar.make(loginLayout, ss, Snackbar.LENGTH_SHORT);

                        snackbar.setCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                super.onDismissed(snackbar, event);
                                mEmailSignInButton.setEnabled(true);
                                onFinish();
                            }
                        });
                        snackbar.show();

                        break;
                    }
                    case FAILURE: {
                        Log.d("LoginFragment", "status 0");
                        showProgress(false);
                        tvErr.setVisibility(View.VISIBLE);
                        mEmailSignInButton.setEnabled(true);
                        break;
                    }
                    case USER_NOT_FOUND: {
                        switch(LoginType.fromValue(loginType)) {
                            case MODE_FB:
                                break;
                            case MODE_GPLUS:
                                break;
                        }
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
                showProgress(false);
                mAuthTask = true;
                mEmailSignInButton.setEnabled(true);

                /*final Snackbar snackbarError = Snackbar.make(loginLayout, NetworkErrors.getErrorMessage(getApplicationContext(), error), Snackbar.LENGTH_INDEFINITE);
                snackbarError.setAction(getApplicationContext().getResources().getString(R.string.label_try_again), new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbarError.dismiss();
                        showProgress(true);
                        AppController.getInstance().addToRequestQueue(requestClient, Flags.NetworkRequestTags.LOGIN);
                    }
                });
                snackbarError.setActionTextColor(getResources().getColor(R.color.primaryBtnPressed));
                snackbarError.show();*/
                Utility.showErrorSnackbar(getApplicationContext(), loginLayout, requestClient, error, Flags.NetworkRequestTags.LOGIN);

                Log.d(Flags.ActivityTag.LOGIN_ACTIVITY, "Error: " + error);
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

        requestClient.setRetryPolicy(new DefaultRetryPolicy(50000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(requestClient, Flags.NetworkRequestTags.LOGIN);
    }



    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        if(show)
            mProgressView.setVisibility(View.VISIBLE);
        else
            mProgressView.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Facebook login
        Profile profile = Profile.getCurrentProfile();
        nextActivity(profile);
    }

    protected void onStop() {
        super.onStop();
        //Facebook login
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
        AppController.getInstance().cancelPendingRequests(Flags.NetworkRequestTags.LOGIN);

    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
            handleSignInResult(result);
        }
        else {
            //Facebook login
            callbackManager.onActivityResult(requestCode, responseCode, intent);
        }
    }


    private void nextActivity(Profile profile){
        if(profile != null){
            /*Intent main = new Intent(LoginActivity.this, MainActivity.class);
            main.putExtra("name", profile.getFirstName());
            main.putExtra("surname", profile.getLastName());
            main.putExtra("imageUrl", profile.getProfilePictureUri(200,200).toString());*/

            /*startActivity(main);*/
            /*showProgress(true);
            loginRequest("info.oktpus@gmail.com", "abcABC123");*/
        }
    }

    @Override
    public void onClick(View v) {
        signInGplus();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("LoginActivity", "handleSignInResult:" + result.getStatus());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.i(Flags.ActivityTag.LOGIN_ACTIVITY, "display name: " + acct.getDisplayName() + ", serverAuthCode: "+ acct.getServerAuthCode()
                    +", idToken: "+ acct.getIdToken());


            String personName = acct.getDisplayName();
            //String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();

            Log.e(Flags.ActivityTag.LOGIN_ACTIVITY, "Name: " + personName + ", email: " + email);

            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient);
            mGoogleApiClient = null;

            showProgress(true);
            loginRequest(email, "", true, LoginType.MODE_GPLUS.getValue(), acct.getIdToken());
            /*txtName.setText(personName);
            txtEmail.setText(email);
            Glide.with(getApplicationContext()).load(personPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfilePic);*/

            //updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
            Log.d(Flags.ActivityTag.LOGIN_ACTIVITY, "Signed out");
        }
    }

    private void signInGplus() {
        try {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void onFinish() {
        try {
            int flag;
            if(!mSession.isDemoChecked()) flag = Flags.Bundle.Values.REFINE_SEARCH;
            else flag = Flags.Bundle.Values.NEW_SEARCH;
            //LoginManager.getInstance().logOut();
            /*Intent i = new Intent(getApplicationContext(), SearchActivity.class);
            ////i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra(Flags.Bundle.Keys.SOURCE_PAGE, flag);
            i.putExtra(Flags.Bundle.Keys.TO_SEARCH_FORM, false);
            startActivity(i);*/
            WallOfDeals.setDeals(this);
            finishAffinity();
        }
        catch(Exception e) {
            Log.d(Flags.ActivityTag.LOGIN_ACTIVITY, e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoginManager.getInstance().logOut();
    }
}