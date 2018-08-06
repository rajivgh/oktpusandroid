package com.app.oktpus.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import com.app.oktpus.activity.SearchActivity;
import com.app.oktpus.model.EditSearchRangeAttributes;
import com.app.oktpus.constants.Flags;

/**
 * Created by Gyandeep on 8/2/17.
 */

public class WallOfDeals {

    public static void setDeals(Activity activity) {
        Map<String, String> attrBMA = new HashMap<String, String>();
        attrBMA.put("min","10");
        attrBMA.put("max","100");

        EditSearchRangeAttributes bma = new Gson().fromJson(new Gson().toJson(attrBMA),
                new TypeToken<EditSearchRangeAttributes>(){}.getType());

        Bundle bundle = new Bundle();
        bundle.putParcelable(Flags.Keys.BELOW_MARKET_AVG, bma);

        /*if(activity instanceof SearchActivity) {
            if(((SearchActivity)activity).mPager.getCurrentItem() != 0) {
                ((SearchActivity)activity).mPager.setCurrentItem(0);
            }
            ((SearchActivity)activity).loadWallOfDeals(Flags.Bundle.Values.WALL_OF_DEALS, bundle);
        }
        else {
            Intent intent = new Intent(activity, SearchActivity.class);
            intent.putExtra(Flags.Bundle.Keys.ATTR_RANGE_PARCEL, bundle);
            intent.putExtra(Flags.Bundle.Keys.WALL_OF_DEALS, true);
            intent.putExtra(Flags.Bundle.Keys.SOURCE_PAGE, Flags.Bundle.Values.WALL_OF_DEALS);
            intent.putExtra(Flags.Bundle.Keys.TO_SEARCH_FORM, true);
            activity.startActivity(intent);
        }*/

        if(activity instanceof SearchActivity) {
            ((SearchActivity)activity).setWallOfDeals(Flags.Bundle.Values.WALL_OF_DEALS, bundle);
        }
        else {
            Intent intent = new Intent(activity, SearchActivity.class);
            intent.putExtra(Flags.Bundle.Keys.ATTR_RANGE_PARCEL, bundle);
            intent.putExtra(Flags.Bundle.Keys.WALL_OF_DEALS, true);
            intent.putExtra(Flags.Bundle.Keys.SOURCE_PAGE, Flags.Bundle.Values.WALL_OF_DEALS);
            intent.putExtra(Flags.Bundle.Keys.TO_SEARCH_FORM, true);
            activity.startActivity(intent);
        }


    }
}