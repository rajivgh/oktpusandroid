package com.app.oktpus.controller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.oktpus.R;
import com.app.oktpus.model.CouponModel;

import java.util.List;


/**
 * Created by remees on 24/6/18.
 */

public class CouponRecyclerController extends RecyclerView.Adapter<CouponRecyclerController.MyViewHolder> {

    private List<CouponModel> couponList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, description;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title_coupon);
            description = (TextView) view.findViewById(R.id.genre_coupon);
            year = (TextView) view.findViewById(R.id.year_coupon);
        }
    }


    public CouponRecyclerController(List<CouponModel> moviesList) {
        this.couponList = moviesList;
    }

    @Override
    public CouponRecyclerController.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coupon_list_row, parent, false);

        return new CouponRecyclerController.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CouponRecyclerController.MyViewHolder holder, int position) {
        CouponModel model = couponList.get(position);
        holder.title.setText(model.getTitle());
        holder.description.setText(model.getDescription());
        holder.year.setText(model.getendDate());
    }

    @Override
    public int getItemCount() {
        return couponList.size();
    }
}

