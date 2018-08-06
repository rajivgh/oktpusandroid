package com.app.oktpus.adapter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.app.oktpus.activity.BaseActivity;
import com.app.oktpus.activity.LocationActivity;
import com.app.oktpus.activity.LoginActivity;
import com.app.oktpus.activity.SearchActivity;
import com.app.oktpus.model.AttrValSpinnerModel;
import com.app.oktpus.model.EditSearchRangeAttributes;
import com.app.oktpus.model.ItemRequestData;
import com.app.oktpus.model.SearchResponse;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.constants.ModelStatus;
import com.app.oktpus.constants.ResponseCode;
import com.app.oktpus.controller.AppController;
import com.app.oktpus.Lib.featureDiscovery.TapTarget;
import com.app.oktpus.listener.OnCallListener;
import com.app.oktpus.listener.OnCheckBoxClickListener;
import com.app.oktpus.R;
import com.app.oktpus.responseModel.Config.CommonAttributes;
import com.app.oktpus.responseModel.Config.CommonFields;
import com.app.oktpus.responseModel.Config.ResponseConfig;
import com.app.oktpus.responseModel.ResponseSearchCreate;
import com.app.oktpus.responseModel.SearchByKeywordsResponse;
import com.app.oktpus.utils.Client;
import com.app.oktpus.utils.ExpandableLayout;
import com.app.oktpus.utils.FlowLayout;
import com.app.oktpus.utils.SessionManagement;
import com.app.oktpus.utils.TagTokenUtils;
import com.app.oktpus.utils.Utility;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.apptik.widget.MultiSlider;

import static com.app.oktpus.activity.SearchActivity.KEYWORD_SEARCH_FINISH;
import static com.app.oktpus.activity.SearchActivity.KEYWORD_SEARCH_START;

/**
 * Created by Gyandeep on 10/3/17.
 */

