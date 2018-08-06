package com.app.oktpus.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.app.oktpus.model.AttrValSpinnerModel;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.listener.OnCheckBoxClickListener;
import com.app.oktpus.R;
import com.app.oktpus.utils.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Gyandeep on 21/7/17.
 */

public class WMCWItemAdapter extends RecyclerView.Adapter<WMCWItemAdapter.RecyclerViewHolder> implements Filterable {

    private List<AttrValSpinnerModel> listState;
    private List<AttrValSpinnerModel> originalList;
    private boolean isFromView = false;
    private String keyName;
    private OnCheckBoxClickListener checkBoxListener;
    private Map<Integer, List<AttrValSpinnerModel>> attributeData;
    private Context mContext;
    private String mSearchString = "";
    private String keyname;
    private OnCheckBoxClickListener listener;
    public WMCWItemAdapter(Context context, List<AttrValSpinnerModel> data, String keyname, OnCheckBoxClickListener listener) {
        this.listState = data;
        this.originalList = data;
        this.keyname = keyname;
        this.listener = listener;
        this.mContext = context;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTvValue;
        public TextView mTvCount;
        public RadioButton mCheckBox;
        public LinearLayout mItem;
        public int mGroup;
        public int mId;
        public int mIntRep;
        public RecyclerViewHolder(View view) {
            super(view);
            mTvValue = (TextView) view.findViewById(R.id.tv_item);
            mTvCount = (TextView) view.findViewById(R.id.tv_item_count);
            mCheckBox = (RadioButton) view.findViewById(R.id.cb_item);
            mItem = (LinearLayout) view.findViewById(R.id.list_item);

            mItem.setOnClickListener(this);
            mId = 0; mGroup = 0; mIntRep = 0;
        }

        @Override
        public void onClick(View v) {
            for(AttrValSpinnerModel attr : listState) {
                attr.setSelected(false);
            }

            for(AttrValSpinnerModel attr : originalList) {
                attr.setSelected(false);
            }

            listState.get(getAdapterPosition()).setSelected(true);
            listener.onCheckBoxItemClicked(keyname, null, 0, listState.get(getAdapterPosition()));
            notifyItemRangeChanged(0, originalList.size());
        }
    }

    public void setListState(List<AttrValSpinnerModel> val, String keyname) {
        this.listState.clear();
        this.listState.addAll(val);
        this.originalList.clear();
        this.originalList.addAll(val);
        this.keyname = keyname;
        this.mSearchString = "";
    }

    @Override
    public WMCWItemAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.spinner_item_radio, parent, false);
        return new WMCWItemAdapter.RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final WMCWItemAdapter.RecyclerViewHolder holder, final int position) {
        if(holder instanceof RecyclerViewHolder) {
            AttrValSpinnerModel item = listState.get(position);
            holder.mTvValue.setText(item.getValue());
            holder.mCheckBox.setChecked(item.isSelected());

            //Highlight filter text
            if (!mSearchString.isEmpty() && item.getValue().toLowerCase().contains(mSearchString)) {
                int startPos = item.getValue().toLowerCase().indexOf(mSearchString);
                int endPos = startPos + mSearchString.length();
                holder.mTvValue.setText(Utility.colorifyFilterText(mContext, mSearchString, holder.mTvValue, startPos, endPos));
            }
        }
    }

    @Override
    public int getItemCount() {
        if(listState == null)
            return 0;
        else
            return listState.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listState = (ArrayList<AttrValSpinnerModel>) results.values;
                WMCWItemAdapter.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<AttrValSpinnerModel> filteredResults = null;
                if (constraint.length() == 0) {
                    mSearchString = "";
                    filteredResults = originalList;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }

    protected List<AttrValSpinnerModel> getFilteredResults(String constraint) {
        List<AttrValSpinnerModel> results = new ArrayList<>();
        this.mSearchString = constraint;
        if(originalList != null) {
            for (AttrValSpinnerModel item : originalList) {
                if(item.getValue() != null) {
                    if(keyname.equals(Flags.Keys.MODEL)) {
                        if (item.getValue().toLowerCase().contains(constraint))
                            results.add(item);
                    }
                    else {
                        if (item.getValue().toLowerCase().startsWith(constraint))
                            results.add(item);
                    }
                }
            }
        }

        return results;
    }
}