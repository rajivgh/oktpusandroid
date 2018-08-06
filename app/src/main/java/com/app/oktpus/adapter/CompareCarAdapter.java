package com.app.oktpus.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.oktpus.activity.CompareCar;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.R;
import com.app.oktpus.responseModel.ProductDetails.Result;
import com.app.oktpus.utils.RecyclerTouchHelper;
import com.app.oktpus.utils.SessionManagement;
import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

/**
 * Created by Gyandeep on 19/6/17.
 */

public class CompareCarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements RecyclerTouchHelper.ItemTouchHelperAdapter {

    public static final int VIEW_TYPE_ADD = 0;
    public static final int VIEW_TYPE_ITEM = 1;

    List<Result> data;
    Context context;
    SessionManagement session;
    Activity activity;
    public CompareCarAdapter(List<Result> listData, Context context, SessionManagement session, Activity activity) {
        this.data = listData;
        this.context = context;
        this.session = session;
        this.activity = activity;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(data, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {}

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle, tvCity, tvDate, tvPrice, tvKms, tvEngine, tvPassengers,
                tvColor, tvInteriorColor, tvDrivetrain, tvDoors, tvMarketAvg, tvMarketAvgValue,  tvMake, tvModel, tvBody, tvTransmission, tvFuel, tvSeeDetails;
        ImageView ivImg, ivClose;
        RelativeLayout rlRemoveItem;
        LinearLayout llItemContainer;
        SpannableString spannableStringMarketAvg;
        RatingBar ratingBar;
        String url;
        public ItemViewHolder(View v) {
            super(v);
            tvTitle = (TextView)v.findViewById(R.id.tv_title);
            tvCity = (TextView)v.findViewById(R.id.tv_city);
            tvDate = (TextView)v.findViewById(R.id.tv_date);
            tvPrice = (TextView)v.findViewById(R.id.tv_price);
            tvKms = (TextView)v.findViewById(R.id.tv_kms);
            tvEngine = (TextView)v.findViewById(R.id.tv_engine);
            tvPassengers = (TextView)v.findViewById(R.id.tv_passengers);
            tvColor = (TextView)v.findViewById(R.id.tv_color);
            tvInteriorColor = (TextView)v.findViewById(R.id.tv_interior_color);
            tvDrivetrain = (TextView)v.findViewById(R.id.tv_drivetrain);
            tvDoors = (TextView)v.findViewById(R.id.tv_doors);
            tvMake = (TextView) v.findViewById(R.id.tv_make);
            tvModel = (TextView) v.findViewById(R.id.tv_model);
            tvTransmission = (TextView) v.findViewById(R.id.tv_transmission);
            tvBody = (TextView) v.findViewById(R.id.tv_body);
            tvMarketAvg = (TextView) v.findViewById(R.id.tv_ma);
            tvMarketAvgValue = (TextView) v.findViewById(R.id.tv_ma_value);
            /*tvAccessories = (TextView) v.findViewById(R.id.tv_accessories);
            tvCylinder = (TextView) v.findViewById(R.id.tv_cylinder);
            tvStockNumber = (TextView) v.findViewById(R.id.tv_stock_number);*/
            tvFuel = (TextView) v.findViewById(R.id.tv_fuel);
            tvSeeDetails = (TextView) v.findViewById(R.id.tv_see_details);
            //tvRating = (TextView) v.findViewById(R.id.tv_rating);
            //tvSafetyRating = (TextView) v.findViewById(R.id.tv_safety_rating);

            llItemContainer = (LinearLayout) v.findViewById(R.id.root_layout_compare_car_item);
            /*android:divider="?android:attr/dividerVertical"*/
            spannableStringMarketAvg = new SpannableString("");

            llItemContainer.setDividerDrawable(context.getResources().getDrawable(R.drawable.vertical_divider));
            rlRemoveItem = (RelativeLayout) v.findViewById(R.id.rl_header);
            /*ratingBar = (RatingBar) v.findViewById(R.id.rb_rating);
            ratingBar.setStepSize(1);*/
            ivImg = (ImageView)v.findViewById(R.id.iv_img);
            ivClose = (ImageView) v.findViewById(R.id.iv_close);
            url = "";
            tvSeeDetails.setText("See Details");
            tvSeeDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    context.startActivity(i);
                }
            });
        }
    }

    public class AddItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llAddItem;
        TextView tvNoItems;
        public AddItemViewHolder(View v){
            super(v);
            llAddItem = (LinearLayout) v.findViewById(R.id.ll_add_item);
            tvNoItems = (TextView) v.findViewById(R.id.tv_no_items);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(data.size() > 0)
            return VIEW_TYPE_ITEM;
        else
            return VIEW_TYPE_ADD;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if(viewType == VIEW_TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_compare_car, parent, false);
            vh = new ItemViewHolder(v);
            return vh;
        }
        else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_compare_car_add, parent, false);
            vh = new AddItemViewHolder(v);
            return vh;
        }
    }

    /*public RawValues getRawValues(int position) {
        return data.get(position).getRawValues();
    }*/

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ItemViewHolder) {
            ItemViewHolder item = (ItemViewHolder)holder;
            if(null != data && data.size() > 0 && position <= data.size()-1) {
                item.tvTitle.setTypeface(Typeface.DEFAULT_BOLD);
                if(null != data.get(position).getItemTitle() && !data.get(position).getItemTitle().isEmpty()) item.tvTitle.setText(data.get(position).getItemTitle()); else item.tvTitle.setText("-");
                if(null != data.get(position).getCity() && !data.get(position).getCity().isEmpty()) item.tvCity.setText(data.get(position).getCity()); else item.tvCity.setText("-");
                if(null != data.get(position).getPostDate() && !data.get(position).getPostDate().isEmpty()) item.tvDate.setText(data.get(position).getPostDate()); else item.tvDate.setText("-");
                item.tvPrice.setText(data.get(position).getPriceList().getUsd());
                //if(null != data.get(position).getStock_number() && !data.get(position).getStock_number().isEmpty()) item.tvStockNumber.setText(data.get(position).getStock_number()); else item.tvStockNumber.setText("-");
                //if(null != data.get(position).getCylinders() && !data.get(position).getCylinders().isEmpty()) item.tvCylinder.setText(data.get(position).getCylinders()); else item.tvCylinder.setText("-");
                //item.tvSafetyRating.setText(data.get(position).getSafetyRating());
                if(null != data.get(position).getBody() && !data.get(position).getBody().isEmpty()) item.tvBody.setText(data.get(position).getBody()); else item.tvBody.setText("-");
                if(null != data.get(position).getTransmission() && !data.get(position).getTransmission().isEmpty()) item.tvTransmission.setText(data.get(position).getTransmission()); else item.tvTransmission.setText("-");
                if(null != data.get(position).getColour() && !data.get(position).getColour().isEmpty()) item.tvColor.setText(data.get(position).getColour()); else item.tvColor.setText("-");
                if(null != data.get(position).getDoors() && !data.get(position).getDoors().isEmpty()) item.tvDoors.setText(data.get(position).getDoors()); else item.tvDoors.setText("-");
                if(null != data.get(position).getDrivetrain() && !data.get(position).getDrivetrain().isEmpty()) item.tvDrivetrain.setText(data.get(position).getDrivetrain()); else item.tvDrivetrain.setText("-");
                if(null != data.get(position).getEngine() && !data.get(position).getEngine().isEmpty()) item.tvEngine.setText(data.get(position).getEngine()); else item.tvEngine.setText("-");
                if(null != data.get(position).getMake() && !data.get(position).getMake().isEmpty()) item.tvMake.setText(data.get(position).getMake()); else item.tvMake.setText("-");
                if(null != data.get(position).getModel() && !data.get(position).getModel().isEmpty()) item.tvModel.setText(data.get(position).getModel()); else item.tvModel.setText("-");
                if(null != data.get(position).getPassengers() && !data.get(position).getPassengers().isEmpty()) item.tvPassengers.setText(data.get(position).getPassengers()); else item.tvPassengers.setText("-");
                if(null != data.get(position).getInteriorColor() && !data.get(position).getInteriorColor().isEmpty()) item.tvInteriorColor.setText(data.get(position).getInteriorColor()); else item.tvInteriorColor.setText("-");
                if(null != data.get(position).getHrefUrl()) item.url = data.get(position).getHrefUrl();
                /*float rating = parseRatingVal((data.get(position).getRating()));
                item.tvRating.setText((rating != 0.0) ? String.valueOf(rating)+"/5.0" : "Not rated");*/
                //item.ratingBar.setRating(Float.valueOf(parseRatingVal(data.get(position).getRating())));
                if(null != data.get(position).getFuel() && !data.get(position).getFuel().isEmpty()) item.tvFuel.setText(data.get(position).getFuel()); else item.tvFuel.setText("-");
                //if(null != data.get(position).getAccessories() && !data.get(position).getAccessories().isEmpty()) item.tvAccessories.setText(data.get(position).getAccessories()); else item.tvAccessories.setText("-");
                if(null != data.get(position).getKmsList() && !data.get(position).getKmsList().getKilometers().isEmpty())
                    item.tvKms.setText(data.get(position).getKmsList().getKilometers());
                else item.tvKms.setText("-");

                if(data.get(position).getMaShow()) {
                    if(data.get(position).getMaKeyword().equals(Flags.Keys.MA_ABOVE)) {
                        item.tvMarketAvg.setText("Above market by");
                        item.tvMarketAvgValue.setText(data.get(position).getMaDifference().getUsd());
                        item.tvMarketAvgValue.setTextColor(context.getResources().getColor(R.color.colorTextButtonRed));
                    }
                    else {
                        item.tvMarketAvg.setText("Below market by");
                        item.tvMarketAvgValue.setText(data.get(position).getMaDifference().getUsd());
                        item.tvMarketAvgValue.setTextColor(context.getResources().getColor(R.color.headingGreen));
                    }
                }
                else {
                    item.tvMarketAvg.setText("-");
                    item.tvMarketAvgValue.setText("");
                    //item.tvMarketAvg.setTextColor(context.getResources().getColor(R.color.colorAccent));
                }

                if(item != null) {
                    Glide.with(context)
                            .load(data.get(position).getImageUrl())
                            .error(R.drawable.default_img)
                            //.placeholder(context.getResources().getDrawable(R.drawable.shape_circular_placeholder))
                            //.diskCacheStrategy(DiskCacheStrategy.ALL)
                            //.bitmapTransform(new RoundedCornersTransformation(context, 100, 0))
                            .into(item.ivImg);
                }
                else {
                    Glide.clear(item.ivImg);
                    item.ivImg.setImageDrawable(null);
                }
                item.rlRemoveItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println(data.get(position).getItemTitle());
                        session.removeCompareCarItem(data.get(position).getProductId());
                        data.remove(data.get(position));
                        ((CompareCar)activity).updateTotalItems(data.size());
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, getItemCount());
                    }
                });
            }
        }
    }

    /*public float parseRatingVal(String val) {
        if(val != null && !val.equals("null") && !val.isEmpty()) {
            return Float.valueOf(val);
        }
        else
            return 0;
    }*/

    @Override
    public int getItemCount() {
        return data.size();
    }
}