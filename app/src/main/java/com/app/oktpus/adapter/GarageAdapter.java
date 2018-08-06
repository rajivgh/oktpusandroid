package com.app.oktpus.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.oktpus.activity.BaseActivity;
import com.app.oktpus.activity.SignupActivity;
import com.app.oktpus.model.SearchResponse;
import com.app.oktpus.model.SearchResultItem;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.controller.AppController;
import com.app.oktpus.listener.OnCallListener;
import com.app.oktpus.R;
import com.app.oktpus.utils.Client;
import com.app.oktpus.utils.SessionManagement;
import com.app.oktpus.utils.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.security.Key;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Gyandeep on 29/5/17.
 */

public class GarageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private SessionManagement mSession, sessionCompareCars;
    private List<SearchResultItem> itemsList;

    public static final int VIEW_TYPE_ITEM = 1;
    private final int VIEW_TYPE_LOADING = 2;

    String currencyFormat;
    String kilometerFormat;
    public GarageAdapter(Context context, List<SearchResultItem> itemsList, SessionManagement session) {
        this.mContext = context;
        this.itemsList = itemsList;
        this.mSession = session;
        sessionCompareCars = new SessionManagement(mContext, null);
        this.currencyFormat = AppController.getInstance().getCurrencyFormat();
        this.kilometerFormat = AppController.getInstance().getKilometerFormat();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if(viewType == VIEW_TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.garage_item, parent, false);
            vh = new GarageAdapter.GarageItemsViewHolder(v);
            return vh;
        }
        else if (viewType == VIEW_TYPE_LOADING){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_bar_layout, parent, false);
            vh = new GarageAdapter.LoadingViewHolder(v);
            return vh;
        }
        else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.blank_view, parent, false);
            vh = new GarageAdapter.BlankViewHolder(v);
            return vh;
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public View oktSpinner;
        public TextView oktLoadingText;
        private RelativeLayout loadingLayout;
        public LoadingViewHolder(View v) {
            super(v);
            oktSpinner = (View) v.findViewById(R.id.okt_spinner);
            oktLoadingText = (TextView) v.findViewById(R.id.okt_loading_txt);
            loadingLayout = (RelativeLayout) v.findViewById(R.id.loading_bar);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) loadingLayout.getLayoutParams();
            params.height = 400;
            loadingLayout.setLayoutParams(params);
            oktLoadingText.setVisibility(View.GONE);
        }
    }

    public class BlankViewHolder extends RecyclerView.ViewHolder {
        public BlankViewHolder(View v) {
            super(v);
        }
    }

    public class GarageItemsViewHolder extends RecyclerView.ViewHolder {
        public TextView itemTitle, price, kilometers, city, marketAvgValue, marketAvgLabel, tvOverflowMenu;
        public ImageView imageView, ivFav, ivShare;
        public String url, shareUrl, img0, title, date;
        public int productID = -1;
        public Intent share = new Intent(Intent.ACTION_SEND);
        LinearLayout llMarketAvg;
        View vMarketAverageBar;
        public GarageItemsViewHolder(View view) {
            super(view);
            try {
                itemTitle = (TextView) view.findViewById(R.id.tv_nh_title);
                price = (TextView) view.findViewById(R.id.tv_nh_price);
                /*sentDate = (TextView) view.findViewById(R.id.tv_nh_sent_date);*/
                imageView = (ImageView) view.findViewById(R.id.niv_notif_history);
                kilometers = (TextView) view.findViewById(R.id.tv_nh_kms);
                city = (TextView) view.findViewById(R.id.tv_nh_city);
                ivFav = (ImageView) view.findViewById(R.id.iv_fav);
                tvOverflowMenu = (TextView) view.findViewById(R.id.overflow_menu);
                ivShare = (ImageView) view.findViewById(R.id.iv_share);
                //bma = (TextView) view.findViewById(R.id.tv_nh_bma);
                marketAvgValue = (TextView) view.findViewById(R.id.market_avg_value);
                marketAvgLabel = (TextView) view.findViewById(R.id.market_avg_label);
                llMarketAvg = (LinearLayout) view.findViewById(R.id.ll_ma_bg);
                vMarketAverageBar = (View) view.findViewById(R.id.v_ma_bar);
                //overflow = (ImageView) view.findViewById(R.id.overflow);
                img0 = new String();
                //imageView.setBackgroundResource(R.drawable.spinner);
                /*AnimationDrawable frameAnimation = (AnimationDrawable) imageView.getBackground();
                frameAnimation.start();*/

                itemTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        mContext.startActivity(i);
                    }
                });

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        mContext.startActivity(i);
                    }
                });

                ivShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utility.shareIntent(share, mContext, shareUrl, title, date);
                    }
                });
            }
            catch(Exception e) {
                Log.e("GarageAdapter", e.getMessage());
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof GarageAdapter.LoadingViewHolder){
            AnimationDrawable frameAnimation = (AnimationDrawable) ((GarageAdapter.LoadingViewHolder)holder).oktSpinner.getBackground();
            frameAnimation.start();
        } else if(itemsList.size() > 0 && (position) < (itemsList.size()) && holder instanceof GarageAdapter.GarageItemsViewHolder) {
            final SearchResultItem item = itemsList.get(position);
            final GarageItemsViewHolder itemHolder = ((GarageAdapter.GarageItemsViewHolder)holder);
            itemHolder.itemTitle.setText(item.getItemTitle().trim());
            //itemHolder.sentDate.setText(item.getSentDate());
            itemHolder.url = item.getHref_url();
            itemHolder.shareUrl = item.getHref_url();
            itemHolder.city.setText(item.getPostDate()+ " in " +item.getCity());
            itemHolder.productID = item.getProductID();

            Drawable drawable = (item.isFavActive())? mContext.getResources().getDrawable(R.drawable.ic_fav_active) : mContext.getResources().getDrawable(R.drawable.ic_fav_inactive);
            itemHolder.ivFav.setImageDrawable(drawable);
            itemHolder.title = item.getItemTitle();
            itemHolder.date = item.getPostDate() + " in " + item.getCity();

            itemHolder.ivFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemHolder.ivFav.isEnabled()) {
                        itemHolder.ivFav.setEnabled(false);
                        actionRequest(item.getProductID(), Flags.Garage.FlagName.FAVORITE,
                                (item.isFavActive()? Flags.Garage.FlagAction.UNFLAG : Flags.Garage.FlagAction.FLAG),
                                itemHolder);
                        item.setFavActive((item.isFavActive())? false : true);
                        notifyItemChanged(position);
                    }
                }
            });

            itemHolder.tvOverflowMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(item.getProductID() != -1) {
                        PopupMenu popup = new PopupMenu(mContext, itemHolder.tvOverflowMenu);
                        popup.inflate(R.menu.search_result_item);
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
                                    case R.id.item_hide:
                                        if(mSession.isLoggedIn()) {
                                            actionRequest(item.getProductID(), Flags.Garage.FlagName.HIDDEN, Flags.Garage.FlagAction.FLAG, itemHolder);
                                            itemsList.remove(item);
                                            notifyItemRemoved(position);
                                            notifyItemRangeChanged(position, getItemCount());
                                        }
                                        else {
                                            mContext.startActivity(new Intent(mContext, SignupActivity.class));
                                        }
                                        break;
                                    case R.id.item_compare: {
                                        sessionCompareCars.addCompareCarProductId(String.valueOf(item.getProductID()), String.valueOf(item.getItemTitle()));
                                        break;
                                    }
                                }
                                return false;
                            }
                        });
                        popup.show();
                    }
                }
            });

            /*if(item.getMaKeyword().equals(Flags.Keys.MA_BELOW) && item.isMaShow()){
                itemHolder.marketAvgLabel.setVisibility(View.VISIBLE);
                itemHolder.marketAvgValue.setVisibility(View.VISIBLE);

                itemHolder.marketAvgLabel.setText("Below market by ");
                setCurrencyFormat(itemHolder, currencyFormat, item);
            }
            else {
                itemHolder.marketAvgLabel.setVisibility(View.GONE);
                itemHolder.marketAvgValue.setVisibility(View.GONE);
            }*/
            if(item.getMaKeyword().equals(Flags.Keys.MA_ABOVE) && item.isMaShow()) {
                itemHolder.marketAvgLabel.setText("Above market by");
                itemHolder.marketAvgValue.setTextColor(mContext.getResources().getColor(R.color.colorTextButtonRed));
                itemHolder.marketAvgValue.setTypeface(Typeface.DEFAULT);
                itemHolder.llMarketAvg.setVisibility(View.VISIBLE);
                itemHolder.vMarketAverageBar.setVisibility(View.INVISIBLE);
                setCurrencyFormat(itemHolder, currencyFormat, item);
            }
            else if(item.getMaKeyword().equals(Flags.Keys.MA_BELOW) && item.isMaShow()){
                itemHolder.marketAvgLabel.setText("Below market by");
                itemHolder.marketAvgValue.setTextColor(mContext.getResources().getColor(R.color.headingGreen));
                itemHolder.marketAvgValue.setTypeface(Typeface.DEFAULT_BOLD);
                itemHolder.llMarketAvg.setVisibility(View.VISIBLE);
                itemHolder.vMarketAverageBar.setVisibility(View.VISIBLE);
                setCurrencyFormat(itemHolder, currencyFormat, item);
            }
            else {
                itemHolder.llMarketAvg.setVisibility(View.GONE);
            }

            if(currencyFormat.equals("USD")){
                itemHolder.price.setText(item.getCurrency_usd().replaceAll("\"",""));
            }else if(currencyFormat.equals("CAD")){
                itemHolder.price.setText(item.getCurrency_cad().replaceAll("\"",""));
            }

            if(kilometerFormat.equals("kilometers")){
                itemHolder.kilometers.setText(item.getKilometers_kilometers().replaceAll("\"",""));
            }else if(kilometerFormat.equals("miles")){
                itemHolder.kilometers.setText(item.getKilometers_miles().replaceAll("\"",""));
            }

            if(item != null) {
                itemHolder.img0 = item.getImage();
                /*if(!((NotificationsViewHolder)holder).img0.startsWith("http:")) ((NotificationsViewHolder)holder).img0
                        = "http:" + ((NotificationsViewHolder)holder).img0;*/

                Log.d(Flags.ActivityTag.GARAGE, "img url-> "+ itemHolder.img0);
                Glide.with(AppController.getInstance().getApplicationContext())
                        .load(itemHolder.img0)
                        .error(R.drawable.default_img)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(itemHolder.imageView);
                //((TextViewHolder)holder).imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            else {
                Glide.clear(itemHolder.imageView);
                itemHolder.imageView.setImageDrawable(null);
            }
        }
    }

    void setCurrencyFormat(GarageAdapter.GarageItemsViewHolder tvHolder, String currencyFormat, SearchResultItem item) {
        if(currencyFormat.equals("USD")){
            tvHolder.marketAvgValue.setText(item.getbelowMarketBy_usd().replaceAll("\"",""));
        }else if(currencyFormat.equals("CAD")){
            tvHolder.marketAvgValue.setText(item.getbelowMarketBy_cad().replaceAll("\"",""));
        }
    }

    private boolean isLoadingBarEnabled = true;
    private boolean isPositionHeader(int position) {
        return position == 0;
    }
    public int headerViewType() {
        if(isLoadingBarEnabled) return VIEW_TYPE_LOADING;
        else return VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position) && isLoadingBarEnabled) {
            return headerViewType();
        }
        else
            return VIEW_TYPE_ITEM;
    }

    public void enableLoadingBar(boolean isEnabled) {
        this.isLoadingBarEnabled = isEnabled;
    }

    @Override
    public int getItemCount() {
        if(isLoadingBarEnabled)
            return 1;
        return itemsList.size();
    }

    private void actionRequest(final int productID, final String flagName, final String flagAction, final GarageItemsViewHolder holder) {
        try {
            //Log.d(Flags.ActivityTag.GARAGE,"attrib url: " + url);
            Client jsObjRequest = new Client(Request.Method.POST, Flags.getGarageActionUrl(mSession), SearchResponse.class, new OnCallListener() {
                @Override
                public void nwResponseData(String pData) {
                }
            }, new Response.Listener<Object>() {
                @Override
                public void onResponse(Object response) {
                    SearchResponse res = (SearchResponse)response;
                    if(res.getStatus() == 1) {
                        if(flagName.equals(Flags.Garage.FlagName.FAVORITE)) {
                            holder.ivFav.setEnabled(true);
                        }

                        if(flagName.equals(Flags.Garage.FlagName.HIDDEN)) {
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(Flags.ActivityTag.GARAGE, "Error: " + error);
                    holder.ivFav.setEnabled(true);
                }
            }) {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    return Flags.buildGarageActionRequest(productID, flagName, flagAction);
                }
            };
            String tag_json_obj = "json_obj_req";
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsObjRequest, tag_json_obj);
        }
        catch(Exception e) {
            Log.d(Flags.ActivityTag.GARAGE, e.getMessage());
        }
    }
}