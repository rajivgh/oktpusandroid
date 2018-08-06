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
import com.app.oktpus.activity.SignupActivity;
import com.app.oktpus.model.SearchResponse;
import com.app.oktpus.model.SearchResultItem;
import com.app.oktpus.listener.OnCallListener;
import com.app.oktpus.utils.Client;
import com.app.oktpus.utils.SessionManagement;
import com.app.oktpus.utils.Utility;
import com.bumptech.glide.Glide;

import java.util.List;

import com.app.oktpus.constants.Flags;
import com.app.oktpus.controller.AppController;
import com.app.oktpus.R;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by Gyandeep on 9/11/16.
 */

public class NotificationsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private List<SearchResultItem> notificationsList;
    private final int VIEW_TYPE_HEADER = 0;
    private final int VIEW_TYPE_ITEM = 1;
    private final int VIEW_TYPE_LOADING = 2;
    private final int VIEW_TYPE_FOOTER = 3;
    private final int VIEW_TYPE_BLANK = 4;

    //private boolean isFooterEnabled = false;
    private boolean isLoadingBarEnabled = true;

    public class NotificationsViewHolder extends RecyclerView.ViewHolder {
        public TextView itemTitle, price, kilometers, city, marketAvgValue, marketAvgLabel, tvOverflowMenu;
        public ImageView imageView, ivFav, ivShare;
        public int productID = -1;
        public Uri bmpUri;
        public String url, shareUrl, img0, title, date;
        public Intent share = new Intent(Intent.ACTION_SEND);
        LinearLayout llMarketAvg;
        View vMarketAverageBar;
        public NotificationsViewHolder(View view) {
            super(view);
            try {
                itemTitle = (TextView) view.findViewById(R.id.tv_nh_title);
                price = (TextView) view.findViewById(R.id.tv_nh_price);
                imageView = (ImageView) view.findViewById(R.id.niv_notif_history);
                kilometers = (TextView) view.findViewById(R.id.tv_nh_kms);
                city = (TextView) view.findViewById(R.id.tv_nh_city);
                //bma = (TextView) view.findViewById(R.id.tv_nh_bma);
                marketAvgValue = (TextView) view.findViewById(R.id.market_avg_value);
                marketAvgLabel = (TextView) view.findViewById(R.id.market_avg_label);
                llMarketAvg = (LinearLayout) view.findViewById(R.id.ll_ma_bg);
                vMarketAverageBar = (View) view.findViewById(R.id.v_ma_bar);

                ivShare = (ImageView) view.findViewById(R.id.iv_share);
                ivFav = (ImageView) view.findViewById(R.id.iv_fav);
                tvOverflowMenu = (TextView) view.findViewById(R.id.overflow_menu);
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

                price.setOnClickListener(new View.OnClickListener() {
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
                Log.e("NotificationListAdapter", e.getMessage());
            }
        }
    }

    String currencyFormat;
    String kilometerFormat;
    SessionManagement mSession, sessionCompareCars;
    public NotificationsListAdapter(Context mContext, List<SearchResultItem> notificationsList, SessionManagement session) {
        this.mContext = mContext;
        this.notificationsList = notificationsList;
        this.mSession = session;
        sessionCompareCars = new SessionManagement(mContext, null);
        currencyFormat = AppController.getInstance().getCurrencyFormat();
        kilometerFormat = AppController.getInstance().getKilometerFormat();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if(viewType == VIEW_TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.notification_history_item, parent, false);
            vh = new NotificationsListAdapter.NotificationsViewHolder(v);
            return vh;
        }else if(viewType == VIEW_TYPE_FOOTER){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.blank_view, parent, false);

            vh = new NotificationsListAdapter.FooterViewHolder(v);
            return vh;
        }
        else if(viewType == VIEW_TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rv_notif_history_header_layout, parent, false);
            vh = new NotificationsListAdapter.HeaderViewHolder(v);
            return vh;
        }
        else if (viewType == VIEW_TYPE_LOADING){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_bar_layout, parent, false);
            vh = new NotificationsListAdapter.LoadingViewHolder(v);
            return vh;
        }
        else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.blank_view, parent, false);
            vh = new NotificationsListAdapter.BlankViewHolder(v);
            return vh;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position) && isLoadingBarEnabled) {
            return headerViewType();
        }
        /*else if(isPositionFooter(position)) {
            return footerViewType();
        }*/
        else
            return VIEW_TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    /*private boolean isPositionFooter (int position) {
        return position == notificationsList.size () + 1;
    }*/

    public int headerViewType() {
        if(isLoadingBarEnabled) return VIEW_TYPE_LOADING;
        else return VIEW_TYPE_HEADER;
    }

    /*public int footerViewType() {
        if(isFooterEnabled) return VIEW_TYPE_FOOTER;
        else return VIEW_TYPE_BLANK;
    }

    public void enableFooter(boolean isEnabled) { this.isFooterEnabled = isEnabled; }*/
    public void enableLoadingBar(boolean isEnabled) {
        this.isLoadingBarEnabled = isEnabled;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof NotificationsListAdapter.LoadingViewHolder){
            AnimationDrawable frameAnimation = (AnimationDrawable) ((NotificationsListAdapter.LoadingViewHolder)holder).oktSpinner.getBackground();
            frameAnimation.start();
        }
        else if(holder instanceof NotificationsListAdapter.FooterViewHolder) {
            //((FooterViewHolder) holder).display.setVisibility(View.VISIBLE);
        } else if(notificationsList.size() > 0 && (position) < (notificationsList.size()) && holder instanceof NotificationsListAdapter.NotificationsViewHolder) {
            final SearchResultItem item = notificationsList.get(position);
            final NotificationsViewHolder itemsHolder = ((NotificationsViewHolder)holder);
            itemsHolder.itemTitle.setText(item.getItemTitle().trim());
            itemsHolder.url = item.getHref_url();
            itemsHolder.shareUrl = item.getHref_url();
            itemsHolder.city.setText(item.getPostDate()+ " in " +item.getCity());

            itemsHolder.title = item.getItemTitle();
            itemsHolder.date = item.getPostDate() + " in " + item.getCity();

            if(item.getMaKeyword().equals(Flags.Keys.MA_ABOVE) && item.isMaShow()) {
                itemsHolder.marketAvgLabel.setText("Above market by");
                itemsHolder.marketAvgValue.setTextColor(mContext.getResources().getColor(R.color.colorTextButtonRed));
                itemsHolder.marketAvgValue.setTypeface(Typeface.DEFAULT);
                itemsHolder.llMarketAvg.setVisibility(View.VISIBLE);
                itemsHolder.vMarketAverageBar.setVisibility(View.INVISIBLE);
                setCurrencyFormat(itemsHolder, currencyFormat, item);
            }
            else if(item.getMaKeyword().equals(Flags.Keys.MA_BELOW) && item.isMaShow()){
                itemsHolder.marketAvgLabel.setText("Below market by");
                itemsHolder.marketAvgValue.setTextColor(mContext.getResources().getColor(R.color.headingGreen));
                itemsHolder.marketAvgValue.setTypeface(Typeface.DEFAULT_BOLD);
                itemsHolder.llMarketAvg.setVisibility(View.VISIBLE);
                itemsHolder.vMarketAverageBar.setVisibility(View.VISIBLE);
                setCurrencyFormat(itemsHolder, currencyFormat, item);
            }
            else {
                itemsHolder.llMarketAvg.setVisibility(View.GONE);
            }
            /*if(item.getMaKeyword().equals(Flags.Keys.MA_BELOW) && item.isMaShow()){
                itemsHolder.marketAvgLabel.setVisibility(View.VISIBLE);
                itemsHolder.marketAvgValue.setVisibility(View.VISIBLE);

                itemsHolder.marketAvgLabel.setText("Below market by ");
                setCurrencyFormat(itemsHolder, currencyFormat, item);
            }
            else {
                itemsHolder.marketAvgLabel.setVisibility(View.GONE);
                itemsHolder.marketAvgValue.setVisibility(View.GONE);
            }*/

            if(currencyFormat.equals("USD")){
                itemsHolder.price.setText(item.getCurrency_usd().replaceAll("\"",""));
            }else if(currencyFormat.equals("CAD")){
                itemsHolder.price.setText(item.getCurrency_cad().replaceAll("\"",""));
            }

            if(kilometerFormat.equals("kilometers")){
                itemsHolder.kilometers.setText(item.getKilometers_kilometers().replaceAll("\"",""));
            }else if(kilometerFormat.equals("miles")){
                itemsHolder.kilometers.setText(item.getKilometers_miles().replaceAll("\"",""));
            }

            itemsHolder.productID = item.getProductID();

            Drawable drawable = (item.isFavActive())? mContext.getResources().getDrawable(R.drawable.ic_fav_active) : mContext.getResources().getDrawable(R.drawable.ic_fav_inactive);
            itemsHolder.ivFav.setImageDrawable(drawable);

            itemsHolder.ivFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(item.getProductID() != -1) {
                        if(itemsHolder.ivFav.isEnabled()) {
                            itemsHolder.ivFav.setEnabled(false);
                            actionRequest(item.getProductID(), Flags.Garage.FlagName.FAVORITE,
                                    (item.isFavActive()? Flags.Garage.FlagAction.UNFLAG : Flags.Garage.FlagAction.FLAG),
                                    itemsHolder);
                            item.setFavActive((item.isFavActive())? false : true);
                            notifyItemChanged(position);
                        }
                    }
                }
            });

            /*itemsHolder.tvHide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(item.getProductID() != -1) {
                        System.out.println("item: " + item.getProductID() + "   position: "+ position);
                        actionRequest(item.getProductID(), Flags.Garage.FlagName.HIDDEN, Flags.Garage.FlagAction.FLAG, itemsHolder);
                        notificationsList.remove(item);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, getItemCount());
                    }
                }
            });*/
            itemsHolder.tvOverflowMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(item.getProductID() != -1) {
                        PopupMenu popup = new PopupMenu(mContext, itemsHolder.tvOverflowMenu);
                        popup.inflate(R.menu.search_result_item);
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
                                    case R.id.item_hide:
                                        if(mSession.isLoggedIn()) {
                                            actionRequest(item.getProductID(), Flags.Garage.FlagName.HIDDEN, Flags.Garage.FlagAction.FLAG, itemsHolder);
                                            notificationsList.remove(item);
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

            if(item != null) {
                itemsHolder.img0 = item.getImage();
                Log.d(Flags.ActivityTag.SEARCH_ACTIVITY, "img url-> "+ ((NotificationsViewHolder)holder).img0);

                Glide.with(mContext)
                        .load(((NotificationsViewHolder)holder).img0)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .error(R.drawable.default_img)
                        .into(((NotificationsViewHolder)holder).imageView);
            }
            else {
                Glide.clear(((NotificationsViewHolder)holder).imageView);
                itemsHolder.imageView.setImageDrawable(null);
            }
        }
    }

    private void actionRequest(final int productID, final String flagName, final String flagAction, final NotificationsListAdapter.NotificationsViewHolder holder) {
        try {
            //Log.d(Flags.ActivityTag.GARAGE,"attrib url: " + url);
            Client jsObjRequest = new Client(Request.Method.POST, Flags.getGarageActionUrl(mSession), SearchResponse.class, new OnCallListener() {
                @Override
                public void nwResponseData(String pData) {
                }
            }, new Response.Listener<Object>() {
                @Override
                public void onResponse(Object response) {
                    //SearchResponse res = (SearchResponse)response;
                    holder.ivFav.setEnabled(true);

                    /*if(res.getStatus() == 1) {
                        if(flagName.equals(Flags.Garage.FlagName.FAVORITE)) {

                        }

                        if(flagName.equals(Flags.Garage.FlagName.HIDDEN)) {
                        }
                    }*/
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

    void setCurrencyFormat(NotificationsListAdapter.NotificationsViewHolder tvHolder, String currencyFormat, SearchResultItem item) {
        if(currencyFormat.equals("USD")){
            tvHolder.marketAvgValue.setText(item.getbelowMarketBy_usd().replaceAll("\"",""));
        }else if(currencyFormat.equals("CAD")){
            tvHolder.marketAvgValue.setText(item.getbelowMarketBy_cad().replaceAll("\"",""));
        }
    }

    @Override
    public int getItemCount() {
        if(isLoadingBarEnabled)
            return 1;
        else
            return notificationsList.size();
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

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        //TextView headerDate;
        public HeaderViewHolder(View v){
            super(v);
            //headerModel = (TextView) v.findViewById(R.id.column_model_title);
            /*headerDate = (TextView) v.findViewById(R.id.column_date_title);
            headerDate.setTypeface(AppController.getFontType(mContext));*/
            //headerModel.setTypeface(AppController.getFontType(mContext));
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        //public TextView display;
        public FooterViewHolder(View v) {
            super(v);
            //display = (TextView) v.findViewById(R.id.footer_text);
        }
    }
}