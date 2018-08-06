package com.app.oktpus.adapter.carfinance;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.oktpus.R;
import com.app.oktpus.activity.CarFinance;
import com.app.oktpus.constants.ListViewType;
import com.app.oktpus.constants.carfinance.FinanceConstants;
import com.app.oktpus.fragment.carfinance.CFStartFragment;
import com.app.oktpus.model.carfinance.CFLoanInfoModel;
import com.app.oktpus.model.carfinance.CFPersonalInfoModel;
import com.app.oktpus.model.carinsurance.QuestionItems;
import com.app.oktpus.utils.FlowLayout;
import com.app.oktpus.utils.TagTokenUtils;

import java.util.ArrayList;

import wizardlib.model.Page;

/**
 * Created by Gyandeep on 16/1/18.
 */

public class CFStartPageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private Activity mActivity;
    private Page mPage;
    private ArrayList<QuestionItems> mDataItems;
    private TagTokenUtils optionButtonCreator;
    private CFLoanInfoModel mLoanInfoModel;
    private CFPersonalInfoModel mPersonalInfoModel,mCoApplicantPersonalInfoModel;
    public CFStartPageAdapter(Activity activity, Context context, Page page, ArrayList<QuestionItems> dataItems, CFStartFragment.AdapterListener listener,
                              CFLoanInfoModel loanInfoModel, CFPersonalInfoModel personalInfoModel, CFPersonalInfoModel coApplicantPersonalInfoModel) {
        mContext = context;
        mActivity = activity;
        mPage = page;
        mDataItems = dataItems;
        mLoanInfoModel = loanInfoModel;
        optionButtonCreator = new TagTokenUtils(mContext);
        mPersonalInfoModel = personalInfoModel;
        mCoApplicantPersonalInfoModel = coApplicantPersonalInfoModel;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if(viewType == ListViewType.VIEW_SINGLE_CHOICE) {
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
            btnPrevious.setVisibility(View.INVISIBLE);
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((CarFinance)mActivity).onNextButtonClick();
                }
            });

            /*btnPrevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((CarFinance)mActivity).onPreviousButtonClick();
                }
            });*/
        }
    }

    private class BlankViewHolder extends RecyclerView.ViewHolder {
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
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof SingleChoicedItem) {
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

                        if(questionItem.getParamKey().equals(FinanceConstants.APIKeys.LOAN_OR_FINANCE_TYPE)) {
                            mLoanInfoModel.setData(questionItem.getValue());
                        }
                        else if(questionItem.getParamKey().equals(FinanceConstants.APIKeys.APPLICATION_TYPE)) {
                            if(questionItem.getValue().equals(FinanceConstants.ApplicationType.INDIVIDUAL)) {
                                ((CarFinance)mActivity).removeAdditionalPages();
                                mPersonalInfoModel.setApplicationType(FinanceConstants.ApplicationType.INDIVIDUAL);
                            }
                            else {
                                ((CarFinance)mActivity).setApplicationType(FinanceConstants.ApplicationType.JOINT);
                                mPersonalInfoModel.setApplicationType(FinanceConstants.ApplicationType.JOINT);
                                ((CarFinance)mActivity).addPages();
                            }
                        }
                    }
                });
                item.flOptions.addView(button);
                item.flOptions.setGravity(Gravity.CENTER);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataItems.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == mDataItems.size()) {
            return ListViewType.VIEW_BOTTOM;
        }

        return mDataItems.get(position).getType();
    }
}
