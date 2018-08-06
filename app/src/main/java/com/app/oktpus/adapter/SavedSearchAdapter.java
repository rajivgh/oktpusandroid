package com.app.oktpus.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;

import java.util.List;

import com.app.oktpus.model.SavedSearchItem;
import com.app.oktpus.constants.ReceiveNotification;
import com.app.oktpus.constants.SavedSearchOperations;
import com.app.oktpus.controller.AppController;
import com.app.oktpus.listener.OnClickSavedSearchItem;
import com.app.oktpus.R;

/**
 * Created by Gyandeep on 9/2/17.
 */

public class SavedSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<SavedSearchItem> itemsList;
    private Context context;
    public OnClickSavedSearchItem onClickListener;
    private final int VIEW_TYPE_ITEM = 1;
    private final int VIEW_TYPE_LOADING = 0;
    private final int VIEW_TYPE_BLANK = 2;
    private final int VIEW_TYPE_NO_RESULTS = 3;

    public SavedSearchAdapter(Context context, List<SavedSearchItem> itemsList,
                              OnClickSavedSearchItem onClickListener) {
        this.context = context;
        this.itemsList = itemsList;
        this.onClickListener = onClickListener;
    }

    public class NoResultsViewHolder extends RecyclerView.ViewHolder {
        public NoResultsViewHolder(View v) {
            super(v);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public View oktSpinner;
        public TextView oktLoadingText;
        private RelativeLayout loadingLayout;
        public LoadingViewHolder(View v) {
            super(v);
            oktSpinner = (View) v.findViewById(R.id.okt_spinner);
            oktLoadingText = (TextView) v.findViewById(R.id.okt_loading_txt);
            loadingLayout = (RelativeLayout) v.findViewById(R.id.loading_bar);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) loadingLayout.getLayoutParams();
            params.height = 400;
            loadingLayout.setLayoutParams(params);
            oktLoadingText.setVisibility(View.GONE);
        }
    }

    public class BlankViewHolder extends RecyclerView.ViewHolder {
        public BlankViewHolder(View v) {
            super(v);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public AlertDialog.Builder builder;
        public AlertDialog dialog;
        private TextView groupHeaderTitle, groupHeaderTitleDesc, launchSearch, deleteSearch, tvTitle, tvDesc;
        private ImageView indicator, btnClose, ivEdit;
        private SwitchButton receiveNotifSwitch;
        private LinearLayout rowLayout;

        public ItemViewHolder(View view) {
            super(view);
            groupHeaderTitle = (TextView) view.findViewById(R.id.grp_header_title);
            groupHeaderTitleDesc = (TextView) view.findViewById(R.id.grp_header_title_desc);
            indicator = (ImageView) view.findViewById(R.id.grp_indicator);
            rowLayout = (LinearLayout) view.findViewById(R.id.row_layout);
        }
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    private boolean isLoadingBarEnabled = true;
    public void enableLoadingBar(boolean isEnabled) {
        this.isLoadingBarEnabled = isEnabled;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0 && isLoadingBarEnabled) {
            return VIEW_TYPE_LOADING;
        }
        else if(position == 0 && itemsList.size() == 0)
            return VIEW_TYPE_NO_RESULTS;
        else
            return VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if(viewType == VIEW_TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ex_list_group_saved_search, parent, false);
            vh = new SavedSearchAdapter.ItemViewHolder(itemView);
            return vh;
        }
        else if (viewType == VIEW_TYPE_LOADING){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_bar_layout, parent, false);
            vh = new SavedSearchAdapter.LoadingViewHolder(v);
            return vh;
        }
        else if(viewType == VIEW_TYPE_NO_RESULTS) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_no_items, parent, false);
            vh = new SavedSearchAdapter.NoResultsViewHolder(v);
            return vh;
        }
        else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.blank_view, parent, false);
            vh = new SavedSearchAdapter.BlankViewHolder(v);
            return vh;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof NotificationsListAdapter.LoadingViewHolder){
            AnimationDrawable frameAnimation = (AnimationDrawable) ((NotificationsListAdapter.LoadingViewHolder)holder).oktSpinner.getBackground();
            frameAnimation.start();
        } else if(itemsList.size() > 0 && (position - 1) < (itemsList.size()) && holder instanceof ItemViewHolder) {
            final ItemViewHolder ivh = (ItemViewHolder) holder;
            final SavedSearchItem item = itemsList.get(position);
            ivh.groupHeaderTitle.setText(item.getTitleHeader());
            ivh.groupHeaderTitle.setTypeface(AppController.getFontType(context), Typeface.NORMAL);
            ivh.groupHeaderTitleDesc.setText(item.getSearchValues());
            ivh.indicator.setImageResource(R.drawable.group_down);

            ivh.rowLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ivh.builder = null;
                    ivh.builder = new AlertDialog.Builder(context);
                    View childView = LayoutInflater.from(context).inflate(R.layout.ex_list_item_saved_search, null);
                    ivh.builder.setView(childView);
                    ivh.dialog = ivh.builder.create();
                    childViewInflater(ivh, childView, position, item);
                }
            });
        }
    }

    private void childViewInflater(final ItemViewHolder holder, final View childView, final int position, SavedSearchItem item) {
        holder.receiveNotifSwitch = (SwitchButton) childView.findViewById(R.id.receive_notif_switch);
        holder.launchSearch = (TextView) childView.findViewById(R.id.launch_link);
        holder.deleteSearch = (TextView) childView.findViewById(R.id.delete_link);
        holder.btnClose = (ImageView) childView.findViewById(R.id.btn_close);
        //holder.ivEdit = (ImageView) childView.findViewById(R.id.iv_edit);
        holder.tvTitle = (TextView) childView.findViewById(R.id.tv_ss_dialog_title);
        holder.tvDesc = (TextView) childView.findViewById(R.id.tv_ss_dialog_title_content);
        holder.btnClose.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.dialog.dismiss();
            }
        });

        holder.tvTitle.setText(item.getTitleHeader());
        holder.tvTitle.setTypeface(AppController.getFontType(context), Typeface.NORMAL);
        holder.tvDesc.setText(item.getSearchValues());

        final ReceiveNotification status = ReceiveNotification.fromValue(item.getStatusID());
        switch(status) {
            case RECEIVE_NOTIF_DISABLED:
                break;
            case RECEIVE_NOTIF_ENABLED_HIDE:
                turnOff(holder);
                break;
            case RECEIVE_NOTIF_ENABLED_SHOW:
                turnOn(holder);
                break;
        }

        holder.receiveNotifSwitch.setOnTouchListener(new SwitchButton.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        onCheckEvent(holder, position);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        holder.launchSearch.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onItemClick(itemsList.get(position).getSearchID(),
                        itemsList.get(position).getStatusID(), SavedSearchOperations.LAUNCH_SEARCH.getValue(),
                        itemsList.get(position).getSerializedValues());
                holder.dialog.dismiss();
            }
        });

        holder.deleteSearch.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialog);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                dialog.setCancelable(false);

                TextView msgText = (TextView) dialog.findViewById(R.id.txt_dialogcontent);
                msgText.setText(Html.fromHtml("Are you sure you want to remove this search?"+ "<br>"+"You will stop receiving email alerts for this search."));

                dialog.show();

                ImageView imageViewClose =(ImageView) dialog.findViewById(R.id.imageView_close);
                imageViewClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                Button btnOk  =(Button) dialog.findViewById(R.id.btnok);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickListener.onItemClick(itemsList.get(position).getSearchID(),
                                itemsList.get(position).getStatusID(), SavedSearchOperations.DELETE_SEARCH.getValue(),
                                itemsList.get(position).getSerializedValues());

                        if(itemsList.contains(itemsList.get(position))) {
                            itemsList.remove(itemsList.get(position));
                            /*notifyItemRemoved(position);
                            notifyItemRangeInserted(position, getItemCount());
                            //*/notifyDataSetChanged();
                        }
                        dialog.dismiss();
                        holder.dialog.dismiss();
                    }
                });

                Button btnCancel = (Button) dialog.findViewById(R.id.btncancel);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        holder.dialog.show();
    }

    public void onCheckEvent(final ItemViewHolder holder, final int position) {
        final Dialog dialog = new Dialog(context);
        final boolean isBtnChecked = holder.receiveNotifSwitch.isChecked();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        ImageView imageViewClose =(ImageView) dialog.findViewById(R.id.imageView_close);
        TextView msgText = (TextView) dialog.findViewById(R.id.txt_dialogcontent);
        Button btnCancel = (Button) dialog.findViewById(R.id.btncancel);
        Button btnOk  =(Button) dialog.findViewById(R.id.btnok);

        if(isBtnChecked) {
            msgText.setText("Stop receiving email notification?");
        }else {
            msgText.setText("Start receiving email notification?");
        }

        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1 == Disable, 2 == Enable
                int sId = 1;
                if(!isBtnChecked) {
                    turnOn(holder);
                    sId = 2;
                }
                else {
                    turnOff(holder);
                    sId = 1;
                }

                itemsList.get(position).setStatusID(sId);
                onClickListener.onItemClick(itemsList.get(position).getSearchID(),
                        itemsList.get(position).getStatusID(),
                        SavedSearchOperations.RECEIVE_NOTIF.getValue(),
                        itemsList.get(position).getSerializedValues());

                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void turnOn(ItemViewHolder holder) {
        holder.receiveNotifSwitch.setChecked(true);
        holder.receiveNotifSwitch.setThumbColorRes(R.color.switchActiveColor);
    }

    public void turnOff(ItemViewHolder holder) {
        holder.receiveNotifSwitch.setChecked(false);
        holder.receiveNotifSwitch.setThumbColorRes(R.color.white);
    }
}