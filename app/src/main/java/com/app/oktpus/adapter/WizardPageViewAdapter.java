package com.app.oktpus.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.oktpus.R;
import com.app.oktpus.activity.WhatsMyCarWorth;

import com.app.oktpus.constants.Flags;
import com.app.oktpus.constants.ListViewType;
import com.app.oktpus.constants.ModelStatus;
import com.app.oktpus.listener.OnCheckBoxClickListener;
import com.app.oktpus.listener.OnRowClickListener;
import com.app.oktpus.listener.SearchFilterModelListener;
import com.app.oktpus.model.AttrValSpinnerModel;
import com.app.oktpus.model.SimpleListItemModel;
import com.app.oktpus.model.carinsurance.QuestionItems;
import com.app.oktpus.model.wmcw.WMCWVehicleModel;
import com.app.oktpus.service.SearchFilterModelAPI;
import com.app.oktpus.utils.FlowLayout;
import com.app.oktpus.utils.TagTokenUtils;
import com.app.oktpus.utils.Utility;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wizardlib.model.Page;

/**
 * Created by Gyandeep on 13/3/18.
 */

public class WizardPageViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private List<QuestionItems> mItemsList;
    private Page mPage;

    public static final int FIELD_MAKE = 1, FIELD_MODEL = 2, FIELD_TRANSMISSION = 3;
    static final String SELECT = "Select";
    Map<String, List<AttrValSpinnerModel>> mListData;
    Map<Integer, String> mFieldData;
    int mId = 0, mGroup = 0;
    //private int modelStatus = 1;
    boolean isMakeChanged = false;
    private TagTokenUtils optionButtonCreator;
    private Activity mActivity;
    //private WizardActivityGeneric wActivity;
    //private ArrayList<QuestionItems> singleChoiceItemsData;

    public WizardPageViewAdapter(Activity activity, Context context, Page page, Map<String, List<AttrValSpinnerModel>> listData,
                                 ArrayList<QuestionItems> singleChoiceItemsData){
        mContext = context;
        mPage = page;
        mListData = listData;
        mActivity = activity;

        mFieldData = new HashMap<>();
        mFieldData.put(FIELD_MAKE, SELECT);
        mFieldData.put(FIELD_TRANSMISSION, SELECT);
        mFieldData.put(FIELD_MODEL, "");
        optionButtonCreator = new TagTokenUtils(mContext);

        this.mItemsList = singleChoiceItemsData;
    }

    /*public WizardPageViewAdapter(WizardActivityGeneric activity, Context context, Page page, Map<String, List<AttrValSpinnerModel>> listData,
                                 ArrayList<QuestionItems> singleChoiceItemsData){
        mContext = context;
        mPage = page;
        mListData = listData;
        wActivity = activity;

        mFieldData = new HashMap<>();
        mFieldData.put(FIELD_MAKE, SELECT);
        mFieldData.put(FIELD_TRANSMISSION, SELECT);
        mFieldData.put(FIELD_MODEL, "");

        optionButtonCreator = new TagTokenUtils(mContext);
        this.mItemsList = singleChoiceItemsData;

    }*/

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if(viewType == ListViewType.VIEW_POPUP) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ci_item_popup_custom, parent, false);
            vh = new PopUpItem(v);
            return vh;
        }
        else if(viewType == ListViewType.VIEW_EDIT_FIELD) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ci_item_edit_field, parent, false);
            vh = new EditFieldItem(v);
            return vh;
        }
        else if(viewType == ListViewType.VIEW_EDIT_FIELD_NO_TITLE) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ci_item_edit_field_layout_wrapped, parent, false);
            vh = new EditFieldItemNoTitle(v);
            return vh;
        }
        else if(viewType == ListViewType.VIEW_SINGLE_CHOICE) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ci_item_single_choiced, parent, false);
            vh = new SingleChoicedItem(v);
            return vh;
        }
        else if(viewType == ListViewType.VIEW_BOTTOM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_navigate_buttons, parent, false);
            vh = new FooterView(v);
            return vh;
        }
        else if(viewType == ListViewType.VIEW_SIMPLE_POPUP) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ci_item_popup_layout, parent, false);
            vh = new PopupView(v);
            return vh;
        }
        else if(viewType == ListViewType.VIEW_EDITTEXT_WITH_UNIT) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_edit_field_with_unit, parent, false);
            vh = new EditFieldItemWithUnit(v);
            return vh;
        }
        else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.blank_view, parent, false);
            vh = new BlankViewHolder(v);
            return vh;
        }
    }

    public class FooterView extends RecyclerView.ViewHolder {
        Button btnNext, btnPrevious;
        public FooterView(View v) {
            super(v);
            btnNext = (Button) v.findViewById(R.id.next_button);
            btnPrevious = (Button) v.findViewById(R.id.prev_button);

            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((WhatsMyCarWorth)mActivity).onNextButtonClick();
                }
            });

            btnPrevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((WhatsMyCarWorth)mActivity).onPreviousButtonClick();
                }
            });
        }
    }

    public class BlankViewHolder extends RecyclerView.ViewHolder {
        public BlankViewHolder(View v) {
            super(v);
        }
    }

    private class SingleChoicedItem extends RecyclerView.ViewHolder {
        TextView tvTitle;
        FlowLayout flOptions;
        public SingleChoicedItem(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.ci_item_tv_title);
            flOptions = (FlowLayout) itemView.findViewById(R.id.ci_item_fl_options);
        }
    }

    private class EditFieldItemNoTitle extends RecyclerView.ViewHolder {
        EditText etField;
        TextView tvLabel;
        public EditFieldItemNoTitle(View itemView) {
            super(itemView);
            tvLabel = (TextView) itemView.findViewById(R.id.tv_wmcw_item_editfield_label);
            etField = (EditText) itemView.findViewById(R.id.et_wmcw_item_editfield_value);
            etField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    mItemsList.get(getAdapterPosition()).setValue(etField.getText().toString());
                    //mPage.notifyDataChanged();
                    //updateData(getAdapterPosition(), value);
                }
            });
        }
    }

    private class EditFieldItem extends RecyclerView.ViewHolder {
        TextInputEditText etField;
        TextInputLayout til;
        String value = "";
        public EditFieldItem(View itemView) {
            super(itemView);
            //tvLabel = (TextView) itemView.findViewById(R.id.tv_wmcw_item_editfield_label);
            etField = (TextInputEditText) itemView.findViewById(R.id.custom_et);
            til = (TextInputLayout) itemView.findViewById(R.id.text_input_layout);
            etField.setText(value);

            etField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    mItemsList.get(getAdapterPosition()).setValue(etField.getText().toString());
                    mPage.notifyDataChanged();
                    //updateData(getAdapterPosition(), value);
                }
            });
        }
    }

    private class EditFieldItemWithUnit extends RecyclerView.ViewHolder{
        TextView tvLabel;
        EditText etField;
        Spinner spinnerKmsUnit;
        String value = "";
        DecimalFormat numberFormatter;
        String formatted = "";
        ArrayAdapter<String> dataAdapter;
        List<String> mKmsUnit = new ArrayList<>();
        public static final String mKmsUnitKms = "kms";
        public static final String mKmsUnitMiles = "mi";
        public EditFieldItemWithUnit(View itemView) {
            super(itemView);
            tvLabel = (TextView) itemView.findViewById(R.id.tv_wmcw_item_editfield_label);
            etField = (EditText) itemView.findViewById(R.id.et_wmcw_item_editfield_value);
            spinnerKmsUnit = (Spinner) itemView.findViewById(R.id.spinner_wmcw_kms);
            spinnerKmsUnit.setPadding(0,0,0,0);
            numberFormatter = new DecimalFormat("#,###");
            mKmsUnit.add(mKmsUnitKms);
            mKmsUnit.add(mKmsUnitMiles);
            etField.setInputType(InputType.TYPE_CLASS_NUMBER);
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
                        mItemsList.get(getAdapterPosition()).setValue(formatted);
                    }
                    else {
                        mItemsList.get(getAdapterPosition()).setValue("");
                    }
                }
            });

            etField.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        etField.setText(mItemsList.get(getAdapterPosition()).getValue());
                        etField.clearFocus();
                        Utility.hideKeyboard(mContext, etField);
                        return true;
                    }
                    return false;
                }
            });

            dataAdapter = new ArrayAdapter<String>(mContext, R.layout.wmcw_spinner_item, mKmsUnit);

            spinnerKmsUnit.setAdapter(dataAdapter);
            spinnerKmsUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mItemsList.get(getAdapterPosition()).setExtraValue(parent.getItemAtPosition(position).toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == mItemsList.size())
            return ListViewType.VIEW_BOTTOM;

        return mItemsList.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return mItemsList.size()+1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof PopUpItem) {
            PopUpItem item = (PopUpItem)holder;
            String label="", value;
            if(position == FIELD_MAKE) {
                label = mContext.getResources().getString(R.string.label_make);

                if(mItemsList.get(position).getValue() != null && !mItemsList.get(position).getValue().isEmpty())
                    value = mItemsList.get(position).getValue();
                else
                    value = mFieldData.get(FIELD_MAKE);
                item.tvValue.setText(value);
                item.fieldLayout.setBackground(mContext.getResources().getDrawable(R.drawable.selector_edit_box));
            }
            else if (position == FIELD_MODEL){
                label = mContext.getResources().getString(R.string.label_model);
                if(mItemsList.get(position).getValue() != null && !mItemsList.get(position).getValue().isEmpty()) {
                    value = mItemsList.get(position).getValue();
                    item.tvValue.setText(value);
                    item.fieldLayout.setBackground(mContext.getResources().getDrawable(R.drawable.selector_edit_box));
                }
                else {
                    ModelStatus status = ModelStatus.fromValue(mItemsList.get(position).getModelStatus());
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
            }
            else if(position == FIELD_TRANSMISSION) {
                label = mContext.getResources().getString(R.string.label_transmission);

                if(mItemsList.get(position).getValue() != null && !mItemsList.get(position).getValue().isEmpty())
                    value = mItemsList.get(position).getValue();
                else
                    value = mFieldData.get(FIELD_TRANSMISSION);
                item.tvValue.setText(value);
                item.fieldLayout.setBackground(mContext.getResources().getDrawable(R.drawable.selector_edit_box));
            }
            else {
                label = "";
            }
            item.tvLabel.setText(label);
        }
        else if(holder instanceof SingleChoicedItem) {
            final SingleChoicedItem item = (SingleChoicedItem) holder;
            //createSingleChoiceView(position-3, item);
            final QuestionItems questionItem = mItemsList.get(position);
            item.tvTitle.setText(questionItem.getQuestion());
            item.flOptions.removeAllViewsInLayout();
            for(String opt: questionItem.getOptions()) {
                final LinearLayout button = optionButtonCreator.createOption(opt, mContext);
                button.findViewById(R.id.dynamic_text_view).setTag(opt);
                button.setPadding(16, 16, 16, 16);

                if(questionItem.getValue().equals(opt)) {
                    button.setBackground(mContext.getResources().getDrawable(R.drawable.option_button_selected));
                }

                button.setOnClickListener(new LinearLayout.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for(int i=0; i<item.flOptions.getChildCount(); i++) {
                            item.flOptions.getChildAt(i).setBackground(mContext.getResources().getDrawable(R.drawable.option_button_unselected));
                        }
                        questionItem.setValue(((TextView)v.findViewById(R.id.dynamic_text_view)).getText().toString());
                        v.setBackground(mContext.getResources().getDrawable(R.drawable.option_button_selected));
                    }
                });
                item.flOptions.addView(button);
                item.flOptions.setGravity(Gravity.CENTER);
            }
        }
        else if (holder instanceof EditFieldItem) {
            final EditFieldItem holderItem = (EditFieldItem) holder;
            final QuestionItems dataItem = mItemsList.get(position);
            holderItem.til.setHint(dataItem.getQuestion());
            holderItem.etField.setText(dataItem.getValue());
        }
        else if(holder instanceof EditFieldItemNoTitle) {
            final EditFieldItemNoTitle holderItem = (EditFieldItemNoTitle) holder;
            holderItem.etField.setText(mItemsList.get(position).getValue());
            holderItem.tvLabel.setText(mItemsList.get(position).getQuestion());

            if(mItemsList.get(position).getQuestion() == "Year") {
                holderItem.etField.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
            else {
                holderItem.etField.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        }
        else if(holder instanceof PopupView) {
            final PopupView holderItem = (PopupView) holder;
            holderItem.tvTitleToHide.setText(mItemsList.get(position).getQuestion());
            holderItem.tvValue.setText(mItemsList.get(position).getValue());
            holderItem.fieldLayout.setBackground(mContext.getResources().getDrawable(R.drawable.selector_edit_box));
        }
        else if(holder instanceof EditFieldItemWithUnit) {
            final EditFieldItemWithUnit holderItem = (EditFieldItemWithUnit) holder;
            holderItem.etField.setText(mItemsList.get(position).getValue());
            holderItem.tvLabel.setText(mItemsList.get(position).getQuestion());
            int spinnerPosition = holderItem.dataAdapter.getPosition(mItemsList.get(position).getExtraValue());
            holderItem.spinnerKmsUnit.setSelection(spinnerPosition);
        }
        if(holder instanceof FooterView) {
            final FooterView holderItem = (FooterView) holder;
            if(mPage.getTitle() == "Vehicle") {
                holderItem.btnPrevious.setVisibility(View.INVISIBLE);
            }
            else {
                holderItem.btnPrevious.setVisibility(View.VISIBLE);
            }
        }
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
                public void onCheckBoxItemClicked(String keyName, boolean checked, String intRep, String value, String group, String id, int position) {
                }

                @Override
                public void onCheckBoxItemClicked(boolean checked, String value) {
                }

                @Override
                public void onCheckBoxItemClicked(String keyname, Map<Integer, AttrValSpinnerModel> attrMap, int intRep, AttrValSpinnerModel tmpAttr) {
                    dialog.dismiss();
                    attrValue = tmpAttr.getValue();
                    mItemsList.get(getAdapterPosition()).setValue(attrValue);

                    if (keyname.equals(Flags.Keys.MAKE)) {
                        mItemsList.get(getAdapterPosition() + 1).setValue("");
                        if (Integer.valueOf(tmpAttr.getGroup()) > 0) {
                            mGroup = tmpAttr.getId();
                            mId = 0;
                        } else {
                            mId = tmpAttr.getId();
                            mGroup = 0;
                        }
                        mItemsList.get(getAdapterPosition() + 1).setModelStatus(ModelStatus.LOADING.getValue());
                        //modelStatus = ModelStatus.LOADING.getValue();
                        mFieldData.put(FIELD_MAKE, attrValue);
                        new SearchFilterModelAPI(new SearchFilterModelListener() {
                            @Override
                            public void modelDataFromNetworkListener(ModelStatus status, List<AttrValSpinnerModel> data) {
                                mListData.put(Flags.Keys.MODEL, data);
                                mItemsList.get(getAdapterPosition() + 1).setModelStatus(status.getValue());
                                //modelStatus = status.getValue();
                                notifyDataSetChanged();
                            }

                            @Override
                            public void modelStatusListner(ModelStatus status) {
                                mItemsList.get(getAdapterPosition() + 1).setModelStatus(status.getValue());
                                //modelStatus = status.getValue();
                                notifyDataSetChanged();
                            }
                        }, Flags.getModelUrl(mId, mGroup));
                    }
                    else if (keyname == Flags.Keys.TRANSMISSION) {
                        mFieldData.put(FIELD_TRANSMISSION, attrValue);
                        notifyDataSetChanged();
                    }else {
                        mFieldData.put(FIELD_MODEL, attrValue);
                        mItemsList.get(getAdapterPosition() + 1).setModelStatus(ModelStatus.READY.getValue());
                        //modelStatus = ModelStatus.READY.getValue();
                        notifyDataSetChanged();
                    }
                }
            });

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
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        dialog.dismiss();
                    }
                    return false;
                }
            });

            fieldLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() == FIELD_MODEL) {
                        if (mListData != null && mListData.containsKey(Flags.Keys.MODEL) && mListData.get(Flags.Keys.MODEL).size() > 0) {
                            btnLayout.setVisibility(View.GONE);
                            tvFilterPopupTitle.setText(SELECT + " Model");
                            childRecyclerAdapter.setListState(mListData.get(Flags.Keys.MODEL), Flags.Keys.MODEL);
                            childRecyclerAdapter.getFilter().filter("");
                            childRecyclerAdapter.notifyDataSetChanged();
                            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                            dialog.show();
                            isMakeChanged = false;
                        }
                    } else if (getAdapterPosition() == FIELD_MAKE && mListData != null && mListData.containsKey(Flags.Keys.MAKE) && mListData.get(Flags.Keys.MAKE).size() > 0) {
                        btnLayout.setVisibility(View.GONE);
                        if (getAdapterPosition() == FIELD_MAKE) {
                            tvFilterPopupTitle.setText(SELECT + " Make");
                            childRecyclerAdapter.setListState(mListData.get(Flags.Keys.MAKE), Flags.Keys.MAKE);
                        }
                        childRecyclerAdapter.getFilter().filter("");
                        childRecyclerAdapter.notifyDataSetChanged();
                        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                        dialog.show();
                    } else if (getAdapterPosition() == FIELD_TRANSMISSION && mListData != null && mListData.containsKey(Flags.Keys.TRANSMISSION) && mListData.get(Flags.Keys.TRANSMISSION).size() > 0) {
                        btnLayout.setVisibility(View.GONE);
                        if (getAdapterPosition() == FIELD_TRANSMISSION) {
                            tvFilterPopupTitle.setText(SELECT + " Transmission");
                            childRecyclerAdapter.setListState(mListData.get(Flags.Keys.TRANSMISSION), Flags.Keys.TRANSMISSION);
                        }
                        childRecyclerAdapter.getFilter().filter("");
                        childRecyclerAdapter.notifyDataSetChanged();
                        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                        dialog.show();
                    } else {
                        WMCWVehicleModel model = (WMCWVehicleModel) mPage;
                        if(model.mAttributeData != null) {
                            mListData = model.mAttributeData;
                            notifyDataSetChanged();
                        }
                        else {
                            model.getDataFromNetwork();
                        }
                    }
                }
            });
        }
    }

    public class PopupView extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvValue, tvFilterPopupTitle, tvTitleToHide;
        private RecyclerView recyclerView;
        private SearchView searchView;
        private ImageView ivPopupClose;
        private CardView cardView;

        private TextFilterAdapter childRecyclerAdapter;

        public AlertDialog.Builder builder;
        public AlertDialog dialog;
        private LinearLayout btnLayout, fieldLayout, container;
        private List<SimpleListItemModel> dataList;
        private PopupView(View view) {
            super(view);
            tvTitle = (TextView) itemView.findViewById(R.id.ci_item_tv_title);
            tvValue = (TextView) view.findViewById(R.id.tv_wmcw_item_dropdown_value);
            fieldLayout = (LinearLayout) view.findViewById(R.id.ll_wmcw_dropdown_value);
            tvTitleToHide = (TextView) view.findViewById(R.id.tv_wmcw_item_dropdown_label);
            cardView = (CardView) view.findViewById(R.id.cv_item_popup);
            container = (LinearLayout) view.findViewById(R.id.ll_item_popup);
            ViewGroup.MarginLayoutParams layoutParams =
                    (ViewGroup.MarginLayoutParams) cardView.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, 0);
            cardView.requestLayout();

            ViewGroup.MarginLayoutParams lp =
                    (ViewGroup.MarginLayoutParams) container.getLayoutParams();
            lp.setMargins(0, 0, 0, 0);
            container.setPadding(10,10,10,30);
            container.requestLayout();

            tvTitle.setVisibility(View.GONE);
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
            dataList = new ArrayList<SimpleListItemModel>();
            childRecyclerAdapter = new TextFilterAdapter(mContext, dataList, new OnRowClickListener() {
                @Override
                public void rowClickListener(String val) {
                    dialog.dismiss();
                    mItemsList.get(getAdapterPosition()).setValue(val);
                    notifyDataSetChanged();
                }
            });
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
                    btnLayout.setVisibility(View.GONE);
                    childRecyclerAdapter.setListState(mItemsList.get(getAdapterPosition()).getmPopupOptions());
                    childRecyclerAdapter.getFilter().filter("");
                    childRecyclerAdapter.notifyDataSetChanged();
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    dialog.show();
                }
            });
        }
    }
}