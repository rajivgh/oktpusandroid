package com.app.oktpus.controller;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.app.oktpus.R;
import com.app.oktpus.adapter.LruBitmapCache;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.service.FetchAddress;
import com.app.oktpus.utils.GetSessionID;
import com.app.oktpus.utils.NetworkStateChecker;
import com.app.oktpus.utils.OkHttpStack;
import com.app.oktpus.utils.SessionManagement;

/*import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp.StethoInterceptor;*/
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import com.squareup.okhttp.OkHttpClient;

import java.util.Map;
import java.util.Queue;

/**
 * Created by Gyandeep on 20/9/16.
 */
public class AppController extends Application implements Application.ActivityLifecycleCallbacks{

    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "cookie";
    private static final String SESSION_COOKIE = "PHPSESSID";
    private static final String SRVNAME = "SRVNAME";
    public SessionManagement mSession;
    public static final String TAG = AppController.class.getSimpleName();
    private RequestQueue mRequestQueue;

    private String currencyFormat;
    private String kilometerFormat;

    private static AppController mInstance;
    public Bundle defaultCityBundle, variableCityBundle;
    private Tracker mTracker;
    private ImageLoader mImageLoader;


    public static Activity appActivity;

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

    synchronized public Bundle getDefaultCityBundle() {
        return defaultCityBundle;
    }
    public void setDefaultCityBundle(Bundle bundle) {
        this.defaultCityBundle = bundle;
        this.variableCityBundle = bundle;
    }

    public void setVariableCityBundle(Bundle bundle) {
        this.variableCityBundle = bundle;
    }

    synchronized public Bundle getVariableCityBundle() {
        return variableCityBundle;
    }

    public Boolean checkNetworkConn() {
        ConnectivityManager cn = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected() == true) {
            return true;
        } else {
            /*Toast.makeText(this, "No internet connection.!",
                    Toast.LENGTH_LONG).show();*/
            return false;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
        /*if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);*/

        mInstance = this;
        //_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSession = new SessionManagement(this);
        mResultReceiver = new AddressResultReceiver(new Handler());         //Location Service
        //startIntentService();
        currencyFormat = mSession.getCurrencyFormat();
        kilometerFormat = mSession.getKilometerFormat();
        //Check if session ID exists in shared pref if not get a fresh session ID from server

        /*if(com.app.oktpus.BuildConfig.DEBUG) {
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                            .build());
        }*/

        checkReleaseMode();

        ComponentName component = new ComponentName(this, NetworkStateChecker.class);
        int status = getPackageManager().getComponentEnabledSetting(component);
        if(status == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            Log.d("NwReceiver","receiver is enabled");
        } else if(status == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
            Log.d("NwReceiver","receiver is disabled");
        }
    }