public class SearchFormAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final int VIEW_TYPE_HEADER = 0;
    private final int VIEW_TYPE_MS_TOP = 1;
    private final int VIEW_TYPE_RANGE = 2;
    private final int VIEW_TYPE_MS_BOTTOM = 3;
    private final int VIEW_TYPE_PROGRESSBAR = 4;
    private final int VIEW_TYPE_BLANK = 5;
    private final int VIEW_TYPE_BOTTOM = 6;
    private final int VIEW_TYPE_INC_NO_IMG_CARS = 7;

    private final int POS_COUNTRY = 1, POS_CITY = 2, POS_MAKE = 3, POS_MODEL = 4,
            POS_PRICE = 5, POS_KILOMETERS = 6, POS_YEAR = 7, POS_BELOW_MARKET_AVG = 8,
            POS_COLOUR = 9, POS_BODY = 10, POS_TRANSMISSION = 11, POS_DOORS = 12, POS_DRIVETRAIN = 13, POS_USER_TYPE = 14;


    final private Activity activity;
    //private int searchType = 0;
    private Context context;
    private Map<String, List<AttrValSpinnerModel>> multiSelectOriginal, multiSelectWorker;
    private Map<String, String> attrLabels;
    private Map<String, CommonFields> rangeOriginal, rangeWorker;
    private Map<String, Map<String, String>> labelList;
    private Map<String, ArrayList<String>> msCheckedValues, msCheckedGroups;
    public Map<Integer, ArrayList<String>> checkedAttrValueNames;
    private Map<Integer, String> stateMSAttribValues;
    private LayoutInflater layoutInflater;
    private HashSet<Integer> expandedPositionSet;
    private SessionManagement mSession;
    private String responseData;
    public FlowLayout parentTagLayout;
    public Button btnSearch;
    private RelativeLayout saveSearchLayout, receiveNotificationLayout;
    private boolean receiveNotificationSwitch, saveSearchSwitch;

    private String mGroup = "0", mId = "0", searchID = null;
    public int idCount = 0, groupCount = 0, sortParam;
    private int modelStatus;
    public static final String COMMA = ",";
    private final int REQ_CODE_SPEECH_INPUT = 100;
    public String fetchModelUrl;
    private String keywords;
    private boolean voiceSearchActive, isGeoLocUpdateInProgress = false;
    private TagTokenUtils tagTokenNew;
    private TagToken tagToken;
    private List<TapTarget> viewsListForDemo;
    public SearchActivity.EventListener scrollEventListener;


    public Bundle saveBundle;

    public SearchFormAdapter(final Activity activity, final Context context, Map<String, List<AttrValSpinnerModel>> msMap,
                             Map<String, CommonFields> rangeMap, Map<String, String> attrLabels,
                             Map<String, Map<String, String>> labelList, SearchActivity.EventListener scrollEventListener) {
        this.activity = activity;
        this.mSession = new SessionManagement(context);
        this.multiSelectOriginal = msMap;
        this.multiSelectWorker = msMap;
        this.rangeOriginal = rangeMap;
        this.rangeWorker = rangeMap;
        this.attrLabels = attrLabels;
        this.labelList = labelList;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);

        stateMSAttribValues = new HashMap<Integer, String>();
        checkedAttrValueNames = new HashMap<Integer, ArrayList<String>>();
        attrValueCheckedList = new HashMap<String, ArrayList<String>>();

        tagTokenNew = new TagTokenUtils(context);
        tagToken = new TagToken();
        modelStatus = ModelStatus.CHOOSE_A_MAKE_FIRST.getValue();
        sortParam = Flags.Sort.Keys.NEWEST_ARRIVALS;
        keywords = "";
        expandedPositionSet = new HashSet<>();
        msCheckedValues = new HashMap<>(); msCheckedGroups = new HashMap<>();

        msCheckedValues.put(Flags.Keys.COUNTRY, new ArrayList<String>());
        msCheckedValues.put(Flags.Keys.CITY, new ArrayList<String>());
        msCheckedValues.put(Flags.Keys.MAKE, new ArrayList<String>());
        msCheckedValues.put(Flags.Keys.MODEL, new ArrayList<String>());
        msCheckedValues.put(Flags.Keys.COLOUR, new ArrayList<String>());
        msCheckedValues.put(Flags.Keys.TRANSMISSION, new ArrayList<String>());
        msCheckedValues.put(Flags.Keys.BODY, new ArrayList<String>());
        msCheckedValues.put(Flags.Keys.USER_TYPE, new ArrayList<String>());
        msCheckedValues.put(Flags.Keys.DOORS, new ArrayList<String>());
        msCheckedValues.put(Flags.Keys.DRIVETRAIN, new ArrayList<String>());

        msCheckedGroups.put(Flags.Keys.COUNTRY, new ArrayList<String>());
        msCheckedGroups.put(Flags.Keys.CITY, new ArrayList<String>());
        msCheckedGroups.put(Flags.Keys.MAKE, new ArrayList<String>());
        msCheckedGroups.put(Flags.Keys.MODEL, new ArrayList<String>());
        msCheckedGroups.put(Flags.Keys.COLOUR, new ArrayList<String>());
        msCheckedGroups.put(Flags.Keys.DOORS, new ArrayList<String>());
        msCheckedGroups.put(Flags.Keys.DRIVETRAIN, new ArrayList<String>());
        msCheckedGroups.put(Flags.Keys.TRANSMISSION, new ArrayList<String>());
        msCheckedGroups.put(Flags.Keys.BODY, new ArrayList<String>());
        msCheckedGroups.put(Flags.Keys.USER_TYPE, new ArrayList<String>());

        fetchModelUrl = Flags.URL.HOST+"/get/1/attribute/4/by/attribute_values/"+mId+"/groups/"+mGroup;

        this.scrollEventListener = scrollEventListener;

        //tapTargetSequence = new TapTargetSequence(activity);
        viewsListForDemo = new ArrayList<>();

        if(saveBundle != null) {
            //saveBundle.putParcelableArrayList("city", msCheckedValues.get());
        }

        final View view = ((BaseActivity)activity).ivReceiveNotifFeatureHelp;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.startFeatureAnim(activity, view,
                        context.getResources().getString(R.string.receive_notif),
                        context.getResources().getString(R.string.receive_notif_desc));
            }
        });

    }

    public EditText searchField;
    public ImageButton btnGeoLocate;
    public ProgressBar pbGeoLoc;
    public ImageButton keywordSearchBtn, voiceSearch;
    private ProgressBar pbKeywordSearchButton;
    public class HeaderLayoutHolder extends RecyclerView.ViewHolder {
        public TextView tvGeoLoc;
        public ViewGroup root;
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        public HeaderLayoutHolder(View iv) {
            super(iv);
            voiceSearch = (ImageButton) iv.findViewById(R.id.ibtn_search_voice);
            keywordSearchBtn = (ImageButton)iv.findViewById(R.id.ibtn_search);
            pbKeywordSearchButton = (ProgressBar) iv.findViewById(R.id.ibtn_progress_bar);
            searchField = (EditText) iv.findViewById(R.id.et_search);
            parentTagLayout = (FlowLayout) iv.findViewById(R.id.tag_layout);
            tvGeoLoc = (TextView) iv.findViewById(R.id.tv_geo_loc);
            btnGeoLocate = (ImageButton) iv.findViewById(R.id.btn_geo_loc);
            root = (ViewGroup) iv.findViewById(R.id.root);
            pbGeoLoc = (ProgressBar) iv.findViewById(R.id.pb_geo_loc);
            searchField.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //searchField.requestFocus();
                    ((BaseActivity)activity).mAppBarLayout.setExpanded(false, true);
                    return false;
                }
            });

            /*searchField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus) {
                        //if(searchField.getShowSoftInputOnFocus())
                        imm.showSoftInputFromInputMethod(searchField.getWindowToken(), 0);
                        ((BaseActivity)activity).mBottomLayout.setVisibility(View.GONE);
                        *//*else
                            ((BaseActivity)activity).mBottomLayout.setVisibility(View.VISIBLE);*//*
                    }
                    else {
                        imm.hideSoftInputFromInputMethod(searchField.getWindowToken(), 0);
                        ((BaseActivity)activity).mBottomLayout.setVisibility(View.VISIBLE);
                    }
                }
            });*/

            keywordSearchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    keywords = String.valueOf(searchField.getText());
                    imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);
                    searchField.clearFocus();
                    if(keywords.length() > 0) {
                        ((SearchActivity)activity).searchBarEvent(keywords, false);
                        keywordSearchAction(String.valueOf(keywords));
                        setKeywordSearchProgress(KEYWORD_SEARCH_START);
                    }
                }
            });

            tvGeoLoc.setPaintFlags(tvGeoLoc.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            /* Display Geo Location notif Dialog */
            if(!mSession.isDemoChecked()) {
                /*new Tooltip.Builder(context)
                        .anchor(btnGeoLocate, Tooltip.BOTTOM)
                        .content(activity.getLayoutInflater().inflate(R.layout.tooltip_location, null))
                        .into(vgRoot)
                        .animate(new TooltipAnimation(TooltipAnimation.SCALE_AND_FADE, 300))
                        .autoAdjust(true)
                        .withTip(new Tooltip.Tip(40, 40, context.getResources().getColor(R.color.primaryBgNew)))
                        .show();*/
                /*Utility.startFeatureAnim((SearchActivity)activity, btnGeoLocate, context.getResources().getString(R.string.btn_geo),
                        context.getResources().getString(R.string.geo_desc));*/

                viewsListForDemo.add(Utility.buildTapTarget(((BaseActivity)activity).ivReceiveNotifFeatureHelp,
                        context.getResources().getString(R.string.receive_notif),
                        context.getResources().getString(R.string.receive_notif_desc)));

                viewsListForDemo.add(Utility.buildTapTarget(btnGeoLocate, context.getResources().getString(R.string.btn_geo),
                        context.getResources().getString(R.string.geo_desc)));
            }

            voiceSearchActive = true;
            searchField.setHint(Utility.getSearchHint(context));
            searchField.setText(keywords);
            searchField.addTextChangedListener(new TextWatcher(){
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(count > 0){
                        voiceSearch.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_clear));
                        voiceSearch.setScaleType(ImageView.ScaleType.CENTER);
                        voiceSearchActive = false;
                    }
                    else {
                        voiceSearch.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_mic));
                        //voiceSearch.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        voiceSearchActive = true;
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if(actionId == EditorInfo.IME_ACTION_SEARCH){
                        keywords = String.valueOf(searchField.getText());
                        imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);
                        searchField.clearFocus();
                        if(keywords.length() > 0) {
                            ((SearchActivity)activity).searchBarEvent(keywords, false);
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
                        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, context.getResources().getString(R.string.mic_msg));
                        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 10*1000);
                        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 10*1000);
                        intent.putExtra("ISFROMRESULT", false);
                        try {
                            activity.startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);

                        } catch (ActivityNotFoundException a) {
                            Toast.makeText(context,"Sorry! Your device doesn\\'t support speech input",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        ((SearchActivity)activity).keywordSearchBarController(true, false);
                        searchField.setText("");
                        keywords = "";
                        ((SearchActivity)activity).searchBarEvent(keywords, false);
                        //callbackListener.searchBarKeyword(keywords);
                        //notifyDataSetChanged();
                        Log.d("voice search : ", "clear composing Text");
                    }
                }
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
        }
    }

    public void clearKeywordSearchField() {
        if(String.valueOf(searchField.getText()).length() > 0) {
            searchField.setText("");
            ((SearchActivity)activity).searchBarEvent("", false);
            //((SearchActivity)activity).keywordSearchBarController(true, KEYWORD_SEARCH_FINISH);
        }
    }

    public void keywordSearchAction(String keywords) {
        try {
            //searchField.setText(keywords);
            if(keywords.length() > 0) {
                if(clearAll(true)) {
                    searchField.setText(keywords);
                    this.keywords = keywords;
                    searchByKeywordsRequest(Request.Method.GET, Flags.URL.ITEM_GET_ATTRIBUTE_VALUE+
                                    "&keywords="+Utility.getFormattedQuery(keywords),
                            SearchByKeywordsResponse.class);
                }
            }
            else {
                //Toast.makeText(context, "Please enter some keywords", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public class BottomLayoutHolder extends RecyclerView.ViewHolder {
        public BottomLayoutHolder(View v) {
            super(v);
            btnSearch = (Button) v.findViewById(R.id.btnSearch);
            saveSearchLayout = (RelativeLayout) v.findViewById(R.id.save_search_layout);
            receiveNotificationLayout = (RelativeLayout) v.findViewById(R.id.receive_notif_layout);
            saveSearchSwitch = (SwitchButton) v.findViewById(R.id.save_search_checkbox);
            receiveNotificationSwitch = (SwitchButton) v.findViewById(R.id.chkbox_receive_notif);

            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    attemptSearchResultNavigation();
                }
            });

            saveSearchLayout.setOnClickListener(new RelativeLayout.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!saveSearchSwitch.isChecked()) {
                        setSwitchOn(saveSearchSwitch);
                        saveSearchSwitch.setThumbColorRes(R.color.switchActiveColor);
                    }
                    else {
                        setSwitchOff(saveSearchSwitch);
                        saveSearchSwitch.setThumbColorRes(R.color.white);
                        if(receiveNotificationSwitch.isChecked()) {
                            setSwitchOff(receiveNotificationSwitch);
                            setSwitchOff(saveSearchSwitch);
                        }
                    }
                }
            });

            receiveNotificationLayout.setOnClickListener(new RelativeLayout.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(!receiveNotificationSwitch.isChecked()) {
                        setSwitchOn(receiveNotificationSwitch);
                        receiveNotificationSwitch.setThumbColorRes(R.color.switchActiveColor);
                    }
                    else {
                        setSwitchOff(receiveNotificationSwitch);
                        receiveNotificationSwitch.setThumbColorRes(R.color.white);
                    }
                    if(!saveSearchSwitch.isChecked()) {
                        setSwitchOn(saveSearchSwitch);
                    }
                }
            });

            receiveNotificationSwitch.setBackColorRes(R.color.colorPrimary);
            saveSearchSwitch.setBackColorRes(R.color.colorPrimary);
            saveSearchSwitch.setThumbColorRes(R.color.white);
            receiveNotificationSwitch.setThumbColorRes(R.color.white);
        }
    }*/

    /*public void setSwitchOn(SwitchButton switchButton) {
        switchButton.setChecked(true);
        switchButton.setThumbColorRes(R.color.switchActiveColor);
    }

    public void setSwitchOff(SwitchButton switchButton) {
        switchButton.setChecked(false);
        switchButton.setThumbColorRes(R.color.colorAccent);
    }*/

    private CheckBox includeNoImageCarsCB;
    public class IncludeNoImageCarsLayoutHolder extends RecyclerView.ViewHolder{

        private LinearLayout includeNoImgCarsLayout;
        public IncludeNoImageCarsLayoutHolder(View iv) {
            super(iv);
            includeNoImageCarsCB = (CheckBox) iv.findViewById(R.id.include_no_img_cars_cb);
            includeNoImgCarsLayout = (LinearLayout) iv.findViewById(R.id.include_no_img_cars_layout);
            includeNoImgCarsLayout.setOnClickListener(new RelativeLayout.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(!includeNoImageCarsCB.isChecked()) {
                        includeNoImageCarsCB.setChecked(true);
                        /*setSwitchOn(image0);
                        image0.setThumbColorRes(R.color.switchActiveColor);*/
                    }
                    else {
                        includeNoImageCarsCB.setChecked(false);
                        /*setSwitchOff(image0);
                        image0.setThumbColorRes(R.color.colorAccent);*/
                    }
                }
            });

            if(activity.getIntent().hasExtra(Flags.Bundle.Keys.INCLUDE_NO_IMAGE_PARCEL)) {
                boolean includeNoImage = (boolean) activity.getIntent().getBooleanExtra(Flags.Bundle.Keys.INCLUDE_NO_IMAGE_PARCEL, false);
                if (includeNoImage) includeNoImageCarsCB.setChecked(true);
                else includeNoImageCarsCB.setChecked(false);
            }
        }
    }

    private Map<String, ArrayList<String>> attrValueCheckedList;
    public Map<Integer, AttrValSpinnerModel> tmpAttributesMap = new HashMap<>();

    public class MultiSelectHolder extends RecyclerView.ViewHolder {
        public TextView lblListHeader, tvCheckedValues, tvFilterPopupTitle;
        //private ImageView grpIndicator, searchCloseButton;
        private RecyclerView recyclerView;
        private SearchView searchView;
        //private Button btnOk;
        private ImageView ivPopupClose;

        public ArrayList<Integer> modelState;
        private boolean isOkBtnClicked = false;
        private MultiSelectItemSearchViewAdapter childRecyclerAdapter;
        private List<AttrValSpinnerModel> attrList;

        private String keyName = "";
        public AlertDialog.Builder builder;
        public AlertDialog dialog;

        private LinearLayout llGroupHeader, btnOkLayout;

        public void setKeyname(String keyname) {
            this.keyName = keyname;
        }

        public MultiSelectHolder(View view) {
            super(view);
            lblListHeader = (TextView) view
                    .findViewById(R.id.lblListHeader);
            tvCheckedValues = (TextView) view.findViewById(R.id.checked_values);
            llGroupHeader = (LinearLayout) view.findViewById(R.id.ll_expandable_list_grp_header);

            attrList = new ArrayList<AttrValSpinnerModel>();

            builder = new AlertDialog.Builder(context);
            View convertView = null;
            LayoutInflater layoutInflator = LayoutInflater.from(context);
            convertView = layoutInflator.inflate(R.layout.ex_list_item, null);

            recyclerView = (RecyclerView) convertView.findViewById(R.id.attribute_recycler_view);
            searchView = (SearchView) convertView.findViewById(R.id.sv_attrib_filter);
            ivPopupClose = (ImageView) convertView.findViewById(R.id.btn_popup_close);
            tvFilterPopupTitle = (TextView) convertView.findViewById(R.id.tv_filter_popup_title);
            ivPopupClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            //btnOk = (Button) convertView.findViewById(R.id.btn_ok);
            btnOkLayout = (LinearLayout) convertView.findViewById(R.id.btn_layout);

            childRecyclerAdapter = new MultiSelectItemSearchViewAdapter(context,
                    attrList, "", new OnCheckBoxClickListener() {
                @Override
                public void onCheckBoxItemClicked(String keyName, boolean checked, String intRep, String value, String group, String id, int position) {
                    //checkBoxListener.onCheckBoxItemClicked(keyName, checked, intRep, value, group, id, position);
                    //displayCheckedValues(checked, value, holder);
                    //addHeaderTexts(checked, value, checkedAttrValueNames);
                }

                @Override
                public void onCheckBoxItemClicked(boolean checked, String value) {}

                @Override
                public void onCheckBoxItemClicked(String keyname, Map<Integer, AttrValSpinnerModel> attrMap, int intRep, AttrValSpinnerModel tmpAttr) {
                    tmpAttributesMap.put(intRep, tmpAttr);//attrMap;
                }
            });

            searchView.setIconifiedByDefault(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(context,
                    LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(childRecyclerAdapter);
            //recyclerAdapterNotifier.fetchAdapter(keyName, childRecyclerAdapter);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
                    childRecyclerAdapter.getFilter().filter(newText);
                    return true;
                }
            });

            builder.setView(convertView);
            dialog = builder.create();

            Window window = dialog.getWindow();
            window.getDecorView().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        dialog.dismiss();
                    }
                    return false;
                }
            });

            btnOkLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(AttrValSpinnerModel attr : (multiSelectWorker.get(keyName))) {
                        if(tmpAttributesMap.containsKey(attr.getInteger_representation())) {
                            boolean isSelected = tmpAttributesMap.get(attr.getInteger_representation()).isSelected();
                            if(isSelected != attr.isSelected()) {
                                ((SearchActivity)activity).refreshNotifier();
                                addRemoveMultiSelectTags(isSelected, keyName, attr);
                            }
                            attr.setTmpSelected(attr.isSelected());
                        }
                    }

                    //Logic to clear city when Country selected
                    if(msCheckedValues.get(Flags.Keys.COUNTRY).size() > 0 || msCheckedGroups.get(Flags.Keys.COUNTRY).size() > 0) {
                        clearCity();
                    }

                    dialog.dismiss();
                    //notifyDataSetChanged();
                    multiSelectEventTrigger(keyName);
                }
            });

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    childRecyclerAdapter.clearTmpHolder();
                    for(AttrValSpinnerModel attr : (multiSelectWorker.get(keyName))) {
                        attr.setTmpSelected(attr.isSelected());
                    }
                    notifyDataSetChanged();
                }
            });

            llGroupHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //if(!dialog.isShowing()) {
                    childRecyclerAdapter.setKeyName(keyName);
                    tvFilterPopupTitle.setText("Select "+attrLabels.get(keyName));
                    childRecyclerAdapter.setListState((List<AttrValSpinnerModel>) multiSelectWorker.get(keyName));
                    childRecyclerAdapter.getFilter().filter("");
                    tmpAttributesMap.clear();
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

                    if(keyName.equals(Flags.Keys.MODEL)) {                          //Disabling Model for No Make selected
                        if(multiSelectWorker.get(Flags.Keys.MODEL).size()>0)
                            dialog.show();
                    }
                    else if(keyName.equals(Flags.Keys.CITY)) {                      //Disabling City for a Country Selected
                        if(!(msCheckedValues.get(Flags.Keys.COUNTRY).size() > 0 || msCheckedGroups.get(Flags.Keys.COUNTRY).size() > 0))
                            dialog.show();
                    }
                    else {
                        dialog.show();
                    }
                }
                //}
            });
        }
    }

    public boolean isNotFromView = false;
    public class RangeHolder extends RecyclerView.ViewHolder {
        private ExpandableLayout expandableLayout;
        public TextView tvSelectedValue, tvMin, tvMax, tvHeader;
        public ImageView indicator, featureHelp;
        public LinearLayout multiUnitLayout; public LinearLayout rowLayout;
        public MultiSlider rangeSeekBar;
        public CommonFields rangeValues;
        //private int minVal, maxVal;
        private String keyname = "";
        public void setKeyname(String keyname) {
            this.keyname = keyname;
        }
        public RangeHolder(View view) {
            super(view);
            expandableLayout = (ExpandableLayout) view.findViewById(R.id.expandable_layout);
            indicator = (ImageView) view.findViewById(R.id.grp_indicator);
            tvSelectedValue = (TextView) view.findViewById(R.id.tv_selected_val);
            tvHeader = (TextView) view.findViewById(R.id.tv_group_header_price_model);
            rangeSeekBar = (MultiSlider) view.findViewById(R.id.range_slider);
            tvMin = (AppCompatEditText) view.findViewById(R.id.tv_min_price_model);
            tvMax = (AppCompatEditText) view.findViewById(R.id.tv_max_price_model);
            multiUnitLayout = (LinearLayout) view.findViewById(R.id.multi_unit_layout);
            rowLayout = (LinearLayout) view.findViewById(R.id.ll_header);
            featureHelp = (ImageView) view.findViewById(R.id.iv_help);

            //tvHeader.setTypeface(AppController.getFontType(context), Typeface.NORMAL);
            tvMin.setSelectAllOnFocus(true);
            tvMax.setSelectAllOnFocus(true);

            expandableLayout.setOnExpandListener(new ExpandableLayout.OnExpandListener() {
                @Override
                public void onExpand(boolean expanded) {
                    rangeWorker.get(keyname).setExpanded(expanded);
                    rangeExpandMods(expanded, indicator, rowLayout, rangeWorker.get(keyname).getViewPos());
                }
            });

            rangeSeekBar.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener(){
                @Override
                public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, final int thumbIndex, final int value) {
                    if(!isNotFromView) {
                        if(tvMin.hasFocus()) tvMin.onEditorAction(EditorInfo.IME_ACTION_DONE);
                        if(tvMax.hasFocus()) tvMax.onEditorAction(EditorInfo.IME_ACTION_DONE);

                        if(thumbIndex == 0) {
                            rangeWorker.get(keyname).getValues().setMin(value);
                            String fmin = formatMinVal(value, rangeWorker.get(keyname));
                            tvMin.setText(fmin);
                            rangeWorker.get(keyname).setMinFormatted(fmin);
                        } else {
                            rangeWorker.get(keyname).getValues().setMax(value);
                            String fmax = formatMaxVal(value, rangeWorker.get(keyname));
                            tvMax.setText(fmax);
                            rangeWorker.get(keyname).setMaxFormatted(fmax);
                        }

                        tagToken.addOrUpdateRangeTag(context, rangeWorker.get(keyname), keyname);

                        multiSlider.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                /*switch(event.getAction()){
                                    case MotionEvent.ACTION_UP:
                                        ///v1.2 requestItemGetCountAll(Request.Method.POST, Flags.URL.ITEM_GETCOUNT + Flags.URL.DOMAIN_ID, SearchResponse.class);
                                        break;
                                }*/
                                rangeWorker.get(keyname).getField().setDuplicateStep(rangeWorker.get(keyname).getField().getStep());
                                rangeSeekBar.setStep(rangeWorker.get(keyname).getField().getDuplicateStep());
                                return false;
                            }
                        });
                        checkDefaultSet(tvMin, tvMax, tvSelectedValue, rangeWorker.get(keyname).getValues().getMin(), rangeWorker.get(keyname).getValues().getMax(), rangeWorker.get(keyname));
                    }
                    else {
                        //fix for year min automatically changes to 100 on scrolling up and down
                        if(keyname == Flags.Keys.YEAR) {
                            rangeSeekBar.setMin(rangeWorker.get(keyname).getDefaultValue().getMin(), true, true);
                            rangeSeekBar.setMax(rangeWorker.get(keyname).getDefaultValue().getMax(), true, true);
                            /*rangeSeekBar.getThumb(0).setValue(rangeWorker.get(keyname).getValues().getMin());
                            rangeSeekBar.getThumb(1).setValue(rangeWorker.get(keyname).getValues().getMax());*/
                        }
                    }
                    //isNotFromView = false;
                }
            });


            tvMin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if(actionId == EditorInfo.IME_ACTION_DONE) {
                        if(tvMin.hasFocus()) tvMin.clearFocus();
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        /*minMaxValueNotifier.fetchMinMax(attributeKey, holder.minVal, holder.maxVal, String.valueOf(holder.tvMin.getText()),
                            String.valueOf(holder.tvMax.getText()), fields.getKeyName());*/
                        checkDefaultSet(tvMin, tvMax, tvSelectedValue, rangeWorker.get(keyname).getValues().getMin(), rangeWorker.get(keyname).getValues().getMax(), rangeWorker.get(keyname));
                    }
                    return false;
                }
            });

            tvMax.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if(actionId == EditorInfo.IME_ACTION_DONE) {
                        if(tvMax.hasFocus()) tvMax.clearFocus();
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        /*minMaxValueNotifier.fetchMinMax(attributeKey, holder.minVal, holder.maxVal, String.valueOf(holder.tvMin.getText()),
                                String.valueOf(holder.tvMax.getText()), fields.getKeyName());*/
                        checkDefaultSet(tvMin, tvMax, tvSelectedValue, rangeWorker.get(keyname).getValues().getMin(), rangeWorker.get(keyname).getValues().getMax(), rangeWorker.get(keyname));
                    }
                    return false;
                }
            });

            tvMin.setOnFocusChangeListener(new AppCompatEditText.OnFocusChangeListener(){
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    try {
                        if(!hasFocus) {
                            String val = String.valueOf(tvMin.getText());
                            if(val.length() > 0) {
                                Long l = Long.valueOf(numberInspector(String.valueOf(tvMin.getText())));
                                //BigInteger bi = BigInteger.valueOf(l);
                                rangeWorker.get(keyname).getValues().setMin(Math.abs(l.intValue())); //Integer.valueOf(numberInspector(String.valueOf(tvMin.getText())))
                            }
                            rangeWorker.get(keyname).getField().setDuplicateStep(1);
                            rangeSeekBar.setStep(rangeWorker.get(keyname).getField().getDuplicateStep());

                            compareRangeMinMax(keyname, true);
                            String fmin = formatMinVal(rangeWorker.get(keyname).getValues().getMin(), rangeWorker.get(keyname));
                            tvMin.setText(fmin);
                            rangeWorker.get(keyname).setMinFormatted(fmin);
                            //isNotFromView = true;
                            rangeSeekBar.getThumb(0).setValue(rangeWorker.get(keyname).getValues().getMin());
                            tagToken.addOrUpdateRangeTag(context, rangeWorker.get(keyname), keyname);
                            //checkMinMaxForTagRemoval();
                        }
                        else {
                            tvMin.setText(String.valueOf(rangeWorker.get(keyname).getValues().getMin()));
                            ((AppCompatEditText)v).selectAll();
                        }
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            tvMax.setOnFocusChangeListener(new View.OnFocusChangeListener(){
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    try {
                        if(!hasFocus) {
                            String val = String.valueOf(tvMax.getText());
                            if(val.length() > 0) {
                                Long l = Long.valueOf(numberInspector(String.valueOf(tvMax.getText())));
                                rangeWorker.get(keyname).getValues().setMax(Math.abs(l.intValue()));
                            }
                            rangeWorker.get(keyname).getField().setDuplicateStep(1);
                            rangeSeekBar.setStep(rangeWorker.get(keyname).getField().getDuplicateStep());
                            compareRangeMinMax(keyname, false);
                        /*if(rangeWorker.get(keyname).getValues().getMax() <= rangeWorker.get(keyname).getDefaultValue().getMin()) rangeWorker.get(keyname).getValues().setMax(rangeWorker.get(keyname).getDefaultValue().getMin());
                        if(rangeWorker.get(keyname).getValues().getMax() >= rangeWorker.get(keyname).getDefaultValue().getMax()) rangeWorker.get(keyname).getValues().setMax(rangeWorker.get(keyname).getDefaultValue().getMax());*/
                            String fmax = formatMaxVal(rangeWorker.get(keyname).getValues().getMax(), rangeWorker.get(keyname));
                            tvMax.setText(fmax);
                            rangeWorker.get(keyname).setMaxFormatted(fmax);
                            //isNotFromView = true;
                            rangeSeekBar.getThumb(1).setValue(rangeWorker.get(keyname).getValues().getMax());
                            tagToken.addOrUpdateRangeTag(context, rangeWorker.get(keyname), keyname);
                            //checkMinMaxForTagRemoval();
                        }
                        else {
                            tvMax.setText(String.valueOf(rangeWorker.get(keyname).getValues().getMax()));
                            ((AppCompatEditText)v).selectAll();
                        }
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            featureHelp.setOnClickListener(new ImageView.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!ViewCompat.isLaidOut(v)) {
                        notifyDataSetChanged();
                    }

                    if(keyname.equals(Flags.Keys.PRICE) && rangeWorker.get(keyname).getViewPos() == POS_PRICE) {
                        Utility.startFeatureAnim(activity, featureHelp,
                                context.getResources().getString(R.string.price_filter),
                                context.getResources().getString(R.string.price_filter_desc));
                    }
                    else if(keyname.equals(Flags.Keys.BELOW_MARKET_AVG) && rangeWorker.get(keyname).getViewPos() == POS_BELOW_MARKET_AVG){
                        Utility.startFeatureAnim((SearchActivity)activity, featureHelp,
                                context.getResources().getString(R.string.bma_filter),
                                context.getResources().getString(R.string.bma_filter_desc));
                    }
                }
            });

        }   //End of RangeHolder constructor

        /*public void checkMinMaxForTagRemoval(){
            if(rangeWorker.get(keyname).isTagCreated()) {
                if(rangeWorker.get(keyname).getValues().getMin() == rangeWorker.get(keyname).getDefaultValue().getMin()
                        && rangeWorker.get(keyname).getValues().getMax() == rangeWorker.get(keyname).getDefaultValue().getMax()) {
                    tagToken.removeRangeTag(rangeWorker.get(keyname), keyname);
                }
            }
        }*/

        public void createMultiUnitViews(final String keyname) {
            try {
                if(keyname.equals(Flags.Keys.PRICE)) {
                    tvHeader.setText((labelList.get(keyname)).get(AppController.getInstance().getCurrencyFormat()));
                    //rangeWorker.get(keyname).setKeyName(AppController.getInstance().getCurrencyFormat());
                }
                else if(keyname.equals(Flags.Keys.KILOMETERS)) {
                    tvHeader.setText((labelList.get(keyname)).get(AppController.getInstance().getKilometerFormat()));
                    //rangeWorker.get(keyname).setKeyName(AppController.getInstance().getKilometerFormat());
                }
                else
                    tvHeader.setText(rangeWorker.get(keyname).getField().getLabel());

                rangeSeekBar.setMin(rangeWorker.get(keyname).getDefaultValue().getMin(), true, true);
                rangeSeekBar.setMax(rangeWorker.get(keyname).getDefaultValue().getMax(), true, true);

                rangeWorker.get(keyname).setMinFormatted(formatMinVal(rangeWorker.get(keyname).getValues().getMin(), rangeWorker.get(keyname)));
                rangeWorker.get(keyname).setMaxFormatted(formatMaxVal(rangeWorker.get(keyname).getValues().getMax(), rangeWorker.get(keyname)));

                if(keyname == Flags.Keys.PRICE || keyname == Flags.Keys.KILOMETERS) {
                    tvMin.setText(rangeWorker.get(keyname).getMinFormatted());
                    tvMax.setText(rangeWorker.get(keyname).getMaxFormatted());
                }
                else {
                    tvMin.setText(rangeWorker.get(keyname).getMinFormatted());
                    tvMax.setText(rangeWorker.get(keyname).getMaxFormatted());
                }


                /*tvMin.setText(formatMinVal(minVal, rangeWorker.get(keyname)));
                tvMax.setText(formatMaxVal(maxVal, rangeWorker.get(keyname)));*/
                checkDefaultSet(tvMin, tvMax, tvSelectedValue, rangeWorker.get(keyname).getValues().getMin(),
                        rangeWorker.get(keyname).getValues().getMax(), rangeWorker.get(keyname));

                rangeSeekBar.setStep(rangeWorker.get(keyname).getField().getDuplicateStep());
                rangeSeekBar.getThumb(0).setRange(null);
                rangeSeekBar.getThumb(0).setValue(rangeWorker.get(keyname).getValues().getMin());
                rangeSeekBar.getThumb(1).setValue(rangeWorker.get(keyname).getValues().getMax());
                /*minMaxValueNotifier.fetchMinMax(attributeKey, holder.minVal, holder.maxVal, String.valueOf(holder.tvMin.getText()),
                        String.valueOf(holder.tvMax.getText()), fields.getKeyName());*/
                multiUnitLayout.removeAllViewsInLayout();

                if(labelList.get(keyname) != null && labelList.get(keyname).size() > 1) {
                    final String currLabel;
                    //boolean isMismatch = false;
                    if(keyname.equals(Flags.Keys.PRICE)) {
                        currLabel = AppController.getInstance().getCurrencyFormat();
                        /*if(!currLabel.equals(rangeWorker.get(keyname).getKeyName()))
                            isMismatch = true;*/
                    }
                    else {
                        currLabel = AppController.getInstance().getKilometerFormat();
                        /*if(!currLabel.equals(rangeWorker.get(keyname).getKeyName()))
                            isMismatch = true;*/
                    }

                    for(Map.Entry<String, String> label : labelList.get(keyname).entrySet()) {
                        if(!(label.getKey().equals(currLabel)) && !(label.getKey().equals("EUR"))) {
                            final TextView labelView = new TextView(context);
                            labelView.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
                            labelView.setText(label.getKey());
                            labelView.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                            labelView.setTypeface(labelView.getTypeface(), Typeface.BOLD);
                            labelView.setPadding(5, 5, 5, 5);
                            labelView.setTextSize(16);

                            /*if(isMismatch) {
                                multiUnitConfigApi(Request.Method.POST, Flags.getConfigUrl(mSession,
                                        searchType, searchID), ResponseConfig.class,
                                        getMultiUnitRequestData(keyname, currLabel,
                                                rangeWorker.get(keyname).getValues().getMin(),
                                                rangeWorker.get(keyname).getValues().getMax(), currLabel),
                                        labelView, keyname);
                            }*/

                            labelView.setOnClickListener(new TextView.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //listener(String.valueOf(labelView.getText()));
                                    multiUnitLayout.setEnabled(false);
                                    ///labelView.setEnabled(false);
                                    /*String formatText = String.valueOf(labelView.getText());
                                    if(keyname.equals(Flags.Keys.PRICE)){
                                        AppController.getInstance().setCurrencyFormat(String.valueOf(labelView.getText()));
                                    }
                                    else {
                                        //if(formatText.equals("kilometers")||formatText.equals("miles"))
                                        AppController.getInstance().setKilometerFormat(labelView.getText().toString());
                                    }*/
                                    String formatText=labelView.getText().toString();
                                    if(formatText.equals("USD")||formatText.equals("CAD")){
                                        AppController.getInstance().setCurrencyFormat(labelView.getText().toString());
                                    }else if(formatText.equals("kilometers")||formatText.equals("miles")){
                                        AppController.getInstance().setKilometerFormat(labelView.getText().toString());
                                    }

                                    Log.d("PriceModelListAdap", "isEnabled: "+labelView.isEnabled());
                                    multiUnitConfigApi(Request.Method.POST, Flags.getConfigUrl(mSession,
                                            ((SearchActivity)activity).getSearchType(), searchID), ResponseConfig.class,
                                            getMultiUnitRequestData(keyname, currLabel,
                                                    rangeWorker.get(keyname).getValues().getMin(),
                                                    rangeWorker.get(keyname).getValues().getMax(), String.valueOf(labelView.getText())),
                                            labelView, keyname);

                                }
                            });
                            multiUnitLayout.addView(labelView);
                        }
                    }
                    final TextView tvChangeTo = new TextView(context);
                    tvChangeTo.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                            LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                            LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
                    tvChangeTo.setText(context.getResources().getString(R.string.multi_unit_change_to));
                    tvChangeTo.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                    tvChangeTo.setTextSize(16);
                    multiUnitLayout.addView(tvChangeTo);
                }
            }
            catch(Exception e) {
                Log.e("PriceModelAdapter", e.getMessage());
            }
        }
    }

    public void compareRangeMinMax(String keyname, boolean isForMin) {
        try {
            if(isForMin) {
                if(rangeWorker.get(keyname).getValues().getMin() <= rangeWorker.get(keyname).getDefaultValue().getMin())
                    rangeWorker.get(keyname).getValues().setMin(rangeWorker.get(keyname).getDefaultValue().getMin());
                if(rangeWorker.get(keyname).getValues().getMin() >= rangeWorker.get(keyname).getDefaultValue().getMax())
                    rangeWorker.get(keyname).getValues().setMin(rangeWorker.get(keyname).getDefaultValue().getMax());
            }
            else {
                if(rangeWorker.get(keyname).getValues().getMax() <= rangeWorker.get(keyname).getDefaultValue().getMin())
                    rangeWorker.get(keyname).getValues().setMax(rangeWorker.get(keyname).getDefaultValue().getMin());
                if(rangeWorker.get(keyname).getValues().getMax() >= rangeWorker.get(keyname).getDefaultValue().getMax())
                    rangeWorker.get(keyname).getValues().setMax(rangeWorker.get(keyname).getDefaultValue().getMax());
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public class BlankViewHolder extends RecyclerView.ViewHolder {
        public BlankViewHolder(View v) {
            super(v);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if(viewType == VIEW_TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sf_header_layout, parent, false);
            vh = new HeaderLayoutHolder(v);
            return vh;
        }
        else if(viewType == VIEW_TYPE_BOTTOM) {
            /*View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sf_bottom_layout, parent, false);
            vh = new BottomLayoutHolder(v);*/
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.blank_view, parent, false);
            vh = new BlankViewHolder(v);
            return vh;
        }
        else if(viewType == VIEW_TYPE_INC_NO_IMG_CARS) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sf_include_no_img_layout, parent, false);
            vh = new IncludeNoImageCarsLayoutHolder(v);
            return vh;
        }
        else if(viewType== VIEW_TYPE_MS_TOP) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ex_list_group, parent, false);
            vh = new MultiSelectHolder(v);
            return vh;
        }else if(viewType == VIEW_TYPE_MS_BOTTOM){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ex_list_group, parent, false);

            vh = new MultiSelectHolder(v);
            return vh;
        }
        else if(viewType == VIEW_TYPE_RANGE) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.range_expandable, parent, false);

            vh = new RangeHolder(v);
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof HeaderLayoutHolder) {
            if(null != (checkedAttrValueNames.get(POS_CITY)) && (checkedAttrValueNames.get(POS_CITY)).size() > 0) {
                StringBuilder content = new StringBuilder(stateMSAttribValues.get(POS_CITY));
                if((checkedAttrValueNames.get(POS_CITY)).size()>3) {
                    content.append(" citie(s)");
                }
                ((HeaderLayoutHolder) holder).tvGeoLoc.setText(content);
                ((SearchActivity)activity).updateGeoCities(content.toString());
            }
            else {
                ((HeaderLayoutHolder) holder).tvGeoLoc.setText(context.getResources().getString(R.string.header_geo_suffix_all_cities));
                ((SearchActivity)activity).updateGeoCities(context.getResources().getString(R.string.header_geo_suffix_all_cities));
            }
        }

        if(holder instanceof MultiSelectHolder) {
            String keyName;
            switch(position) {
                case POS_COUNTRY: keyName = Flags.Keys.COUNTRY; bindViewDataMultiSelect(holder, position, keyName); break;
                case POS_CITY: keyName = Flags.Keys.CITY;      bindViewDataMultiSelect(holder, position, keyName); break;
                case POS_MAKE: keyName = Flags.Keys.MAKE;      bindViewDataMultiSelect(holder, position, keyName); break;
                case POS_MODEL: keyName = Flags.Keys.MODEL;     bindViewDataMultiSelect(holder, position, keyName); break;
                case POS_DOORS: keyName = Flags.Keys.DOORS;    bindViewDataMultiSelect(holder, position, keyName); break;
                case POS_DRIVETRAIN: keyName = Flags.Keys.DRIVETRAIN;    bindViewDataMultiSelect(holder, position, keyName); break;
                case POS_USER_TYPE: keyName = Flags.Keys.USER_TYPE;    bindViewDataMultiSelect(holder, position, keyName); break;
                case POS_COLOUR: keyName = Flags.Keys.COLOUR;    bindViewDataMultiSelect(holder, position, keyName); break;
                case POS_BODY: keyName = Flags.Keys.BODY;      bindViewDataMultiSelect(holder, position, keyName); break;
                case POS_TRANSMISSION: keyName = Flags.Keys.TRANSMISSION;    bindViewDataMultiSelect(holder, position, keyName); break;

                default:
                    keyName = "";
            }
        }
        else if(rangeWorker != null && rangeWorker.size() > 0 && holder instanceof RangeHolder) {
            String keyName = "";
            switch(position) {
                case POS_PRICE : keyName = Flags.Keys.PRICE;bindViewDataRange(holder, position, keyName); break;
                case POS_KILOMETERS: keyName = Flags.Keys.KILOMETERS; bindViewDataRange(holder, position, keyName); break;
                case POS_YEAR: keyName = Flags.Keys.YEAR; bindViewDataRange(holder, position, keyName); break;
                case POS_BELOW_MARKET_AVG: keyName = Flags.Keys.BELOW_MARKET_AVG;
                    bindViewDataRange(holder, position, keyName);
                    break;
            }
        }

    }

    public void bindViewDataMultiSelect(RecyclerView.ViewHolder viewHolder, int position, String keyName) {
        try {
            MultiSelectHolder holder = (MultiSelectHolder) viewHolder;
            holder.setKeyname(keyName);
            holder.lblListHeader.setText(attrLabels.get(keyName));
            holder.tvCheckedValues.setTextColor(activity.getResources().getColor(R.color.groupHeaderSelectedText));
            switch(keyName) {
                case Flags.Keys.COUNTRY :
                    if(position == POS_COUNTRY) {
                        if (null == (checkedAttrValueNames.get(position)) || (checkedAttrValueNames.get(position)).size() < 1) {
                            holder.tvCheckedValues.setText(context.getResources().getString(R.string.select));
                        }
                        else {
                            holder.tvCheckedValues.setText(stateMSAttribValues.get(position));
                        }
                    }
                    break;
                case Flags.Keys.CITY :
                    if(position == POS_CITY) {
                        if (null == (checkedAttrValueNames.get(position)) || (checkedAttrValueNames.get(position)).size() < 1) {
                            if(null != checkedAttrValueNames.get(POS_COUNTRY) && (checkedAttrValueNames.get(POS_COUNTRY)).size() > 0) {
                                holder.tvCheckedValues.setText(stateMSAttribValues.get(POS_COUNTRY) + " selected");
                                holder.tvCheckedValues.setTextColor(activity.getResources().getColor(R.color.groupHeaderSelected));
                            }
                            else
                                holder.tvCheckedValues.setText(context.getResources().getString(R.string.select));
                        }
                        else {
                            holder.tvCheckedValues.setText(stateMSAttribValues.get(position));
                        }
                    }
                    break;
                case Flags.Keys.MAKE:
                    if(position == POS_MAKE) {
                        if (null == (checkedAttrValueNames.get(position)) || (checkedAttrValueNames.get(position)).size() < 1) {
                            holder.tvCheckedValues.setText(context.getResources().getString(R.string.select));
                        }
                        else {
                            holder.tvCheckedValues.setText(stateMSAttribValues.get(position));
                        }
                    }
                    break;
                case Flags.Keys.MODEL :
                    if(position == POS_MODEL) {
                        if(null != (checkedAttrValueNames.get(position)) && (checkedAttrValueNames.get(position)).size() > 0) {
                            holder.tvCheckedValues.setText(stateMSAttribValues.get(position));
                        }
                        else {
                            ModelStatus status = ModelStatus.fromValue(modelStatus);
                            switch(status) {
                                case CHOOSE_A_MAKE_FIRST:
                                    holder.tvCheckedValues.setText(context.getResources().getString(R.string.default_model_header));
                                    holder.tvCheckedValues.setTextColor(activity.getResources().getColor(R.color.groupHeaderSelected));
                                    break;
                                case NO_MODELS_FOUND:
                                    holder.tvCheckedValues.setText(context.getResources().getString(R.string.no_model_avail));
                                    holder.tvCheckedValues.setTextColor(activity.getResources().getColor(R.color.groupHeaderSelected));
                                    break;
                                case SELECT:
                                    holder.tvCheckedValues.setText(context.getResources().getString(R.string.select));
                                    break;
                                case LOADING:
                                    holder.tvCheckedValues.setText(context.getResources().getString(R.string.msg_loading));
                                    holder.tvCheckedValues.setTextColor(activity.getResources().getColor(R.color.groupHeaderSelected));
                                    break;
                                default: //show loading;
                            }
                        }
                    }
                    break;
                case Flags.Keys.DOORS:
                    if(position == POS_DOORS) {
                        if (null == (checkedAttrValueNames.get(position)) || (checkedAttrValueNames.get(position)).size() < 1) {
                            holder.tvCheckedValues.setText("");
                        }
                        else {
                            holder.tvCheckedValues.setText(stateMSAttribValues.get(position));
                        }
                    }
                    break;
                case Flags.Keys.DRIVETRAIN:
                    if(position == POS_DRIVETRAIN) {
                        if (null == (checkedAttrValueNames.get(position)) || (checkedAttrValueNames.get(position)).size() < 1) {
                            holder.tvCheckedValues.setText("");
                        }
                        else {
                            holder.tvCheckedValues.setText(stateMSAttribValues.get(position));
                        }
                    }
                    break;
                case Flags.Keys.COLOUR:
                    if(position == POS_COLOUR) {
                        if (null == (checkedAttrValueNames.get(position)) || (checkedAttrValueNames.get(position)).size() < 1) {
                            holder.tvCheckedValues.setText("");
                        }
                        else {
                            holder.tvCheckedValues.setText(stateMSAttribValues.get(position));
                        }
                    }
                    break;
                case Flags.Keys.USER_TYPE:
                    if(position == POS_USER_TYPE) {
                        if (null == (checkedAttrValueNames.get(position)) || (checkedAttrValueNames.get(position)).size() < 1) {
                            holder.tvCheckedValues.setText("");
                        }
                        else {
                            holder.tvCheckedValues.setText(stateMSAttribValues.get(position));
                        }
                    }
                    break;

                case Flags.Keys.BODY:
                    if(position == POS_BODY) {
                        if (null == (checkedAttrValueNames.get(position)) || (checkedAttrValueNames.get(position)).size() < 1) {
                            holder.tvCheckedValues.setText("");
                        }
                        else {
                            holder.tvCheckedValues.setText(stateMSAttribValues.get(position));
                        }
                    }
                    break;
                case Flags.Keys.TRANSMISSION:
                    if(position == POS_TRANSMISSION) {
                        if (null == (checkedAttrValueNames.get(position)) || (checkedAttrValueNames.get(position)).size() < 1) {
                            holder.tvCheckedValues.setText("");
                        }
                        else {
                            holder.tvCheckedValues.setText(stateMSAttribValues.get(position));
                        }
                    }
                    break;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void bindViewDataRange(RecyclerView.ViewHolder viewHolder, int position, final String keyName) {
        RangeHolder holder = (RangeHolder) viewHolder;
        isNotFromView = true;
        switch(keyName) {
            case Flags.Keys.PRICE:
                if(position == POS_PRICE) {
                    holder.setKeyname(keyName);
                    rangeWorker.get(keyName).setViewPos(position);
                    holder.createMultiUnitViews(keyName);
                    if(!mSession.isDemoChecked()) viewsListForDemo.add(Utility.buildTapTarget(((RangeHolder) holder).featureHelp,
                            context.getResources().getString(R.string.btn_geo),
                            context.getResources().getString(R.string.geo_desc)));
                    holder.featureHelp.setVisibility(View.VISIBLE);

                    //rangeExpandMods(rangeWorker.get(keyName).isExpanded(), holder.indicator, holder.rowLayout);
                }
                break;
            case Flags.Keys.KILOMETERS:
                if(position == POS_KILOMETERS) {
                    holder.setKeyname(keyName);
                    holder.createMultiUnitViews(keyName);
                    rangeWorker.get(keyName).setViewPos(position);

                    ((RangeHolder) holder).featureHelp.setVisibility(View.INVISIBLE);
                    //rangeExpandMods(rangeWorker.get(keyName).isExpanded(), holder.indicator, holder.rowLayout);
                }
                break;
            case Flags.Keys.YEAR:
                if(position == POS_YEAR) {
                    holder.setKeyname(keyName);
                    holder.createMultiUnitViews(keyName);
                    rangeWorker.get(keyName).setViewPos(position);

                    ((RangeHolder) holder).featureHelp.setVisibility(View.INVISIBLE);
                    //rangeExpandMods(rangeWorker.get(keyName).isExpanded(), holder.indicator, holder.rowLayout);
                    //holder.multiUnitLayout.setVisibility(View.GONE);
                }
                break;
            case Flags.Keys.BELOW_MARKET_AVG:
                if(position == POS_BELOW_MARKET_AVG) {
                    holder.setKeyname(keyName);
                    holder.createMultiUnitViews(keyName);
                    rangeWorker.get(keyName).setViewPos(position);

                    if(!mSession.isDemoChecked()) viewsListForDemo.add(Utility.buildTapTarget(((RangeHolder) holder).featureHelp,
                            context.getResources().getString(R.string.btn_geo),
                            context.getResources().getString(R.string.geo_desc)));
                    ((RangeHolder) holder).featureHelp.setVisibility(View.VISIBLE);

                    //rangeExpandMods(rangeWorker.get(keyName).isExpanded(), holder.indicator, holder.rowLayout);
                    //holder.multiUnitLayout.setVisibility(View.GONE);
                }
                break;
        }
        isNotFromView = false;
    }

    private boolean isPositionHeader (int position) {
        return position == 0;
    }
    private boolean isPositionMsTop(int position) {
        return (position >= 1 && position <=4);
    }
    private boolean isPositionMsBottom(int position) {
        return (position >= 9 && position <= 14);
    }

    private boolean isPositionBottomLayout(int position) {
        return position == 16;
    }
    private boolean isPositionRange(int position) {
        return (position >= 5 && position <= 8);
    }

    private boolean isPositionIncNoImgCars(int position) {
        return position == 15;
    }
    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position)) {
            /*((SearchActivity)context).mFab.hide();
            ((SearchActivity)context).fabBehaviour.enableScrollToTop(false);*/
            return VIEW_TYPE_HEADER;
        }
        else if(isPositionMsTop(position)) {
            return VIEW_TYPE_MS_TOP;
        }
        else if(isPositionMsBottom(position))
            return VIEW_TYPE_MS_BOTTOM;
        else if(isPositionRange(position))
            return VIEW_TYPE_RANGE;
        else if(isPositionIncNoImgCars(position))
            return VIEW_TYPE_INC_NO_IMG_CARS;
        else if(isPositionBottomLayout(position))
            return VIEW_TYPE_BOTTOM;
        else
            return VIEW_TYPE_BLANK;
    }

    @Override
    public int getItemCount() {
        return 17;
    }

    /*************************          Utilities       *******************************/

    public String addHeaderTexts(int pos, boolean checked, String selectedValue, ArrayList<String> checkedAttrValueNames, String defaultValue) {
        try {
            String parsedValue = "";
            if(selectedValue.contains(",")) parsedValue = selectedValue.substring(0, selectedValue.indexOf(","));
            else parsedValue = selectedValue;

            if(checked) checkedAttrValueNames.add(parsedValue);
            else checkedAttrValueNames.remove(parsedValue);

            this.checkedAttrValueNames.put(pos, checkedAttrValueNames);

            if(checkedAttrValueNames.size() < 1)
                return defaultValue;

            if(checkedAttrValueNames.size() <= 3)
                return android.text.TextUtils.join(",", checkedAttrValueNames);
            else
                return checkedAttrValueNames.size() + context.getResources().getString(R.string.selected);
        }
        catch(Exception e){
            e.printStackTrace();
            Log.e("SearchFormAdapter", e.getMessage());
            return "";
        }
    }

    public void setMultiSelectUI(String keyname, boolean checked, String value, ArrayList<String> attrValueList) {
        switch(keyname) {
            case Flags.Keys.COUNTRY:
                stateMSAttribValues.put(POS_COUNTRY, addHeaderTexts(POS_COUNTRY, checked, value, attrValueList, context.getResources().getString(R.string.select)));
                break;
            case Flags.Keys.CITY :
                stateMSAttribValues.put(POS_CITY, addHeaderTexts(POS_CITY, checked, value, attrValueList, context.getResources().getString(R.string.select)));
                break;
            case Flags.Keys.MAKE:
                stateMSAttribValues.put(POS_MAKE, addHeaderTexts(POS_MAKE, checked, value, attrValueList, context.getResources().getString(R.string.select)));
                break;
            case Flags.Keys.MODEL :
                stateMSAttribValues.put(POS_MODEL, addHeaderTexts(POS_MODEL, checked, value, attrValueList, context.getResources().getString(R.string.select)));
                //holder.llGroupHeader.setEnabled(true);
                break;
            case Flags.Keys.BODY:
                stateMSAttribValues.put(POS_BODY, addHeaderTexts(POS_BODY, checked, value, attrValueList, ""));
                break;
            case Flags.Keys.TRANSMISSION:
                stateMSAttribValues.put(POS_TRANSMISSION, addHeaderTexts(POS_TRANSMISSION, checked, value, attrValueList, ""));
                break;
            case Flags.Keys.USER_TYPE:
                stateMSAttribValues.put(POS_USER_TYPE, addHeaderTexts(POS_USER_TYPE, checked, value, attrValueList, ""));
                break;
            case Flags.Keys.COLOUR:
                stateMSAttribValues.put(POS_COLOUR, addHeaderTexts(POS_COLOUR, checked, value, attrValueList, ""));
                break;
            case Flags.Keys.DOORS:
                stateMSAttribValues.put(POS_DOORS, addHeaderTexts(POS_DOORS, checked, value, attrValueList, ""));
                break;
            case Flags.Keys.DRIVETRAIN:
                stateMSAttribValues.put(POS_DRIVETRAIN, addHeaderTexts(POS_DRIVETRAIN, checked, value, attrValueList, ""));
                break;
        }
    }

    private void registerExpand(int position) {
        if (expandedPositionSet.contains(position)) {
            removeExpand(position);
            //textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);
            //textView.setText("Show description");

        } else {
            addExpand(position);
            //textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_up, 0);
            //textView.setText("Hide description");

        }
    }

    private void removeExpand(int position) {
        expandedPositionSet.remove(position);
    }

    private void addExpand(int position) {
        expandedPositionSet.add(position);
    }

    private void checkDefaultSet(TextView tvMin, TextView tvMax, TextView tvSelectedValue,
                                 int minVal, int maxVal, CommonFields fields) {
        if(minVal == fields.getDefaultValue().getMin()) {
            tvMin.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }
        else {
            tvMin.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        }

        if(maxVal == fields.getDefaultValue().getMax()) {
            tvMax.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }
        else {
            tvMax.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        }

        if(minVal == fields.getDefaultValue().getMin() && maxVal == fields.getDefaultValue().getMax()) {
            tvSelectedValue.setVisibility(View.INVISIBLE);
        }
        else {
            setSelectedValue(tvSelectedValue, String.valueOf(tvMin.getText()), String.valueOf(tvMax.getText()));
            tvSelectedValue.setVisibility(View.VISIBLE);
        }
    }

    public void setSelectedValue(TextView tvSelectedValue, String min, String max) {
        tvSelectedValue.setText(min + " - " + max);
    }

    private String numberInspector(String text) {
        return text.replaceAll("[^\\d.]", "");
    }

    private String formatMinVal(int minValue, CommonFields fields) {
        return Utility.formMinNumberFormat(minValue, fields);
    }

    private String formatMaxVal(int maxValue, CommonFields fields) {
        return Utility.formMaxNumberFormat(maxValue, fields);
    }

    private void rangeExpandMods(boolean isExpanded, ImageView indicator, LinearLayout rowLayout, int pos) {
        try {
            if(isExpanded) {
                indicator.setImageResource(R.drawable.group_up);
                rowLayout.setBackgroundResource(R.color.primaryBgNew);
                notifyDataSetChanged();     //Not good, find a better way to keep rangeseekbar sane
                scrollEventListener.scrollToTopListener(pos);
            }
            else {
                indicator.setImageResource(R.drawable.group_down);
                rowLayout.setBackgroundResource(R.color.white);
            }
        }
        catch(Exception e) {
            Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, e.getMessage());
        }
    }

    public byte[] getMultiUnitRequestData(String attributeKey, String currFormat, int currMinVal, int currMaxVal, String formatTo){
        String strRequest = "config[attribute]["+attributeKey+"][type]="+formatTo+"&current_values[from_format]="+currFormat+"&current_values[values][0]="+
                currMinVal+"&current_values[values][1]="+currMaxVal;
        if(((SearchActivity)activity).getSearchType() == Flags.Bundle.Values.EDIT_SEARCH) strRequest = strRequest + "&search_id="+searchID;
        Log.d("RequestData", strRequest);
        return strRequest.getBytes(StandardCharsets.UTF_8);
    }

    public void multiUnitConfigApi(int methodType, String url, Class responseClass,
                                   final byte[] request, final TextView multiUnitLabelView, final String keyname) {
        ///api/user/{user_id}/config
        /*GET Params:
        -- domain_id=1
        - POST Params:
        -- config[attribute][price][type]:USD // format we are switching to
        -- current_values[values][0]:0 // current min value in the form input field
        -- current_values[values][1]:100000 // current max value in the form input field
        -- current_values[from_format]:CAD // current format
        -- search_id:1154 // If we are switching within an edit search, need to pass the search ID, because we will update not only the user's*/
        try {
            Map<String, String> params = new HashMap<String, String>();
            Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY,"attrib url: " + url);
            Client jsObjRequest = new Client(methodType, url, responseClass, params, new OnCallListener() {
                @Override
                public void nwResponseData(String pData) {}
            }, new Response.Listener<Object>() {
                @Override
                public void onResponse(Object response) {

                    ResponseConfig res = (ResponseConfig) response;
                    Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, "multiunit call status: " + res.getStatus());

                    ResponseCode resCode = ResponseCode.fromValue(res.getStatus());
                    switch (resCode) {
                        case STATUS_SUCCESS: {
                            try {
                                //JsonObject jObj = new JsonParser().parse(responseData).getAsJsonObject();
                                //Log.d("Config: ", "responseData: " + responseData);
                                CommonAttributes attributes = res.getDisplayFormat();
                                CommonFields cf;
                                switch (keyname) {
                                    case Flags.Keys.PRICE:
                                        cf = attributes.getPrice();
                                        AppController.getInstance().setCurrencyFormat(cf.getKeyName());
                                        break;
                                    case Flags.Keys.KILOMETERS:
                                        cf = attributes.getKms();
                                        AppController.getInstance().setKilometerFormat(cf.getKeyName());
                                        break;
                                    case Flags.Keys.YEAR:
                                        cf = attributes.getYear();
                                        break;
                                    case Flags.Keys.BELOW_MARKET_AVG:
                                        cf = attributes.getBmaPercent();
                                        break;
                                    default:
                                        cf = null;
                                }

                                LinkedList<Double> newValues = res.getNewValues();
                                if (cf != null)
                                    updateMultiUnitRange(keyname, cf, multiUnitLabelView, newValues);

                                notifyDataSetChanged();
                                if(multiUnitLabelView != null) multiUnitLabelView.setEnabled(true);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                        case STATUS_FAILURE: {
                            Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, "status 0");
                            break;
                        }
                        default: {
                            Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, "default");
                        }

                        //multiUnitLayout.setEnabled(true);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, "Error: " + error);
                }
            }){
                @Override
                public byte[] getBody() throws AuthFailureError {
                    Log.e(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, "custom request for search API only");
                    return request;
                }
            };
            String tag_json_obj = "json_obj_req";
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsObjRequest, tag_json_obj);
        }
        catch(Exception e) {
            Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, e.getMessage());
        }
    }

    public synchronized String alterMakeDataSelected(boolean checked, ArrayList<String> valuesList, ArrayList<String> groupsList,
                                                     String intRep, String group, String id) {
        boolean isGroup = false;
        if(Integer.valueOf(group) > 0) isGroup = true;
        if(checked) {
            if(isGroup) {
                ++groupCount;
                if(groupCount > 1) mGroup += COMMA+id;
                else mGroup = id;
                groupsList.add(id);
            }
            else {
                ++idCount;
                if(idCount > 1) mId += COMMA+id;
                else mId = id;
                valuesList.add(intRep);
            }
        }
        else {
            if(isGroup) {
                if(groupCount > 1) {

                    if(mGroup.contains(id)) {
                        int indexOfId = mGroup.indexOf(id);
                        if(indexOfId != 0 && (mGroup.substring(indexOfId-1, indexOfId)).equals(COMMA)) {
                            mGroup = mGroup.replaceFirst(COMMA+id, "");
                        }
                        else {
                            mGroup = mGroup.replaceFirst(id, "");
                        }
                    }
                    if(mGroup.startsWith(COMMA)) mGroup = mGroup.replaceFirst(COMMA, "");
                    if(mGroup.endsWith(COMMA)) mGroup = mGroup.substring(0, (mId.length()-1));//mGroup = mGroup.substring(mGroup.lastIndexOf(mGroup)).trim();
                }
                else mGroup = mGroup.replace(id, "0");
                groupsList.remove(id);
                --groupCount;
            }
            else {
                if(idCount > 1) {
                    if(mId.contains(id)) {
                        int indexOfId = mId.indexOf(id);
                        if(indexOfId != 0 && (mId.substring(indexOfId-1, indexOfId)).equals(COMMA)) {
                            mId = mId.replaceFirst(COMMA+id, "");
                        }
                        else {
                            mId = mId.replaceFirst(id, "");
                        }
                    }
                    if(mId.startsWith(COMMA)) {
                        mId = mId.replaceFirst(COMMA, "");
                    }
                    if(mId.endsWith(COMMA)) {
                        mId = mId.substring(0, (mId.length()-1));
                    }
                }
                else mId = mId.replace(id, "0");

                valuesList.remove(intRep);
                --idCount;
            }
        }

        //Fetch Model attribute data
        this.fetchModelUrl = Flags.URL.HOST+"/get/1/attribute/4/by/attribute_values/"+mId+"/groups/"+mGroup;
        Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, "Fetch Model url: "+this.fetchModelUrl);

        return fetchModelUrl;
    }

    /*public synchronized String alterMakeDataSelected(boolean checked, ArrayList<String> valuesList, ArrayList<String> groupsList,
                                                     String intRep, String group, String id) {
        boolean isGroup = false;
        if(Integer.valueOf(group) > 0) isGroup = true;
        if(checked) {
            if(isGroup) {
                ++groupCount;
                if(groupCount > 1) mGroup += COMMA+id;
                else mGroup = id;
                groupsList.add(id);
            }
            else {
                ++idCount;
                if(idCount > 1) mId += COMMA+id;
                else mId = id;
                valuesList.add(intRep);
            }
        }
        else {
            if(isGroup) {
                if(groupCount > 1) {

                    if(mGroup.contains(id)) {
                        int indexOfId = mGroup.indexOf(id);
                        if(indexOfId != 0 && (mGroup.substring(indexOfId-1, indexOfId)).equals(COMMA)) {
                            mGroup = mGroup.replaceFirst(COMMA+id, "");
                        }
                        else {
                            mGroup = mGroup.replaceFirst(id, "");
                        }
                    }
                    if(mGroup.startsWith(COMMA)) mGroup = mGroup.replaceFirst(COMMA, "");
                    if(mGroup.endsWith(COMMA)) mGroup = mGroup.substring(mGroup.lastIndexOf(mGroup)).trim();
                }
                else mGroup = mGroup.replace(id, "0");
                groupsList.remove(id);
                --groupCount;
            }
            else {
                if(idCount > 1) {
                    if(mId.contains(id)) {
                        int indexOfId = mId.indexOf(id);
                        if(indexOfId != 0 && (mId.substring(indexOfId-1, indexOfId)).equals(COMMA)) {
                            mId = mId.replaceFirst(COMMA+id, "");
                        }
                        else {
                            mId = mId.replaceFirst(id, "");
                        }
                    }
                    if(mId.startsWith(COMMA)) {
                        mId = mId.replaceFirst(COMMA, "");
                    }
                    if(mId.endsWith(COMMA)) {
                        mId = mId.substring(0, (mId.length()-1));
                    }
                }
                else mId = mId.replace(id, "0");

                valuesList.remove(intRep);
                --idCount;
            }
        }

        //Fetch Model attribute data
        this.fetchModelUrl = Flags.URL.HOST+"/get/1/attribute/4/by/attribute_values/"+mId+"/groups/"+mGroup;
        Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, "Fetch Model url: "+this.fetchModelUrl);

        return fetchModelUrl;
    }*/

    private void updateMultiUnitRange(String keyname, CommonFields cf,
                                      TextView multiUnitLabelView, LinkedList<Double> newValues) {
        try {
            rangeWorker.get(keyname).setValueFormatType(cf.getValueFormatType());
            rangeWorker.get(keyname).setField(cf.getField());
            rangeWorker.get(keyname).setMask(cf.getMask());
            rangeWorker.get(keyname).setDefaultValue(cf.getDefaultValue());

            if(multiUnitLabelView != null) multiUnitLabelView.setText(String.valueOf(cf.getKeyName()));

            if(rangeWorker.get(keyname).getValues().getMax()
                    != rangeWorker.get(keyname).getDefaultValue().getMax()) {
                double maxValue = newValues.get(1);
                rangeWorker.get(keyname).getValues().setMax((int)maxValue);
                compareRangeMinMax(keyname, false);
            }

            if(rangeWorker.get(keyname).getValues().getMin()
                    != rangeWorker.get(keyname).getDefaultValue().getMin()) {
                double minValue = newValues.get(0);
                rangeWorker.get(keyname).getValues().setMin((int)minValue);
                compareRangeMinMax(keyname, true);
            }

            setRangeMax(keyname, rangeWorker.get(keyname).getValues().getMax());
            setRangeMin(keyname, rangeWorker.get(keyname).getValues().getMin());


            //Temporary data set
            if(((SearchActivity)activity).isFirstLoad && ((SearchActivity)activity).getSearchType() == Flags.Bundle.Values.WALL_OF_DEALS
                    && keyname.equals(Flags.Keys.PRICE)) {
                rangeWorker.get(Flags.Keys.PRICE).getValues().setMin(1000);
                rangeWorker.get(Flags.Keys.PRICE).setViewPos(POS_PRICE);
                rangeWorker.get(Flags.Keys.PRICE).setKeyName(Flags.Keys.PRICE);
                setRangeMin(Flags.Keys.PRICE, 1000);
                setRangeMax(Flags.Keys.PRICE, rangeWorker.get(Flags.Keys.PRICE).getValues().getMax());
                ((SearchActivity)activity).isFirstLoad = false;
            }
            else {
                notifyDataSetChanged();
            }
        }
        catch(Exception e) {
            Log.e("PricelModelAdapter", e.getMessage());
        }
    }

    public void refactorAttributeListData(String keyName, boolean checked, ArrayList<String> valuesList, ArrayList<String> groupsList,
                                          String intRep, String group) {
        boolean isGroup = false;
        if(Integer.valueOf(group) > 0) isGroup = true;
        if(checked){
            if(isGroup) {
                groupsList.add(intRep);
            }
            else {
                valuesList.add(intRep);
            }
        }
        else {
            if(isGroup) {
                groupsList.remove(intRep);
            }
            else {
                valuesList.remove(intRep);
            }
        }
    }

    //Populate result count data into root list for CITY, MAKE, MODEL
    /*public void populateCountData(JsonArray jArr, List<AttrValSpinnerModel> attrList) {
        Map<Integer, String> mapIntRepCount = new HashMap<Integer, String>();
        for(int i=0; i<jArr.size(); i++) {
            JsonObject inJo = (JsonObject) jArr.get(i);
            Set<Map.Entry<String, JsonElement>> entrySet = inJo.entrySet();
            for(Map.Entry<String,JsonElement> entry : entrySet){
                //Log.d("KeyValue: ", "Key: "+entry.getKey() + ", value: " + entry.getValue() +"");
                JsonObject jCount = (JsonObject) entry.getValue();
                //Log.d("CountKeyValue: ", "count: "+jCount.get("count"));
                mapIntRepCount.put(Integer.valueOf(entry.getKey()), String.valueOf(jCount.get(Flags.Keys.COUNT).getAsString()));
            }
        }
        for(AttrValSpinnerModel attrib : attrList) {
            if(mapIntRepCount.containsKey(attrib.getInteger_representation())) {
                attrib.setCount(" (" + mapIntRepCount.get(attrib.getInteger_representation()) + ")");
            }
            else attrib.setCount(" (" + 0 + ")");
        }
    }

    //Populate result count data into root list  for BODY, TRANSMISSION and COLOUR
    public void populateCountData2(JsonObject inJoOuter, List<AttrValSpinnerModel> attrList) {
        try {
            Set<Map.Entry<String, JsonElement>> entrySetOuter = inJoOuter.entrySet();
            Map<Integer, String> mapIntRepCount = new HashMap<Integer, String>();
            for(Map.Entry<String, JsonElement> entryOuter : entrySetOuter) {
                JsonObject inJo = (JsonObject)entryOuter.getValue();
                Set<Map.Entry<String, JsonElement>> entrySet = inJo.entrySet();
                for(Map.Entry<String,JsonElement> entry : entrySet){
                    //Log.e("KeyValue: ", "Key: "+entry.getKey() + ", value: " + entry.getValue() +"");
                    JsonObject jCount = (JsonObject) entry.getValue();
                    //Log.e("CountKeyValue: ", "count: "+jCount.get("count"));
                    mapIntRepCount.put(Integer.valueOf(entry.getKey()), String.valueOf(jCount.get(Flags.Keys.COUNT).getAsString()));
                }
            }

            for(AttrValSpinnerModel attrib : attrList) {
                if(mapIntRepCount.containsKey(attrib.getInteger_representation())) {
                    attrib.setCount(" (" + mapIntRepCount.get(attrib.getInteger_representation()) + ")");
                }
                else attrib.setCount(" (" + 0 + ")");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }*/



    /*******************************    Network calls   *******************************************/


    /*public void requestItemGetCount(final int methodType, final String url, final Class<SearchResponse> clazz, final int id) {
        try {

            String tag_json_obj = "req_count_make";
            final byte[] request = requestData(requestBuilder());
            Client jsObjRequest = new Client(methodType, url, clazz, request, new OnCallListener() {
                @Override
                public void nwResponseData(String response) {
                    responseData = response;
                }
            }, new Response.Listener<Object>() {
                @Override
                public void onResponse(Object response) {
                    if(null != responseData) {
                        JsonObject jObj = new JsonParser().parse(responseData).getAsJsonObject();
                        Log.e("ClientResponse", "Response id: " + id);
                        //Clear Model data if status is 0
                        requestItemGetCountResponse(jObj, id);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, "Error: " + error);
                }
            }){
                @Override
                public byte[] getBody() throws AuthFailureError {
                    //Log.e(ActivityTag.SEARCH_FORM_ACTIVITY, "custom request for search API only");
                    return request;
                }
            };
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsObjRequest, tag_json_obj);

        }
        catch(Exception e) {
            Log.e(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, e.getMessage());
        }
    }

    public void requestItemGetCountResponse(JsonObject jObj, int id) {
        try {
            if(jObj.has(Flags.Keys.STATUS)) {
                JsonPrimitive status = (JsonPrimitive) jObj.get(Flags.Keys.STATUS);
                if(status.getAsInt() == 0) {
                    //clearModelFields();
                            *//*AttrValSpinnerModel labelObj = new AttrValSpinnerModel();
                            labelObj.setLabel(Flags.Labels.MODEL);
                            listModel.add(0, labelObj);*//*
                }
                else if(status.getAsInt() == 1) {
                    switch(id) {
                        case Flags.Labels.CITY_ID :
                                    *//*if(jObj.has(Keys.RESULT) && jObj.get(Keys.RESULT).isJsonObject()) {
                                        JsonObject o = (JsonObject) jObj.get(Keys.RESULT);
                                        if(o.has(Keys.CITY)) {
                                            JsonArray jArr = (JsonArray) o.get(Keys.CITY);
                                            populateCountData(jArr, recyclerAdapterCity, listCity);
                                        }
                                    }*//*
                            break;
                        case Flags.Labels.MAKE_ID :
                        {
                            if(jObj.has(Flags.Keys.RESULT)) {
                                //Populate Attribue model data
                                if(jObj.get(Flags.Keys.RESULT).isJsonObject()) {
                                    JsonObject o = (JsonObject)jObj.get(Flags.Keys.RESULT) ;
                                    if(o.has(Flags.Keys.MAKE)) {
                                        if(o.get(Flags.Keys.MAKE).isJsonArray()) {
                                            JsonArray jArr = (JsonArray) o.get(Flags.Keys.MAKE);
                                            populateCountData(jArr, multiSelectWorker.get(Flags.Keys.MAKE));
                                        }
                                        else if(o.get(Flags.Keys.MAKE).isJsonObject()) {
                                            JsonObject jo = (JsonObject) o.get(Flags.Keys.MAKE);
                                            populateCountData2(jo, multiSelectWorker.get(Flags.Keys.MAKE));
                                        }
                                    }
                                }
                            }
                        }
                        break;
                        case Flags.Labels.MODEL_ID :
                            if(jObj.has(Flags.Keys.RESULT) && jObj.get(Flags.Keys.RESULT).isJsonObject()) {
                                JsonObject o = (JsonObject) jObj.get(Flags.Keys.RESULT);
                                if(o.has(Flags.Keys.MODEL)) {
                                    JsonArray jArr = (JsonArray) o.get(Flags.Keys.MODEL);
                                    populateCountData(jArr, multiSelectWorker.get(Flags.Keys.MODEL));
                                }
                            }
                            break;
                        case Flags.Labels.BODY_COLOUR_TRANS_ID :
                            if(jObj.has(Flags.Keys.RESULT) && jObj.get(Flags.Keys.RESULT).isJsonObject()) {

                                JsonObject o = (JsonObject) jObj.get(Flags.Keys.RESULT);

                                if(o.has(Flags.Keys.BODY) && o.get(Flags.Keys.BODY).isJsonObject()) {
                                    JsonObject inJoOuter = (JsonObject) o.get(Flags.Keys.BODY);
                                    populateCountData2(inJoOuter, multiSelectWorker.get(Flags.Keys.BODY));
                                }

                                if(o.has(Flags.Keys.USER_TYPE) && o.get(Flags.Keys.USER_TYPE).isJsonObject()) {
                                    JsonObject inJoOuter = (JsonObject) o.get(Flags.Keys.USER_TYPE);
                                    populateCountData2(inJoOuter, multiSelectWorker.get(Flags.Keys.USER_TYPE));
                                }

                                if(o.has(Flags.Keys.TRANSMISSION) && o.get(Flags.Keys.TRANSMISSION).isJsonObject()) {
                                    JsonObject inJoOuter = (JsonObject) o.get(Flags.Keys.TRANSMISSION);
                                    populateCountData2(inJoOuter, multiSelectWorker.get(Flags.Keys.TRANSMISSION));
                                }

                                if(o.has(Flags.Keys.COLOUR) && o.get(Flags.Keys.COLOUR).isJsonObject()) {
                                    JsonObject inJoOuter = (JsonObject) o.get(Flags.Keys.COLOUR);
                                    populateCountData2(inJoOuter, multiSelectWorker.get(Flags.Keys.COLOUR));
                                }

                                if(o.has(Flags.Keys.DOORS) && o.get(Flags.Keys.DOORS).isJsonObject()) {
                                    JsonObject inJoOuter = (JsonObject) o.get(Flags.Keys.DOORS);
                                    populateCountData2(inJoOuter, multiSelectWorker.get(Flags.Keys.DOORS));
                                }

                                if(o.has(Flags.Keys.DRIVETRAIN) && o.get(Flags.Keys.DRIVETRAIN).isJsonObject()) {
                                    JsonObject inJoOuter = (JsonObject) o.get(Flags.Keys.DRIVETRAIN);
                                    populateCountData2(inJoOuter, multiSelectWorker.get(Flags.Keys.DRIVETRAIN));
                                }
                            }
                            break;
                    }
                }
                ////notifyDataSetChanged();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }*/

    private void requestSearchCreate(final int methodType, final String url, final Class<ResponseSearchCreate> resClass, final byte[] requestData) {

        try {
            //startTime = System.currentTimeMillis();
            //Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY,"Request start time: " + startTime);
            Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY,"attrib url: " + url);
            Client jsObjRequest = new Client(methodType, url, resClass, requestData, new OnCallListener() {
                @Override
                public void nwResponseData(String pData) {

                }
            }, new Response.Listener<Object>() {
                @Override
                public void onResponse(Object response) {
                    ((SearchActivity)activity).searchButton.setEnabled(true);
                    ResponseSearchCreate res = (ResponseSearchCreate) response;
                    //System.out.println("Response found: " + res.getStatus());
                    //Log.d(TAG, "Kilometers: "+res.getAttribute_label().getKilometers().getKey_name());

                    ResponseCode resCode = ResponseCode.fromValue(res.getStatus());
                    switch (resCode) {
                        case STATUS_SUCCESS: {
                            try {
                                /*searchID = res.getResult().getSearchID();
                                saveSearchLayout.setVisibility(View.GONE);
                                searchButton.setText(getResources().getString(R.string.save_and_search));
                                saveSearchSwitch.setChecked(true);
                                clearFormButton.setVisibility(View.GONE);
                                newFormButton.setVisibility(View.VISIBLE);
                                searchType = Flags.Bundle.Values.EDIT_SEARCH;
                                startSearchResult();*/

                                ((SearchActivity)activity).saveSearchLayout.setVisibility(View.GONE);
                                ((SearchActivity)activity).setSearchType(Flags.Bundle.Values.EDIT_SEARCH);
                                ((SearchActivity)activity).tvSearchButton.setText(context.getResources().getString(R.string.save_and_search));
                                searchID = res.getResult().getSearchID();
                                startSearchResult();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                        case STATUS_FAILURE: {
                            Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, "status 0");
                            break;
                        }
                        default: {
                            Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, "default");
                        }
                    }

                    //long timeTaken = System.currentTimeMillis() - startTime;
                    //Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, "Total time taken: " + timeTaken);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ((SearchActivity)activity).searchButton.setEnabled(true);
                    Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, "Error: " + error);
                }
            }, Request.Priority.HIGH){
                @Override
                public byte[] getBody() throws AuthFailureError {
                    return requestData;
                }
            };
            String tag_json_obj = "json_obj_req";
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsObjRequest, tag_json_obj);
        }
        catch(Exception e) {
            Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, e.getMessage());
        }
    }

    public void requestUpdateSavedSearch(int methodType, String url, final Class clazz) {
        try {

            Client jsObjRequest = new Client(methodType, url, clazz, new OnCallListener() {
                @Override
                public void nwResponseData(String pData) {

                }
            }, new Response.Listener<Object>() {
                @Override
                public void onResponse(Object response) {

                    SearchResponse res = (SearchResponse) response;
                    if (res.getStatus() == 1) {
                        startSearchResult();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(Flags.ActivityTag.SAVED_SEARCH_ACTIVITY, "Error: " + error);
                }
            }){
                @Override
                public byte[] getBody() throws AuthFailureError {
                    //Log.d(Flags.ActivityTag.SAVED_SEARCH_ACTIVITY, "custom request for search API");
                    return requestData(requestBuilder());
                }
            };
            String tag_json_obj = "json_obj_req";
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsObjRequest, tag_json_obj);
        }
        catch(Exception e) {
            Log.e(Flags.ActivityTag.SAVED_SEARCH_ACTIVITY, e.getMessage());
        }
    }

    /*public void requestItemGetCountAll(final int methodType, final String url, final Class<SearchResponse> clazz) {
        try {

            String reqTag = "req_count_all";
            AppController.getInstance().getRequestQueue().cancelAll(reqTag);

            final byte[] request = requestData(requestBuilder());
            //startTime = System.currentTimeMillis();
            //Log.d(ActivityTag.SEARCH_FORM_ACTIVITY,"Request start time: " + startTime);
            Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY,"count_attrib_url: " + url);
            Client jsObjRequest = new Client(methodType, url, clazz, request, new OnCallListener() {
                @Override
                public void nwResponseData(String pData) {
                    responseData = pData;
                }
            }, new Response.Listener<Object>() {
                @Override
                public void onResponse(Object response) {
                    JsonObject jObj = new JsonParser().parse(responseData).getAsJsonObject();
                    Log.e("ClientResponse", "ALL COUNT response");
                    //Clear Model data if status is 0
                    requestItemGetCountAllResponse(jObj);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, "Error: " + error);
                }
            }) {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    //Log.e(ActivityTag.SEARCH_FORM_ACTIVITY, "custom request for search API only");
                    return request;
                }
            };
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(100000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsObjRequest, reqTag);
        }
        catch(Exception e) {
            Log.e(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, e.getMessage());
        }
    }*/

    /*public void requestItemGetCountAllResponse(JsonObject jObj) {
        try {
            if (jObj.has(Flags.Keys.STATUS)) {
                JsonPrimitive status = (JsonPrimitive) jObj.get(Flags.Keys.STATUS);
                if (status.getAsInt() == 0) {
                    //clearModelFields();
                            *//*AttrValSpinnerModel labelObj = new AttrValSpinnerModel();
                            labelObj.setLabel(Flags.Labels.MODEL);
                            listModel.add(0, labelObj);*//*
                } else if (status.getAsInt() == 1) {
                    if (jObj.has(Flags.Keys.RESULT) && jObj.get(Flags.Keys.RESULT).isJsonObject()) {
                        JsonObject o = (JsonObject) jObj.get(Flags.Keys.RESULT);

                        *//*if (o.has(Flags.Keys.COUNTRY) && o.get(Flags.Keys.COUNTRY).isJsonArray()) {
                            JsonArray jArr = (JsonArray) o.get(Flags.Keys.COUNTRY);
                            populateCountData(jArr, multiSelectWorker.get(Flags.Keys.COUNTRY));
                        }*//*

                        if (o.has(Flags.Keys.CITY) && o.get(Flags.Keys.CITY).isJsonArray()) {
                            JsonArray jArr = (JsonArray) o.get(Flags.Keys.CITY);
                            populateCountData(jArr, multiSelectWorker.get(Flags.Keys.CITY));
                        }

                        if (o.has(Flags.Keys.MAKE) && o.get(Flags.Keys.MAKE).isJsonArray()) {
                            JsonArray jArr = (JsonArray) o.get(Flags.Keys.MAKE);
                            populateCountData(jArr, multiSelectWorker.get(Flags.Keys.MAKE));
                        }

                        if (o.has(Flags.Keys.MODEL) && o.get(Flags.Keys.MODEL).isJsonArray()) {
                            JsonArray jArr = (JsonArray) o.get(Flags.Keys.MODEL);
                            populateCountData(jArr, multiSelectWorker.get(Flags.Keys.MODEL));
                        }

                        if (o.has(Flags.Keys.BODY) && o.get(Flags.Keys.BODY).isJsonObject()) {
                            JsonObject inJoOuter = (JsonObject) o.get(Flags.Keys.BODY);
                            populateCountData2(inJoOuter, multiSelectWorker.get(Flags.Keys.BODY));
                        }

                        if (o.has(Flags.Keys.USER_TYPE) && o.get(Flags.Keys.USER_TYPE).isJsonObject()) {
                            JsonObject inJoOuter = (JsonObject) o.get(Flags.Keys.USER_TYPE);
                            populateCountData2(inJoOuter, multiSelectWorker.get(Flags.Keys.USER_TYPE));
                        }

                        if (o.has(Flags.Keys.TRANSMISSION) && o.get(Flags.Keys.TRANSMISSION).isJsonObject()) {
                            JsonObject inJoOuter = (JsonObject) o.get(Flags.Keys.TRANSMISSION);
                            populateCountData2(inJoOuter, multiSelectWorker.get(Flags.Keys.TRANSMISSION));
                        }

                        if (o.has(Flags.Keys.COLOUR) && o.get(Flags.Keys.COLOUR).isJsonObject()) {
                            JsonObject inJoOuter = (JsonObject) o.get(Flags.Keys.COLOUR);
                            populateCountData2(inJoOuter, multiSelectWorker.get(Flags.Keys.COLOUR));
                        }

                        if(o.has(Flags.Keys.DOORS) && o.get(Flags.Keys.DOORS).isJsonObject()) {
                            JsonObject inJoOuter = (JsonObject) o.get(Flags.Keys.DOORS);
                            populateCountData2(inJoOuter, multiSelectWorker.get(Flags.Keys.DOORS));
                        }

                        if(o.has(Flags.Keys.DRIVETRAIN) && o.get(Flags.Keys.DRIVETRAIN).isJsonObject()) {
                            JsonObject inJoOuter = (JsonObject) o.get(Flags.Keys.DRIVETRAIN);
                            populateCountData2(inJoOuter, multiSelectWorker.get(Flags.Keys.DRIVETRAIN));
                        }

                    }
                }
                ////notifyDataSetChanged();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }*/

    public boolean isSearchByKeyword = false;
    public void searchByKeywordsRequest(final int methodType, final String url, final Class resClass) {
        try {
            isSearchByKeyword = true;
            //clearForm(isSearchByKeyword, null);
            Map<String, String> params = new HashMap<String, String>();
            params.put("domain_id", "1");

            Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY,"attrib url: " + url);
            Client jsObjRequest = new Client(methodType, url, resClass, new OnCallListener() {
                @Override
                public void nwResponseData(String pData) {

                }
            }, new Response.Listener<Object>() {
                @Override
                public void onResponse(Object response) {
                    SearchByKeywordsResponse res = (SearchByKeywordsResponse) response;
                    Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, "status: " + res.getStatus());

                    ResponseCode resCode = ResponseCode.fromValue(res.getStatus());
                    switch (resCode) {
                        case STATUS_SUCCESS: {
                            try {
                                List<AttrValSpinnerModel> attrValues = res.getAttrValues();
                                if(attrValues.size() > 0) {
                                    //parseAndSetKeywordSearchData(attrValues);

                                    final List<AttrValSpinnerModel> tmpModel = new ArrayList<AttrValSpinnerModel>();

                                    for(AttrValSpinnerModel resultAttr : attrValues) {
                                        String key = resultAttr.getAttrbuteKey();
                                        switch(resultAttr.getAttrbuteKey()) {
                                            case Flags.Keys.COUNTRY:
                                                parseAndSetMultiSelectData(multiSelectWorker.get(key), resultAttr, key);
                                                break;
                                            case Flags.Keys.CITY:
                                                parseAndSetMultiSelectData(multiSelectWorker.get(key), resultAttr, key);
                                                //isFound = true;
                                                break;
                                            case Flags.Keys.MAKE:
                                                for(AttrValSpinnerModel attr : multiSelectWorker.get(key)) {
                                                    for(AttrValSpinnerModel resultList : attrValues) {
                                                        if(attr.getInteger_representation() == resultList.getInteger_representation()){
                                                            if(!attr.isSelected()){
                                                                attr.setSelected(true);
                                                                attr.setTmpSelected(true);
                                                                //attrValues.remove(resultList);
                                                                alterMakeDataSelected(attr.isSelected(), msCheckedValues.get(key),
                                                                        msCheckedGroups.get(key),
                                                                        String.valueOf(attr.getInteger_representation()),
                                                                        String.valueOf(attr.getGroup()), String.valueOf(attr.getId()));
                                                                tagToken.createMultiSelectTag(context, attr, key);
                                                                if(attrValueCheckedList.get(key) == null)
                                                                    attrValueCheckedList.put(key, new ArrayList<String>());
                                                                setMultiSelectUI(key, attr.isSelected(), attr.getValue(), attrValueCheckedList.get(key));
                                                                //isFound = true;
                                                            }
                                                        }
                                                    }
                                                }
                                                //requestModelData(Request.Method.GET, fetchModelUrl, SearchResponse.class);
                                                break;
                                            case Flags.Keys.MODEL:
                                                tmpModel.add(resultAttr);
                                                break;
                                            case Flags.Keys.BODY:
                                                parseAndSetMultiSelectData(multiSelectWorker.get(key), resultAttr, key);
                                                break;
                                            case Flags.Keys.USER_TYPE:
                                                parseAndSetMultiSelectData(multiSelectWorker.get(key), resultAttr, key);
                                                break;
                                            case Flags.Keys.TRANSMISSION:
                                                parseAndSetMultiSelectData(multiSelectWorker.get(key), resultAttr, key);
                                                break;
                                            case Flags.Keys.COLOUR:
                                                parseAndSetMultiSelectData(multiSelectWorker.get(key), resultAttr, key);
                                                break;
                                            case Flags.Keys.DOORS:
                                                parseAndSetMultiSelectData(multiSelectWorker.get(key), resultAttr, key);
                                                break;
                                            case Flags.Keys.DRIVETRAIN:
                                                parseAndSetMultiSelectData(multiSelectWorker.get(key), resultAttr, key);
                                                break;
                                            case Flags.Keys.YEAR:
                                                parseAndSetRangeData(rangeWorker.get(key), resultAttr, key);
                                                break;
                                        }
                                    }

                                    //Check if model data are available, initiate fetch model call
                                    if(msCheckedValues.get(Flags.Keys.MAKE).size() > 0 || msCheckedGroups.get(Flags.Keys.MAKE).size() > 0 || tmpModel.size() > 0) {
                                        String tag_json_obj = "fetch_model_req";
                                        modelStatus = ModelStatus.LOADING.getValue();
                                        notifyItemChanged(POS_MODEL);
                                        Client jsObjRequest = new Client(Request.Method.GET, fetchModelUrl, SearchResponse.class, new OnCallListener() {
                                            @Override
                                            public void nwResponseData(String pData) {
                                                responseData = pData;
                                            }
                                        }, new Response.Listener<Object>() {
                                            @Override
                                            public void onResponse(Object response) {
                                                boolean isResultAvailable = true;
                                                JsonObject jObj = new JsonParser().parse(responseData).getAsJsonObject();
                                                Log.d("ClientResponse", "Response MODEL data on MAKE select");
                                                if (jObj.has(Flags.Keys.STATUS)) {
                                                    JsonPrimitive statu = (JsonPrimitive) jObj.get(Flags.Keys.STATUS);
                                                    if(null != multiSelectWorker.get(Flags.Keys.MODEL)) {
                                                        for(AttrValSpinnerModel attr : multiSelectWorker.get(Flags.Keys.MODEL)) {
                                                            if(attr.isSelected()) {
                                                                attr.setSelected(false);
                                                                attr.setTmpSelected(false);
                                                                tagToken.removeMultiSelectTag(attr, Flags.Keys.MODEL);
                                                                refactorAttributeListData(Flags.Keys.MODEL, attr.isSelected(),
                                                                        msCheckedValues.get(Flags.Keys.MODEL), msCheckedGroups.get(Flags.Keys.MODEL),
                                                                        String.valueOf(attr.getInteger_representation()),
                                                                        String.valueOf(attr.getGroup()));
                                                                setMultiSelectUI(Flags.Keys.MODEL, false,attr.getValue(), attrValueCheckedList.get(Flags.Keys.MODEL));
                                                            }
                                                        }
                                                        multiSelectWorker.get(Flags.Keys.MODEL).clear();
                                                    }


                                                    if (statu.getAsInt() == 0) {
                                                        isResultAvailable = false;
                                                        modelStatus = ModelStatus.CHOOSE_A_MAKE_FIRST.getValue();

                                                    } else if (statu.getAsInt() == 1) {
                                                        if (jObj.has(Flags.Keys.RESULT)) {
                                                            if (jObj.get(Flags.Keys.RESULT).isJsonArray()) {
                                                                JsonArray jArr = (JsonArray) jObj.get(Flags.Keys.RESULT);
                                                                if (jArr.size() == 0) {
                                                                    modelStatus = ModelStatus.NO_MODELS_FOUND.getValue();
                                                                }
                                                                else
                                                                    modelStatus = ModelStatus.SELECT.getValue();

                                                                List<AttrValSpinnerModel> tmpList = new Gson().fromJson(jArr, new TypeToken<List<AttrValSpinnerModel>>() {
                                                                }.getType());
                                                                for (AttrValSpinnerModel attr : tmpList) {
                                                                    multiSelectWorker.get(Flags.Keys.MODEL).add(attr);
                                                                }
                                                                for(AttrValSpinnerModel attr : tmpModel) {
                                                                    parseAndSetMultiSelectData(multiSelectWorker.get(Flags.Keys.MODEL), attr, Flags.Keys.MODEL);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                if(!isResultAvailable) {
                                                    for (Map.Entry<String, ArrayList<String>> entry : msCheckedValues.entrySet()) {
                                                        if (entry.getValue().size() > 0) {
                                                            isResultAvailable = true;
                                                            break;
                                                        }
                                                    }
                                                }

                                                if(!isResultAvailable) {
                                                    for(Map.Entry<String, ArrayList<String>> entry : msCheckedGroups.entrySet()) {
                                                        if(entry.getValue().size() > 0) {
                                                            isResultAvailable = true;
                                                            break;
                                                        }
                                                    }
                                                }

                                                notifyDataSetChanged();
                                                searchByKeywordTriggerResult(isResultAvailable);
                                                swipeView();
                                                ///v1.2 attributeCountBatchRequest();
                                                ///v1.2 requestItemGetCountAll(Request.Method.POST, Flags.URL.ITEM_GETCOUNT + Flags.URL.DOMAIN_ID, SearchResponse.class);
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.e(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, "Error: " + error);
                                            }
                                        }, Request.Priority.IMMEDIATE);

                                        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                        AppController.getInstance().addToRequestQueue(jsObjRequest, tag_json_obj);
                                    }
                                    else {
                                        notifyDataSetChanged();
                                        triggerResult();
                                        swipeView();
                                        ((SearchActivity)activity).keywordSearchBarController(false, KEYWORD_SEARCH_FINISH);

                                        ///v1.2 requestItemGetCountAll(Request.Method.POST, Flags.URL.ITEM_GETCOUNT + Flags.URL.DOMAIN_ID, SearchResponse.class);
                                    }
                                }
                                else {
                                    searchByKeywordTriggerResult(false);
                                    //displayNoResult(String.valueOf(searchField.getText()));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                ((SearchActivity)activity).keywordSearchBarController(false, KEYWORD_SEARCH_FINISH);
                            }

                            break;
                        }
                        case STATUS_FAILURE: {
                            Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, "status 0");
                            ((SearchActivity)activity).keywordSearchBarController(false, KEYWORD_SEARCH_FINISH);
                            break;
                        }
                        default: {
                            Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, "default");
                            ((SearchActivity)activity).keywordSearchBarController(false, KEYWORD_SEARCH_FINISH);
                        }
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, "Error: " + error);
                    ((SearchActivity)activity).keywordSearchBarController(false, KEYWORD_SEARCH_FINISH);
                }
            }, Request.Priority.HIGH);
            String tag_json_obj = "json_obj_req";
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsObjRequest, tag_json_obj);
        }
        catch(Exception e) {
            Log.e(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, e.getMessage());
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

    private void searchByKeywordTriggerResult(boolean isResultAvailable) {
        if(isResultAvailable)
            triggerResult();
        else {
            Bundle bundle = new Bundle();
            bundle.putBoolean("ResultAvailable", false);
            bundle.putString(Flags.Bundle.Keys.KEYWORD, keywords);
            //bundle.putByteArray(Flags.Bundle.Keys.REQUEST_DATA, requestData(requestBuilder()));
            ((SearchActivity)activity).setRequestData(bundle);
        }
        swipeView();
        ((SearchActivity)activity).keywordSearchBarController(false, KEYWORD_SEARCH_FINISH);
    }

    public void parseAndSetMultiSelectData(List<AttrValSpinnerModel> originalList, AttrValSpinnerModel resultAttr, String key) {
        for(AttrValSpinnerModel attr : (List<AttrValSpinnerModel>)originalList) {
            if(attr.getInteger_representation() == resultAttr.getInteger_representation()){
                if(!attr.isSelected()){
                    attr.setSelected(true);
                    attr.setTmpSelected(true);
                    //attrValues.remove(resultList);
                    refactorAttributeListData(key, attr.isSelected(),
                            msCheckedValues.get(key), msCheckedGroups.get(key),
                            String.valueOf(attr.getInteger_representation()),
                            String.valueOf(attr.getGroup()));
                    tagToken.createMultiSelectTag(context, attr, key);
                    if(attrValueCheckedList.get(key) == null)
                        attrValueCheckedList.put(key, new ArrayList<String>());
                    setMultiSelectUI(key, attr.isSelected(), attr.getValue(), attrValueCheckedList.get(key));
                }
            }
        }
    }

    /*{"attribute_name":"year","attribute_data_type":"int","id":"256","value":"2012","integer_representation":"2012","group":"0"}*/
    public void parseAndSetRangeData(CommonFields rangeField, AttrValSpinnerModel result, String key) {

        rangeField.getValues().setMax(Integer.valueOf(result.getValue()));
        rangeField.getValues().setMin(Integer.valueOf(result.getValue()));

        if(!result.getValue().isEmpty()) {
            rangeWorker.get(key).getValues().setMin(Integer.valueOf(result.getValue()));
            applyRangeMinFormat(key, Integer.valueOf(result.getValue()));
        }
        else {
            rangeWorker.get(key).getValues().setMin(rangeWorker.get(key).getDefaultValue().getMin());
            applyRangeMinFormat(key, rangeWorker.get(key).getValues().getMin());
        }

        if(!result.getValue().isEmpty()) {
            rangeWorker.get(key).getValues().setMax(Integer.valueOf(result.getValue()));
            applyRangeMaxFormat(key, Integer.valueOf(result.getValue()));
        }
        else {
            rangeWorker.get(key).getValues().setMax(rangeWorker.get(key).getDefaultValue().getMax());
            applyRangeMaxFormat(key, rangeWorker.get(key).getValues().getMax());
        }
        updateRangeTags(key);

    }


    public ItemRequestData.Builder requestBuilder() {

        ItemRequestData.Builder builder = new ItemRequestData.Builder();

        if(rangeWorker.get(Flags.Keys.PRICE).getMinFormatted().isEmpty()) {
            formatMinVal(rangeWorker.get(Flags.Keys.PRICE).getValues().getMin(), rangeWorker.get(Flags.Keys.PRICE));
        }

        if(rangeWorker.get(Flags.Keys.PRICE).getMaxFormatted().isEmpty()) {
            formatMaxVal(rangeWorker.get(Flags.Keys.PRICE).getValues().getMax(), rangeWorker.get(Flags.Keys.PRICE));
        }

        if(rangeWorker.get(Flags.Keys.KILOMETERS).getMinFormatted().isEmpty()) {
            formatMinVal(rangeWorker.get(Flags.Keys.KILOMETERS).getValues().getMin(), rangeWorker.get(Flags.Keys.KILOMETERS));
        }

        if(rangeWorker.get(Flags.Keys.KILOMETERS).getMaxFormatted().isEmpty()) {
            formatMaxVal(rangeWorker.get(Flags.Keys.KILOMETERS).getValues().getMax(), rangeWorker.get(Flags.Keys.KILOMETERS));
        }

        if(rangeWorker.get(Flags.Keys.YEAR).getMinFormatted().isEmpty()) {
            formatMinVal(rangeWorker.get(Flags.Keys.YEAR).getValues().getMin(), rangeWorker.get(Flags.Keys.YEAR));
        }

        if(rangeWorker.get(Flags.Keys.YEAR).getMaxFormatted().isEmpty()) {
            formatMaxVal(rangeWorker.get(Flags.Keys.YEAR).getValues().getMax(), rangeWorker.get(Flags.Keys.YEAR));
        }

        if(rangeWorker.get(Flags.Keys.BELOW_MARKET_AVG).getMinFormatted().isEmpty()) {
            formatMinVal(rangeWorker.get(Flags.Keys.BELOW_MARKET_AVG).getValues().getMin(), rangeWorker.get(Flags.Keys.BELOW_MARKET_AVG));
        }

        if(rangeWorker.get(Flags.Keys.BELOW_MARKET_AVG).getMaxFormatted().isEmpty()) {
            formatMaxVal(rangeWorker.get(Flags.Keys.BELOW_MARKET_AVG).getValues().getMax(), rangeWorker.get(Flags.Keys.BELOW_MARKET_AVG));
        }


        builder.distanceMinRaw((rangeWorker.get(Flags.Keys.KILOMETERS).getValues().getMin() == rangeWorker.get(Flags.Keys.KILOMETERS).getDefaultValue().getMin())? "" : String.valueOf(rangeWorker.get(Flags.Keys.KILOMETERS).getValues().getMin()))
                .distanceMaxRaw((rangeWorker.get(Flags.Keys.KILOMETERS).getValues().getMax() == rangeWorker.get(Flags.Keys.KILOMETERS).getDefaultValue().getMax())? "":String.valueOf(rangeWorker.get(Flags.Keys.KILOMETERS).getValues().getMax()))
                .priceMinRaw((rangeWorker.get(Flags.Keys.PRICE).getValues().getMin() == rangeWorker.get(Flags.Keys.PRICE).getDefaultValue().getMin())? "" : String.valueOf(rangeWorker.get(Flags.Keys.PRICE).getValues().getMin()))
                .priceMaxRaw((rangeWorker.get(Flags.Keys.PRICE).getValues().getMax() == rangeWorker.get(Flags.Keys.PRICE).getDefaultValue().getMax())?"":String.valueOf(rangeWorker.get(Flags.Keys.PRICE).getValues().getMax()))
                .yearMinRaw((rangeWorker.get(Flags.Keys.YEAR).getValues().getMin()==rangeWorker.get(Flags.Keys.YEAR).getDefaultValue().getMin())?"":String.valueOf(rangeWorker.get(Flags.Keys.YEAR).getValues().getMin()))
                .yearMaxRaw((rangeWorker.get(Flags.Keys.YEAR).getValues().getMax() == rangeWorker.get(Flags.Keys.YEAR).getDefaultValue().getMax())?"":String.valueOf(rangeWorker.get(Flags.Keys.YEAR).getValues().getMax()))
                .bmaMinRaw((rangeWorker.get(Flags.Keys.BELOW_MARKET_AVG).getValues().getMin() == rangeWorker.get(Flags.Keys.BELOW_MARKET_AVG).getDefaultValue().getMin())?"":String.valueOf(rangeWorker.get(Flags.Keys.BELOW_MARKET_AVG).getValues().getMin()))
                .bmaMaxRaw((rangeWorker.get(Flags.Keys.BELOW_MARKET_AVG).getValues().getMax() == rangeWorker.get(Flags.Keys.BELOW_MARKET_AVG).getDefaultValue().getMax())?"":String.valueOf(rangeWorker.get(Flags.Keys.BELOW_MARKET_AVG).getValues().getMax()))

                .priceMinVal(rangeWorker.get(Flags.Keys.PRICE).getMinFormatted())
                .priceMaxVal(rangeWorker.get(Flags.Keys.PRICE).getMaxFormatted())
                .distanceMinVal(rangeWorker.get(Flags.Keys.KILOMETERS).getMinFormatted())
                .distanceMaxVal(rangeWorker.get(Flags.Keys.KILOMETERS).getMaxFormatted())
                .yearMinVal(rangeWorker.get(Flags.Keys.YEAR).getMinFormatted())
                .yearMaxVal(rangeWorker.get(Flags.Keys.YEAR).getMaxFormatted())
                .bmaMinVal(rangeWorker.get(Flags.Keys.BELOW_MARKET_AVG).getMinFormatted())
                .bmaMaxVal(rangeWorker.get(Flags.Keys.BELOW_MARKET_AVG).getMaxFormatted())

                .kilometersFormat(AppController.getInstance().getKilometerFormat())                  //Set format types to application class
                .priceFormat(AppController.getInstance().getCurrencyFormat());

        if(msCheckedValues.get(Flags.Keys.COUNTRY) != null && msCheckedValues.get(Flags.Keys.COUNTRY).size() > 0) builder.countryValueList(msCheckedValues.get(Flags.Keys.COUNTRY));
        if(msCheckedGroups.get(Flags.Keys.COUNTRY) != null && msCheckedGroups.get(Flags.Keys.COUNTRY).size() > 0) builder.countryGroupList(msCheckedGroups.get(Flags.Keys.COUNTRY));
        if(msCheckedValues.get(Flags.Keys.CITY) != null && msCheckedValues.get(Flags.Keys.CITY).size() > 0) builder.cityValueList(msCheckedValues.get(Flags.Keys.CITY));
        if(msCheckedGroups.get(Flags.Keys.CITY) != null && msCheckedGroups.get(Flags.Keys.CITY).size() > 0) builder.cityGroupList(msCheckedGroups.get(Flags.Keys.CITY));
        if(msCheckedValues.get(Flags.Keys.MAKE) != null && msCheckedValues.get(Flags.Keys.MAKE).size() > 0) builder.makeValueList(msCheckedValues.get(Flags.Keys.MAKE));
        if(msCheckedGroups.get(Flags.Keys.MAKE) != null && msCheckedGroups.get(Flags.Keys.MAKE).size() > 0) builder.makeGroupList(msCheckedGroups.get(Flags.Keys.MAKE));
        if(msCheckedValues.get(Flags.Keys.MODEL) != null && msCheckedValues.get(Flags.Keys.MODEL).size() > 0) builder.modelValueList(msCheckedValues.get(Flags.Keys.MODEL));
        if(msCheckedGroups.get(Flags.Keys.MODEL) != null && msCheckedGroups.get(Flags.Keys.MODEL).size() > 0) builder.modelGroupList(msCheckedGroups.get(Flags.Keys.MODEL));
        if(msCheckedValues.get(Flags.Keys.BODY) != null && msCheckedValues.get(Flags.Keys.BODY).size() > 0) builder.bodyValueList(msCheckedValues.get(Flags.Keys.BODY));
        if(msCheckedGroups.get(Flags.Keys.BODY) != null && msCheckedGroups.get(Flags.Keys.BODY).size() > 0) builder.bodyGroupList(msCheckedGroups.get(Flags.Keys.BODY));
        if(msCheckedValues.get(Flags.Keys.USER_TYPE) != null && msCheckedValues.get(Flags.Keys.USER_TYPE).size() > 0) builder.userTypeValueList(msCheckedValues.get(Flags.Keys.USER_TYPE));
        if(msCheckedGroups.get(Flags.Keys.USER_TYPE) != null && msCheckedGroups.get(Flags.Keys.USER_TYPE).size() > 0) builder.userTypeGroupList(msCheckedGroups.get(Flags.Keys.USER_TYPE));
        if(msCheckedValues.get(Flags.Keys.TRANSMISSION) != null && msCheckedValues.get(Flags.Keys.TRANSMISSION).size() > 0) builder.transmissionValueList(msCheckedValues.get(Flags.Keys.TRANSMISSION));
        if(msCheckedGroups.get(Flags.Keys.TRANSMISSION) != null && msCheckedGroups.get(Flags.Keys.TRANSMISSION).size() > 0) builder.transmissionGroupList(msCheckedGroups.get(Flags.Keys.TRANSMISSION));
        if(msCheckedValues.get(Flags.Keys.COLOUR) != null && msCheckedValues.get(Flags.Keys.COLOUR).size() > 0) builder.colourValueList(msCheckedValues.get(Flags.Keys.COLOUR));
        if(msCheckedGroups.get(Flags.Keys.COLOUR) != null && msCheckedGroups.get(Flags.Keys.COLOUR).size() > 0) builder.colourGroupList(msCheckedGroups.get(Flags.Keys.COLOUR));
        if(msCheckedValues.get(Flags.Keys.DOORS) != null && msCheckedValues.get(Flags.Keys.DOORS).size() > 0) builder.doorsValueList(msCheckedValues.get(Flags.Keys.DOORS));
        if(msCheckedGroups.get(Flags.Keys.DOORS) != null && msCheckedGroups.get(Flags.Keys.DOORS).size() > 0) builder.doorsGroupList(msCheckedGroups.get(Flags.Keys.DOORS));
        if(msCheckedValues.get(Flags.Keys.DRIVETRAIN) != null && msCheckedValues.get(Flags.Keys.DRIVETRAIN).size() > 0) builder.drivetrainValueList(msCheckedValues.get(Flags.Keys.DRIVETRAIN));
        if(msCheckedGroups.get(Flags.Keys.DRIVETRAIN) != null && msCheckedGroups.get(Flags.Keys.DRIVETRAIN).size() > 0) builder.drivetrainGroupList(msCheckedGroups.get(Flags.Keys.DRIVETRAIN));

        if(null != includeNoImageCarsCB && includeNoImageCarsCB.isChecked()) builder.image0(true);

        builder.sort(this.sortParam);

        if(saveSearchSwitch) {
            int enableNotification = (this.receiveNotificationSwitch) ? 1 : 0;
            builder.enableNotification(enableNotification);
            builder.saveSubmit(saveSearchSwitch);
            if(searchID != null) builder.searchID(searchID);
        }

        return builder;
    }

    public byte[] requestData(ItemRequestData.Builder builder) {
        ItemRequestData data = builder.build();
        return data.paramsByteFormat(data.getRequestData());
    }

    List<AttrValSpinnerModel> selectedModels = new ArrayList<>();
    public synchronized void requestModelData(final int methodType, final String url, final Class<SearchResponse> clazz) {
        try {
            String tag_json_obj = "fetch_model_req";
            //AppController.getInstance().cancelPendingRequests(tag_json_obj);
            modelStatus = ModelStatus.LOADING.getValue();
            notifyItemChanged(POS_MODEL);
            //isMakeRequestInProgress = true;
            Client jsObjRequest = new Client(methodType, url, clazz, new OnCallListener() {
                @Override
                public void nwResponseData(String pData) {
                    responseData = pData;
                }
            }, new Response.Listener<Object>() {
                @Override
                public void onResponse(Object response) {

                    JsonObject jObj = new JsonParser().parse(responseData).getAsJsonObject();
                    Log.d("ClientResponse", "Response MODEL data on MAKE select");
                    if(jObj.has(Flags.Keys.STATUS)) {
                        JsonPrimitive statu = (JsonPrimitive) jObj.get(Flags.Keys.STATUS);
                        selectedModels.clear();

                        if(null != multiSelectWorker.get(Flags.Keys.MODEL) && multiSelectWorker.get(Flags.Keys.MODEL).size() > 0) {

                            for(AttrValSpinnerModel attr : multiSelectWorker.get(Flags.Keys.MODEL)) {
                                if(attr.isSelected()) {
                                    selectedModels.add(attr);
                                }
                            }
                        }

                        if(null != multiSelectWorker.get(Flags.Keys.MODEL)) {
                            multiSelectWorker.get(Flags.Keys.MODEL).clear();                //Clear old list
                        }

                        if(statu.getAsInt() == 0) {
                            modelStatus = ModelStatus.CHOOSE_A_MAKE_FIRST.getValue();
                            if(selectedModels.size() > 0) {
                                for(AttrValSpinnerModel attr : selectedModels) {
                                    attr.setSelected(false);
                                    attr.setTmpSelected(false);
                                    tagToken.removeMultiSelectTag(attr, Flags.Keys.MODEL);
                                    refactorAttributeListData(Flags.Keys.MODEL, attr.isSelected(),
                                            msCheckedValues.get(Flags.Keys.MODEL), msCheckedGroups.get(Flags.Keys.MODEL),
                                            String.valueOf(attr.getInteger_representation()),
                                            String.valueOf(attr.getGroup()));
                                    setMultiSelectUI(Flags.Keys.MODEL, false, attr.getValue(), attrValueCheckedList.get(Flags.Keys.MODEL));
                                }
                            }

                        } else if (statu.getAsInt() == 1) {
                            if (jObj.has(Flags.Keys.RESULT)) {
                                if (jObj.get(Flags.Keys.RESULT).isJsonArray()) {
                                    JsonArray jArr = (JsonArray) jObj.get(Flags.Keys.RESULT);

                                    if (jArr.size() == 0) {
                                        modelStatus = ModelStatus.NO_MODELS_FOUND.getValue();
                                        if(selectedModels.size() > 0) {
                                            for(AttrValSpinnerModel attr : selectedModels) {
                                                attr.setSelected(false);
                                                attr.setTmpSelected(false);
                                                tagToken.removeMultiSelectTag(attr, Flags.Keys.MODEL);
                                                refactorAttributeListData(Flags.Keys.MODEL, attr.isSelected(),
                                                        msCheckedValues.get(Flags.Keys.MODEL), msCheckedGroups.get(Flags.Keys.MODEL),
                                                        String.valueOf(attr.getInteger_representation()),
                                                        String.valueOf(attr.getGroup()));
                                                setMultiSelectUI(Flags.Keys.MODEL, false, attr.getValue(), attrValueCheckedList.get(Flags.Keys.MODEL));
                                            }
                                        }
                                    }
                                    else
                                        modelStatus = ModelStatus.SELECT.getValue();

                                    List<AttrValSpinnerModel> tmpList = new Gson().fromJson(jArr, new TypeToken<List<AttrValSpinnerModel>>() {}.getType());
                                    //for (AttrValSpinnerModel attr : tmpList) {
                                    multiSelectWorker.get(Flags.Keys.MODEL).addAll(tmpList);
                                    //}



                                    for(AttrValSpinnerModel attr : selectedModels) {
                                        boolean found = false;
                                        for(AttrValSpinnerModel attrNew : multiSelectWorker.get(Flags.Keys.MODEL)) {
                                            if(attr.getInteger_representation() == attrNew.getInteger_representation()) {
                                                attrNew.setSelected(true);
                                                attrNew.setTmpSelected(true);
                                                attrNew.setLabel(attr.getLabel());
                                                found = true;
                                            }
                                        }
                                        if(!found) {
                                            attr.setSelected(false);
                                            attr.setTmpSelected(false);
                                            tagToken.removeMultiSelectTag(attr, Flags.Keys.MODEL);
                                            refactorAttributeListData(Flags.Keys.MODEL, attr.isSelected(),
                                                    msCheckedValues.get(Flags.Keys.MODEL), msCheckedGroups.get(Flags.Keys.MODEL),
                                                    String.valueOf(attr.getInteger_representation()),
                                                    String.valueOf(attr.getGroup()));
                                            setMultiSelectUI(Flags.Keys.MODEL, false, attr.getValue(), attrValueCheckedList.get(Flags.Keys.MODEL));
                                        }
                                    }

                                    /*if (isResultRequestInitiated) {
                                        isResultRequestInitiated = false;
                                        if (tmpModelList.size() > 0) {
                                            for (AttrValSpinnerModel attr : tmpModelList) {
                                                attr.getValue();
                                                refactorOriginalList(listModel, attr.getId(), attr, modelValues, modelGroup);
                                            }
                                            tmpModelList.clear();
                                        }
                                        triggerResult();
                                    } else if ((!(makeValues.size() > 0) || !(makeGroup.size() > 0))) {
                                        checkListModel(Flags.Keys.MODEL, true, listModel, modelValues, modelGroup, modelHeaders);
                                        if (null != recyclerAdapterModel)
                                            recyclerAdapterModel.notifyDataSetChanged();
                                    }*/
                                }
                            }
                        }
                    }

                    if(msCheckedValues.get(Flags.Keys.MAKE).size() < 1 && msCheckedGroups.get(Flags.Keys.MAKE).size() < 1) {
                        for(AttrValSpinnerModel attr : multiSelectWorker.get(Flags.Keys.MODEL)) {
                            if(attr.isSelected()) {
                                attr.setSelected(false);
                                attr.setTmpSelected(false);
                                tagToken.removeMultiSelectTag(attr, Flags.Keys.MODEL);
                                refactorAttributeListData(Flags.Keys.MODEL, attr.isSelected(),
                                        msCheckedValues.get(Flags.Keys.MODEL), msCheckedGroups.get(Flags.Keys.MODEL),
                                        String.valueOf(attr.getInteger_representation()),
                                        String.valueOf(attr.getGroup()));
                                setMultiSelectUI(Flags.Keys.MODEL, false, attr.getValue(), attrValueCheckedList.get(Flags.Keys.MODEL));
                            }
                        }

                        selectedModels.clear();

                        if(null != multiSelectWorker.get(Flags.Keys.MODEL) && multiSelectWorker.get(Flags.Keys.MODEL).size() > 0) {
                            multiSelectWorker.get(Flags.Keys.MODEL).clear();
                        }

                        modelStatus = ModelStatus.CHOOSE_A_MAKE_FIRST.getValue();
                    }
                    //multiSelectModelAdapter.notifyDataSetChanged();
                    notifyDataSetChanged();
                    ///v1.2 attributeCountBatchRequest();
                    ///v1.2 requestItemGetCountAll(Request.Method.POST, Flags.URL.ITEM_GETCOUNT + Flags.URL.DOMAIN_ID, SearchResponse.class);
                    //isMakeRequestInProgress = false;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, "Error: " + error);
                }
            }, Request.Priority.IMMEDIATE);

            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsObjRequest, tag_json_obj);

        }
        catch(Exception e) {
            Log.e(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, e.getMessage());
        }
    }

    public void attributeCountBatchRequest() {
        //requestItemGetCount(Request.Method.POST, Flags.URL.ITEM_GETCOUNT + Flags.URL.ATTRIBUTE_CITY, SearchResponse.class, Flags.Labels.CITY_ID);
        ///v1.2 requestItemGetCount(Request.Method.POST, Flags.URL.ITEM_GETCOUNT + Flags.URL.ATTRIBUTE_MAKE, SearchResponse.class, Flags.Labels.MAKE_ID);
        ///v1.2 requestItemGetCount(Request.Method.POST, Flags.URL.ITEM_GETCOUNT + Flags.URL.ATTRIBUTE_BODY_TRANS_COLOUR, SearchResponse.class, Flags.Labels.BODY_COLOUR_TRANS_ID);
        ///v1.2 requestItemGetCount(Request.Method.POST, Flags.URL.ITEM_GETCOUNT + Flags.URL.ATTRIBUTE_MODEL, SearchResponse.class, Flags.Labels.MODEL_ID);
    }


    public void multiSelectEventTrigger(String keyname) {
        try {
            if(keyname.equals(Flags.Keys.MAKE))
                requestModelData(Request.Method.GET, this.fetchModelUrl, SearchResponse.class);
            ///v1.2 else
            ///v1.2 requestItemGetCountAll(Request.Method.POST, Flags.URL.ITEM_GETCOUNT + Flags.URL.DOMAIN_ID, SearchResponse.class);
        }
        catch(Exception e){
            Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, e.getMessage());
        }
    }

    public void triggerResult() {
        try {
            Bundle bundle = new Bundle();
            bundle.putByteArray(Flags.Bundle.Keys.REQUEST_DATA, requestData(requestBuilder()));
            bundle.putString(Flags.Bundle.Keys.KEYWORD, keywords);
            ((SearchActivity)activity).setRequestData(bundle);

            //SearchBar buttons
            //enableButtons();
        }
        catch(Exception e) {
            Log.e(Flags.ActivityTag.SEARCH_ACTIVITY, e.getMessage());
        }
    }


    public class TagToken{

        public boolean isTagExists() {
            if(parentTagLayout.getChildCount() > 0)
                return true;
            else
                return false;
        }

        public void createRangeTag(Context context, final CommonFields cf, final String keyname) {

            if(cf.getValues().getMin() != cf.getDefaultValue().getMin()
                    || cf.getValues().getMax() != cf.getDefaultValue().getMax()) {
                final LinearLayout tagLayout = tagTokenNew.createRangeTag(cf);

                tagLayout.setOnClickListener(new LinearLayout.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeRangeTag(cf, keyname);
                        ((SearchActivity)activity).refreshNotifier();
                    }
                });
                ((SearchActivity)activity).syncTagLayout(false, true, null, cf, false, keyname);
                cf.setTagCreated(true);
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

                            if(clearAll(false)){
                                triggerResult();
                            }
                        }
                    });
                    parentTagLayout.addView(tl, parentTagLayout.getChildCount());
                }
            }
        }

        public void updateRangeTag(CommonFields rangeAttr, String keyname) {
            TextView tv = (TextView) parentTagLayout.findViewWithTag(rangeAttr.getKeyName());
            String val = rangeAttr.getMinFormatted() + " - " + rangeAttr.getMaxFormatted();
            tv.setText(val);
            ((SearchActivity)activity).syncTagLayout(false, true, null, rangeAttr, false, keyname);

            if(rangeAttr.isTagCreated()) {
                if(rangeAttr.getValues().getMin() == rangeAttr.getDefaultValue().getMin()
                        && rangeAttr.getValues().getMax() == rangeAttr.getDefaultValue().getMax()) {
                    removeRangeTag(rangeAttr, keyname);
                }
            }
        }

        public void addOrUpdateRangeTag(Context context, final CommonFields cf, final String keyname) {
            if(cf.isTagCreated())
                updateRangeTag(cf, keyname);
            else
                createRangeTag(context, cf, keyname);
        }

        public void removeRangeTag(CommonFields cf, String keyname) {
            if(rangeWorker.containsKey(keyname)) {
                cf.getValues().setMin(cf.getDefaultValue().getMin());
                cf.getValues().setMax(cf.getDefaultValue().getMax());
                cf.setTagCreated(false);
                cf.setMaxFormatted("");
                cf.setMinFormatted("");
                parentTagLayout.removeView((LinearLayout)parentTagLayout.findViewWithTag(cf));
                ((SearchActivity)activity).syncTagLayout(false, false, null, cf, false, keyname);
                checkForLastTag();
                notifyDataSetChanged();
            }
        }

        public void createMultiSelectTag(Context context, final AttrValSpinnerModel attr, final String keyname) {

            final LinearLayout tagLayout = tagTokenNew.createMultiSelectTag(attr, keyname);

            tagLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(multiSelectWorker.containsKey(attr.getAttrbuteKey())) {
                        for(AttrValSpinnerModel attribute : ((List<AttrValSpinnerModel>)multiSelectWorker.get(attr.getAttrbuteKey()))) {
                            if(attribute.getInteger_representation() == attr.getInteger_representation()) {
                                attribute.setSelected(false);
                                attribute.setTmpSelected(false);
                                parentTagLayout.removeView(tagLayout);
                                if(!keyname.equals(Flags.Keys.MAKE)) {
                                    refactorAttributeListData(keyname, attr.isSelected(), msCheckedValues.get(keyname), msCheckedGroups.get(keyname),
                                            String.valueOf(attr.getInteger_representation()), String.valueOf(attr.getGroup()));
                                }
                                else {
                                    requestModelData(Request.Method.GET, alterMakeDataSelected(attr.isSelected(), msCheckedValues.get(keyname), msCheckedGroups.get(keyname),
                                            String.valueOf(attr.getInteger_representation()),
                                            String.valueOf(attr.getGroup()), String.valueOf(attr.getId())), SearchResponse.class);
                                    /*alterMakeDataSelected(attr.isSelected(), msCheckedValues.get(keyname), msCheckedGroups.get(keyname),
                                            String.valueOf(attr.getInteger_representation()),
                                            String.valueOf(attr.getGroup()), String.valueOf(attr.getId()));*/
                                }

                                ((SearchActivity)activity).syncTagLayout(false, false, attr, null, true, keyname);

                                setMultiSelectUI(keyname, attr.isSelected(), attr.getValue(), attrValueCheckedList.get(keyname));
                                checkForLastTag();
                                ((SearchActivity)activity).refreshNotifier();
                                //multiSelectEventTrigger(keyname);
                                notifyDataSetChanged();//notifyItemChanged(multiSelectPosition(keyname));

                                break;
                            }
                        }
                    }
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
                        /*if(clearAll(false)) {
                            triggerResult();
                        }*/
                        ((SearchActivity)activity).clearAllTags();
                    }
                });
                parentTagLayout.addView(tl, parentTagLayout.getChildCount());
            }
            //final LinearLayout cloneTagLayout = tagLayout;
            ((SearchActivity)activity).syncTagLayout(false, true, attr, null, true, keyname);
        }

        public void removeMultiSelectTag(AttrValSpinnerModel attr, String keyname) {
            parentTagLayout.removeView((LinearLayout)parentTagLayout.findViewWithTag(keyname+attr.getInteger_representation()));
            ((SearchActivity)activity).syncTagLayout(false, false, attr, null, true, keyname);
            checkForLastTag();
        }

        public void checkForLastTag() {
            if(parentTagLayout.getChildCount() == 1) {
                parentTagLayout.removeView((LinearLayout)parentTagLayout.findViewWithTag("cleartag"));
            }
        }
    }

    public void attemptSearchResultNavigation() {
        try {
            if(saveSearchSwitch) {
                if(mSession != null && mSession.isLoggedIn()) {
                    boolean isReadyForSave = false;
                    for(Map.Entry<String, ArrayList<String>> entry : msCheckedValues.entrySet()) {
                        if(entry.getValue().size() > 0) {
                            isReadyForSave = true;
                            break;
                        }
                    }

                    if(!isReadyForSave) {
                        for(Map.Entry<String, ArrayList<String>> entry : msCheckedGroups.entrySet()) {
                            if(entry.getValue().size() > 0) {
                                isReadyForSave = true;
                                break;
                            }
                        }
                    }

                    if(!isReadyForSave) {
                        for(Map.Entry<String, CommonFields> entry : rangeWorker.entrySet()) {
                            if(entry.getValue().getValues().getMin() != entry.getValue().getDefaultValue().getMin()
                                    || entry.getValue().getValues().getMax() != entry.getValue().getDefaultValue().getMax()) {
                                isReadyForSave = true;
                                break;
                            }
                        }
                    }

                    if(isReadyForSave) {
                        if(((SearchActivity)activity).getSearchType() == Flags.Bundle.Values.EDIT_SEARCH) {
                            requestUpdateSavedSearch(Request.Method.POST, Flags.URL.UPDATE_SAVED_SEARCH+searchID+"?domain_id=1", SearchResponse.class);
                        }
                        else {
                            ((SearchActivity)activity).searchButton.setEnabled(false);
                            requestSearchCreate(Request.Method.POST, Flags.URL.SEARCH_CREATE, ResponseSearchCreate.class, requestData(requestBuilder()));
                        }

                    }
                    else{
                        //TODO Show dialog box with message to choose atleast one field
                        Toast.makeText(context, "Please select at least one field", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    //TODO: Need to implement fragment on SearchActivity so user doesnt lose selected Search fields data
                    Intent intent = new Intent(((SearchActivity)activity).getApplicationContext(), LoginActivity.class);
                    ((SearchActivity)activity).startActivity(intent);//, 1);
                }
            }
            else
                startSearchResult();

            //Empty Keyword search field
            clearKeywordSearchField();
        }
        catch(Exception e) {
            e.printStackTrace();
            Log.e(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, e.getMessage());
        }
    }

    public void sortResult(int param) {
        try {
            this.sortParam = param;
            triggerResult();
        }
        catch(Exception e) {
            Log.e(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, e.getMessage());
        }
    }

    private void startSearchResult() {
        try {
            triggerResult();
            swipeView();
        }
        catch(Exception e) {
            e.printStackTrace();
            Log.e(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, e.getMessage());
        }
    }

    public void swipeView() {
        if(null != activity) {
            scrollEventListener.scrollToTopListener(0);
            ((BaseActivity)activity).mBottomLayout.setVisibility(View.GONE);
            ((SearchActivity)activity).mPager.setCurrentItem(1, true);
        }
    }

    public void enableSaveSearch(boolean checked) {
        this.saveSearchSwitch = checked;
    }

    public void setSearchID(String id) {
        this.searchID = id;
    }

    /*public void setSearchType(int type) {
        this.searchType = type;
    }*/

    public void enableReceiveNotification(boolean checked) {
        this.receiveNotificationSwitch = checked;
    }

    public void clearAllTags() {
        if(clearAll(false))
            triggerResult();
    }

    public boolean applyDefaultCity(boolean isDefault) {
        if(null != (AppController) activity.getApplication()) {
            Bundle bundle;
            clearCountry();

            scrollEventListener.scrollToTopListener(0);
            if(isDefault) {
                clearCity();
                bundle = ((AppController) activity.getApplication()).getDefaultCityBundle();
            }
            else
                bundle = ((AppController) activity.getApplication()).getVariableCityBundle();

            if(null != bundle) {
                List<AttrValSpinnerModel> attrList = (List<AttrValSpinnerModel>) bundle.get(Flags.Bundle.Keys.DEFAULT_CITY);
                for(AttrValSpinnerModel targetAttr : attrList) {
                    if(null != multiSelectWorker.get(Flags.Keys.CITY)) {
                        for(AttrValSpinnerModel attr : multiSelectWorker.get(Flags.Keys.CITY)) {
                            if(attr.getInteger_representation() == targetAttr.getInteger_representation()){//||
                                //(attr.getId() == targetAttr.getId()) || (attr.getInteger_representation() == targetAttr.getId())) { //Second condition here is to satisfy model as response has id whose value matches with the int_rep
                                if(!attr.isSelected()) {
                                    attr.setSelected(true);
                                    attr.setTmpSelected(true);
                                    refactorAttributeListData(attr.getAttrbuteKey(), true,
                                            msCheckedValues.get(Flags.Keys.CITY), msCheckedGroups.get(Flags.Keys.CITY),
                                            String.valueOf(attr.getInteger_representation()),
                                            String.valueOf(attr.getGroup()));

                                    if(attr.isSelected())
                                        tagToken.createMultiSelectTag(context, attr, Flags.Keys.CITY);

                                    if(attrValueCheckedList.get(Flags.Keys.CITY) == null)
                                        attrValueCheckedList.put(Flags.Keys.CITY, new ArrayList<String>());

                                    setMultiSelectUI(Flags.Keys.CITY, attr.isSelected(), attr.getValue(), attrValueCheckedList.get(Flags.Keys.CITY));
                                    //multiSelectEventTrigger(Flags.Keys.CITY);
                                    notifyDataSetChanged();
                                }
                            }
                        }
                    }
                }
            }

            if(isGeoLocUpdateInProgress && null != pbGeoLoc && null != btnGeoLocate) {
                pbGeoLoc.setVisibility(View.GONE);
                btnGeoLocate.setVisibility(View.VISIBLE);
                isGeoLocUpdateInProgress = false;
            }
        }
        return true;
    }

    public List<AttrValSpinnerModel> getMultiSelectAttr(String key){
        return this.multiSelectWorker.get(key);
    }

    public ArrayList<String> getMSCheckedValues(String key) {
        return this.msCheckedValues.get(key);
    }

    public ArrayList<String> getMSCheckedGroups(String key) {
        return this.msCheckedGroups.get(key);
    }

    public void wallOfDealsData() {
        try {
            Bundle rangeBundle = ((SearchActivity)activity).parcelBundle;
            EditSearchRangeAttributes attributes = (EditSearchRangeAttributes) rangeBundle.get(Flags.Keys.BELOW_MARKET_AVG);
            rangeWorker.get(Flags.Keys.BELOW_MARKET_AVG).getValues().setMin(Integer.valueOf(attributes.getMin()));
            rangeWorker.get(Flags.Keys.BELOW_MARKET_AVG).setViewPos(POS_BELOW_MARKET_AVG);
            rangeWorker.get(Flags.Keys.BELOW_MARKET_AVG).setKeyName(Flags.Keys.BELOW_MARKET_AVG);
            //tagToken.addOrUpdateRangeTag(context, (CommonFields) rangeWorker.get(Flags.Keys.BELOW_MARKET_AVG), Flags.Keys.BELOW_MARKET_AVG);
            setRangeMin(Flags.Keys.BELOW_MARKET_AVG, Integer.valueOf(attributes.getMin()));
            setRangeMax(Flags.Keys.BELOW_MARKET_AVG, rangeWorker.get(Flags.Keys.BELOW_MARKET_AVG).getValues().getMax());

            rangeWorker.get(Flags.Keys.PRICE).getValues().setMin(1000);
            rangeWorker.get(Flags.Keys.PRICE).setViewPos(POS_PRICE);
            rangeWorker.get(Flags.Keys.PRICE).setKeyName(Flags.Keys.PRICE);
            //tagToken.addOrUpdateRangeTag(context, (CommonFields) rangeWorker.get(Flags.Keys.PRICE), Flags.Keys.PRICE);
            setRangeMin(Flags.Keys.PRICE, 1000);
            setRangeMax(Flags.Keys.PRICE, rangeWorker.get(Flags.Keys.PRICE).getValues().getMax());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setWallOfDealsData() {
        try {
            wallOfDealsData();
            notifyDataSetChanged();
            triggerResult();
            ///v1.2 attributeCountBatchRequest();
            ///v1.2 requestItemGetCountAll(Request.Method.POST, Flags.URL.ITEM_GETCOUNT + Flags.URL.DOMAIN_ID, SearchResponse.class);
        }
        catch(Exception e) {
            e.printStackTrace();
            Log.e(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, e.getMessage());
        }
    }

    public void applyRangeMaxFormat(String keyname, int maxVal) {
        String fmax = formatMaxVal(maxVal, rangeWorker.get(keyname));
        rangeWorker.get(keyname).setMaxFormatted(fmax);
    }

    public void applyRangeMinFormat(String keyname, int minVal) {
        String fmin = formatMinVal(minVal, rangeWorker.get(keyname));
        rangeWorker.get(keyname).setMinFormatted(fmin);
    }

    public void setRangeMax(String keyname, int maxVal) {
        applyRangeMaxFormat(keyname, maxVal);
        updateRangeTags(keyname);
    }

    public void setRangeMin(String keyname, int minVal) {
        applyRangeMinFormat(keyname, minVal);
        updateRangeTags(keyname);
    }

    public void updateRangeTags(String keyname) {
        try {
            if(rangeWorker.get(keyname).getDefaultValue().getMin() != rangeWorker.get(keyname).getValues().getMin()
                    || rangeWorker.get(keyname).getDefaultValue().getMax() != rangeWorker.get(keyname).getValues().getMax())
                tagToken.addOrUpdateRangeTag(context, rangeWorker.get(keyname), keyname);
            else {
                removeRangeTagToken(rangeWorker.get(keyname), keyname);
            }
        }
        catch(Exception e) {

        }
    }

    public void setEditSearchParcelData() {
        try {
            Bundle rangeBundle = ((SearchActivity)activity).getIntent().getBundleExtra(Flags.Bundle.Keys.ATTR_RANGE_PARCEL);
            Bundle multiSelectBundle = ((SearchActivity)activity).getIntent().getBundleExtra(Flags.Bundle.Keys.ATTRIBUTE_PARCEL);

            for(Map.Entry<String, CommonFields> entry : rangeWorker.entrySet()) {
                if(null != rangeBundle && rangeBundle.containsKey(entry.getKey())) {
                    EditSearchRangeAttributes attributes = (EditSearchRangeAttributes) rangeBundle.get(entry.getKey());

                    switch (entry.getKey()) {
                        case Flags.Keys.PRICE:
                            rangeWorker.get(entry.getKey()).setViewPos(POS_PRICE);
                            AppController.getInstance().setCurrencyFormat(attributes.getDisplayFormat());
                            //String currencyFormat=AppController.getInstance().getCurrencyFormat();
                            break;
                        case Flags.Keys.KILOMETERS:
                            rangeWorker.get(entry.getKey()).setViewPos(POS_KILOMETERS);
                            AppController.getInstance().setKilometerFormat(attributes.getDisplayFormat());
                            //String kilometerFormat=AppController.getInstance().getKilometerFormat();
                            break;
                        case Flags.Keys.BELOW_MARKET_AVG:
                            rangeWorker.get(entry.getKey()).setViewPos(POS_BELOW_MARKET_AVG);
                            break;
                        case Flags.Keys.YEAR:
                            rangeWorker.get(entry.getKey()).setViewPos(POS_YEAR);
                            break;
                    }

                    if(!attributes.getMin().isEmpty()) {
                        rangeWorker.get(entry.getKey()).getValues().setMin(Integer.valueOf(attributes.getMin()));
                        applyRangeMinFormat(entry.getKey(), Integer.valueOf(attributes.getMin()));
                    }
                    else {
                        rangeWorker.get(entry.getKey()).getValues().setMin(rangeWorker.get(entry.getKey()).getDefaultValue().getMin());
                        applyRangeMinFormat(entry.getKey(), rangeWorker.get(entry.getKey()).getValues().getMin());
                    }

                    if(!attributes.getMax().isEmpty()) {
                        rangeWorker.get(entry.getKey()).getValues().setMax(Integer.valueOf(attributes.getMax()));
                        applyRangeMaxFormat(entry.getKey(), Integer.valueOf(attributes.getMax()));
                    }
                    else {
                        rangeWorker.get(entry.getKey()).getValues().setMax(rangeWorker.get(entry.getKey()).getDefaultValue().getMax());
                        applyRangeMaxFormat(entry.getKey(), rangeWorker.get(entry.getKey()).getValues().getMax());
                    }
                    updateRangeTags(entry.getKey());
                }
            }

            List<AttrValSpinnerModel> tmpModel = new ArrayList<>();
            for(Map.Entry<String, List<AttrValSpinnerModel>> entry : multiSelectWorker.entrySet()) {
                if(null != multiSelectBundle && multiSelectBundle.containsKey(entry.getKey())) {
                    List<AttrValSpinnerModel> attrList = (List<AttrValSpinnerModel>) multiSelectBundle.get(entry.getKey());
                    for(AttrValSpinnerModel targetAttr : attrList){
                        if(targetAttr.getInteger_representation() == 0 && targetAttr.getId() != 0) { //targetAttr.getIsGroup() == 0 &&
                            targetAttr.setInteger_representation(targetAttr.getId());
                        }

                        if(entry.getKey().equals(Flags.Keys.MODEL)) {
                            tmpModel.add(targetAttr);
                        }
                        else {
                            for(AttrValSpinnerModel attr : multiSelectWorker.get(entry.getKey())) {
                                //if((attr.getId() == targetAttr.getId()) || (attr.getInteger_representation() == targetAttr.getId())) { //Second condition here is to satisfy model as response has id whose value matches with the int_rep
                                if(attr.getInteger_representation() == targetAttr.getInteger_representation()) {
                                    attr.setSelected(true);
                                    attr.setTmpSelected(true);

                                    if(entry.getKey().equals(Flags.Keys.MAKE)) {
                                        alterMakeDataSelected(attr.isSelected(), msCheckedValues.get(entry.getKey()), msCheckedGroups.get(entry.getKey()),
                                                String.valueOf(attr.getInteger_representation()),
                                                String.valueOf(attr.getGroup()), String.valueOf(attr.getId()));
                                    }
                                    else {
                                        refactorAttributeListData(attr.getAttrbuteKey(), true,
                                                msCheckedValues.get(entry.getKey()), msCheckedGroups.get(entry.getKey()),
                                                String.valueOf(attr.getInteger_representation()),
                                                String.valueOf(attr.getGroup()));
                                    }

                                    if(attr.isSelected())
                                        tagToken.createMultiSelectTag(context, attr, entry.getKey());

                                    if(attrValueCheckedList.get(entry.getKey()) == null)
                                        attrValueCheckedList.put(entry.getKey(), new ArrayList<String>());

                                    setMultiSelectUI(entry.getKey(), attr.isSelected(), attr.getValue(), attrValueCheckedList.get(entry.getKey()));
                                }
                            }
                        }
                    }
                }
            }

            //Check if model data are available, initiate fetch model call
            if(tmpModel.size() > 0 || getMSCheckedValues(Flags.Keys.MAKE).size() > 0 || getMSCheckedGroups(Flags.Keys.MAKE).size() > 0) {
                final List<AttrValSpinnerModel> tmpModelFinal = tmpModel;
                String tag_json_obj = "fetch_model_req";
                modelStatus = ModelStatus.LOADING.getValue();
                notifyItemChanged(POS_MODEL);
                Client jsObjRequest = new Client(Request.Method.GET, fetchModelUrl, SearchResponse.class, new OnCallListener() {
                    @Override
                    public void nwResponseData(String pData) {
                        responseData = pData;
                    }
                }, new Response.Listener<Object>() {
                    @Override
                    public void onResponse(Object response) {
                        JsonObject jObj = new JsonParser().parse(responseData).getAsJsonObject();
                        Log.d("ClientResponse", "Response MODEL data on MAKE select");
                        if (jObj.has(Flags.Keys.STATUS)) {
                            JsonPrimitive statu = (JsonPrimitive) jObj.get(Flags.Keys.STATUS);
                            if(null != multiSelectWorker.get(Flags.Keys.MODEL)) {
                                for(AttrValSpinnerModel attr : multiSelectWorker.get(Flags.Keys.MODEL)) {
                                    if(attr.isSelected()) {
                                        attr.setSelected(false);
                                        attr.setTmpSelected(false);
                                        tagToken.removeMultiSelectTag(attr, Flags.Keys.MODEL);
                                        refactorAttributeListData(Flags.Keys.MODEL, attr.isSelected(),
                                                msCheckedValues.get(Flags.Keys.MODEL), msCheckedGroups.get(Flags.Keys.MODEL),
                                                String.valueOf(attr.getInteger_representation()),
                                                String.valueOf(attr.getGroup()));
                                        setMultiSelectUI(Flags.Keys.MODEL, false,attr.getValue(), attrValueCheckedList.get(Flags.Keys.MODEL));
                                    }
                                }
                                multiSelectWorker.get(Flags.Keys.MODEL).clear();
                            }

                            if (statu.getAsInt() == 0) {
                                modelStatus = ModelStatus.CHOOSE_A_MAKE_FIRST.getValue();
                            } else if (statu.getAsInt() == 1) {
                                if (jObj.has(Flags.Keys.RESULT)) {
                                    if (jObj.get(Flags.Keys.RESULT).isJsonArray()) {
                                        JsonArray jArr = (JsonArray) jObj.get(Flags.Keys.RESULT);

                                        if (jArr.size() == 0) {
                                            modelStatus = ModelStatus.NO_MODELS_FOUND.getValue();
                                        }
                                        else
                                            modelStatus = ModelStatus.SELECT.getValue();

                                        List<AttrValSpinnerModel> tmpList = new Gson().fromJson(jArr, new TypeToken<List<AttrValSpinnerModel>>() {
                                        }.getType());
                                        for (AttrValSpinnerModel attr : tmpList) {
                                            multiSelectWorker.get(Flags.Keys.MODEL).add(attr);
                                        }

                                        for(AttrValSpinnerModel targetAttr : tmpModelFinal) {
                                            for(AttrValSpinnerModel attr : multiSelectWorker.get(Flags.Keys.MODEL)) {
                                                if(attr.getInteger_representation() == targetAttr.getInteger_representation()){
                                                    //if((attr.getId() == targetAttr.getId()) || (attr.getInteger_representation() == targetAttr.getId())) { //Second condition here is to satisfy model as response has id whose value matches with the int_rep
                                                    attr.setSelected(true);
                                                    attr.setTmpSelected(true);
                                                    refactorAttributeListData(attr.getAttrbuteKey(), true,
                                                            msCheckedValues.get(Flags.Keys.MODEL), msCheckedGroups.get(Flags.Keys.MODEL),
                                                            String.valueOf(attr.getInteger_representation()),
                                                            String.valueOf(attr.getGroup()));
                                                    if(attr.isSelected())
                                                        tagToken.createMultiSelectTag(context, attr, Flags.Keys.MODEL);

                                                    if(attrValueCheckedList.get(Flags.Keys.MODEL) == null)
                                                        attrValueCheckedList.put(Flags.Keys.MODEL, new ArrayList<String>());

                                                    setMultiSelectUI(Flags.Keys.MODEL, attr.isSelected(), attr.getValue(), attrValueCheckedList.get(Flags.Keys.MODEL));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        notifyDataSetChanged();
                        triggerResult();
                        attributeCountBatchRequest();
                        ///v1.2 requestItemGetCountAll(Request.Method.POST, Flags.URL.ITEM_GETCOUNT + Flags.URL.DOMAIN_ID, SearchResponse.class);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, "Error: " + error);
                    }
                }, Request.Priority.IMMEDIATE);

                jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                AppController.getInstance().addToRequestQueue(jsObjRequest, tag_json_obj);
            }
            else {
                triggerResult();
                ///v1.2 requestItemGetCountAll(Request.Method.POST, Flags.URL.ITEM_GETCOUNT + Flags.URL.DOMAIN_ID, SearchResponse.class);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public boolean clearCountry() {
        try {
            for(AttrValSpinnerModel attr : multiSelectWorker.get(Flags.Keys.COUNTRY)) {
                if(attr.isSelected()) {
                    attr.setSelected(false);
                    attr.setTmpSelected(false);
                    refactorAttributeListData(attr.getAttrbuteKey(), false,
                            msCheckedValues.get(Flags.Keys.CITY), msCheckedGroups.get(Flags.Keys.COUNTRY),
                            String.valueOf(attr.getInteger_representation()),
                            String.valueOf(attr.getGroup()));
                    if(!attr.isSelected())
                        tagToken.removeMultiSelectTag(attr, Flags.Keys.COUNTRY);
                    setMultiSelectUI(Flags.Keys.COUNTRY, attr.isSelected(), attr.getValue(), attrValueCheckedList.get(Flags.Keys.COUNTRY));
                }
            }

            notifyDataSetChanged();
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean clearCity() {
        try {
            for(AttrValSpinnerModel attr : multiSelectWorker.get(Flags.Keys.CITY)) {
                if(attr.isSelected()) {
                    attr.setSelected(false);
                    attr.setTmpSelected(false);
                    refactorAttributeListData(attr.getAttrbuteKey(), false,
                            msCheckedValues.get(Flags.Keys.CITY), msCheckedGroups.get(Flags.Keys.CITY),
                            String.valueOf(attr.getInteger_representation()),
                            String.valueOf(attr.getGroup()));
                    if(!attr.isSelected())
                        tagToken.removeMultiSelectTag(attr, Flags.Keys.CITY);
                    setMultiSelectUI(Flags.Keys.CITY, attr.isSelected(), attr.getValue(), attrValueCheckedList.get(Flags.Keys.CITY));
                }
            }

            notifyDataSetChanged();
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean clearAll(boolean ignoreCity) {
        for(Map.Entry<String, CommonFields> entry : rangeWorker.entrySet()) {
            if(((CommonFields)entry.getValue()).getValues().getMin() != ((CommonFields)entry.getValue()).getDefaultValue().getMin()
                    || ((CommonFields)entry.getValue()).getValues().getMax() != ((CommonFields)entry.getValue()).getDefaultValue().getMax()) {
                CommonFields cf = (CommonFields)entry.getValue();
                cf.getValues().setMin(cf.getDefaultValue().getMin());
                cf.getValues().setMax(cf.getDefaultValue().getMax());
                removeRangeTagToken(cf, entry.getKey());
                ((SearchActivity)activity).syncTagLayout(false, false, null, cf, false, entry.getKey());
            }
        }

        for(Map.Entry<String, List<AttrValSpinnerModel>> entry : multiSelectWorker.entrySet()) {
            if(!entry.getKey().equals(Flags.Keys.CITY)) {
                for(AttrValSpinnerModel attr : multiSelectWorker.get(entry.getKey())) {
                    if(attr.isSelected()) {
                        attr.setTmpSelected(false);
                        addRemoveMultiSelectTags(false, entry.getKey(), attr);
                        ((SearchActivity)activity).syncTagLayout(false, false, attr, null, true, entry.getKey());
                    }
                }
            }
        }

        multiSelectWorker.get(Flags.Keys.MODEL).clear();
        modelStatus = ModelStatus.CHOOSE_A_MAKE_FIRST.getValue();
        clearKeywordSearchField();

        if(!ignoreCity)
            applyDefaultCity(true);

        mGroup = "0"; mId = "0";
        idCount = 0; groupCount = 0;

        if(((SearchActivity)activity).getSearchType() == Flags.Bundle.Values.WALL_OF_DEALS) {
            wallOfDealsData();
        }

        notifyDataSetChanged();
        return true;
    }

    public void removeRangeTagToken(CommonFields rf, String keyname) {
        tagToken.removeRangeTag(rf, keyname);
    }

    public void removeMultiSelectTagToken(boolean isSelected, String keyName, AttrValSpinnerModel attr) {
        addRemoveMultiSelectTags(isSelected, keyName, attr);
        multiSelectEventTrigger(keyName);
    }

    public void addRemoveMultiSelectTags(boolean isSelected, String keyName, AttrValSpinnerModel attr) {
        try {
            attr.setSelected(isSelected);
            attr.setTmpSelected(isSelected);
            if(keyName.equals(Flags.Keys.MAKE)) {
                alterMakeDataSelected(attr.isSelected(), msCheckedValues.get(keyName), msCheckedGroups.get(keyName),
                        String.valueOf(attr.getInteger_representation()),
                        String.valueOf(attr.getGroup()), String.valueOf(attr.getId()));
            }
            else {
                refactorAttributeListData(keyName, attr.isSelected(),
                        msCheckedValues.get(keyName), msCheckedGroups.get(keyName),
                        String.valueOf(attr.getInteger_representation()),
                        String.valueOf(attr.getGroup()));
            }
            if(attr.isSelected())
                tagToken.createMultiSelectTag(context, attr, keyName);
            else
                tagToken.removeMultiSelectTag(attr, keyName);

            if(attrValueCheckedList.get(keyName) == null)
                attrValueCheckedList.put(keyName, new ArrayList<String>());

            setMultiSelectUI(keyName, attr.isSelected(), attr.getValue(), attrValueCheckedList.get(keyName));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void initDemo() {
        if(null != viewsListForDemo && viewsListForDemo.size() > 0)
            Utility.startSequenceFeatureAnim(activity, viewsListForDemo);
    }


}