package com.app.oktpus.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.app.oktpus.fragment.SortDialogFragment;
import com.app.oktpus.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Gyandeep on 23/4/17.
 */

public class SortRecyclerAdapter extends RecyclerView.Adapter<SortRecyclerAdapter.SortItemView> {

    private List<String> listData;
    private int lastPos = 0;
    private SortDialogFragment.OptionSelectListener listener;
    public SortRecyclerAdapter(List<String> data, SortDialogFragment.OptionSelectListener optionSelectListener) {
        this.listData = data;
        this.listener = optionSelectListener;
    }

    public class SortItemView extends RecyclerView.ViewHolder {

        public TextView mTvValue, mGroupValue;
        private RadioButton mRadioBtn;
        private LinearLayout mItemRow;
        private Map<String, Integer> position;

        public SortItemView(View iv) {
            super(iv);
            mTvValue = (TextView) iv.findViewById(R.id.tv_item);
            mRadioBtn = (RadioButton) iv.findViewById(R.id.rb_item);
            mItemRow = (LinearLayout) iv.findViewById(R.id.ll_item);
        }
    }

    @Override
    public SortItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        SortItemView vh;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sort_spinner, parent, false);
        vh = new SortItemView(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final SortItemView holder, final int position) {
        holder.mTvValue.setText(listData.get(position));
        if(position == lastPos) {
            holder.mRadioBtn.setChecked(true);
            holder.mRadioBtn.setTag(position);
        }
        else
            holder.mRadioBtn.setChecked(false);

        holder.mItemRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastPos = position;
                holder.mRadioBtn.setChecked(true);
                listener.sortSelected(position);
            }
        });
    }

    public String getCurrentOption() {
        return listData.get(lastPos);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}