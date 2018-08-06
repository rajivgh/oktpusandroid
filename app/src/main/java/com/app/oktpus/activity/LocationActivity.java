package com.app.oktpus.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.oktpus.model.AttrValSpinnerModel;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.constants.ResponseCode;
import com.app.oktpus.controller.AppController;
import com.app.oktpus.listener.OnCallListener;
import com.app.oktpus.responseModel.CommonResponses;
import com.app.oktpus.utils.Client;
import com.app.oktpus.utils.SessionManagement;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gyandeep on 8/2/17.
 */

public class LocationActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;
    private boolean isUpdateRequested = false;//, isOnlySpecialPermission = false;
    private SessionManagement mSession;
    private boolean isRequestedOnce = false, isSpecialPermissionChecked = false;
    private LocationSettingsRequest.Builder builder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isUpdateRequested = this.getIntent().getBooleanExtra(Flags.IS_LOC_UPDATE_REQUESTED, false);
        //isOnlySpecialPermission = this.getIntent().getBooleanExtra(Flags.IS_ONLY_SPCL_PERMISSION, false);
        mSession = new SessionManagement(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds*/

        builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest).setAlwaysShow(true);

    }


    public static String TAG = Flags.ActivityTag.MAIN_ACTIVITY;
    /**
     * Constant used in the location settings dialog.
     */
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    public boolean isreqPermission = false;

    /**
     * If connected get lat and long
     *
     */
    @Override
    public void onConnected(Bundle bundle) {
        try {
            final Activity activity = this;
            final String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (isUpdateRequested) {
                PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
                result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                    @Override
                    public void onResult(LocationSettingsResult result) {
                        final Status status = result.getStatus();
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            /*if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                                builder.setMessage("To receive relevant location based notifications you have to allow us access to your location.");
                                builder.setTitle("Location Services");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ActivityCompat.requestPermissions(activity, permissions, 0);
                                    }
                                });
                                builder.show();
                            } else {
                                ActivityCompat.requestPermissions(activity, permissions, 0);
                            }*/
                            if(!isSpecialPermissionChecked) {
                                isSpecialPermissionChecked = true;
                                ActivityCompat.requestPermissions(activity, permissions, 0);

                                /*if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                                    *//*AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                                    builder.setMessage("To receive relevant location based notifications you have to allow us access to your location.");
                                    builder.setTitle("Location Services");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ActivityCompat.requestPermissions(activity, permissions, 0);
                                        }
                                    });
                                    builder.show();*//*


                                } else {
                                    ActivityCompat.requestPermissions(activity, permissions, 0);
                                }*/
                            }
                            else {
                                /*if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                                    ConfirmDisableLocationPermission dialog = ConfirmDisableLocationPermission.newInstance();
                                    dialog.show(activity.getFragmentManager(), "c_dialog");
                                    dialog.setCancelable(false);
                                }
                                else*/
                                finish();
                            }

                            return;
                        }

                        //    if(!isOnlySpecialPermission) {
                        switch (status.getStatusCode()) {
                            case LocationSettingsStatusCodes.SUCCESS:
                                Log.i(TAG, "All location settings are satisfied.");
                                mSession.setLocAllowed(true);
                                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new LocationListener() {
                                    @Override
                                    public void onLocationChanged(Location location) {
                                        //double lat = 0, lng = 0;
                                        if(location != null) {

                                                /*//lat = 45.5088400;
                                                //lng = -73.5878100;
                                                mSession.setLocation(lat, lng);
                                                if(0 != lat && 0 != lng)
                                                    getCityFromLatLng(45.5088400, -73.5878100);*/
                                            mSession.setLocation(location.getLatitude(), location.getLongitude());
                                            getCityFromLatLng(location.getLatitude(), location.getLongitude());
                                        }
                                        else {
                                            getCityFromLatLng(mSession.getLatitude(), mSession.getLongitude());
                                        }
                                        //mSession.setLocation(45.5088400, -73.5878100);

                                        if (mGoogleApiClient.isConnected()) {
                                            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                                            mGoogleApiClient.disconnect();
                                        }
                                        //getCityFromLatLng(mSession.getLatitude(), mSession.getLongitude());
                                        //getCityFromLatLng(45.5088400, -73.5878100);
                                    }
                                });
                                break;
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                                try {
                                    if(!isRequestedOnce) {
                                        status.startResolutionForResult(LocationActivity.this, REQUEST_CHECK_SETTINGS);
                                        isRequestedOnce = true;
                                    }
                                    else {
                                        finish();
                                    }
                                } catch (IntentSender.SendIntentException e) {
                                    finish();
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                finish();
                                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                                break;
                        }
                        //}
                    }
                });

            } else {
                if(location != null) {
                    //mSession.setLocation(45.5088400, -73.5878100);
                    //getCityFromLatLng(45.5088400, -73.5878100);
                    mSession.setLocation(location.getLatitude(), location.getLongitude());
                    getCityFromLatLng(location.getLatitude(), location.getLongitude());
                }
                else {
                    getCityFromLatLng(mSession.getLatitude(), mSession.getLongitude());
                }
            }
        }
        catch(Exception e) {
            finish();
            Log.d(Flags.ActivityTag.LOCATION_ACTIVITY, e.getMessage());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /**
     * If locationChanges change lat and long
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        if(location != null) {
            //Toast.makeText(getApplicationContext(), "Lat: "+location.getLatitude() + ", long: "+ location.getLongitude(),Toast.LENGTH_LONG);
            /*mSession.setLocation(location.getLatitude(), location.getLongitude());
            getCityFromLatLng(mSession.getLatitude(), mSession.getLongitude());*/

            /*if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                mGoogleApiClient.disconnect();
            }*/
        }
    }

    public String response;
    public void getCityFromLatLng(final double lat, final double lng) {
        try {
            String url = Flags.URL.CITY_BY_LOCATION+ "&filters[latitude]="+lat+"&filters[longitude]="+lng;
            Client jsObjRequest = new Client(Request.Method.GET, url, CommonResponses.class, new OnCallListener() {
                @Override
                public void nwResponseData(String res) {
                    Log.d("LocationActivity", "status: "+ res.toString());
                    response = res;
                }
            }, new Response.Listener<Object>() {
                @Override
                public void onResponse(Object obj) {
                    CommonResponses res = (CommonResponses) obj;
                    Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, "status: "+ res.getStatus());
                    Intent intent = new Intent();

                    ResponseCode resCode = ResponseCode.fromValue(res.getStatus());
                    switch(resCode) {
                        case STATUS_SUCCESS: {
                            try {
                                if(response != null) {
                                    JsonObject jObj = new JsonParser().parse(response).getAsJsonObject();
                                    if(jObj.has("attribute_value")) {
                                        JsonArray attrValue = (JsonArray) jObj.get("attribute_value");
                                        List<AttrValSpinnerModel> attribute = new Gson().fromJson(attrValue, new TypeToken<List<AttrValSpinnerModel>>(){}.getType());

                                        Bundle bundle = new Bundle();
                                        bundle.putParcelableArrayList(Flags.Bundle.Keys.DEFAULT_CITY, (ArrayList<? extends Parcelable>) attribute);
                                        AppController application = (AppController) getApplication();
                                        application.setDefaultCityBundle(bundle);

                                        //intent.putExtra(Flags.Bundle.Keys.ATTR_PARCEL_BY_LOC, bundle);
                                        setResult(101,intent);
                                        finish();
                                    }
                                }
                            }
                            catch(Exception e) {
                                e.printStackTrace();
                            }

                            break;
                        }
                        case STATUS_FAILURE: {
                            Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY,  "status 0");
                            setResult(101,intent);
                            finish();
                            break;
                        }
                        default : {
                            Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY,  "default");
                            setResult(101,intent);
                            finish();
                        }
                    }
                    //overridePendingTransition(R.anim.frag_slide_in_from_bottom, R.anim.frag_slide_in_from_bottom);
                    //showProgress(false);
                    //mAuthTask = true;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, "Error: " + error);
                    Intent intent=new Intent();
                    setResult(101,intent);
                    finish();
                    //overridePendingTransition(R.anim.frag_slide_in_from_bottom, R.anim.frag_slide_in_from_bottom);
                }
            }, Request.Priority.HIGH);
            String tag_json_obj = "json_obj_req";
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsObjRequest, tag_json_obj);
        }
        catch(Exception e) {
            Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, e.getMessage());
        }
    }
}