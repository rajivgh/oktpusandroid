package com.app.oktpus.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.app.oktpus.R;
import com.app.oktpus.activity.SearchActivity;
import com.facebook.login.LoginManager;

import com.app.oktpus.activity.MainActivity;
import com.app.oktpus.constants.Flags;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Gyandeep on 3/10/16.
 */

public class SessionManagement {

    private SharedPreferences pref, prefCompareCars;
    private SharedPreferences.Editor editor, editorCompareCars;

    private Context _context;

    private static final String PREF_NAME = "com.app.oktpus.pref_data";
    private static final String PREF_COMPARE_CARS = "pref_compare_cars";
    private static final String SESSION_COOKIE_PHPSESSID = "PHPSESSID";
    private static final String SESSION_COOKIE_SRVNAME = "SRVNAME";
    private static final String USERNAME = "username";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String USER_ID = "UserId";
    private static final String LABEL_LIST = "LabelList";
    private static final String ACTIVE_LABELS = "activeLabels";
    private static final String CURRENCY_FORMAT = "currencyFormat";
    private static final String KILOMETER_FORMAT = "kilometerFormat";
    private static final String LONGITUDE = "lng", LATITUDE = "lat";
    private static final String DEMO = "demo";
    private static final String FAV_DEMO = "fdemo";
    private static final String APP_START_MODAL = "modal";
    private static final String IS_SOCIAL_LOGIN = "socialLogin";
    private static final String IS_LOC_ALLOWED = "isLocAllowed";
    private static final String COMPARE_CAR_ITEM_IDS = "ccItemIds";
    private static final String RELEASE_MODE = "release_mode";

    public SessionManagement(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.commit();
    }

    public SessionManagement(Context context, Activity activity) {
        this._context = context;
        prefCompareCars = _context.getSharedPreferences(PREF_COMPARE_CARS, Context.MODE_PRIVATE);
        editorCompareCars = prefCompareCars.edit();
        editorCompareCars.commit();
    }

    public String getSessionCookie() {
        return pref.getString(SESSION_COOKIE_PHPSESSID, null);
    }
    public void setSessionCookie(String cookie) {
        editor.putString(SESSION_COOKIE_PHPSESSID, cookie);
        editor.commit();
    }

    public String getSrvNameCookie() {
        return pref.getString(SESSION_COOKIE_SRVNAME, null);
    }
    public void setSrvNameCookie(String cookie) {
        editor.putString(SESSION_COOKIE_SRVNAME, cookie);
        editor.commit();
    }

    public int getReleaseMode() {
        return pref.getInt(RELEASE_MODE, 0);
    }
    public void setReleaseMode(int mode) {
        editor.putInt(RELEASE_MODE, mode);
        editor.commit();
    }

    public String getUsername() {
        return pref.getString(USERNAME, null);
    }
    public void setUsername(String username){
        editor.putString(USERNAME, username);
        editor.commit();
    }

    public String getLabelList() {
        return pref.getString(LABEL_LIST, null);
    }
    public void setLabelList(String labelList){
        editor.putString(LABEL_LIST, labelList);
        editor.commit();
    }

    public String getActiveLabels() {
        return pref.getString(ACTIVE_LABELS, null);
    }
    public void setActiveLabels(String activeLabels){
        editor.putString(ACTIVE_LABELS, activeLabels);
        editor.commit();
    }

    public int getUserID() {
        return pref.getInt(USER_ID, 0);
    }
    public void setUserID(int userID){
        editor.putInt(USER_ID, userID);
        editor.commit();
    }

    public String getCurrencyFormat() {
        return pref.getString(CURRENCY_FORMAT, "CAD");
    }
    public void setCurrencyFormat(String currencyFormat){
        editor.putString(CURRENCY_FORMAT, currencyFormat);
        editor.commit();
    }
    public String getKilometerFormat() {
        return pref.getString(KILOMETER_FORMAT, "kilometers");
    }
    public void setKilometerFormat(String kilometerFormat){
        editor.putString(KILOMETER_FORMAT, kilometerFormat);
        editor.commit();
    }

    public boolean isDemoChecked() { return pref.getBoolean(DEMO, false);}
    public void setIsDemoChecked(boolean isChecked) {
        editor.putBoolean(DEMO, isChecked);
        editor.commit();
    }

    public boolean isFavDemoChecked() { return pref.getBoolean(FAV_DEMO, false);}
    public void setIsFavDemoChecked(boolean isChecked) {
        editor.putBoolean(FAV_DEMO, isChecked);
        editor.commit();
    }

