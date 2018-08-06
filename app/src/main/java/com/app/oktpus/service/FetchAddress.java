package com.app.oktpus.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.app.oktpus.utils.SessionManagement;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class FetchAddress extends IntentService {
    public static final String CITY = "city", STATE = "state", COUNTRY = "country", COUNTRY_CODE = "countryCode";

    public FetchAddress() {
        super("FetchAddress");
    }

    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME =
            "com.google.android.gms.location.sample.locationaddress";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
            ".LOCATION_DATA_EXTRA";


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            SessionManagement location = new SessionManagement(getApplicationContext());

            mReceiver = intent.getParcelableExtra(RECEIVER);

            String errorMessage = "";

            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(
                        location.getLatitude(), location.getLongitude(),
                        //40.730610, -73.935242,         // New york
                        //45.5016889, -73.56725,         // Montreal
                        //19.42847, -99.12766,          // Mexico city
                        // In this sample, get just a single address.
                        1);
            } catch (IOException ioException) {
                // Catch network or other I/O problems.
                //errorMessage = getString(R.string.service_not_available);
                //Log.e(TAG, errorMessage, ioException);
            } catch (IllegalArgumentException illegalArgumentException) {
                // Catch invalid latitude or longitude values.
                //errorMessage = getString(R.string.invalid_lat_long_used);
                Log.e(TAG, errorMessage + ". " +
                        "Latitude = " + location.getLatitude() +
                        ", Longitude = " +
                        location.getLongitude(), illegalArgumentException);
            }

            // Handle case where no address was found.
            if (addresses == null || addresses.size()  == 0) {
                if (errorMessage.isEmpty()) {
                    errorMessage = "No address found";
                    Log.e(TAG, errorMessage);
                }
            } else {
                Address address = addresses.get(0);
                /*System.out.println("state: "+address.getAdminArea());
                System.out.println(address.getCountryCode());
                System.out.println("country: "+address.getCountryName());
                //System.out.println(address.getPostalCode());
                System.out.println("city: "+address.getLocality());*/

                // Fetch the address lines using getAddressLine,
                // join them, and send them to the thread.
                /*for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressFragments.add(address.getAddressLine(i));
                }*/
                /*Log.i(TAG, "Address found+ "+TextUtils.join(System.getProperty("line.separator"),
                        addressFragments));*/

                //if(address.getLocality() != null && !address.getLocality().equals("null"))

                deliverResultToReceiver(SUCCESS_RESULT, (address.getLocality() != null && !address.getLocality().equals("null"))? address.getLocality():"",
                        (address.getAdminArea() != null && !address.getAdminArea().equals("null"))? address.getAdminArea():"",
                        (address.getCountryName() != null && !address.getCountryName().equals("null"))? address.getCountryName():"",
                        (address.getCountryCode() != null && !address.getCountryCode().equals("null"))? address.getCountryCode():"");
            }
        }
    }

    protected ResultReceiver mReceiver;
    private void deliverResultToReceiver(int resultCode, String city, String state,
                                         String country, String countryCode) {
        Bundle bundle = new Bundle();
        bundle.putString(CITY, city);
        bundle.putString(STATE, state);
        bundle.putString(COUNTRY, country);
        bundle.putString(COUNTRY_CODE, countryCode);
        mReceiver.send(resultCode, bundle);
    }
}
