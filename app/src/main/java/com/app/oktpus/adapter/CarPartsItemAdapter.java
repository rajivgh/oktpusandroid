package com.app.oktpus.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.oktpus.model.CarPartsItem;
import com.app.oktpus.fragment.CarParts;
import com.app.oktpus.R;
import com.app.oktpus.utils.CarParts.JsonParsingManager;

import java.io.IOException;
import java.util.List;

/**
 * Created by Gyandeep on 13/7/17.
 */

public class CarPartsItemAdapter extends RecyclerView.Adapter{

    private List<CarPartsItem> mItemsList;
    public static final int VIEW_TYPE_ITEM = 1;
    public static final int VIEW_TYPE_BACK = 2;
    public static final int VIEW_TYPE_BLANK = 3;

    private Activity mActivity;
    private Dialog mDialog;
    private CarParts.CallbackListener mListener;
    public CarPartsItemAdapter(Dialog dialog, Activity activity, List<CarPartsItem> itemsList, CarParts.CallbackListener listener) {
        this.mItemsList = itemsList;
        this.mActivity = activity;
        this.mDialog = dialog;
        this.mListener = listener;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView tvCarPartsName;
        ImageView ivCarpartsExapnd;
        LinearLayout llRowContainer;
        public ItemViewHolder(View itemView) {
            super(itemView);
            tvCarPartsName = (TextView) itemView.findViewById(R.id.tv_carparts_name);
            llRowContainer = (LinearLayout) itemView.findViewById(R.id.ll_row_container);
            ivCarpartsExapnd = (ImageView) itemView.findViewById(R.id.iv_carparts_expand);
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llBackButton;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            llBackButton = (LinearLayout) itemView.findViewById(R.id.ll_carparts_back);

            llBackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener.animationConfigReverse()){
                        if(mActivity.getFragmentManager().getBackStackEntryCount() > 0) {
                            mActivity.getFragmentManager().popBackStack();
                        }
                    }
                }
            });
        }
    }

    private class BlankViewHolder extends RecyclerView.ViewHolder {
        public BlankViewHolder(View v) {
            super(v);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if(viewType == VIEW_TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.carparts_list_item, parent, false);
            vh = new ItemViewHolder(v);
            return vh;
        }
        else if(viewType == VIEW_TYPE_BACK) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_carparts_backbutton, parent, false);
            vh = new HeaderViewHolder(v);
            return vh;
        }
        else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.blank_view, parent, false);
            vh = new BlankViewHolder(v);
            return vh;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ItemViewHolder) {
            ItemViewHolder itemHolder = (ItemViewHolder)holder;
            itemHolder.tvCarPartsName.setText(mItemsList.get(position-1).getName());

            if(mItemsList.get(position-1).getChildren() != null)
                itemHolder.ivCarpartsExapnd.setVisibility(View.VISIBLE);
            else
                itemHolder.ivCarpartsExapnd.setVisibility(View.INVISIBLE);

            itemHolder.llRowContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mItemsList.get(position-1).getChildren() != null) {
                        try {
                            JsonParsingManager.startCarPartsFragmentView(mActivity, mItemsList.get(position-1).getChildren().toString(), false);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        if(!mItemsList.get(position-1).getUrl().isEmpty()) {
                            mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mItemsList.get(position-1).getUrl())));
                        }
                    }
                }
            });
        }
    }

    public boolean isContainerNode = false;
    @Override
    public int getItemCount() {
        return mItemsList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            if(isContainerNode)
                return VIEW_TYPE_BLANK;
            else
                return VIEW_TYPE_BACK;
        }
        else {
            return VIEW_TYPE_ITEM;
        }
    }
}