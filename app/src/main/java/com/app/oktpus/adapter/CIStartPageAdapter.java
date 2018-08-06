package com.app.oktpus.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.oktpus.R;
import com.app.oktpus.constants.CarInsurance;
import com.app.oktpus.model.carinsurance.CIDriverModel;
import com.app.oktpus.model.carinsurance.QuestionItems;
import com.app.oktpus.utils.FlowLayout;
import com.app.oktpus.utils.TagTokenUtils;

import java.util.ArrayList;

import wizardlib.model.Page;

/**
 * Created by Gyandeep on 16/11/17.
 */

public class CIStartPageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private Page mPage;
    private ArrayList<QuestionItems> mDataItems;
    private TagTokenUtils optionButtonCreator;
    private CIDriverModel mDriverModel;
    private Activity mActivity;
    private static int VIEW_SINGLE_CHOICE = 1;

    public CIStartPageAdapter(Activity activity, Context context, Page page, ArrayList<QuestionItems> dataItems, CIDriverModel driverModel) {
        mContext = context;
        mActivity = activity;
        mPage = page;
        mDataItems = dataItems;
        optionButtonCreator = new TagTokenUtils(mContext);
        mDriverModel = driverModel;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if(viewType == VIEW_SINGLE_CHOICE) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ci_item_single_choiced, parent, false);
            vh = new SingleChoicedItem(v);
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
        CardView cardViewLayout;
        public SingleChoicedItem(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.ci_item_tv_title);
            flOptions = (FlowLayout) itemView.findViewById(R.id.ci_item_fl_options);
            cardViewLayout = (CardView) itemView.findViewById(R.id.cv_item_single_choiced);

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            cardViewLayout.setLayoutParams(lp);
        }
    }

    public class BlankViewHolder extends RecyclerView.ViewHolder {
        public BlankViewHolder(View v) {
            super(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof SingleChoicedItem) {
            final SingleChoicedItem item = (SingleChoicedItem) holder;
            //createSingleChoiceView(position-3, item);
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

                        if(questionItem.getValue().equals("Yes, I have insurance")) {
                            mDriverModel.setModelData(CarInsurance.HAVE_INSURANCE);
                        }
                        else {
                            mDriverModel.setModelData(CarInsurance.DONT_HAVE_INSURANCE);
                        }
                        ((com.app.oktpus.activity.CarInsurance)mActivity).onNextButtonClick();
                    }
                });
                item.flOptions.addView(button);
                item.flOptions.setGravity(Gravity.CENTER);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position == mDataItems.size()-1) {
            return VIEW_SINGLE_CHOICE;
        }
        else
            return position;
    }
}
