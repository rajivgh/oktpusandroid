package com.app.oktpus.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.app.oktpus.model.AttrValSpinnerModel;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.listener.OnCheckBoxClickListener;
import com.app.oktpus.R;
import com.app.oktpus.utils.Utility;

/**
 * Created by Gyandeep on 15/11/16.
 */

public class MultiSelectItemSearchViewAdapter extends RecyclerView.Adapter<MultiSelectItemSearchViewAdapter.RecyclerViewHolder> implements Filterable{
    private Context mContext;
    private List<AttrValSpinnerModel> listState;
    private List<AttrValSpinnerModel> originalList;
    private boolean isFromView = false;
    private String keyName;
    private OnCheckBoxClickListener checkBoxListener;
    private Map<Integer, AttrValSpinnerModel> tmpAttributesMap;
    private String searchString = "";

    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listState = (ArrayList<AttrValSpinnerModel>) results.values;
                MultiSelectItemSearchViewAdapter.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                List<AttrValSpinnerModel> filteredResults = null;
                if (constraint.length() == 0) {
                    searchString = "";
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
        this.searchString = constraint;
        if(originalList != null) {
            for (AttrValSpinnerModel item : originalList) {
                if(item.getValue() != null) {
                    if(keyName.equals(Flags.Keys.MODEL)) {
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

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvValue;
        public TextView mTvCount;
        public CheckBox mCheckBox;
        public LinearLayout mItem;
        public int mGroup;
        public int mId;
        public int mIntRep;
        public RecyclerViewHolder(View view) {
            super(view);
            mTvValue = (TextView) view.findViewById(R.id.tv_item);
            mTvCount = (TextView) view.findViewById(R.id.tv_item_count);
            mCheckBox = (CheckBox) view.findViewById(R.id.cb_item);
            mItem = (LinearLayout) view.findViewById(R.id.list_item);

            mId = 0; mGroup = 0; mIntRep = 0;
        }
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public MultiSelectItemSearchViewAdapter(Context mContext, List<AttrValSpinnerModel> modelList, String keyName,
                                            OnCheckBoxClickListener checkBoxListener) {
        this.mContext = mContext;
        this.listState = modelList;
        this.keyName = keyName;
        this.checkBoxListener = checkBoxListener;
        if(modelList != null) this.originalList = modelList;
        tmpAttributesMap = new HashMap<>();
    }

    public void setListState(List<AttrValSpinnerModel> val) {
        this.listState.clear();
        this.listState.addAll(val);
        this.originalList.clear();
        this.originalList.addAll(val);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.spinner_item, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        AttrValSpinnerModel item = listState.get(position);
        holder.mTvValue.setText(item.getValue());
        holder.mGroup = item.getGroup();
        holder.mId = item.getId();
        holder.mIntRep = item.getInteger_representation();
        holder.mTvCount.setText(item.getCount());

        //Highlight filter text
        if (!searchString.isEmpty() && item.getValue().toLowerCase().contains(searchString)) {
            int startPos = item.getValue().toLowerCase().indexOf(searchString);
            int endPos = startPos + searchString.length();
            holder.mTvValue.setText(Utility.colorifyFilterText(mContext, searchString, holder.mTvValue, startPos, endPos));
        }

        if(Flags.Keys.CITY.equals(keyName))
            holder.mTvCount.setVisibility(View.INVISIBLE);

        // To check whether checked event fired from getview() or user input
        isFromView = true;
        holder.mCheckBox.setChecked(listState.get(position).isTmpSelected());
        isFromView = false;

        holder.mCheckBox.setTag(position);
        final int pos = position;


        /*holder.mItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println("TOUCHED");
                return false;
            }
        });*/


        /*if(attributeList.get(keyName).get(position).isSelected())
                                attributeList.get(keyName).get(position).setSelected(false);
                            else
                                attributeList.get(keyName).get(position).setSelected(true);

                            checkBoxListener.onCheckBoxItemClicked(keyName,
                                    attributeList.get(keyName).get(position).isSelected(),
                                    String.valueOf(attributeList.get(keyName).get(position).getInteger_representation()),
                                    attributeList.get(keyName).get(position).getValue(),
                                    String.valueOf(attributeList.get(keyName).get(position).getGroup()),
                                    String.valueOf(attributeList.get(keyName).get(position).getId()));

                            displayCheckedValues(attributeList.get(keyName).get(position).isSelected(),
                                    attributeList.get(keyName).get(position).getValue(), holder);

                            childRecyclerAdapter.notifyDataSetChanged();*/

        holder.mItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFromView){
                    if(holder.mCheckBox.isChecked()) {
                        listState.get(pos).setTmpSelected(false);
                        holder.mCheckBox.setChecked(false);
                    } else {
                        listState.get(pos).setTmpSelected(true);
                        holder.mCheckBox.setChecked(true);
                    }
                    //Log.d("OnCheckChangedListener", "checkbox checked value: "+listState.get(pos).getValue());

                    AttrValSpinnerModel tmpAttr = new AttrValSpinnerModel();
                    tmpAttr.setSelected(holder.mCheckBox.isChecked());
                    tmpAttr.setAttrbuteKey(keyName);
                    tmpAttr.setInteger_representation(listState.get(pos).getInteger_representation());
                    tmpAttr.setId(listState.get(pos).getId());
                    tmpAttr.setGroup(listState.get(pos).getGroup());

                    tmpAttributesMap.put(listState.get(pos).getInteger_representation(), tmpAttr);
                    checkBoxListener.onCheckBoxItemClicked(keyName, tmpAttributesMap, listState.get(pos).getInteger_representation(), tmpAttr);

                    /*listState.get(pos).setSelected(holder.mCheckBox.isChecked());
                    checkBoxListener.onCheckBoxItemClicked(keyName,
                            listState.get(pos).isSelected(),
                            String.valueOf(listState.get(pos).getInteger_representation()),
                            listState.get(pos).getValue(),
                            String.valueOf(listState.get(pos).getGroup()),
                            String.valueOf(listState.get(pos).getId()),
                            pos);*/

                    /*checkBoxListenerExpList.onCheckBoxItemClicked(listState.get(pos).isSelected(),
                            listState.get(pos).getValue());*/
                }
            }
        });


        /*holder.mItem.setOnTouchListener(new LinearLayout.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(MotionEvent.ACTION_DOWN == event.getAction() || MotionEvent.ACTION_UP == event.getAction()) {
                    //v.canScrollVertically(View.LAYOUT_DIRECTION_INHERIT);
                }
                else if(MotionEvent.ACTION_BUTTON_PRESS == event.getAction()) {
                    if(!isFromView){
                        if(holder.mCheckBox.isChecked())
                            holder.mCheckBox.setChecked(false);
                        else holder.mCheckBox.setChecked(true);
                        //Log.d("OnCheckChangedListener", "checkbox checked value: "+listState.get(pos).getValue());
                        listState.get(pos).setSelected(holder.mCheckBox.isChecked());
                        checkBoxListener.onCheckBoxItemClicked(keyName,
                                listState.get(pos).isSelected(),
                                String.valueOf(listState.get(pos).getInteger_representation()),
                                listState.get(pos).getValue(),
                                String.valueOf(listState.get(pos).getGroup()),
                                String.valueOf(listState.get(pos).getId()));

                        checkBoxListenerExpList.onCheckBoxItemClicked(listState.get(pos).isSelected(),
                                listState.get(pos).getValue());
                    }
                }

                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });*/


        /*holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isFromView){
                    //Log.d("OnCheckChangedListener", "checkbox checked value: "+listState.get(pos).getValue());
                    listState.get(pos).setSelected(buttonView.isChecked());
                    checkBoxListener.onCheckBoxItemClicked(keyName,
                            listState.get(pos).isSelected(),
                            String.valueOf(listState.get(pos).getInteger_representation()),
                            listState.get(pos).getValue(),
                            String.valueOf(listState.get(pos).getGroup()),
                            String.valueOf(listState.get(pos).getId()));

                    checkBoxListenerExpList.onCheckBoxItemClicked(listState.get(pos).isSelected(),
                            listState.get(pos).getValue());
                }
            }
        });*/
    }

    public void clearTmpHolder(){
        this.tmpAttributesMap.clear();
    }

    @Override
    public int getItemCount() {
        if(listState == null)
            return 0;
        else
            return listState.size();
    }

}