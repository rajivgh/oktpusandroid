package com.app.oktpus.adapter.carfinance;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.oktpus.R;
import com.app.oktpus.activity.CarFinance;
import com.app.oktpus.constants.ListViewType;
import com.app.oktpus.constants.carfinance.FinanceConstants;
import com.app.oktpus.fragment.carfinance.CFPersonalInfo;
import com.app.oktpus.model.carinsurance.QuestionItems;
import com.app.oktpus.utils.FlowLayout;
import com.app.oktpus.utils.TagTokenUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import wizardlib.model.Page;

/**
 * Created by Gyandeep on 15/1/18.
 */

public class CFPersonalInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private Page mPage;
    private ArrayList<QuestionItems> mDataItems;
    private TagTokenUtils optionButtonCreator;
    private CFPersonalInfo.AdapterListener mListener;
    private Activity mActivity;

    public CFPersonalInfoAdapter(Activity activity, Context context, Page page, ArrayList<QuestionItems> dataItems, CFPersonalInfo.AdapterListener listener) {
        mContext = context;
        mActivity = activity;
        mPage = page;
        mDataItems = dataItems;
        optionButtonCreator = new TagTokenUtils(mContext);
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if(viewType == ListViewType.VIEW_EDIT_FIELD) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ci_item_edit_field, parent, false);
            vh = new EditFieldItem(v);
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
        else if(viewType == ListViewType.VIEW_DATE_PICKER_EDIT_FIELD) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ci_item_date_picker, parent, false);
            vh = new DatePickerEditField(v);
            return vh;
        }
        else if(viewType == ListViewType.VIEW_SIMPLE_TEXT) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ci_item_simple_text, parent, false);
            vh = new SimpleText(v);
            return vh;
        }
        else if(viewType == ListViewType.VIEW_CHECKBOX_ROW) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.wizard_checkbox_row, parent, false);
            vh = new CheckboxItemRow(v);
            return vh;
        }
        else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.blank_view, parent, false);
            vh = new BlankViewHolder(v);
            return vh;
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

    private class MultiChoiceView extends RecyclerView.ViewHolder {
        TextView tvTitle;
        LinearLayout llMultiChoiceOptions;
        public MultiChoiceView(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.ci_item_tv_title);
            llMultiChoiceOptions = (LinearLayout) itemView.findViewById(R.id.ci_item_ll_multi_choice_options);
        }
    }

    public class SimpleText extends RecyclerView.ViewHolder {
        TextView tv;
        public SimpleText(View v) {
            super(v);
            tv = (TextView) v.findViewById(R.id.tv_simple_text);
        }
    }

    public class CheckboxItemRow extends RecyclerView.ViewHolder {
        TextView tv;
        CheckBox cb;
        LinearLayout rowLayout;
        public CheckboxItemRow(View v) {
            super(v);
            tv = (TextView) v.findViewById(R.id.tv_wizard_item);
            cb = (CheckBox) v.findViewById(R.id.cb_wizard_item);
            rowLayout = (LinearLayout) v.findViewById(R.id.ll_wizard_item);
            rowLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!cb.isChecked()) {
                        cb.setChecked(true);
                        mDataItems.get(getAdapterPosition()).setValue((cb.isChecked())?"Yes":"");
                        if(mDataItems.get(getAdapterPosition()).getParamKey().equals(FinanceConstants.APIKeys.CO_APPLICANT_IS_ADDRESS_SAME_APPLICANT)) {
                            mListener.updateCoApplicantAddress();
                        }
                    }
                    else {
                        cb.setChecked(false);
                        mDataItems.get(getAdapterPosition()).setValue((cb.isChecked())?"Yes":"");
                    }

                }
            });
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
                    ((CarFinance)mActivity).onNextButtonClick();
                }
            });

            btnPrevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((CarFinance)mActivity).onPreviousButtonClick();
                }
            });
        }
    }

    public class BlankViewHolder extends RecyclerView.ViewHolder {
        public BlankViewHolder(View v) {
            super(v);
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
                    mDataItems.get(getAdapterPosition()).setValue(etField.getText().toString());
                    //mPage.notifyDataChanged();
                    //updateData(getAdapterPosition(), value);
                }
            });
        }
    }

    private class DatePickerEditField extends RecyclerView.ViewHolder {
        EditText etDatePicker;
        Calendar myCalendar;
        DatePickerDialog.OnDateSetListener date;

        public DatePickerEditField(View itemView) {
            super(itemView);
            etDatePicker = (EditText) itemView.findViewById(R.id.custom_et_datepicker);
            myCalendar = Calendar.getInstance();
            date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel();
                }
            };

            etDatePicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    new DatePickerDialog(mContext, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
        }
        public void updateLabel() {
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
            String date = sdf.format(myCalendar.getTime());
            etDatePicker.setText(date);
            mDataItems.get(getAdapterPosition()).setValue(date);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof EditFieldItem) {
            final EditFieldItem holderItem = (EditFieldItem) holder;
            final QuestionItems dataItem = mDataItems.get(position);
            holderItem.til.setHint(dataItem.getQuestion());
            holderItem.etField.setText(dataItem.getValue());
        }
        else if(holder instanceof DatePickerEditField) {
            final DatePickerEditField holderItem = (DatePickerEditField) holder;
            final QuestionItems dataItem = mDataItems.get(position);
            holderItem.etDatePicker.setHint(dataItem.getQuestion());
            holderItem.etDatePicker.setText(dataItem.getValue());
        }
        else if(holder instanceof SingleChoicedItem) {
            final SingleChoicedItem item = (SingleChoicedItem) holder;
            final QuestionItems questionItem = mDataItems.get(position);
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

                        if(mDataItems.get(position).getHasExtension() && questionItem.getValue().equals("Yes")) {
                            int pos = position + 1;
                            if(!mDataItems.get(position).isExpanded()) {
                                for(int i=0; i<mDataItems.get(position).getSubQuestionsList().size(); i++) {
                                    mDataItems.add(pos+i, mDataItems.get(position).getSubQuestionsList().get(i));
                                    mDataItems.get(position).getSubQuestionsList().get(i).setExtension(true);
                                }
                                mDataItems.get(position).setExpanded(true);
                            }
                            notifyDataSetChanged();
                        }
                        else if(mDataItems.get(position).getHasExtension() && questionItem.getValue().equals("No")) {
                            if(mDataItems.get(position).isExpanded()) {
                                int pos = position+1;
                                for(int i=0; i<mDataItems.get(position).getSubQuestionsList().size(); i++) {
                                    mDataItems.remove(pos);
                                }
                                mDataItems.get(position).setExpanded(false);
                            }
                            notifyDataSetChanged();
                        }
                    }
                });
                item.flOptions.addView(button);
                item.flOptions.setGravity(Gravity.CENTER);
            }
        }
        else if(holder instanceof MultiChoiceView) {
            final MultiChoiceView item = (MultiChoiceView) holder;
            //createSingleChoiceView(position-3, item);
            final QuestionItems questionItem = mDataItems.get(position);
            item.tvTitle.setText(questionItem.getQuestion());
            item.llMultiChoiceOptions.removeAllViewsInLayout();
            for(final String choice : questionItem.getOptions()) {
                final LinearLayout row = optionButtonCreator.createMultiChoiceItem(choice, mContext);
                row.findViewById(R.id.dynamic_text_view).setTag(choice);
                row.setPadding(16, 10, 16, 0);

                if(questionItem.getMultiChoiceValues().containsKey(choice)) {
                    ((CheckBox)row.findViewById(R.id.checkbox_item)).setChecked(questionItem.getMultiChoiceValues().get(choice));
                }
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v.findViewById(R.id.checkbox_item);
                        if(cb.isChecked()) {
                            cb.setChecked(false);
                        } else {
                            cb.setChecked(true);
                        }
                        questionItem.getMultiChoiceValues().put(choice, cb.isChecked());
                    }
                });
                item.llMultiChoiceOptions.addView(row);
            }
        }
        else if(holder instanceof SimpleText) {
            final SimpleText holderItem = (SimpleText) holder;
            holderItem.tv.setText(mDataItems.get(position).getQuestion());
        }
        else if(holder instanceof CheckboxItemRow) {
            final CheckboxItemRow holderItem = (CheckboxItemRow) holder;
            holderItem.tv.setText(mDataItems.get(position).getQuestion());
            //mDataItems.get(position).setValue((holderItem.cb.isChecked())?"Yes":"No");

            if(!mDataItems.get(position).getValue().isEmpty() && mDataItems.get(position).getValue().equals("Yes")) {
                holderItem.cb.setChecked(true);
            }
            else {
                holderItem.cb.setChecked(false);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == mDataItems.size())
            return ListViewType.VIEW_BOTTOM;
        return mDataItems.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return mDataItems.size() + 1;
    }
}