    public void checkReleaseMode() {
        try {
            if(mSession.getReleaseMode() != Flags.releaseMode) {
                mSession.clearPref();
                mSession.setReleaseMode(Flags.releaseMode);
            }
            checkSession();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void checkSession() {
        try {
            if(null == mSession.getSessionCookie()) {
                new GetSessionID(this).getSessIDFromServer();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            OkHttpClient client = new OkHttpClient();

            //if(com.app.oktpus.BuildConfig.DEBUG) client.networkInterceptors().add(new StethoInterceptor());
            mRequestQueue = Volley.newRequestQueue(getApplicationContext(), new OkHttpStack(client));
        }
        return mRequestQueue;
    }

    /**
     * Checks the response headers for session cookie and saves it
     * if it finds it.
     * @param headers Response Headers.
     */
    //TODO : Add session expiry checking and renew cookie
    public final void checkSessionCookie(Map<String, String> headers) {
        //System.out.println("Check session cookie");
        if(null == mSession.getSessionCookie() || null == mSession.getSrvNameCookie()) {
            if (headers.containsKey(SET_COOKIE_KEY)
                    && headers.get(SET_COOKIE_KEY).startsWith(SESSION_COOKIE)) {

                String cookie = headers.get(SET_COOKIE_KEY);
                //System.out.println("header cookie: " + cookie);
                if (cookie.length() > 0) {
                    String[] splitCookie = cookie.split(";");
                    String[] splitSessionId = splitCookie[0].split("=");
                    cookie = splitSessionId[1];
                    mSession.setSessionCookie(cookie);
                }
                //System.out.println("cookie PHPSESSID: " + cookie);
            }

            //Only for staging url
            /*if(mSession.getSrvNameCookie() != null){
                mSession.setSrvNameCookie(null);
            }*/


            /*
            * Uncomment block for production url
            * */
            if(headers.containsKey(SET_COOKIE_KEY) && headers.get(SET_COOKIE_KEY).startsWith(SRVNAME)) {
                String cookie = headers.get(SET_COOKIE_KEY);
                if (cookie.length() > 0) {
                    String[] splitCookie = cookie.split(";");
                    String[] splitSessionId = splitCookie[0].split("=");
                    cookie = splitSessionId[1];
                    mSession.setSrvNameCookie(cookie);
                }
                //System.out.println("cookie SRVNAME: " + cookie);
            }
        }
    }

    /**
     * Adds session cookie to headers if exists.
     * @param headers
     */
    public final void addSessionCookie(Map<String, String> headers) {

        if(null == mSession.getSessionCookie())
            new GetSessionID(this).getSessIDFromServer();

        String sessionId = mSession.getSessionCookie();
        //System.out.println("sessionId: "+ sessionId);
        if (sessionId != null) {
            StringBuilder builder = new StringBuilder();
            builder.append(SESSION_COOKIE);
            builder.append("=");
            builder.append(sessionId + ";");
            if (headers.containsKey(COOKIE_KEY)) {
                //builder.append("; ");
                builder.append(headers.get(COOKIE_KEY));
            }

            if(null != mSession.getSrvNameCookie()) {
                builder.append(SRVNAME + "=" + mSession.getSrvNameCookie() + ";");
            }
            //System.out.println("newcookie: " + builder.toString());

            headers.put(COOKIE_KEY, builder.toString());//"PHPSESSID=45kp4ijaahjl8f28t1g49f9bk7"); //"PHPSESSID=mqdba0pa4p4fjgq01s0q50qba6"
            headers.put("content-Type", "application/x-www-form-urlencoded");
        }
    }

    public <T> void addToRequestQueue(Request req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setShouldCache(true);
        getRequestQueue().add(req);
    }

    /*public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        req.setShouldCache(false);
        getRequestQueue().add(req);
    }*/

    public <T> void addToCacheRequestQueue(Request req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setShouldCache(true);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static Typeface getFontType(Context context) {
        Typeface fontOswaldBold = Typeface.createFromAsset(context.getApplicationContext().getAssets(),
                "fonts/Oswald-Bold.ttf");
        return fontOswaldBold;
    }

    public void setCurrencyFormat(String currencyFormat){
        this.currencyFormat = currencyFormat;
        mSession.setCurrencyFormat(currencyFormat);
    }

    public void setKilometerFormat(String kilometerFormat){
        this.kilometerFormat = kilometerFormat;
        mSession.setKilometerFormat(kilometerFormat);
    }

    public String getCurrencyFormat(){
        return mSession.getCurrencyFormat();
    }

    public String getKilometerFormat(){
        return mSession.getKilometerFormat();
    }

    protected Location mLastLocation;
    private AddressResultReceiver mResultReceiver;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        appActivity = activity;
        Intent i = new Intent(this, NetworkStateChecker.class);
        sendBroadcast(i);
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            System.out.println(""+resultData.getString(FetchAddress.CITY)+" -> "+
                    resultData.getString(FetchAddress.STATE)+" -> "+
                    resultData.getString(FetchAddress.COUNTRY)+" -> "+
                    resultData.getString(FetchAddress.COUNTRY_CODE));

            switch (resultData.getString(FetchAddress.COUNTRY_CODE)){
                case "CA":
                    mSession.setCurrencyFormat("CAD");
                    mSession.setKilometerFormat("kilometers");
                    break;
                default:
                    mSession.setCurrencyFormat("USD");
                    mSession.setKilometerFormat("miles");
                    break;
            }
            currencyFormat = mSession.getCurrencyFormat();
            kilometerFormat = mSession.getKilometerFormat();
        }
    }
    public void startIntentService() {
        Intent intent = new Intent(this, FetchAddress.class);
        intent.putExtra(FetchAddress.RECEIVER, new AddressResultReceiver(new Handler()));
        intent.putExtra(FetchAddress.LOCATION_DATA_EXTRA, mLastLocation);
        startService(intent);

    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }
}