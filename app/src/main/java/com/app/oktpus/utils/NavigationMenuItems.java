package com.app.oktpus.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.app.oktpus.R;
import com.app.oktpus.activity.AboutActivity;
import com.app.oktpus.activity.CarFinance;
import com.app.oktpus.activity.CarInsurance;
import com.app.oktpus.activity.CarPartsActivity;
import com.app.oktpus.activity.CompareCar;
import com.app.oktpus.activity.CouponActivity;
import com.app.oktpus.activity.GarageActivity;
import com.app.oktpus.activity.LoginActivity;
import com.app.oktpus.activity.NotificationHistory;
import com.app.oktpus.activity.SavedSearchActivity;
import com.app.oktpus.activity.SearchActivity;
import com.app.oktpus.activity.SignupActivity;
import com.app.oktpus.activity.UserSettings;
import com.app.oktpus.activity.WhatsMyCarWorth;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.controller.AppController;
import com.app.oktpus.fragment.AppStartModal;


/**
 * Created by Gyandeep on 8/2/17.
 */

public class NavigationMenuItems {

    public static void selectDrawerItem(Activity activity, int pos, SessionManagement session) {
        Log.d("Drawer", "position: "+ pos);
        Intent intent;
        if(!session.isLoggedIn()) {
            switch(pos) {
                case Flags.NavMenu.NOT_LOGGED_IN.LOGIN :
                    Intent login = new Intent(activity, LoginActivity.class);
                    activity.startActivityForResult(login, 1);
                    break;
                case Flags.NavMenu.NOT_LOGGED_IN.SIGNUP :
                    Intent signup = new Intent(activity, SignupActivity.class);
                    activity.startActivityForResult(signup, 2);
                    break;
                case Flags.NavMenu.NOT_LOGGED_IN.SEARCH :
                    Intent search = new Intent(activity,  SearchActivity.class);
                    search.putExtra(Flags.Bundle.Keys.SOURCE_PAGE, Flags.Bundle.Values.NEW_SEARCH);
                    search.putExtra(Flags.Bundle.Keys.TO_SEARCH_FORM, true);
                    activity.startActivity(search);
                    break;
                case Flags.NavMenu.NOT_LOGGED_IN.WALL_OF_DEALS :
                    WallOfDeals.setDeals(activity);
                    break;
                case Flags.NavMenu.NOT_LOGGED_IN.COMPARE_CARS :
                    Intent compareCars = new Intent(activity, CompareCar.class);
                    compareCars.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(compareCars);
                    break;
                case Flags.NavMenu.NOT_LOGGED_IN.CAR_INSURANCE :
                    Intent carInsurance = new Intent(activity, CarInsurance.class);
                    carInsurance.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(carInsurance);
                    break;
                case Flags.NavMenu.NOT_LOGGED_IN.CAR_FINANCE :
                    //activity.closeContextMenu();
                    Intent carFinance = new Intent(activity, CarFinance.class);
                    carFinance.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(carFinance);
                    break;
                case Flags.NavMenu.NOT_LOGGED_IN.WMCW:
                    //activity.closeContextMenu();
                    Intent wmcw = new Intent(activity, WhatsMyCarWorth.class);
                    //activity.startActivity(wmcw, ActivityOptions.makeCustomAnimation(activity, R.anim.slide_out, R.anim.slide_out).toBundle());
                    activity.startActivity(wmcw);
                    //activity.startActivity(wmcw, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
                    break;
                case Flags.NavMenu.NOT_LOGGED_IN.COUPON:
                    Intent coupon = new Intent(activity, CouponActivity.class);
                    activity.startActivity(coupon);
                    break;
                case Flags.NavMenu.NOT_LOGGED_IN.CAR_PARTS:
                    Intent carparts = new Intent(activity, CarPartsActivity.class);
                    activity.startActivity(carparts);
                    break;
                /*case Flags.NavMenu.NOT_LOGGED_IN.TERMS_AND_CONDITIONS :
                    Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(Flags.URL.TERMS));
                    browse.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(browse);
                    break;*/

                case Flags.NavMenu.NOT_LOGGED_IN.ABOUT:
                    Intent aboutActivity = new Intent(activity, AboutActivity.class);
                    activity.startActivity(aboutActivity);
                    /*AppStartModal modalFrag = AppStartModal.newInstance();
                    modalFrag.show(activity.getFragmentManager(), "dialog");
                    modalFrag.setCancelable(true);*/
                    break;
            }
        }
        else if(session.isSocialLogin()){
            switch(pos) {
                case Flags.NavMenu.LOGGED_IN_SOCIAL.SEARCH :
                    intent = new Intent(activity, SearchActivity.class);
                    intent.putExtra(Flags.Bundle.Keys.SOURCE_PAGE, Flags.Bundle.Values.NEW_SEARCH);
                    intent.putExtra(Flags.Bundle.Keys.TO_SEARCH_FORM, true);
                    activity.startActivity(intent);
                    break;
                case Flags.NavMenu.LOGGED_IN_SOCIAL.SAVED_SEARCHES:
                    intent = new Intent(activity, SavedSearchActivity.class);
                    activity.startActivity(intent);
                    break;
                case Flags.NavMenu.LOGGED_IN_SOCIAL.WALL_OF_DEALS:
                    WallOfDeals.setDeals(activity);
                    break;
                case Flags.NavMenu.LOGGED_IN_SOCIAL.GARAGE :
                    intent = new Intent(activity, GarageActivity.class);
                    activity.startActivity(intent);
                    break;
                case Flags.NavMenu.LOGGED_IN_SOCIAL.COMPARE_CARS :
                    Intent compareCars = new Intent(activity, CompareCar.class);
                    compareCars.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(compareCars);
                    break;
                case Flags.NavMenu.LOGGED_IN_SOCIAL.CAR_INSURANCE :
                    Intent carInsurance = new Intent(activity, CarInsurance.class);
                    carInsurance.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(carInsurance);
                    break;
                case Flags.NavMenu.LOGGED_IN_SOCIAL.CAR_FINANCE :
                    Intent carFinance = new Intent(activity, CarFinance.class);
                    carFinance.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(carFinance);
                    break;
                case Flags.NavMenu.LOGGED_IN_SOCIAL.NOTIFICATION_HISTORY :
                    intent = new Intent(activity, NotificationHistory.class);
                    activity.startActivity(intent);
                    break;
                case Flags.NavMenu.LOGGED_IN_SOCIAL.WMCW:
                    Intent wmcw = new Intent(activity, WhatsMyCarWorth.class);
                    activity.startActivity(wmcw);
                    break;
                case Flags.NavMenu.LOGGED_IN_SOCIAL.COUPON:
                    Intent coupon = new Intent(activity, CouponActivity.class);
                    activity.startActivity(coupon);
                    break;
                case Flags.NavMenu.LOGGED_IN_SOCIAL.CAR_PARTS:
                    Intent carparts = new Intent(activity, CarPartsActivity.class);
                    activity.startActivity(carparts);
                    break;
                case Flags.NavMenu.LOGGED_IN_SOCIAL.LOGOUT :
                    session.logoutUser();
                    AppController.getInstance().checkSession();
                    break;
                /*case Flags.NavMenu.LOGGED_IN_SOCIAL.TERMS_AND_CONDITIONS :
                    Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(Flags.URL.TERMS));
                    browse.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(browse);
                    break;*/
                case Flags.NavMenu.LOGGED_IN_SOCIAL.ABOUT:
                    Intent aboutActivity = new Intent(activity, AboutActivity.class);
                    activity.startActivity(aboutActivity);
                    /*AppStartModal modalFrag = AppStartModal.newInstance();
                    modalFrag.show(activity.getFragmentManager(), "dialog");
                    modalFrag.setCancelable(true);*/
                    break;
            }
        }
        else {
            switch(pos) {
                /*case Flags.NavMenu.LOGGED_IN.PROFILE :
                    break;*/
                case Flags.NavMenu.LOGGED_IN.SETTINGS:
                    intent = new Intent(activity, UserSettings.class);
                    activity.startActivity(intent);
                    break;
                case Flags.NavMenu.LOGGED_IN.SEARCH :
                    intent = new Intent(activity, SearchActivity.class);
                    intent.putExtra(Flags.Bundle.Keys.SOURCE_PAGE, Flags.Bundle.Values.NEW_SEARCH);
                    intent.putExtra(Flags.Bundle.Keys.TO_SEARCH_FORM, true);
                    activity.startActivity(intent);
                    break;
                case Flags.NavMenu.LOGGED_IN.SAVED_SEARCHES:
                    intent = new Intent(activity, SavedSearchActivity.class);
                    activity.startActivity(intent);
                    break;
                case Flags.NavMenu.LOGGED_IN.WALL_OF_DEALS:
                    WallOfDeals.setDeals(activity);
                    break;
                case Flags.NavMenu.LOGGED_IN.GARAGE :
                    intent = new Intent(activity, GarageActivity.class);
                    activity.startActivity(intent);
                    break;
                case Flags.NavMenu.LOGGED_IN.COMPARE_CARS :
                    Intent compareCars = new Intent(activity, CompareCar.class);
                    compareCars.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(compareCars);
                    break;
                case Flags.NavMenu.LOGGED_IN.CAR_INSURANCE :
                    Intent carInsurance = new Intent(activity, CarInsurance.class);
                    carInsurance.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(carInsurance);
                    break;
                case Flags.NavMenu.LOGGED_IN.CAR_FINANCE :
                    Intent carFinance = new Intent(activity, CarFinance.class);
                    carFinance.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(carFinance);
                    break;
                case Flags.NavMenu.LOGGED_IN.NOTIFICATION_HISTORY :
                    intent = new Intent(activity, NotificationHistory.class);
                    activity.startActivity(intent);
                    break;
                case Flags.NavMenu.LOGGED_IN.WMCW:
                    Intent wmcw = new Intent(activity, WhatsMyCarWorth.class);
                    activity.startActivity(wmcw);
                    break;
                case Flags.NavMenu.LOGGED_IN.COUPON:
                    Intent coupon = new Intent(activity, CouponActivity.class);
                    activity.startActivity(coupon);
                    break;
                case Flags.NavMenu.LOGGED_IN.CAR_PARTS:
                    Intent carparts = new Intent(activity, CarPartsActivity.class);
                    activity.startActivity(carparts);
                    break;
                case Flags.NavMenu.LOGGED_IN.LOGOUT :
                    session.logoutUser();
                    AppController.getInstance().checkSession();
                    break;
                /*case Flags.NavMenu.LOGGED_IN.TERMS_AND_CONDITIONS :
                    Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(Flags.URL.TERMS));
                    browse.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(browse);
                    break;*/
                case Flags.NavMenu.LOGGED_IN.ABOUT:
                    Intent aboutActivity = new Intent(activity, AboutActivity.class);
                    activity.startActivity(aboutActivity);
                    /*AppStartModal modalFrag = AppStartModal.newInstance();
                    modalFrag.show(activity.getFragmentManager(), "dialog");
                    modalFrag.setCancelable(true);*/
                    break;
            }
        }
    }
}