package com.app.oktpus.adapter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.oktpus.Lib.featureDiscovery.TapTarget;
import com.app.oktpus.R;
import com.app.oktpus.activity.LocationActivity;
import com.app.oktpus.activity.SearchActivity;
import com.app.oktpus.activity.SignupActivity;
import com.app.oktpus.constants.SearchResultViewType;
import com.app.oktpus.fragment.AppStartModal;
import com.app.oktpus.model.AttrValSpinnerModel;
import com.app.oktpus.model.SearchResponse;
import com.app.oktpus.model.SearchResultItem;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.controller.AppController;
import com.app.oktpus.fragment.SearchResult;
import com.app.oktpus.fragment.SortDialogFragment;
import com.app.oktpus.listener.OnCallListener;
import com.app.oktpus.responseModel.Config.CommonFields;
import com.app.oktpus.utils.CarParts.JsonParsingManager;
import com.app.oktpus.utils.Client;
import com.app.oktpus.utils.FlowLayout;
import com.app.oktpus.utils.SessionManagement;
import com.app.oktpus.utils.TagTokenUtils;
import com.app.oktpus.utils.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
/*import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;*/

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.app.oktpus.activity.SearchActivity.KEYWORD_SEARCH_START;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Gyandeep on 29/12/16.
 */

