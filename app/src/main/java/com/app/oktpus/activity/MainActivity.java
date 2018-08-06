package com.app.oktpus.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.oktpus.constants.Flags;
import com.app.oktpus.controller.AppController;
import com.app.oktpus.fragment.AppStartModal;
import com.app.oktpus.fragment.FragmentDrawer;
import com.app.oktpus.R;
import com.app.oktpus.utils.SessionManagement;
import com.app.oktpus.utils.Utility;
import com.app.oktpus.utils.WallOfDeals;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.Locale;

import static com.app.oktpus.utils.NavigationMenuItems.selectDrawerItem;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener{

    public static final String TAG  = "MainActivity";
    public ImageButton mTextSearch, mVoiceSearch;
    private boolean voiceSearchActive;
    private EditText mSearchField;
    private Button mRefineSearch;
    private Toolbar mToolbar;
    public FragmentDrawer drawerFragment;
    public DrawerLayout mDrawerLayout;
    private ActionBar actionBar;
    public SessionManagement mSession;
    public LinearLayout contentBody;
    private Tracker mTracker;
    private AppController application;
    private RelativeLayout nwLayout;
    private boolean isFromLogout = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            getWindow().setBackgroundDrawable(null);
            application = (AppController) getApplication();
            mTracker = application.getDefaultTracker();
            mTracker.setScreenName("Home");
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());

            mSession = new SessionManagement(this);
            nwLayout = (RelativeLayout) findViewById(R.id.layout_nw);
            isFromLogout = getIntent().getBooleanExtra(Flags.Bundle.Keys.ACTION_LOGIN_POPUP, false);
            checkConn();
        }
        catch(Exception e) {
            Log.e(Flags.ActivityTag.MAIN_ACTIVITY, e.getMessage());
        }
    }

    private void checkConn() {
        if(application.checkNetworkConn()) {
            nwLayout.setVisibility(View.GONE);

            if(!mSession.isLocationOn() && !isFromLogout) {
                Intent intent = new Intent(this, LocationActivity.class);
                intent.putExtra(Flags.IS_LOC_UPDATE_REQUESTED, true);
                startActivityForResult(intent, Flags.REQ_CODE_LOCATION);
            }
            else {
                init();
            }
        }
        else {
            nwLayout.setVisibility(View.VISIBLE);
        }
    }

    private void init() {
        try {
            /*if(!mSession.isAppStartModalDisplayed()) {
                AppStartModal modalFrag = AppStartModal.newInstance();
                modalFrag.show(this.getFragmentManager(), "dialog");
                modalFrag.setCancelable(true);
            }*/
            //startMainActivity();
            newStartActivity();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void newStartActivity(){
        AppController.getInstance().checkReleaseMode();
        WallOfDeals.setDeals(this);
        finish();
    }

    private void startMainActivity() {
        try {
            AppController.getInstance().checkReleaseMode();

            if(mSession.isLoggedIn()) {
                Intent i = new Intent(getApplicationContext(), SearchActivity.class);
                i.putExtra(Flags.Bundle.Keys.SOURCE_PAGE, Flags.Bundle.Values.NEW_SEARCH);
                i.putExtra(Flags.Bundle.Keys.TO_SEARCH_FORM, true);
                startActivity(i);
                finish();

                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Action")
                        .setAction("Share")
                        .build());
            }
            else {
                contentBody = (LinearLayout) findViewById(R.id.main_content);
                //fixDisplayDimensions();

                mToolbar = (Toolbar) findViewById(R.id.toolbar);
                mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                setSupportActionBar(mToolbar);
                actionBar = getSupportActionBar();
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowCustomEnabled(true);
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setDisplayHomeAsUpEnabled(true);

                int color = Color.parseColor("#FFFFFF");
                final PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                for (int i = 0; i < mToolbar.getChildCount(); i++) {
                    final View v = mToolbar.getChildAt(i);
                    if (v instanceof ImageButton) {
                        ((ImageButton) v).setColorFilter(colorFilter);
                    }
                }

                drawerFragment = (FragmentDrawer)
                        getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

                drawerFragment.setUp(R.id.fragment_navigation_drawer, mDrawerLayout, mToolbar);
                drawerFragment.setDrawerListener(this);

                //mHomeBanner = (TextView)findViewById(R.id.tv_home_banner);
                //mLearnMore = (TextView)findViewById(R.id.tv_learnmore);
                mSearchField = (EditText) findViewById(R.id.et_search);
                mTextSearch = (ImageButton)findViewById(R.id.ibtn_search);
                mVoiceSearch = (ImageButton) findViewById(R.id.ibtn_search_voice);
                //fragmentHolder = (FrameLayout)findViewById(R.id.fragment_placeholder);
                //searchFormMini = (LinearLayout) findViewById(R.id.search_form_mini);
                //searchBar = (LinearLayout) findViewById(R.id.home_search_bar);
                mRefineSearch = (Button) findViewById(R.id.btn_refine_search);
                //scrollView = (LockableScrollView) findViewById(R.id.scroll_view);
                //scrollView.setScrollingEnabled(false);
                mTextSearch.setBackground(getResources().getDrawable(R.drawable.search_btn_bg));
                this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

                //loadSearchFormFragment();
                //fragmentTransaction = getSupportFragmentManager().beginTransaction();
                if(getIntent().hasExtra(Flags.Bundle.Keys.ACTION_LOGIN_POPUP)){
                    if(getIntent().getBooleanExtra(Flags.Bundle.Keys.ACTION_LOGIN_POPUP, false))
                        selectDrawerItem(this, Flags.NavMenu.NOT_LOGGED_IN.LOGIN, mSession);
                }
                voiceSearchActive = true;
                mTextSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        keywordSearchAction(String.valueOf(mSearchField.getText()));
                    }
                });

                mSearchField.setHint(Utility.getSearchHint(getApplicationContext()));
                mSearchField.addTextChangedListener(new TextWatcher(){
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(count > 0){
                            mVoiceSearch.setImageDrawable(getResources().getDrawable(R.drawable.ic_clear));
                            mVoiceSearch.setScaleType(ImageView.ScaleType.CENTER);
                            voiceSearchActive = false;
                        }
                        else {
                            mVoiceSearch.setImageDrawable(getResources().getDrawable(R.drawable.ic_mic));
                            //mVoiceSearch.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            voiceSearchActive = true;
                        }
                    }
                    @Override
                    public void afterTextChanged(Editable s) {}
                });

                mSearchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if(actionId== EditorInfo.IME_ACTION_SEARCH){
                            keywordSearchAction(String.valueOf(v.getText()));
                        }
                        return false;
                    }
                });

                mVoiceSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(voiceSearchActive){
                            //start speech recog
                            Log.d("voice search : ", "start speech recognition");
                            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getResources().getString(R.string.mic_msg));
                            try {
                                startActivityForResult(intent, Flags.REQ_CODE_SPEECH_INPUT);
                            } catch (ActivityNotFoundException a) {
                                Toast.makeText(getApplicationContext(),"Sorry! Your device doesn\\'t support speech input",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            mSearchField.setText("");
                            Log.d("voice search : ", "clear composing Text");
                        }
                    }
                });
                mRefineSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*searchFormMini.setVisibility(View.VISIBLE);
                        Utility.anchorViewToTop((scrollView.getHeight() - searchBar.getBottom()), scrollView);*/
                        Intent search = new Intent(getApplicationContext(), SearchActivity.class);
                        search.putExtra(Flags.Bundle.Keys.SOURCE_PAGE, Flags.Bundle.Values.REFINE_SEARCH);
                        search.putExtra(Flags.Bundle.Keys.TO_SEARCH_FORM, true);
                        startActivity(search);

                        //Analytics
                        mTracker.send(new HitBuilders.EventBuilder()
                                .setCategory("Action")
                                .setAction("Refined Search")
                                .setLabel("Click on refine search")
                                .build());
                    }
                });
            }
        }
        catch(Exception e) {
            Log.e(Flags.ActivityTag.MAIN_ACTIVITY, e.getMessage());
        }
    }

    private void keywordSearchAction(String keywords){
        try {
            if(keywords.length() > 0) {
                //disableButtons();
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra(Flags.Bundle.Keys.KEYWORD, keywords);
                intent.putExtra(Flags.Bundle.Keys.SOURCE_PAGE, Flags.Bundle.Values.SEARCH_BY_KEYWORDS);
                intent.putExtra(Flags.Bundle.Keys.TO_SEARCH_FORM, false);
                startActivity(intent);
            }
            else {
                //Toast.makeText(this, "Please enter some keywords", Toast.LENGTH_LONG).show();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case Flags.REQ_CODE_SPEECH_INPUT: {
                    if (resultCode == RESULT_OK && null != data) {
                        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if(result.get(0).length() > 0) {
                            mSearchField.setText(result.get(0));
                            keywordSearchAction(result.get(0));
                        }
                    }
                    break;
                }
                case Flags.REQ_CODE_LOCATION : {
                    AppController.getInstance().startIntentService();
                    init();
                    //startMainActivity();
                    break;
                }
            }
        }
        catch(Exception e) {
            Log.e(Flags.ActivityTag.MAIN_ACTIVITY, e.getMessage());
        }
    }

    /*private void loadSearchFormFragment() {
        try {
            if(!searchFormInflated) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                SearchFormCompact fragment = new SearchFormCompact();
                fragment.setFragListener(MainActivity.this);
                transaction.replace(R.id.search_form_mini, fragment);
                transaction.commit();
                searchFormInflated = true;
            }
        }
        catch(Exception e) {
            Log.e(Flags.ActivityTag.MAIN_ACTIVITY, e.getMessage());
        }
    }*/


    /*private final void fixDisplayDimensions() {
        try {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels - 40;
            int width = displaymetrics.widthPixels;
            contentBody.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        LinearLayout iv = (LinearLayout) menu.getItem(0).getActionView().findViewById(R.id.ac_title_home);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item != null && item.getItemId() == R.id.ic_user) {
            if(!mSession.isLoggedIn()) {
                selectDrawerItem(this, Flags.NavMenu.NOT_LOGGED_IN.LOGIN, mSession);
            }

        }
        return super.onOptionsItemSelected(item);
    }

    /*public void loginPopup() {
        try{
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Login login = new Login();
            login.setFragListener(MainActivity.this);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.fragment_placeholder, login);
            fragmentTransaction.commit();
            fragmentHolder.setVisibility(View.VISIBLE);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        //selectItem(position);
        selectDrawerItem(this, position, mSession);
    }

    /*@Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();

        } else {
            getSupportActionBar().show();
            getSupportFragmentManager().popBackStackImmediate();
            if(fragmentHolder != null && fragmentHolder.getVisibility() == View.VISIBLE) {
                fragmentHolder.setVisibility(View.GONE);
            }
            drawerFragment.updateMenu();
        }
    }*/

    /*@Override
    public void hideFragment(boolean state) {
        if(state) {
            searchFormMini.setVisibility(View.GONE);
            scrollView.fullScroll(0);
        }

    }*/

    /*@Override
    public void replaceFragment(int fragmentToReplace) {
        if(getSupportFragmentManager().getBackStackEntryCount() != 0)
            getSupportFragmentManager().popBackStack();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch(fragmentToReplace) {
            case Flags.FragmentTag.FORGOT_PASSWORD:
                ForgotPassword forgotPassword = new ForgotPassword();
                transaction.replace(R.id.fragment_placeholder, forgotPassword)
                        .addToBackStack(null)
                        .commit();
                break;
            case Flags.FragmentTag.SIGNUP:
                Signup signup = new Signup();
                transaction.replace(R.id.fragment_placeholder, signup)
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }*/

    /*private void searchByKeywordsRequest(final int methodType, final String url, final Class resClass, final String keywords) {

        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("domain_id", "1");SEARCH_RESULT

            Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY,"attrib url: " + url);

            Client jsObjRequest = new Client(methodType, url, resClass, this, new Response.Listener<Object>() {
                @Override
                public void onResponse(Object response) {
                    SearchByKeywordsResponse res = (SearchByKeywordsResponse)response;
                    Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, "status: "+ res.getStatus());

                    ResponseCode resCode = ResponseCode.fromValue(res.getStatus());
                    switch(resCode) {
                        case STATUS_SUCCESS: {
                            try {
                                List<AttrValSpinnerModel> attrValues = res.getAttrValues();

                                Bundle bundle = new Bundle();
                                bundle.putParcelableArrayList("key", (ArrayList<? extends Parcelable>) attrValues);
                                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                                intent.putExtra(Flags.Bundle.Keys.ATTR_PARCEL_BY_KEYWORDS, bundle);
                                intent.putExtra(Flags.Bundle.Keys.KEYWORD, keywords);
                                intent.putExtra(Flags.Bundle.Keys.SOURCE_PAGE, Flags.Bundle.Values.SEARCH_BY_KEYWORDS);
                                intent.putExtra(Flags.Bundle.Keys.TO_SEARCH_FORM, false);

                                boolean isNoResult = false;
                                if (attrValues.size() < 1) isNoResult = true;
                                intent.putExtra(Flags.Bundle.Keys.NO_RESULT_STATUS, isNoResult);

                                startActivity(intent);
                                finish();
                            }
                            catch(Exception e) {
                                e.printStackTrace();
                            }

                            break;
                        }
                        case STATUS_FAILURE: {
                            Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY,  "status 0");
                            enableButtons();
                            break;
                        }
                        default : {
                            enableButtons();
                            Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY,  "default");
                        }

                    }

                    //showProgress(false);
                    //mAuthTask = true;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, "Error: " + error);
                    enableButtons();
                }
            }, Request.Priority.HIGH);
            String tag_json_obj = "json_obj_req";
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsObjRequest, tag_json_obj);
        }
        catch(Exception e) {
            Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, e.getMessage());
        }
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        //checkConn();
        //init();
        /*if(mSession.isLoggedIn()) {
            Intent i = new Intent(getApplicationContext(), SearchActivity.class);
            i.putExtra(Flags.Bundle.Keys.SOURCE_PAGE, Flags.Bundle.Values.NEW_SEARCH);
            i.putExtra(Flags.Bundle.Keys.TO_SEARCH_FORM, true);
            startActivity(i);
            finish();
        }*/
    }

    public void actionTryAgain(View v) {
        checkConn();
    }
}