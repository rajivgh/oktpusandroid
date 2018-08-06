package com.app.oktpus.utils;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by Gyandeep on 23/6/17.
 */

public class RecyclerTouchHelper extends ItemTouchHelper.Callback {
    private final ItemTouchHelperAdapter mAdapter;
    private RecyclerView mRecyclerView;
    public RecyclerTouchHelper(ItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        mRecyclerView = recyclerView;
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if(null != mRecyclerView && actionState == ItemTouchHelper.ACTION_STATE_IDLE)
            mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    public interface ItemTouchHelperAdapter{
        boolean onItemMove(int fromPosition, int toPosition);
        void onItemDismiss(int position);
    }
}
