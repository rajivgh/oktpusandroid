package com.app.oktpus.fragment;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.oktpus.adapter.CarPartsItemAdapter;
import com.app.oktpus.model.CarPartsItem;
import com.app.oktpus.R;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Gyandeep on 7/7/17.
 */

public class CarParts extends DialogFragment{

    public CarParts() {}
    List<CarPartsItem> listItems;
    RecyclerView recyclerView;
    CarPartsItemAdapter carPartsItemAdapter;
    Bundle mBundle;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_car_parts, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_car_parts);

        mBundle = getArguments();

        String lvl0 = mBundle.getString("key");

        listItems = new ArrayList<>();
        ObjectMapper om = new ObjectMapper();
        ObjectNode node;
        try {
            node = (ObjectNode) om.readTree(lvl0);
            //Map<String, Object> map = om.convertValue(node, Map.class);
            Iterator itr = node.iterator();

            while(itr.hasNext()) {
                ObjectNode objNode = (ObjectNode) itr.next();

                listItems.add(new CarPartsItem((objNode.get("name") != null)? objNode.get("name").getTextValue() : "",
                        (objNode.get("external_url") != null)?objNode.get("external_url").getTextValue() : "",
                        (objNode.get("children").isArray())? null : (ObjectNode) objNode.get("children")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        carPartsItemAdapter = new CarPartsItemAdapter(getDialog(), getActivity(), listItems, new CallbackListener() {
            @Override
            public boolean animationConfigReverse() {
                getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimationReverse;
                return true;
            }
        });

        if(mBundle.containsKey("isContainerNode"))
            carPartsItemAdapter.isContainerNode = mBundle.getBoolean("isContainerNode");

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(carPartsItemAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(mBundle.containsKey("newAnim")) {
            if(mBundle.getBoolean("newAnim")) {
                mBundle.putBoolean("newAnim", false);
                getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            }
            else {
                getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimationReverse;
            }
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);

        /*
        * Remove all Carparts fragments attached to the hierarchy
        * */
        for(int i=0; i<getFragmentManager().getBackStackEntryCount(); i++) {
            if(getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStack();
            }
        }
    }

    public static CarParts newInstance() {
        CarParts carParts = new CarParts();
        return carParts;
    }

    public interface CallbackListener{
        boolean animationConfigReverse();
    }
}