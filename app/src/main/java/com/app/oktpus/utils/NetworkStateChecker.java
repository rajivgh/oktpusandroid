package com.app.oktpus.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.app.oktpus.R;
import com.app.oktpus.controller.AppController;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Gyandeep on 16/4/18.
 */

public class NetworkStateChecker extends BroadcastReceiver {

    static Snackbar snackbar;
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            NetworkStateChecker.snack(null, 0, "No internet connection",context.getApplicationContext());
        }else{
            NetworkStateChecker.hideSnackbar(context.getApplicationContext());
        }
    }

    public static void snack (HashMap<String,View.OnClickListener> actions, int priority, String message, Context context) {
        if(AppController.appActivity != null){
            snackbar = Snackbar.make(AppController.appActivity.findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE);
            if (actions != null) {
                Iterator iterator = actions.entrySet().iterator();
                snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
                while (iterator.hasNext()) {
                    Map.Entry pair = (Map.Entry) iterator.next();
                    snackbar.setAction((String) pair.getKey(), (View.OnClickListener) pair.getValue());
                    iterator.remove(); // avoids a ConcurrentModificationException
                }
            }
            switch (priority) {
                case 0:
                    snackbar.getView().setBackgroundColor(context.getResources().getColor(R.color.errorTextMsg));
                    break;
                case 1:
                    snackbar.getView().setBackgroundColor(Color.parseColor("#66ccff"));
                    break;
                case 2:
                    snackbar.getView().setBackgroundColor(Color.parseColor("#66ff33"));
                    break;
            }
            snackbar.show();
        }
    }
    private static void hideSnackbar(Context context){
        if(snackbar !=null && snackbar.isShown()){
            /*snackbar = Snackbar.make(AppController.appActivity.findViewById(android.R.id.content), "Connection is available now!", Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(context.getResources().getColor(R.color.success_text));
            snackbar.show();*/

            RequestQueue reqQueue = AppController.getInstance().getRequestQueue();

            snackbar.dismiss();
        }
    }
}