    public boolean isAppStartModalDisplayed() { return pref.getBoolean(APP_START_MODAL, false);}
    public void setAppStartModalDisplayed(boolean isChecked) {
        editor.putBoolean(APP_START_MODAL, isChecked);
        editor.commit();
    }

    public boolean isSocialLogin() { return pref.getBoolean(IS_SOCIAL_LOGIN, false);}
    public void setIsSocialLogin(boolean isChecked) {
        editor.putBoolean(IS_SOCIAL_LOGIN, isChecked);
        editor.commit();
    }

    public boolean isLocationOn() { return pref.getBoolean(IS_LOC_ALLOWED, false);}
    public void setLocAllowed(boolean isChecked) {
        editor.putBoolean(IS_LOC_ALLOWED, isChecked);
        editor.commit();
    }



    /**
     * Compare car list operations
     * */
    public Set<String> getCompareCarProductIds() { return prefCompareCars.getStringSet(COMPARE_CAR_ITEM_IDS, null); }

    public void addCompareCarProductId(String productID, String title) {

        //Set<String> tmpList = Collections.emptySet();
        Set<String> compareList = prefCompareCars.getStringSet(COMPARE_CAR_ITEM_IDS, null);
        if(compareList == null) {
            compareList = new HashSet<String>();
        }

        if(compareList.size() == 5){
            Toast.makeText(_context, _context.getResources().getString(R.string.compare_list_full), Toast.LENGTH_LONG).show();
            return;
        }

        if(compareList.contains(productID)) {
            Toast.makeText(_context, _context.getResources().getString(R.string.item_already_added), Toast.LENGTH_LONG).show();
            return;
        }

        compareList.add(productID);
        editorCompareCars.clear();
        editorCompareCars.putStringSet(COMPARE_CAR_ITEM_IDS, compareList);
        editorCompareCars.commit();
        Toast.makeText(_context, "\"" + title +"\""+_context.getResources().getString(R.string.item_added_to_list), Toast.LENGTH_LONG).show();
    }

    public void removeCompareCarItem(String productID) {
        Set<String> compareList = prefCompareCars.getStringSet(COMPARE_CAR_ITEM_IDS, null);
        compareList.remove(productID);
        editorCompareCars.clear();
        editorCompareCars.putStringSet(COMPARE_CAR_ITEM_IDS, compareList);
        editorCompareCars.commit();
    }

    public void removeAllcompareCarItems() {
        editorCompareCars.remove(COMPARE_CAR_ITEM_IDS);
        editorCompareCars.commit();
    }

    /*
    *   Location History
    * */

    public double getLatitude() {
        return Double.longBitsToDouble(pref.getLong(LATITUDE, 0));
    }

    public double getLongitude() {
        return Double.longBitsToDouble(pref.getLong(LONGITUDE, 0));
    }

    public void setLocation(double lat, double lng) {
        try {
            editor.putLong(LATITUDE, Double.doubleToRawLongBits(lat));
            editor.putLong(LONGITUDE, Double.doubleToRawLongBits(lng));
            editor.commit();
        }
        catch(Exception e) {
            e.printStackTrace();
            Log.d("SessionManagement", e.getMessage());
        }
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String username, int userID, boolean isSocialLogin) {
        try {
            // Storing login value as TRUE
            editor.putBoolean(IS_LOGIN, true);
            editor.putString(USERNAME, username);
            editor.putInt(USER_ID, userID);
            editor.putBoolean(IS_SOCIAL_LOGIN, isSocialLogin);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("SessionManagement", e.getMessage());
        }
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void clearPref() {
        try{
            editor.remove(IS_LOGIN);
            editor.remove(USERNAME);
            editor.remove(USER_ID);
            editor.remove(SESSION_COOKIE_PHPSESSID);
            editor.remove(SESSION_COOKIE_SRVNAME);
            editor.remove(IS_SOCIAL_LOGIN);
            editor.commit();
        }
        catch(Exception e) {
            Log.e("SessionManagement", e.getMessage());
        }
    }

    public void logoutUser() {
        try {
            // Clearing all data from Shared Preferences
            clearPref();
            /*Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {

                        }
                    });*/

            //Logout for facebook
            //LoginManager.getInstance().logOut();

            Log.d("SessionManagement","Logging out");
            // After logout redirect user to Main Activity
            Intent i = new Intent(_context, SearchActivity.class);
            // Closing all the Activities
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.putExtra(Flags.Bundle.Keys.ACTION_LOGIN_POPUP, true);
            // Starting Main Activity
            _context.startActivity(i);
        }
        catch(Exception e) {
            Log.e("SessionManagement", e.getMessage());
        }
    }
}