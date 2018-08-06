package com.app.oktpus.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.oktpus.R;
import com.app.oktpus.activity.CarInsurance;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.constants.ListViewType;
import com.app.oktpus.constants.ModelStatus;
import com.app.oktpus.listener.OnCheckBoxClickListener;
import com.app.oktpus.listener.SearchFilterModelListener;
import com.app.oktpus.model.AttrValSpinnerModel;
import com.app.oktpus.model.carinsurance.CIVehicleModel;
import com.app.oktpus.model.carinsurance.QuestionItems;
import com.app.oktpus.service.SearchFilterModelAPI;
import com.app.oktpus.utils.FlowLayout;
import com.app.oktpus.utils.TagTokenUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wizardlib.model.Page;

/**
 * Created by Gyandeep on 6/11/17.
 */

public class CIVehicleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private List<QuestionItems> mItemsList;
    private Page mPage;

    private static Map<String, String> insuranceKeyTitlePairs;
    public static final int FIELD_MAKE = 1, FIELD_MODEL = 2, FIELD_YEAR = 5;
    static final String SELECT = "Select";
    Map<String, List<AttrValSpinnerModel>> mListData;
    Map<Integer, String> mFieldData;
    int mId = 0, mGroup = 0;
    //private int modelStatus = 1;
    boolean isMakeChanged = false;
    private TagTokenUtils optionButtonCreator;
    private Activity mActivity;
    //private ArrayList<QuestionItems> singleChoiceItemsData;

    public CIVehicleAdapter(Activity activity, Context context, Page page, Map<String, List<AttrValSpinnerModel>> listData, ArrayList<QuestionItems> singleChoiceItemsData){
        mContext = context;
        mPage = page;
        mListData = listData;
        mActivity = activity;

        mFieldData = new HashMap<>();
        mFieldData.put(FIELD_MAKE, SELECT);
        mFieldData.put(FIELD_MODEL, "");

        optionButtonCreator = new TagTokenUtils(mContext);

        this.mItemsList = singleChoiceItemsData;
        /*insuranceKeyTitlePairs = Flags.CarInsurance.insuranceKeyTitlePairs();
        singleChoiceItemsData = createSingleChoiceData(insuranceKeyTitlePairs);*/
    }

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
                    .inflate(R.layout.ci_item_edit_field_with_qsn, parent, false);
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
                    ((CarInsurance)mActivity).onNextButtonClick();
                }
            });

            btnPrevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((CarInsurance)mActivity).onPreviousButtonClick();
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
        //TextInputLayout til;
        TextView tvTitle;
        String value = "";
        public EditFieldItem(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.ci_item_tv_title);
            etField = (TextInputEditText) itemView.findViewById(R.id.custom_et);
            //til = (TextInputLayout) itemView.findViewById(R.id.text_input_layout);
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
            holderItem.etField.setText(mItemsList.get(position).getValue());
            holderItem.tvTitle.setText(mItemsList.get(position).getQuestion());
        }
        else if(holder instanceof EditFieldItemNoTitle) {
            final EditFieldItemNoTitle holderItem = (EditFieldItemNoTitle) holder;
            holderItem.etField.setText(mItemsList.get(position).getValue());
            holderItem.tvLabel.setText(mItemsList.get(position).getQuestion());
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
                public void onCheckBoxItemClicked(String keyName, boolean checked, String intRep, String value, String group, String id, int position) {}

                @Override
                public void onCheckBoxItemClicked(boolean checked, String value) {}

                @Override
                public void onCheckBoxItemClicked(String keyname, Map<Integer, AttrValSpinnerModel> attrMap, int intRep, AttrValSpinnerModel tmpAttr) {
                    dialog.dismiss();
                    attrValue = tmpAttr.getValue();
                    mItemsList.get(getAdapterPosition()).setValue(attrValue);
                    mItemsList.get(getAdapterPosition()+1).setValue("");
                    if(keyname.equals(Flags.Keys.MAKE)) {
                        if(Integer.valueOf(tmpAttr.getGroup()) > 0) {
                            mGroup = tmpAttr.getId();
                            mId = 0;
                        }
                        else {
                            mId = tmpAttr.getId();
                            mGroup = 0;
                        }
                        mItemsList.get(getAdapterPosition()+1).setModelStatus(ModelStatus.LOADING.getValue());
                        //modelStatus = ModelStatus.LOADING.getValue();
                        mFieldData.put(FIELD_MAKE, attrValue);
                        new SearchFilterModelAPI(new SearchFilterModelListener() {
                            @Override
                            public void modelDataFromNetworkListener(ModelStatus status, List<AttrValSpinnerModel> data) {
                                mListData.put(Flags.Keys.MODEL, data);
                                mItemsList.get(getAdapterPosition()+1).setModelStatus(status.getValue());
                                //modelStatus = status.getValue();
                                notifyDataSetChanged();
                            }

                            @Override
                            public void modelStatusListner(ModelStatus status) {
                                mItemsList.get(getAdapterPosition()+1).setModelStatus(status.getValue());
                                //modelStatus = status.getValue();
                                notifyDataSetChanged();
                            }
                        }, Flags.getModelUrl(mId, mGroup));
                    }
                    else {
                        mFieldData.put(FIELD_MODEL, attrValue);
                        mItemsList.get(getAdapterPosition()+1).setModelStatus(ModelStatus.READY.getValue());
                        //modelStatus = ModelStatus.READY.getValue();
                        notifyDataSetChanged();
                    }
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
                        if(mListData != null && mListData.containsKey(Flags.Keys.MODEL) && mListData.get(Flags.Keys.MODEL).size() > 0) {
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
                    else if(mListData != null && mListData.containsKey(Flags.Keys.MAKE) && mListData.get(Flags.Keys.MAKE).size() > 0){
                        btnLayout.setVisibility(View.GONE);
                        if(getAdapterPosition() == FIELD_MAKE) {
                            tvFilterPopupTitle.setText(SELECT+ " Make");
                            childRecyclerAdapter.setListState(mListData.get(Flags.Keys.MAKE), Flags.Keys.MAKE);
                        }
                        childRecyclerAdapter.getFilter().filter("");
                        childRecyclerAdapter.notifyDataSetChanged();
                        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                        dialog.show();
                    }
                    else {
                        CIVehicleModel model = (CIVehicleModel) mPage;
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
}