public class SearchResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_HEADER = 0;
    //public static final int VIEW_TYPE_ITEM = 1;
    //private final int VIEW_TYPE_FOOTER = 2;
    public static final int VIEW_TYPE_SORT_LAYOUT = 2;
    public static final int VIEW_TYPE_PROGRESSBAR = 3;
    private final int VIEW_TYPE_NO_RESULT = 4;
    private final int VIEW_BLANK_VIEW = 5;
    public static final int VIEW_NO_MORE_RESULTS = 6;
    public static final int VIEW_NETWORK_FAILURE = 7;

    private SessionManagement mSession;
    private boolean isNetworkAvailable = true;
    private boolean isLoadingFooter = true;
    private boolean isLoadingResult = false;
    private boolean isNoResultEnabled = false;
    public boolean isLoadingCount = false;
    private Context mContext;
    //private Map<String, Integer> totalCount;
    private List<SearchResultItem> searchResultList;
    //private SortSpinnerAdapter dataAdapter;
    private SearchResult.AdapterCallback callbackListener;
    //public ArrayAdapter<String> overflowAdapter;

    private String keywords;
    public int totalCount = 0;
    //private FloatingActionButton backToTopBtn;
    SortDialogFragment sortListDialog;
    private TagTokenUtils tagTokenNew;

    public class ItemsViewHolder extends RecyclerView.ViewHolder {
        public TextView itemTitle, price, dateCity, distance, marketAvgValue, marketAvgLabel, noResults,
                tvOverflow, tvFav;
        public ImageView imageView, ivFav, ivShare, ivOverflow;
        //public Spinner spinnerOverflow;
        public String url, shareUrl, img0, dateAndCity, title;
        public int productID = -1;
        public Intent share = new Intent(Intent.ACTION_SEND);
        public RelativeLayout rlImgPlaceholder;
        public LinearLayout llFav, llShare, llMAbg;
        public View vMAbar;
        //public ImageLoader imageLoader;

        public ItemsViewHolder(View view) {
            super(view);
            noResults = (TextView) view.findViewById(R.id.no_results);
            itemTitle = (TextView) view.findViewById(R.id.item_title);
            price = (TextView) view.findViewById(R.id.price);
            imageView = (ImageView) view.findViewById(R.id.image);
            tvFav = (TextView) view.findViewById(R.id.tv_fav);
            dateCity = (TextView) view.findViewById(R.id.date_city);
            distance = (TextView) view.findViewById(R.id.distance);
            marketAvgValue = (TextView) view.findViewById(R.id.market_avg_value);
            marketAvgLabel = (TextView) view.findViewById(R.id.market_avg_label);
            tvOverflow = (TextView) view.findViewById(R.id.overflow);

            llFav = (LinearLayout) view.findViewById(R.id.ll_fav);
            llShare = (LinearLayout) view.findViewById(R.id.ll_share);
            ivShare = (ImageView) view.findViewById(R.id.iv_share);
            ivFav = (ImageView) view.findViewById(R.id.iv_fav);

            llMAbg = (LinearLayout) view.findViewById(R.id.ll_ma_bg);
            vMAbar = (View) view.findViewById(R.id.v_ma_bar);

            img0 = new String();
            //imageLoader = AppController.getInstance().getImageLoader();
            //imageView.setBackgroundResource(R.drawable.ic_car_loader);
            //imageView.setBackgroundColor(mContext.getResources().getColor(R.color.exlistGroupHeaderSelected));

            /*imageView.setBackgroundResource(R.drawable.spinner);
            AnimationDrawable frameAnimation = (AnimationDrawable) imageView.getBackground();
            frameAnimation.start();*/

            itemTitle.setOnClickListener(new TextView.OnClickListener() {
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

            llShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utility.shareIntent(share, mContext, shareUrl, title, dateAndCity);
                }
            });

            /*ivOverflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });*/

            // attaching data adapter to spinner
            //spinnerOverflow.setAdapter(dataAdapter);
            /*spinnerOverflow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(null != view) {
                        if(mSession.isLoggedIn()) {
                            //System.out.println("item: " + item.getProductID() + "   position: "+ position);
                            actionRequest(productID, Flags.Garage.FlagName.HIDDEN, Flags.Garage.FlagAction.FLAG, null);
                            //searchResultList.remove(item);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, getItemCount());
                        }
                        else {
                            System.out.println("item: " + productID + "   position: "+ position);
                            mContext.startActivity(new Intent(getApplicationContext(), SignupActivity.class));
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });*/
        }
    }

    private class AdCardsViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        String linkUrl, imageUrl;
        public AdCardsViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(linkUrl)); //+"?subject=" + "subject" + "&body=body");
                    activity.startActivity(intent);
                }
            });
        }
    }

    private class RateThisAppViewHolder extends RecyclerView.ViewHolder {
        CardView itemLayout;
        public RateThisAppViewHolder(View itemView) {
            super(itemView);
            itemLayout = (CardView) itemView.findViewById(R.id.result_card_view);
            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.app.oktpus")));
                }
            });
        }
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {
        public View oktSpinner;
        public TextView oktLoadingText;
        public ProgressViewHolder(View v) {
            super(v);
            oktSpinner = (View) v.findViewById(R.id.okt_spinner);
            oktLoadingText = (TextView) v.findViewById(R.id.okt_loading_txt);
        }
    }

    public LinearLayout rlDropDown;
    public class SortViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTotalCount, tvDropDown;
        public ProgressBar progressBarCount;
        public SortViewHolder(View v) {
            super(v);
            tvTotalCount = (TextView) v.findViewById(R.id.total_count);
            rlDropDown = (LinearLayout) v.findViewById(R.id.rl_drop_down);
            tvDropDown = (TextView) v.findViewById(R.id.tv_drop_down);
            progressBarCount = (ProgressBar) v.findViewById(R.id.pb_count);
            rlDropDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(sortListDialog.isAdded()) {
                        sortListDialog.getDialog().dismiss();
                    }
                    else {
                        sortListDialog.show(((SearchActivity)activity).getFragmentManager(), "dialog");
                    }
                }
            });
        }
    }

    public EditText searchField;
    public TextView tvGeoLoc;
    public FlowLayout parentTagLayout;
    public CardView searchHeaderLayout;
    public ProgressBar pbGeoLoc;
    public ImageButton btnGeoLocate;
    public boolean isGeoLocUpdateInProgress = false;
    public ProgressBar pbKeywordSearchButton;
    public ImageButton voiceSearch, keywordSearchBtn;
    public boolean voiceSearchActive;

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout wallOfDealsHeaderLayout;
        public ImageView ivHelp;
        public TextView tvWoDHeader, tvWoDSubHeader;
        public HeaderViewHolder(View v){
            super(v);
            voiceSearch = (ImageButton) v.findViewById(R.id.ibtn_search_voice);
            keywordSearchBtn = (ImageButton)v.findViewById(R.id.ibtn_search);
            pbKeywordSearchButton = (ProgressBar)v.findViewById(R.id.ibtn_progress_bar);
            searchField = (EditText) v.findViewById(R.id.et_search);
            parentTagLayout = (FlowLayout) v.findViewById(R.id.tag_layout);
            btnGeoLocate = (ImageButton) v.findViewById(R.id.btn_geo_loc);
            tvGeoLoc = (TextView) v.findViewById(R.id.tv_geo_loc);
            pbGeoLoc = (ProgressBar) v.findViewById(R.id.pb_geo_loc);
            searchHeaderLayout = (CardView)v.findViewById(R.id.root);
            searchField.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    searchField.requestFocus();
                    return false;
                }
            });

            tvGeoLoc.setPaintFlags(tvGeoLoc.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            wallOfDealsHeaderLayout = (LinearLayout) v.findViewById(R.id.wod_header_layout);
            ivHelp = (ImageView) v.findViewById(R.id.iv_help);
            tvWoDHeader = (TextView) v.findViewById(R.id.tv_wod_header);
            tvWoDSubHeader = (TextView) v.findViewById(R.id.tv_wod_sub_header);
            tvWoDHeader.setTypeface(AppController.getFontType(mContext));
            tvWoDSubHeader.setTypeface(AppController.getFontType(mContext));

            ivHelp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppStartModal modalFrag = AppStartModal.newInstance();
                    modalFrag.show(((SearchActivity)activity).getFragmentManager(), "dialog");
                    modalFrag.setCancelable(true);
                }
            });

            voiceSearchActive = true;
            searchField.setHint(Utility.getSearchHint(mContext));
            searchField.setText(keywords);
            searchField.addTextChangedListener(new TextWatcher(){
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(count > 0){
                        voiceSearch.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_clear));
                        voiceSearch.setScaleType(ImageView.ScaleType.CENTER);
                        voiceSearchActive = false;
                    }
                    else {
                        voiceSearch.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mic));
                        voiceSearch.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        voiceSearchActive = true;
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            btnGeoLocate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnGeoLocate.setVisibility(View.GONE);
                    pbGeoLoc.setVisibility(View.VISIBLE);
                    isGeoLocUpdateInProgress = true;
                    Intent intent = new Intent(((SearchActivity)activity).getApplicationContext(), LocationActivity.class);
                    intent.putExtra(Flags.IS_LOC_UPDATE_REQUESTED, true);
                    ((SearchActivity)activity).startActivityForResult(intent, Flags.REQ_CODE_LOCATION);
                }
            });

            searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if(actionId == EditorInfo.IME_ACTION_SEARCH){
                        keywords = String.valueOf(searchField.getText());
                        if(keywords.length() > 0) {
                            callbackListener.searchBarKeyword(keywords);
                            keywordSearchAction(keywords);
                            setKeywordSearchProgress(KEYWORD_SEARCH_START);
                        }
                    }
                    return false;
                }
            });

            voiceSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(voiceSearchActive){
                        //start speech recog
                        Log.d("voice search : ", "start speech recognition");
                        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, mContext.getResources().getString(R.string.mic_msg));
                        intent.putExtra("ISFROMRESULT", true);
                        try {
                            activity.startActivityForResult(intent, Flags.REQ_CODE_SPEECH_INPUT);

                        } catch (ActivityNotFoundException a) {
                            Toast.makeText(mContext,"Sorry! Your device doesn\\'t support speech input",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        ((SearchActivity)activity).keywordSearchBarController(true, false);
                        searchField.setText("");
                        keywords = "";
                        callbackListener.searchBarKeyword(keywords);

                        //notifyDataSetChanged();
                        Log.d("voice search : ", "clear composing Text");
                    }

                }
            });

            keywordSearchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    keywords = String.valueOf(searchField.getText());
                    InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);
                    if(keywords.length() > 0) {
                        callbackListener.searchBarKeyword(keywords);
                        keywordSearchAction(keywords);
                        setKeywordSearchProgress(KEYWORD_SEARCH_START);
                    }
                }
            });
        }
    }

    public void setKeywordSearchProgress(boolean isInProgress) {
        if(isInProgress) {
            keywordSearchBtn.setVisibility(View.GONE);
            pbKeywordSearchButton.setVisibility(View.VISIBLE);
        }
        else {
            keywordSearchBtn.setVisibility(View.VISIBLE);
            pbKeywordSearchButton.setVisibility(View.GONE);
        }
    }

    public void clearKeywordSearchField(){
        if(String.valueOf(searchField.getText()).length() > 0) {
            searchField.setText("");
            keywords = "";
        }
    }

    public boolean isTagExists() {
        if(parentTagLayout.getChildCount() > 0)
            return true;
        else
            return false;
    }

    public void checkForLastTag() {
        if(parentTagLayout.getChildCount() == 1) {
            parentTagLayout.removeView((LinearLayout)parentTagLayout.findViewWithTag("cleartag"));
        }
    }

    public void addTagLayout(final AttrValSpinnerModel attr, final String keyname) {
        final LinearLayout tagLayout = tagTokenNew.createMultiSelectTag(attr, keyname);
        tagLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeTagLayout(attr, keyname);
                ((SearchActivity)activity).syncTagLayout(true, false, attr, null, true, keyname);
                ((SearchActivity)activity).refreshNotifier();
            }
        });
        parentTagLayout.addView(tagLayout);

        if(isTagExists()) {
            if(null != parentTagLayout.findViewWithTag("cleartag")) {
                parentTagLayout.removeView((LinearLayout)parentTagLayout.findViewWithTag("cleartag"));
            }
            final LinearLayout tl = tagTokenNew.createClearTag();
            tl.setOnClickListener(new LinearLayout.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parentTagLayout.removeView((LinearLayout)parentTagLayout.findViewWithTag("cleartag"));
                    ((SearchActivity)activity).clearAllTags();
                }
            });
            parentTagLayout.addView(tl, parentTagLayout.getChildCount());
        }
    }

    public void addRangeTag(final CommonFields rangeAttr, final String keyname) {
        final LinearLayout tagLayout = tagTokenNew.createRangeTag(rangeAttr);

        tagLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeRangeTag(rangeAttr, keyname);
                ((SearchActivity)activity).syncTagLayout(true, false, null, rangeAttr, false, keyname);
                checkForLastTag();
                ((SearchActivity)activity).refreshNotifier();
            }
        });
        parentTagLayout.addView(tagLayout);
        if(isTagExists()) {
            if(null != parentTagLayout.findViewWithTag("cleartag")) {
                parentTagLayout.removeView((LinearLayout)parentTagLayout.findViewWithTag("cleartag"));
            }
            final LinearLayout tl = tagTokenNew.createClearTag();
            tl.setOnClickListener(new LinearLayout.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parentTagLayout.removeView((LinearLayout)parentTagLayout.findViewWithTag("cleartag"));
                    ((SearchActivity)activity).clearAllTags();
                }
            });
            parentTagLayout.addView(tl, parentTagLayout.getChildCount());
        }
    }

    public void updateRangeTag(CommonFields rangeAttr, String keyname) {
        TextView tv = (TextView) parentTagLayout.findViewWithTag(rangeAttr.getKeyName());
        String val = rangeAttr.getMinFormatted() + " - " + rangeAttr.getMaxFormatted();
        tv.setText(val);

        if(rangeAttr.isTagCreated()) {
            if(rangeAttr.getValues().getMin() == rangeAttr.getDefaultValue().getMin()
                    && rangeAttr.getValues().getMax() == rangeAttr.getDefaultValue().getMax()) {
                removeRangeTag(rangeAttr, keyname);
            }
        }
        if(!((SearchActivity)activity).isFirstLoad)
            ((SearchActivity)activity).refreshNotifier();
    }

    public void removeRangeTag(CommonFields rangeAttr, String keyname) {
        parentTagLayout.removeView((LinearLayout)parentTagLayout.findViewWithTag(rangeAttr));
        checkForLastTag();
    }

    public void removeTagLayout(AttrValSpinnerModel attr, String keyname) {
        attr.setSelected(false);
        parentTagLayout.removeView(parentTagLayout.findViewWithTag(keyname+attr.getInteger_representation()));
        checkForLastTag();
    }

    public void keywordSearchAction(String keywords) {
        try {
            searchField.setText(keywords);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTVGeoCities(String cities) {
        try {
            if(null != tvGeoLoc) tvGeoLoc.setText(cities);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public class NoMoreResults extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvDesc;
        public NoMoreResults(View v) {
            super(v);
            tvTitle = (TextView)v.findViewById(R.id.tv_no_results_title);
            tvDesc = (TextView) v.findViewById(R.id.tv_desc);
            tvTitle.setText(mContext.getResources().getString(R.string.no_more_results));
            tvTitle.setTypeface(AppController.getFontType(getApplicationContext()));
            tvDesc.setText(mContext.getResources().getString(R.string.no_more_results_desc));
        }
    }

    public class NetworkFailure extends RecyclerView.ViewHolder {
        private LinearLayout mainLayout;
        public NetworkFailure(View v) {
            super(v);
            mainLayout = (LinearLayout)  v.findViewById(R.id.main_layout);
            mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callbackListener.triggerRetryNetworkRequests();
                }
            });
        }
    }

    public class BlankViewHolder extends RecyclerView.ViewHolder {
        public BlankViewHolder(View v) {
            super(v);
        }
    }

    public class NoResultsViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle, tvDesc;
        public NoResultsViewHolder(View v) {
            super(v);
            tvTitle = (TextView) v.findViewById(R.id.tv_no_results_title);
            tvDesc = (TextView) v.findViewById(R.id.tv_desc);
            tvTitle.setTypeface(AppController.getFontType(getApplicationContext()));
            tvTitle.setText(mContext.getResources().getString(R.string.no_results));
            tvDesc.setText(mContext.getResources().getString(R.string.no_results_desc));
        }
    }

    public Activity activity;
    public SortRecyclerAdapter sortAdapter;
    public SessionManagement sessionCompareCars;
    public SearchResultAdapter(Context mContext, List<SearchResultItem> searchResultList, final SearchResult.AdapterCallback listener, final Activity activity,
                               String keywords, SessionManagement session) {
        this.mContext = mContext;
        this.searchResultList = searchResultList;
        this.mSession = session;
        this.callbackListener = listener;
        this.activity = activity;
        this.keywords = keywords;
        sessionCompareCars = new SessionManagement(mContext, activity);
        tagTokenNew = new TagTokenUtils(mContext);
        //backToTopBtn = ((SearchActivity)mContext).mFab;

        sortAdapter = new SortRecyclerAdapter(optionsList(mContext), new SortDialogFragment.OptionSelectListener() {
            @Override
            public void sortSelected(int pos) {
                ((SearchActivity)activity).onSortParamSetTriggerRequest(pos);
                sortListDialog.getDialog().dismiss();
            }
        });

        if(null == sortListDialog || !sortListDialog.isInLayout()) {
            sortListDialog = SortDialogFragment.newInstance();
            sortListDialog.setSortAdapter(sortAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return searchResultList.size() + 3;
        //return (searchResultList.size() == 0 && isNoResultEnabled) ? searchResultList.size() + 2 : searchResultList.size() + 3; //
        //return  (isFooterEnabled) ? searchResultList.size() + 1 : searchResultList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position)) {
            /*((SearchActivity)mContext).mFab.hide();
            ((SearchActivity)mContext).fabBehaviour.enableScrollToTop(false);*/
            return VIEW_TYPE_HEADER;
        }
        else if(position == 1) {
            return VIEW_TYPE_SORT_LAYOUT;
        }
        else if(isPositionFooter(position)) {
            if(isLoadingFooter)
                return VIEW_TYPE_PROGRESSBAR;
            else if(isNetworkAvailable && searchResultList.size() == 0) {
                return VIEW_TYPE_NO_RESULT;
            }
            else if(!isNetworkAvailable) {
                return VIEW_NETWORK_FAILURE;
            }
            else
                return VIEW_NO_MORE_RESULTS;
        }
        else if(!isLoadingResult){
            if((position - 2) < searchResultList.size()) {
                return searchResultList.get(position-2).getType();
            }
            else
                return VIEW_BLANK_VIEW;
        }
        else
            return VIEW_BLANK_VIEW;
        //return (isFooterEnabled && position >= searchResultList.size() ) ? footerViewType() : hasItem();
    }

    /*private int hasItem(int position) {
        *//*if (position >= 2 && !backToTopBtn.isShown() && ((SearchActivity)activity).mPager.getCurrentItem() == 1) {
            *//**//*((SearchActivity)mContext).fabBehaviour.enableScrollToTop(true);
            backToTopBtn.show(new FloatingActionButton.OnVisibilityChangedListener() {
                @Override
                public void onShown(FloatingActionButton fab) {
                    backToTopBtn.setAlpha(0.8f);
                }
            });*//**//*
        }*//*
        return VIEW_TYPE_ITEM;
    }*/


    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private boolean isPositionFooter(int position) {
        /*if(isNoResultEnabled)
            return position == searchResultList.size () + 1;
        else*/
        return position == searchResultList.size () + 2;
    }

    /*public int footerViewType() {
        if(isLoadingFooter) return VIEW_TYPE_PROGRESSBAR;
        else return VIEW_TYPE_;
    }*/


    public void setNetworkStatus(boolean isAvailable) { this.isNetworkAvailable = isAvailable; }
    public void enableLoadingFooter(boolean isEnabled) {
        this.isLoadingFooter = isEnabled;
    }

    public void enableLoadingResult(boolean value) {
        this.isLoadingResult = value;
    }


    //public void enableSortOptions(boolean isEnabled) { this.isSortEnabled = isEnabled; }
    public void isNoResult(boolean isEnabled) { this.isNoResultEnabled = isEnabled; }

    public void setSearchkeywords(String keywords) {
        this.keywords = keywords;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if(viewType == SearchResultViewType.VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_result_item, parent, false);
            vh = new ItemsViewHolder(v);
            return vh;
        }
        else if(viewType == SearchResultViewType.VIEW_AD_CARD) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_ad_cards, parent, false);
            vh = new AdCardsViewHolder(v);
            return vh;
        }
        else if(viewType == SearchResultViewType.VIEW_RATE_THIS_APP) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_rate_the_app, parent, false);
            vh = new RateThisAppViewHolder(v);
            return vh;
        }/*else if(viewType == VIEW_TYPE_FOOTER){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_footer, parent, false);

            vh = new FooterViewHolder(v);
            return vh;
        }*/
        else if(viewType == VIEW_TYPE_SORT_LAYOUT) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_sort_layout, parent, false);
            vh = new SortViewHolder(v);
            return vh;
        }
        else if(viewType == VIEW_TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_header, parent, false);
            vh = new HeaderViewHolder(v);
            return vh;
        }
        else if (viewType == VIEW_TYPE_PROGRESSBAR){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_bar_layout, parent, false);
            vh = new ProgressViewHolder(v);
            return vh;
        }
        else if (viewType == VIEW_TYPE_NO_RESULT){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_result_item_no_result, parent, false);
            vh = new NoResultsViewHolder(v);
            return vh;
        }
        else if(viewType == VIEW_NO_MORE_RESULTS) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_result_item_no_result, parent, false);
            vh = new SearchResultAdapter.NoMoreResults(v);
            return vh;
        }
        else if(viewType == VIEW_NETWORK_FAILURE) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_item_network_failure, parent, false);
            vh = new SearchResultAdapter.NetworkFailure(v);
            return vh;
        }
        else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.blank_view, parent, false);
            vh = new SearchResultAdapter.BlankViewHolder(v);
            return vh;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

       /* if(position < 3) {      // && !((BaseActivity)activity).mFabFilter.isShown()
            ((BaseActivity)activity).mFabFilter.hide();//((BaseActivity)activity).mFabFilter.show();
        }
        else if(((SearchActivity)activity).mPager.getCurrentItem() != 0)
            ((BaseActivity)activity).mFabFilter.show();*/
        if(holder instanceof HeaderViewHolder) {
            final HeaderViewHolder headerVH = (HeaderViewHolder) holder;
            if(((SearchActivity)activity).getSearchType() == Flags.Bundle.Values.WALL_OF_DEALS) {
                headerVH.wallOfDealsHeaderLayout.setVisibility(View.VISIBLE);
            }
            else {
                headerVH.wallOfDealsHeaderLayout.setVisibility(View.GONE);
            }
        }
        else if(holder instanceof ProgressViewHolder){
            //((ProgressViewHolder)holder).progressBar.setIndeterminate(true);
            AnimationDrawable frameAnimation = (AnimationDrawable) ((ProgressViewHolder)holder).oktSpinner.getBackground();
            frameAnimation.start();
        }
        else if(holder instanceof SortViewHolder) {
            final SortViewHolder sortHolder = (SortViewHolder) holder;
            if(!isLoadingCount) {
                sortHolder.progressBarCount.setVisibility(View.GONE);
                sortHolder.tvTotalCount.setVisibility(View.VISIBLE);
                String tc = Utility.formatNumberByType(this.totalCount, "#,###") + " results";
                sortHolder.tvTotalCount.setText (tc);
            }
            else {
                sortHolder.progressBarCount.setVisibility(View.VISIBLE);
                sortHolder.tvTotalCount.setVisibility(View.GONE);
            }
            sortHolder.tvDropDown.setText(sortListDialog.getCurrentSortOption());
            //sortOptions.setAdapter(dataAdapter);
        }
        else if(searchResultList.size() > 0 && (position - 2) < searchResultList.size() && holder instanceof AdCardsViewHolder) {
            final AdCardsViewHolder itemsHolder = ((AdCardsViewHolder)holder);
            final SearchResultItem item = searchResultList.get(position - 2);
            itemsHolder.linkUrl = item.getHref_url();
            if(item != null) {
                itemsHolder.imageUrl = item.getImage();
                Log.d(Flags.ActivityTag.SEARCH_ACTIVITY, "img url-> "+ itemsHolder.imageUrl);
                Glide.with(AppController.getInstance().getApplicationContext())
                        .load(itemsHolder.imageUrl)
                        .error(R.drawable.default_img)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(itemsHolder.imageView);
                //((TextViewHolder)holder).imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            else {
                Glide.clear(itemsHolder.imageView);
                itemsHolder.imageView.setImageDrawable(null);
            }
        }
        else if(searchResultList.size() > 0 && (position - 2) < searchResultList.size() && holder instanceof ItemsViewHolder) {
            final ItemsViewHolder itemsHolder = ((ItemsViewHolder)holder);
            if(itemsHolder.noResults.getVisibility() == View.VISIBLE)
                itemsHolder.noResults.setVisibility(View.GONE);

            final SearchResultItem item = searchResultList.get(position - 2);

            if(item != null) {
                itemsHolder.img0 = item.getImage();
                //if(!((TextViewHolder)holder).img0.startsWith("https:")) ((TextViewHolder)holder).img0 = "https:" + ((TextViewHolder)holder).img0;
                Log.d(Flags.ActivityTag.SEARCH_ACTIVITY, "img url-> "+ itemsHolder.img0);
                Glide.with(AppController.getInstance().getApplicationContext())
                        .load(itemsHolder.img0)
                        .error(R.drawable.default_img)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(itemsHolder.imageView);
                //((TextViewHolder)holder).imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            else {
                Glide.clear(itemsHolder.imageView);
                itemsHolder.imageView.setImageDrawable(null);
            }

            itemsHolder.itemTitle.setText(item.getItemTitle().trim());
            itemsHolder.dateCity.setText(item.getPostDate() + " in " + item.getCity());
            itemsHolder.url = item.getHref_url();
            itemsHolder.shareUrl = item.getHref_url();
            itemsHolder.productID = item.getProductID();

            itemsHolder.dateAndCity = item.getPostDate() + " in " + item.getCity();
            itemsHolder.title = item.getItemTitle().trim();

            itemsHolder.tvOverflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(mContext, itemsHolder.tvOverflow);
                    popup.inflate(R.menu.search_result_item);
                    popup.getMenu().findItem(R.id.item_car_parts).setVisible(true);
                    popup.getMenu().findItem(R.id.item_car_review).setVisible(true);
                    popup.getMenu().findItem(R.id.item_car_recall).setVisible(true);

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.item_hide:
                                    if(mSession.isLoggedIn()) {
                                        actionRequest(item.getProductID(), Flags.Garage.FlagName.HIDDEN, Flags.Garage.FlagAction.FLAG, itemsHolder);
                                        searchResultList.remove(item);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, getItemCount());
                                    }
                                    else {
                                        mContext.startActivity(new Intent(getApplicationContext(), SignupActivity.class));
                                    }
                                    break;
                                case R.id.item_compare: {
                                    sessionCompareCars.addCompareCarProductId(String.valueOf(item.getProductID()), String.valueOf(item.getItemTitle()));

                                    break;
                                }
                                case R.id.item_car_parts: {
                                    carPartsNwRequest(item.getProductID());
                                    break;
                                }
                                case R.id.item_car_review: {
                                    carReviewNwRequest(item.getProductID());
                                    break;
                                }
                                case R.id.item_car_recall: {
                                    carRecallNwRequest(item.getProductID());
                                    break;
                                }
                            }
                            return false;
                        }
                    });
                    popup.show();
                }
            });


            //Drawable drawable = (item.isFavActive())? mContext.getResources().getDrawable(R.drawable.ic_fav_active) : mContext.getResources().getDrawable(R.drawable.ic_fav_inactive);
            //iemsHolder.ivFav.setImageDrawable(drawable);

            if((item.isFavActive())) {
                itemsHolder.ivFav.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_fav_active));
                itemsHolder.tvFav.setTextColor(mContext.getResources().getColor(R.color.fav));
            }
            else {
                itemsHolder.ivFav.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_fav_inactive));
                itemsHolder.tvFav.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            }

            itemsHolder.llFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(item.getProductID() != -1 && mSession.isLoggedIn()) {
                        if(itemsHolder.llFav.isEnabled()) {
                            itemsHolder.llFav.setEnabled(false);

                            actionRequest(item.getProductID(), Flags.Garage.FlagName.FAVORITE,
                                    (item.isFavActive()? Flags.Garage.FlagAction.UNFLAG : Flags.Garage.FlagAction.FLAG),
                                    itemsHolder);
                            item.setFavActive((item.isFavActive())? false : true);
                            Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.action_icon_press_fade_in);
                            animFadein.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                    if((item.isFavActive())) {
                                        itemsHolder.ivFav.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_fav_active));
                                        itemsHolder.tvFav.setTextColor(mContext.getResources().getColor(R.color.fav));
                                    }
                                    else {
                                        itemsHolder.ivFav.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_fav_inactive));
                                        itemsHolder.tvFav.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                                    }
                                }
                                @Override
                                public void onAnimationEnd(Animation animation) {}
                                @Override
                                public void onAnimationRepeat(Animation animation) {}
                            });
                            itemsHolder.ivFav.startAnimation(animFadein);


                            //Tooltip
                            if(!mSession.isFavDemoChecked()) {
                                mSession.setIsFavDemoChecked(true);
                                List<TapTarget> views = new ArrayList<>();
                                views.add(Utility.buildTapTarget(itemsHolder.ivFav,
                                        mContext.getResources().getString(R.string.fav_feature),
                                        mContext.getResources().getString(R.string.fav_feature_desc)));
                                Utility.startSequenceFeatureAnim(activity, views);
                            }
                        }
                    }
                    else {
                        mContext.startActivity(new Intent(getApplicationContext(), SignupActivity.class));
                    }
                }
            });

            String currencyFormat=AppController.getInstance().getCurrencyFormat();
            String kilometerFormat=AppController.getInstance().getKilometerFormat();

            if(item.getMaKeyword().equals(Flags.Keys.MA_ABOVE) && item.isMaShow()) {
                itemsHolder.marketAvgLabel.setText(mContext.getResources().getString(R.string.label_market_avg_above));
                itemsHolder.marketAvgValue.setTextColor(mContext.getResources().getColor(R.color.colorTextButtonRed));
                itemsHolder.marketAvgValue.setTypeface(Typeface.DEFAULT);
                itemsHolder.llMAbg.setVisibility(View.VISIBLE);
                itemsHolder.vMAbar.setVisibility(View.GONE);
                setCurrencyFormat(itemsHolder, currencyFormat, item);
            }
            else if(item.getMaKeyword().equals(Flags.Keys.MA_BELOW) && item.isMaShow()){
                itemsHolder.marketAvgLabel.setText(mContext.getResources().getString(R.string.label_market_avg_below));
                itemsHolder.marketAvgValue.setTextColor(mContext.getResources().getColor(R.color.headingGreen));
                itemsHolder.marketAvgValue.setTypeface(Typeface.DEFAULT_BOLD);
                itemsHolder.llMAbg.setVisibility(View.VISIBLE);
                itemsHolder.vMAbar.setVisibility(View.VISIBLE);
                setCurrencyFormat(itemsHolder, currencyFormat, item);
            }
            else {
                itemsHolder.llMAbg.setVisibility(View.INVISIBLE);
            }

            if(currencyFormat.equals("USD")){
                itemsHolder.price.setText(item.getCurrency_usd().replaceAll("\"",""));
            }else if(currencyFormat.equals("CAD")){
                itemsHolder.price.setText(item.getCurrency_cad().replaceAll("\"",""));
            }


            if(kilometerFormat.equals("kilometers")){
                itemsHolder.distance.setText(item.getKilometers_kilometers().replaceAll("\"",""));
            }else if(kilometerFormat.equals("miles")){
                itemsHolder.distance.setText(item.getKilometers_miles().replaceAll("\"",""));
            }
        }
    }

    void setCurrencyFormat(ItemsViewHolder tvHolder, String currencyFormat, SearchResultItem item) {
        if(currencyFormat.equals("USD")){
            tvHolder.marketAvgValue.setText(item.getbelowMarketBy_usd().replaceAll("\"",""));
        }else if(currencyFormat.equals("CAD")){
            tvHolder.marketAvgValue.setText(item.getbelowMarketBy_cad().replaceAll("\"",""));
        }
    }

    private void actionRequest(final int productID, final String flagName, final String flagAction, final SearchResultAdapter.ItemsViewHolder holder) {
        try {
            Client jsObjRequest = new Client(Request.Method.POST, Flags.getGarageActionUrl(mSession), SearchResponse.class, new OnCallListener() {
                @Override
                public void nwResponseData(String pData) {}
            }, new Response.Listener<Object>() {
                @Override
                public void onResponse(Object response) {
                    if(null != holder) holder.llFav.setEnabled(true);
                    notifyDataSetChanged();
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

    private Snackbar snackbar;
    public void carReviewNwRequest(int productID) {
        try {
            //snackbar = Snackbar.make(((SearchActivity)activity).rootView, mContext.getResources().getString(R.string.msg_loading), Snackbar.LENGTH_INDEFINITE);
            //snackbar.show();
            final Client jsObjRequest = new Client(Request.Method.GET, Flags.URL.GET_CAR_REVIEW+ productID, SearchResponse.class, new OnCallListener() {
                @Override
                public void nwResponseData(String pData) throws IOException {
                    JsonNode node = JsonParsingManager.parseResultData(pData, true);
                    ObjectNode objNode = (ObjectNode)node;
                    String url = (objNode.findValue(Flags.Keys.EXTERNAL_URL) != null)?objNode.findValue(Flags.Keys.EXTERNAL_URL).getTextValue() : "";

                    if(url.isEmpty()) {
                        snackbar = Snackbar.make(((SearchActivity)activity).rootView, mContext.getResources().getString(R.string.error_msg), Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                    else {
                        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        snackbar.dismiss();
                    }
                }
            }, new Response.Listener<Object>() {
                @Override
                public void onResponse(Object response) {}
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(Flags.ActivityTag.SEARCH_RESULT, "Error: " + error);
                    snackbar = Snackbar.make(((SearchActivity)activity).rootView, mContext.getResources().getString(R.string.msg_failed_to_connect), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            });
            String tag_json_obj = "json_obj_req";
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsObjRequest, tag_json_obj);
        }
        catch(Exception e) {
            Log.d(Flags.ActivityTag.SEARCH_RESULT, e.getMessage());
        }
    }

    public void carRecallNwRequest(int productID) {
        try {
            //snackbar = Snackbar.make(((SearchActivity)activity).rootView, mContext.getResources().getString(R.string.msg_loading), Snackbar.LENGTH_INDEFINITE);
            //snackbar.show();
            final Client jsObjRequest = new Client(Request.Method.GET, Flags.URL.GET_CAR_RECALL+ productID, SearchResponse.class, new OnCallListener() {
                @Override
                public void nwResponseData(String pData) throws IOException {
                    JsonNode node = JsonParsingManager.parseResultData(pData, true);
                    ObjectNode objNode = (ObjectNode)node;
                    String url = (objNode.findValue(Flags.Keys.EXTERNAL_URL) != null)?objNode.findValue(Flags.Keys.EXTERNAL_URL).getTextValue() : "";
                    if(url.isEmpty()) {
                        snackbar = Snackbar.make(((SearchActivity)activity).rootView, mContext.getResources().getString(R.string.error_msg), Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                    else {
                        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        //snackbar.dismiss();
                    }
                }
            }, new Response.Listener<Object>() {
                @Override
                public void onResponse(Object response) {}
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(Flags.ActivityTag.SEARCH_RESULT, "Error: " + error);
                    snackbar = Snackbar.make(((SearchActivity)activity).rootView, mContext.getResources().getString(R.string.msg_failed_to_connect), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            });
            String tag_json_obj = "json_obj_req";
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsObjRequest, tag_json_obj);
        }
        catch(Exception e) {
            Log.d(Flags.ActivityTag.SEARCH_RESULT, e.getMessage());
        }
    }

    public void carPartsNwRequest(int productID) {
        try {
            final Client jsObjRequest = new Client(Request.Method.GET, Flags.URL.GET_CAR_PARTS + productID, SearchResponse.class, new OnCallListener() {
                @Override
                public void nwResponseData(String pData) throws IOException {
                    JsonParsingManager.startCarPartsFragmentView(activity, pData, true);
                }
            }, new Response.Listener<Object>() {
                @Override
                public void onResponse(Object response) {}
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(Flags.ActivityTag.SEARCH_RESULT, "Error: " + error);
                }
            });
            String tag_json_obj = "json_obj_req";
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsObjRequest, tag_json_obj);
        }
        catch(Exception e) {
            Log.d(Flags.ActivityTag.SEARCH_RESULT, e.getMessage());
        }
    }

    public static List<String> optionsList(Context context) {
        List<String> options = new ArrayList<String>();
        options.add(context.getResources().getString(R.string.newest_arrivals));
        options.add(context.getResources().getString(R.string.most_relevant));
        options.add(context.getResources().getString(R.string.pr_low_high));
        options.add(context.getResources().getString(R.string.pr_high_low));
        options.add(context.getResources().getString(R.string.yr_old_new));
        options.add(context.getResources().getString(R.string.yr_new_old));
        return options;
    }
}