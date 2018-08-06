package com.app.oktpus.utils.CarParts;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.app.oktpus.fragment.CarParts;
import com.app.oktpus.R;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * Created by Gyandeep on 23/7/17.
 */

public class JsonParsingManager {

    public static final String TAG_CAR_PARTS = "carParts";
    public static JsonNode parseResultData(String result, boolean isContainerNode) throws IOException {
        ObjectMapper objMapper = new ObjectMapper();
        JsonNode jsonNode = objMapper.readTree(result);
        JsonNode treeNode;
        if(isContainerNode) {
            treeNode = jsonNode.path("result");
        }
        else
            treeNode = jsonNode;

        return treeNode;
    }

    public static void startCarPartsFragmentView(Activity activity, String jsonResultString,
                                                 boolean isContainerNode) throws IOException {
        FragmentTransaction transaction = (activity.getFragmentManager()).beginTransaction();

        //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        //transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        if((activity.getFragmentManager()).findFragmentByTag(TAG_CAR_PARTS)!=null) {
            activity.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationReverse;
            transaction.remove((activity.getFragmentManager()).findFragmentByTag(TAG_CAR_PARTS));
        }


        CarParts carPartsFrag = new CarParts();
        Bundle bundle = new Bundle();
        bundle.putString("key", parseResultData(jsonResultString, isContainerNode).toString());
        bundle.putBoolean("isContainerNode", isContainerNode);
        bundle.putBoolean("newAnim", true);
        carPartsFrag.setArguments(bundle);
        transaction.addToBackStack(TAG_CAR_PARTS);
        carPartsFrag.show(transaction, TAG_CAR_PARTS);

    }
}