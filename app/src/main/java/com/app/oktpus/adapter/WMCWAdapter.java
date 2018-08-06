package com.app.oktpus.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.oktpus.activity.WhatsMyCarWorthOld;
import com.app.oktpus.model.AttrValSpinnerModel;
import com.app.oktpus.model.SearchResponse;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.constants.ModelStatus;
import com.app.oktpus.controller.AppController;
import com.app.oktpus.listener.OnCallListener;
import com.app.oktpus.listener.OnCheckBoxClickListener;
import com.app.oktpus.R;
import com.app.oktpus.utils.Client;
import com.app.oktpus.utils.SessionManagement;
import com.app.oktpus.utils.Utility;
import com.app.oktpus.utils.Validations;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gyandeep on 21/7/17.
 */

public class WMCWAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TOTAL_ITEMS = 16;

    static final int VIEW_HEADER = 0;
    static final int VIEW_POPUP = 1;
    static final int VIEW_EDITFIELD = 2;
    static final int VIEW_MILEAGE = 4;
    static final int VIEW_BOTTOM = 5;
    static final int VIEW_LOADING = 6;
    static final int VIEW_SUBHEADER = 7;

    public static final String mKmsUnitKms = "kms";
    private final static int IS_READY_FOR_PAYMENT = 100, INVALID_EMAIL = 101, EMPTY_EMAIL = 102;

    public static final int FIELD_MAKE = 2, FIELD_MODEL = 3, FIELD_TRANSMISSION = 4, FIELD_YEAR = 5, FIELD_MILEAGE = 6,
            FIELD_EMAIL = 8, FIELD_FNAME = 9, FIELD_LNAME = 10, FIELD_CITY = 12, FIELD_STATE = 13, FIELD_COUNTRY = 14,
            SUB_HEADER_VEHICLE = 1, SUB_HEADER_CONTACT = 7, SUB_HEADER_LOCATION = 11,
            KMS_UNIT = 50, MILEAGE_FORMATTED = 51;
    static final String SELECT = "Select";
    Map<String, List<AttrValSpinnerModel>> mListData;
    Map<Integer, String> mFieldData;
    List<String> mKmsUnit;
    private Context mContext;
    private Activity mActivity;
    private boolean isProcessComplete = false;
    int mId = 0, mGroup = 0;
    private int modelStatus = 1;
    boolean isMakeChanged = false;
    WhatsMyCarWorthOld.AdapterCallbacks mCallbackListener;

    public WMCWAdapter(Activity activity, SessionManagement session, Context context, Map<String, List<AttrValSpinnerModel>> listData, WhatsMyCarWorthOld.AdapterCallbacks callbackListener) {
        mActivity = activity;
        mContext = context;
        mListData = listData;
        mCallbackListener = callbackListener;

        mKmsUnit = new ArrayList<>();
        mKmsUnit.add(mKmsUnitKms);
        mKmsUnit.add(Flags.Keys.MILES);

        mFieldData = new HashMap<>();
        mFieldData.put(FIELD_MAKE, SELECT);
        mFieldData.put(FIELD_MODEL, "");
        mFieldData.put(FIELD_TRANSMISSION, SELECT);
        mFieldData.put(FIELD_YEAR, "");
        mFieldData.put(FIELD_MILEAGE, "");
        mFieldData.put(MILEAGE_FORMATTED, "");
        mFieldData.put(FIELD_FNAME, "");
        mFieldData.put(FIELD_LNAME, "");
        mFieldData.put(FIELD_EMAIL, (session.isLoggedIn())? session.getUsername() : "");
        mFieldData.put(FIELD_CITY, "");
        mFieldData.put(FIELD_STATE, "");
        mFieldData.put(FIELD_COUNTRY, "");
        mFieldData.put(KMS_UNIT, mKmsUnit.get(0));
    }

    private class PopUpItem extends RecyclerView.ViewHolder {
        private TextView tvLabel, tvValue, tvFilterPopupTitle;
        private RecyclerView recyclerView;
        private SearchView searchView;
        private ImageView ivPopupClose;

        private WMCWItemAdapter childRecyclerAdapter;
        private List<AttrValSpinnerModel> attrList;

        private String keyName = Flags.Keys.MAKE;
        public AlertDialog.Builder builder;
        public AlertDialog dialog;
        private LinearLayout btnLayout, fieldLayout;
        private String attrValue;

        private PopUpItem(View view) {
            super(view);
            tvLabel = (TextView) view.findViewById(R.id.tv_wmcw_item_dropdown_label);
            tvValue = (TextView) view.findViewById(R.id.tv_wmcw_item_dropdown_value);
            fieldLayout = (LinearLayout) view.findViewById(R.id.ll_wmcw_dropdown_value);
            builder = new AlertDialog.Builder(mContext);
            View convertView = null;
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.ex_list_item, null);

            btnLayout = (LinearLayout) convertView.findViewById(R.id.btn_layout);

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
            final List<AttrValSpinnerModel> attrList = new ArrayList<>();
            childRecyclerAdapter = new WMCWItemAdapter(mContext, attrList, keyName, new OnCheckBoxClickListener() {
                @Override
                public void onCheckBoxItemClicked(String keyName, boolean checked, String intRep, String value, String group, String id, int position) {}

                @Override
                public void onCheckBoxItemClicked(boolean checked, String value) {}

                @Override
                public void onCheckBoxItemClicked(String keyname, Map<Integer, AttrValSpinnerModel> attrMap, int intRep, AttrValSpinnerModel tmpAttr) {
                    dialog.dismiss();
                    attrValue = tmpAttr.getValue();
                    if(keyname.equals(Flags.Keys.MAKE)) {
                        if(Integer.valueOf(tmpAttr.getGroup()) > 0) {
                            mGroup = tmpAttr.getId();
                            mId = 0;
                        }
                        else {
                            mId = tmpAttr.getId();
                            mGroup = 0;
                        }
                        modelStatus = ModelStatus.LOADING.getValue();
                        mFieldData.put(FIELD_MAKE, attrValue);
                        requestModelData(Request.Method.GET, Flags.getModelUrl(mId, mGroup), SearchResponse.class);
                    }
                    else if(keyname.equals(Flags.Keys.TRANSMISSION)) {
                        mFieldData.put(FIELD_TRANSMISSION, attrValue);
                    }
                    else if(keyname.equals(Flags.Keys.COUNTRY)) {
                        mFieldData.put(FIELD_COUNTRY, attrValue);
                        /*mFieldData.put(FIELD_STATE, "");
                        mFieldData.put(FIELD_CITY, "");*/
                    }
                    else {
                        mFieldData.put(FIELD_MODEL, attrValue);
                        modelStatus = ModelStatus.READY.getValue();
                    }
                    notifyDataSetChanged();
                }});

            searchView.setIconifiedByDefault(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext,
                    LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(childRecyclerAdapter);

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

            fieldLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getAdapterPosition() == FIELD_MODEL){
                        if(mListData.containsKey(Flags.Keys.MODEL) && mListData.get(Flags.Keys.MODEL).size() > 0) {
                            btnLayout.setVisibility(View.GONE);
                            tvFilterPopupTitle.setText(SELECT+ " Model");
                            childRecyclerAdapter.setListState(mListData.get(Flags.Keys.MODEL), Flags.Keys.MODEL);
                            childRecyclerAdapter.getFilter().filter("");
                            childRecyclerAdapter.notifyDataSetChanged();
                            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                            dialog.show();
                            isMakeChanged = false;
                        }
                    }
                    else {
                        btnLayout.setVisibility(View.GONE);
                        if(getAdapterPosition() == FIELD_MAKE) {
                            tvFilterPopupTitle.setText(SELECT+ " Make");
                            childRecyclerAdapter.setListState(mListData.get(Flags.Keys.MAKE), Flags.Keys.MAKE);
                        }
                        else if(getAdapterPosition() == FIELD_TRANSMISSION) {
                            tvFilterPopupTitle.setText(SELECT+" Transmission");
                            childRecyclerAdapter.setListState(mListData.get(Flags.Keys.TRANSMISSION), Flags.Keys.TRANSMISSION);
                        }
                        else if(getAdapterPosition() == FIELD_COUNTRY) {
                            tvFilterPopupTitle.setText(SELECT+" Country");
                            childRecyclerAdapter.setListState(mListData.get(Flags.Keys.COUNTRY), Flags.Keys.COUNTRY);
                        }
                        childRecyclerAdapter.getFilter().filter("");
                        childRecyclerAdapter.notifyDataSetChanged();
                        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                        dialog.show();
                    }
                }
            });
        }
    }

    public void setListData(Map<String, List<AttrValSpinnerModel>> listData) {
        mListData.putAll(listData);
        notifyDataSetChanged();
    }

    private class SubHeader extends RecyclerView.ViewHolder {
        TextView tvTitle;
        View viewDivider;
        public SubHeader(View view){
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tv_wmcw_subheader);
            viewDivider = (View) view.findViewById(R.id.view_divider);
        }
    }

    private class EditFieldItem extends RecyclerView.ViewHolder {
        TextView tvLabel;
        EditText etField;
        String value = "";
        public EditFieldItem(View itemView) {
            super(itemView);
            tvLabel = (TextView) itemView.findViewById(R.id.tv_wmcw_item_editfield_label);
            etField = (EditText) itemView.findViewById(R.id.et_wmcw_item_editfield_value);
            etField.setText(value);

            etField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    /*if(getAdapterPosition()==YEAR) {
                        InputFilter[] filterArray = new InputFilter[1];
                        filterArray[0] = new InputFilter.LengthFilter(4);
                        etField.setFilters(filterArray);
                    }*/
                }

                @Override
                public void afterTextChanged(Editable s) {
                    value = etField.getText().toString();
                    updateData(getAdapterPosition(), value);
                }
            });
        }
    }

    private class MileageEditFieldItem extends RecyclerView.ViewHolder{
        TextView tvLabel;
        EditText etField;
        Spinner spinnerKmsUnit;
        String value = "";
        DecimalFormat numberFormatter;
        String formatted = "";
        public MileageEditFieldItem(View itemView) {
            super(itemView);
            tvLabel = (TextView) itemView.findViewById(R.id.tv_wmcw_item_editfield_label);
            etField = (EditText) itemView.findViewById(R.id.et_wmcw_item_editfield_value);
            spinnerKmsUnit = (Spinner) itemView.findViewById(R.id.spinner_wmcw_kms);
            spinnerKmsUnit.setPadding(0,0,0,0);
            numberFormatter = new DecimalFormat("#,###");
            etField.setText(value);

            etField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    value = Utility.numberInspector(etField.getText().toString());
                    if(!value.isEmpty()) {
                        formatted = numberFormatter.format(Integer.valueOf(value));
                        mFieldData.put(MILEAGE_FORMATTED, formatted);
                    }
                    updateData(getAdapterPosition(), value);
                }
            });

            etField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus && getAdapterPosition() == FIELD_MILEAGE) {
                        etField.setText(formatted);
                    }
                }
            });

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, R.layout.wmcw_spinner_item, mKmsUnit);

            spinnerKmsUnit.setAdapter(dataAdapter);
            spinnerKmsUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String value = parent.getItemAtPosition(position).toString();
                    mFieldData.put(KMS_UNIT, (value.equals(mKmsUnitKms))?Flags.Keys.KILOMETERS:value);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        }
    }

    private class BottomLayout extends RecyclerView.ViewHolder {
        AlertDialog.Builder builder;
        AlertDialog dialog;
        RelativeLayout btnEvaluate, confirmBtnLayout;
        CheckBox cbTerms;
        ImageView ivConfirmClose;
        TextView tvError, tvTerms, tvConfirmContents, tvConfirmDetailsFieldNames;
        public BottomLayout(View itemView) {
            super(itemView);
            btnEvaluate = (RelativeLayout) itemView.findViewById(R.id.rl_wmcw_evaluate);
            tvError = (TextView) itemView.findViewById(R.id.tv_error);
            cbTerms = (CheckBox) itemView.findViewById(R.id.cb_wmcw_terms);
            tvTerms = (TextView) itemView.findViewById(R.id.tv_wmcw_terms_agree);

            SpannableString ss = new SpannableString(mContext.getResources().getString(R.string.wmcw_terms));
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(Flags.URL.TERMS));
                    browse.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(browse);
                }
            };
            ss.setSpan(clickableSpan, 15, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.link)), 15, ss.length(), 0);

            builder = new AlertDialog.Builder(mContext);
            View convertView = null;
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.wmcw_confirm_details_layout, null);

            confirmBtnLayout = (RelativeLayout) convertView.findViewById(R.id.layout_wmcw_confirm_btn);
            tvConfirmContents = (TextView) convertView.findViewById(R.id.tv_wmcw_confirm_details);
            tvConfirmDetailsFieldNames = (TextView) convertView.findViewById(R.id.tv_wmcw_confirm_details_field_name);
            ivConfirmClose = (ImageView) convertView.findViewById(R.id.iv_wmcw_confirm_close);

            builder.setView(convertView);
            dialog = builder.create();

            tvTerms.setText(ss);
            tvTerms.setMovementMethod(LinkMovementMethod.getInstance());

            btnEvaluate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvError.setVisibility(View.GONE);

                    if(!cbTerms.isChecked()) {
                        Toast.makeText(mContext, "Please check Terms of Service to continue", Toast.LENGTH_LONG).show();
                        return;
                    }

                    int resultCode = checkValidations();

                    if(IS_READY_FOR_PAYMENT == resultCode){
                        //Show confirm dialog
                        //Map<String, StringBuilder> confirmData = prepareConfirmContent(mFieldData);
                        //tvConfirmContents.setText(Html.fromHtml(confirmData.get("content").toString()));
                        tvConfirmContents.setText(prepareConfirm(mFieldData, NEW_LINE));
                        //tvConfirmDetailsFieldNames.setText(confirmData.get("fieldName"));
                        dialog.show();
                    }
                    else {
                        String errorValue = "";
                        switch(resultCode) {
                        /*case FIELD_MAKE:
                            errorValue = mContext.getResources().getString(R.string.label_make);
                            break;*/
                            case INVALID_EMAIL:
                                errorValue = "Invalid Email address";
                                break;
                            case EMPTY_EMAIL:
                                errorValue = "Email is mandatory";
                                break;
                        }
                        tvError.setText(errorValue);
                        tvError.setVisibility(View.VISIBLE);
                        mCallbackListener.displayError(FIELD_EMAIL);
                    }
                }
            });

            confirmBtnLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallbackListener.initiatePayment(mFieldData);
                    dialog.dismiss();
                }
            });

            ivConfirmClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
    }

    private class HeaderLayout extends RecyclerView.ViewHolder {
        LinearLayout layoutThankyou, layoutSubTitle;
        HeaderLayout(View v) {
            super(v);
            layoutThankyou = (LinearLayout) v.findViewById(R.id.layout_wmcw_thank_you);
            layoutSubTitle = (LinearLayout) v.findViewById(R.id.wmcw_sub_sub_title);

            String [] list = mContext.getResources().getStringArray(R.array.wmcw_subtitle_list);
            for(String str : list) {
                ImageView iv = new ImageView(mContext);
                iv.setLayoutParams(new RelativeLayout.LayoutParams(
                        50, 50));
                iv.setPadding(0,10,8,10);

                TextView textView = new TextView(mContext);
                textView.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                        LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                        LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
                textView.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                LinearLayout horizontalLayout = new LinearLayout(mContext);

                iv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.tick_green));
                textView.setText(str);

                horizontalLayout.setPadding(20,0,20,0);
                horizontalLayout.addView(iv);
                horizontalLayout.addView(textView);
                layoutSubTitle.addView(horizontalLayout);
            }
        }
    }

    private class LoadingLayout extends RecyclerView.ViewHolder {
        LoadingLayout(View v) {
            super(v);
        }
    }

    private static final String NEW_LINE = "\n";

    public String prepareConfirm(Map<Integer, String> fieldData, String constraint) {

        StringBuilder make_model = new StringBuilder();
        StringBuilder name = new StringBuilder();
        StringBuilder vehicle = new StringBuilder();
        StringBuilder contact = new StringBuilder();
        StringBuilder location = new StringBuilder();

        if(fieldData.containsKey(FIELD_MAKE) && !fieldData.get(FIELD_MAKE).equals(SELECT) && fieldData.get(FIELD_MAKE).trim().length() > 0 ) {
            make_model.append(fieldData.get(FIELD_MAKE));
            if(fieldData.containsKey(FIELD_MODEL) && !fieldData.get(FIELD_MODEL).equals(SELECT) && fieldData.get(FIELD_MODEL).trim().length() > 0) {
                make_model.append(" " + fieldData.get(FIELD_MODEL));
            }
        }

        if(make_model.length()>0)
            vehicle.append(make_model + constraint);

        if(fieldData.containsKey(FIELD_TRANSMISSION) && !fieldData.get(FIELD_TRANSMISSION).equals(SELECT) && fieldData.get(FIELD_TRANSMISSION).trim().length() > 0) {
            vehicle.append(fieldData.get(FIELD_TRANSMISSION)+constraint);
        }

        if(fieldData.containsKey(FIELD_MILEAGE) && fieldData.get(FIELD_MILEAGE).trim().length() > 0) {
            vehicle.append(fieldData.get(FIELD_MILEAGE)+" " + fieldData.get(KMS_UNIT) + constraint);
        }

        if(fieldData.containsKey(FIELD_YEAR) && fieldData.get(FIELD_YEAR).trim().length() > 0) {
            vehicle.append(fieldData.get(FIELD_YEAR)+ constraint);
        }

        if(vehicle.length()>0)
            vehicle.append(constraint);

        if(fieldData.containsKey(FIELD_FNAME) && fieldData.get(FIELD_FNAME).trim().length() > 0) {
            name.append(fieldData.get(FIELD_FNAME));

            if(fieldData.containsKey(FIELD_LNAME) && fieldData.get(FIELD_LNAME).trim().length() > 0)
                name.append(" " + fieldData.get(FIELD_LNAME) + constraint);
            else
                name.append(constraint);
        }
        else if(fieldData.containsKey(FIELD_LNAME) && fieldData.get(FIELD_LNAME).trim().length() > 0) {
            name.append(fieldData.get(FIELD_LNAME) + constraint);
        }

        /*Contact*/
        if(name.length() > 0)
            contact.append(name);

        if(fieldData.containsKey(FIELD_EMAIL) && fieldData.get(FIELD_EMAIL).trim().length() > 0)
            contact.append(fieldData.get(FIELD_EMAIL) + constraint);

        /*location*/
        if(fieldData.containsKey(FIELD_CITY) && fieldData.get(FIELD_CITY).trim().length() > 0) {
            location.append(fieldData.get(FIELD_CITY) + " ");
        }

        if(fieldData.containsKey(FIELD_STATE) && fieldData.get(FIELD_STATE).trim().length() > 0) {
            location.append(fieldData.get(FIELD_STATE) + " ");
        }

        if(fieldData.containsKey(FIELD_COUNTRY) && fieldData.get(FIELD_COUNTRY).trim().length() > 0) {
            location.append(fieldData.get(FIELD_COUNTRY) + " ");
        }

        return vehicle.toString()+contact.toString()+location.toString();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if(viewType == VIEW_POPUP) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.wmcw_item_dropdown, parent, false);
            vh = new PopUpItem(v);
            return vh;
        }
        else if(viewType == VIEW_EDITFIELD) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.wmcw_item_edit_field, parent, false);
            vh = new EditFieldItem(v);
            return vh;
        }
        else if(viewType == VIEW_MILEAGE) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.wmcw_item_edit_field_mileage, parent, false);
            vh = new MileageEditFieldItem(v);
            return vh;
        }
        else if(viewType == VIEW_BOTTOM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.wmcw_bottom_layout, parent, false);
            vh = new BottomLayout(v);
            return vh;
        }
        else if(viewType == VIEW_SUBHEADER) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.wmcw_subheader, parent, false);
            vh = new SubHeader(v);
            return vh;
        }
        else if(viewType == VIEW_HEADER){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.wmcw_header_layout, parent, false);
            vh = new HeaderLayout(v);
            return vh;
        }
        else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.blank_view, parent, false);
            vh = new LoadingLayout(v);
            return vh;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof PopUpItem) {
            PopUpItem item = (PopUpItem)holder;
            String label="";
            if(position == FIELD_MAKE) {
                label = mContext.getResources().getString(R.string.label_make);
                item.tvValue.setText(mFieldData.get(FIELD_MAKE));
                item.fieldLayout.setBackground(mContext.getResources().getDrawable(R.drawable.selector_edit_box));

            }
            else if(position == FIELD_TRANSMISSION) {
                label = mContext.getResources().getString(R.string.label_transmission);
                item.tvValue.setText(mFieldData.get(FIELD_TRANSMISSION));
                item.fieldLayout.setBackground(mContext.getResources().getDrawable(R.drawable.selector_edit_box));
            }
            else if(position == FIELD_COUNTRY) {
                label = mContext.getResources().getString(R.string.label_country);
                item.tvValue.setText(mFieldData.get(FIELD_COUNTRY));
                item.fieldLayout.setBackground(mContext.getResources().getDrawable(R.drawable.selector_edit_box));
            }
            else if (position == FIELD_MODEL){
                label = mContext.getResources().getString(R.string.label_model);
                ModelStatus status = ModelStatus.fromValue(modelStatus);
                switch (status) {
                    case CHOOSE_A_MAKE_FIRST:
                        item.tvValue.setText(mContext.getResources().getString(R.string.default_model_header));
                        mFieldData.put(FIELD_MODEL, "");
                        item.fieldLayout.setBackground(mContext.getResources().getDrawable(R.drawable.bg_dropdown));
                        break;
                    case NO_MODELS_FOUND:
                        item.tvValue.setText(mContext.getResources().getString(R.string.no_model_avail));
                        mFieldData.put(FIELD_MODEL, "");
                        item.fieldLayout.setBackground(mContext.getResources().getDrawable(R.drawable.bg_dropdown));
                        break;
                    case SELECT:
                        item.tvValue.setText(mContext.getResources().getString(R.string.select));
                        mFieldData.put(FIELD_MODEL, "");
                        item.fieldLayout.setBackground(mContext.getResources().getDrawable(R.drawable.selector_edit_box));
                        break;
                    case LOADING:
                        item.tvValue.setText(mContext.getResources().getString(R.string.msg_loading));
                        mFieldData.put(FIELD_MODEL, "");
                        item.fieldLayout.setBackground(mContext.getResources().getDrawable(R.drawable.bg_dropdown));
                        break;
                    default:
                        item.tvValue.setText(mFieldData.get(FIELD_MODEL));
                        item.fieldLayout.setBackground(mContext.getResources().getDrawable(R.drawable.selector_edit_box));
                }
            }
            item.tvLabel.setText(label);

        }
        else if(holder instanceof EditFieldItem) {
            final EditFieldItem item = (EditFieldItem) holder;
            updateUI(item, position);
        }
        else if(holder instanceof MileageEditFieldItem) {
            updateUI((MileageEditFieldItem)holder, position);
        }
        else if(holder instanceof SubHeader) {
            String title = "";
            switch(position) {
                case SUB_HEADER_VEHICLE:
                    title = mContext.getResources().getString(R.string.label_vehicle);
                    ((SubHeader) holder).viewDivider.setVisibility(View.VISIBLE);
                    break;
                case SUB_HEADER_LOCATION:
                    title = mContext.getResources().getString(R.string.label_location);
                    ((SubHeader) holder).viewDivider.setVisibility(View.VISIBLE);
                    break;
                case SUB_HEADER_CONTACT:
                    title = mContext.getResources().getString(R.string.label_contact);
                    ((SubHeader) holder).viewDivider.setVisibility(View.VISIBLE);
                    break;
            }
            ((SubHeader) holder).tvTitle.setText(title);
        }
        else if(holder instanceof HeaderLayout) {
            if(isProcessComplete) ((HeaderLayout)holder).layoutThankyou.setVisibility(View.VISIBLE);
        }
    }


    public void updateData(int position, String value) {
        switch(position) {
            case FIELD_YEAR:
                mFieldData.put(FIELD_YEAR, value.trim());
                break;
            case FIELD_MILEAGE:
                mFieldData.put(FIELD_MILEAGE, value.trim());
                break;
            case FIELD_FNAME:
                mFieldData.put(FIELD_FNAME, value.trim());
                break;
            case FIELD_LNAME:
                mFieldData.put(FIELD_LNAME, value.trim());
                break;
            case FIELD_EMAIL:
                mFieldData.put(FIELD_EMAIL, value.trim());
                break;
            case FIELD_CITY:
                mFieldData.put(FIELD_CITY, value.trim());
                break;
            case FIELD_STATE:
                mFieldData.put(FIELD_STATE, value.trim());
                break;
        }
    }

    public void updateUI(RecyclerView.ViewHolder item, int position) {
        switch(position) {
            case FIELD_YEAR:
                ((EditFieldItem)item).tvLabel.setText(mContext.getResources().getString(R.string.label_year));
                ((EditFieldItem)item).etField.setInputType(InputType.TYPE_CLASS_NUMBER);
                ((EditFieldItem)item).etField.setText(mFieldData.get(FIELD_YEAR));
                break;
            case FIELD_MILEAGE:
                ((MileageEditFieldItem)item).tvLabel.setText(mContext.getResources().getString(R.string.label_kms));
                ((MileageEditFieldItem)item).etField.setText(mFieldData.get(MILEAGE_FORMATTED));
                ((MileageEditFieldItem)item).etField.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case FIELD_FNAME:
                ((EditFieldItem)item).tvLabel.setText(mContext.getResources().getString(R.string.label_fname));
                ((EditFieldItem)item).etField.setText(mFieldData.get(FIELD_FNAME));
                ((EditFieldItem)item).etField.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case FIELD_LNAME:
                ((EditFieldItem)item).tvLabel.setText(mContext.getResources().getString(R.string.label_lname));
                ((EditFieldItem)item).etField.setText(mFieldData.get(FIELD_LNAME));
                ((EditFieldItem)item).etField.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case FIELD_EMAIL:
                ((EditFieldItem)item).tvLabel.setText(mContext.getResources().getString(R.string.label_email));
                ((EditFieldItem)item).etField.setText(mFieldData.get(FIELD_EMAIL));
                ((EditFieldItem)item).etField.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case FIELD_CITY:
                ((EditFieldItem)item).tvLabel.setText(mContext.getResources().getString(R.string.label_city));
                ((EditFieldItem)item).etField.setText(mFieldData.get(FIELD_CITY));
                ((EditFieldItem)item).etField.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case FIELD_STATE:
                ((EditFieldItem)item).tvLabel.setText(mContext.getResources().getString(R.string.label_state));
                ((EditFieldItem)item).etField.setText(mFieldData.get(FIELD_STATE));
                ((EditFieldItem)item).etField.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
        }
    }

    @Override
    public int getItemViewType(int pos) {
        if(mListData.size() > 0) {
            if(pos == 0) return VIEW_HEADER;
            if(pos == 1 || pos == 7 || pos == 11) return VIEW_SUBHEADER;
            if(pos == 2 || pos == 3 || pos == 4 || pos == 14) {
                return VIEW_POPUP;
            }
            else if(pos == 6) {
                return VIEW_MILEAGE;
            }
            else if(pos == 15) {
                return VIEW_BOTTOM;
            }
            else
                return VIEW_EDITFIELD;
        }
        else {
            return VIEW_LOADING;
        }
    }


    @Override
    public int getItemCount() {
        return TOTAL_ITEMS;
    }

    String responseData;
    public void requestModelData(final int methodType, final String url, final Class<SearchResponse> clazz) {
        try {
            String tag_json_obj = "fetch_model_req";
            Client jsObjRequest = new Client(methodType, url, clazz, new OnCallListener() {
                @Override
                public void nwResponseData(String pData) {
                    responseData = pData;
                }
            }, new Response.Listener<Object>() {
                @Override
                public void onResponse(Object response) {
                    JsonObject jObj = new JsonParser().parse(responseData).getAsJsonObject();

                    if(jObj.has(Flags.Keys.STATUS)) {
                        JsonPrimitive statu = (JsonPrimitive) jObj.get(Flags.Keys.STATUS);

                        if(null != mListData.get(Flags.Keys.MODEL)) {
                            mListData.get(Flags.Keys.MODEL).clear();                //Clear old list
                        }

                        isMakeChanged = true;

                        if(statu.getAsInt() == 0) {
                            //modelStatus = ModelStatus.CHOOSE_A_MAKE_FIRST.getValue();
                        } else if (statu.getAsInt() == 1) {
                            if (jObj.has(Flags.Keys.RESULT)) {
                                if (jObj.get(Flags.Keys.RESULT).isJsonArray()) {
                                    JsonArray jArr = (JsonArray) jObj.get(Flags.Keys.RESULT);

                                    if (jArr.size() == 0) {
                                        modelStatus = ModelStatus.NO_MODELS_FOUND.getValue();
                                    }
                                    else
                                        modelStatus = ModelStatus.SELECT.getValue();

                                    List<AttrValSpinnerModel> tmpList = new Gson().fromJson(jArr, new TypeToken<List<AttrValSpinnerModel>>() {}.getType());
                                    mListData.put(Flags.Keys.MODEL, tmpList);
                                }
                            }
                        }
                    }
                    notifyDataSetChanged();
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

    public void fillLocationData(String city, String state, String country, String countryCode) {
        try {
            mFieldData.put(FIELD_CITY, city.trim());
            mFieldData.put(FIELD_STATE, state.trim());
            mFieldData.put(FIELD_COUNTRY, countryCode.trim());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }


    public int checkValidations() {
        boolean isValid = true;
        int result = IS_READY_FOR_PAYMENT;
        for (Map.Entry<Integer, String> item : mFieldData.entrySet()) {
            if (!isValid)
                break;
            switch (item.getKey()) {
                /*case FIELD_MAKE:
                    if (item.getValue().equals(SELECT) || item.getValue().isEmpty()) {
                        isValid = false;
                        result = FIELD_MAKE;
                    }
                    break;*/
                case FIELD_EMAIL:
                    if (item.getValue().isEmpty()) {
                        isValid = false;
                        result = EMPTY_EMAIL;
                    }
                    else if(!Validations.isEmailValid(item.getValue()) || !Validations.isEmailValid2(item.getValue())){
                        isValid = false;
                        result = INVALID_EMAIL;
                    }
                    break;
            }
        }
        return result;
    }

    public void displayThankyou() {
        this.isProcessComplete = true;
        notifyDataSetChanged();
    }
}