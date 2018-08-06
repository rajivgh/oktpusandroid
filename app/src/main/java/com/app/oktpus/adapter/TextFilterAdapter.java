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

import com.app.oktpus.R;
import com.app.oktpus.listener.OnRowClickListener;
import com.app.oktpus.model.SimpleListItemModel;
import com.app.oktpus.utils.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gyandeep on 19/11/17.
 */

public class TextFilterAdapter extends RecyclerView.Adapter<TextFilterAdapter.RecyclerViewHolder> implements Filterable {

    private List<SimpleListItemModel> listData;
    private List<SimpleListItemModel> originalList;
    private Context mContext;
    private String mSearchString = "";
    private OnRowClickListener mListener;

    public TextFilterAdapter(Context context, List<SimpleListItemModel> data, OnRowClickListener listener) {
        this.listData = data;
        this.originalList = data;
        this.mContext = context;
        this.mListener = listener;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTvValue;
        public RadioButton mCheckBox;
        public LinearLayout mItem;

        public RecyclerViewHolder(View view) {
            super(view);
            mTvValue = (TextView) view.findViewById(R.id.tv_item);
            mCheckBox = (RadioButton) view.findViewById(R.id.cb_item);
            mItem = (LinearLayout) view.findViewById(R.id.list_item);
            mItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            for(SimpleListItemModel item: listData) {
                item.setSelected(false);
            }

            for(SimpleListItemModel attr : originalList) {
                attr.setSelected(false);
            }

            mListener.rowClickListener(listData.get(getAdapterPosition()).getTitle());
            listData.get(getAdapterPosition()).setSelected(true);
            notifyDataSetChanged();

            //listener.onCheckBoxItemClicked(keyname, null, 0, listState.get(getAdapterPosition()));
            //notifyItemRangeChanged(0, originalList.size());
        }
    }

    public void setListState(List<SimpleListItemModel> val) {
        this.listData.clear();
        this.listData.addAll(val);
        this.originalList.clear();
        this.originalList.addAll(val);
        this.mSearchString = "";
    }

    @Override
    public TextFilterAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.spinner_item_radio, parent, false);
        return new TextFilterAdapter.RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TextFilterAdapter.RecyclerViewHolder holder, final int position) {
        if(holder instanceof RecyclerViewHolder) {
            SimpleListItemModel item = listData.get(position);
            holder.mTvValue.setText(item.getTitle());
            holder.mCheckBox.setChecked(item.isSelected());

            //Highlight filter text
            if (!mSearchString.isEmpty() && item.getTitle().toLowerCase().contains(mSearchString)) {
                int startPos = item.getTitle().toLowerCase().indexOf(mSearchString);
                int endPos = startPos + mSearchString.length();
                holder.mTvValue.setText(Utility.colorifyFilterText(mContext, mSearchString, holder.mTvValue, startPos, endPos));
            }
        }
    }

    @Override
    public int getItemCount() {
        if(listData == null)
            return 0;
        else
            return listData.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listData = (ArrayList<SimpleListItemModel>) results.values;
                TextFilterAdapter.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<SimpleListItemModel> filteredResults = null;
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

    protected List<SimpleListItemModel> getFilteredResults(String constraint) {
        List<SimpleListItemModel> results = new ArrayList<>();
        this.mSearchString = constraint;
        if(originalList != null) {
            for (SimpleListItemModel item : originalList) {
                if(item.getTitle() != null) {
                    if (item.getTitle().toLowerCase().startsWith(constraint))
                        results.add(item);
                }
            }
        }

        return results;
    }
}