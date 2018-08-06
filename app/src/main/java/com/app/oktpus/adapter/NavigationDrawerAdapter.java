package com.app.oktpus.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Map;

import com.app.oktpus.model.NavDrawerItem;
import com.app.oktpus.R;
import com.app.oktpus.utils.SessionManagement;

/**
 * Created by Gyandeep on 20/9/16.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //public List<NavDrawerItem> data = Collections.emptyList();
    public Map<Integer, NavDrawerItem> data = null;
    private LayoutInflater inflater;
    private Context context;
    private int menuTxtColor;
    private static final int VIEW_HEADER = 0, VIEW_ITEM = 1;
    private SessionManagement session;

    public NavigationDrawerAdapter(Context context, Map<Integer, NavDrawerItem> data, int menuTxtColor, SessionManagement session) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        this.menuTxtColor = menuTxtColor;
        this.session = session;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if(viewType == VIEW_HEADER) {
            View view = inflater.inflate(R.layout.nav_drawer_header, parent, false);
            vh = new HeaderViewHolder(view);
            return vh;
        }
        else {
            View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
            vh = new DrawerViewHolder(view);
            return vh;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int pos) {
        try {
            if(holder instanceof DrawerViewHolder && pos > 0) {
                DrawerViewHolder ih = (DrawerViewHolder) holder;
                NavDrawerItem item = data.get(pos);
                ih.title.setText(item.getTitle());
                ih.title.setTextColor(context.getResources().getColor(menuTxtColor));
                ih.icon.setImageResource(item.getIcon());
            }
            else if(holder instanceof HeaderViewHolder) {
                if(session.isLoggedIn()) {
                    if(((HeaderViewHolder) holder).tvUserName.getVisibility() == View.GONE) {
                        ((HeaderViewHolder) holder).tvUserName.setText(session.getUsername());
                        ((HeaderViewHolder) holder).tvUserName.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    if(((HeaderViewHolder) holder).tvUserName.getVisibility() == View.VISIBLE)
                        ((HeaderViewHolder) holder).tvUserName.setVisibility(View.GONE);
                }
            }
        }
        catch(Exception e) {
            Log.e("NavigationDrawerAdapter", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return VIEW_HEADER;
        else
            return VIEW_ITEM;
        //return super.getItemViewType(position);
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder{
        TextView tvUserName;
        ImageView ivClose;
        public HeaderViewHolder(View v) {
            super(v);
            tvUserName = (TextView) v.findViewById(R.id.tv_uname);
            /*ivClose = (ImageView) v.findViewById(R.id.menu_close_btn);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {}
            });*/
        }
    }

    class DrawerViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;
        public DrawerViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            title.setTextColor(menuTxtColor);
            icon = (ImageView) itemView.findViewById(R.id.iv_icon);
        }
    }
}