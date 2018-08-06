package com.app.oktpus.controller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.app.oktpus.R;
import com.app.oktpus.model.CarpartsModel;

import java.util.List;


/**
 * Created by remees on 24/6/18.
 */

public class CarpartsRecyclerController extends RecyclerView.Adapter<CarpartsRecyclerController.MyViewHolder> {

    private List<CarpartsModel> carpartsList;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, price, descriton;
        NetworkImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title_parts);
            descriton = (TextView) view.findViewById(R.id.descriton_parts);
            price = (TextView) view.findViewById(R.id.price_parts);
            image = (NetworkImageView) view.findViewById(R.id.image);
        }
    }


    public CarpartsRecyclerController(List<CarpartsModel> moviesList) {
        this.carpartsList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.carparts_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CarpartsModel model = carpartsList.get(position);
        holder.title.setText(model.getTitle());
        holder.descriton.setText(model.getDescription());
        holder.price.setText(model.getPrice());
        holder.image.setImageUrl(model.getimage(),imageLoader);
    }

    @Override
    public int getItemCount() {
        return carpartsList.size();
    }
}