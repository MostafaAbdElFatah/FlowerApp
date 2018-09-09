package com.mostafa.fci.flowerapp.Utillities;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.mostafa.fci.flowerapp.interfaces.RecyclerViewOnTouchItemHelperListener;

public class RecyclerViewOnTouchItemHelper extends ItemTouchHelper.SimpleCallback {

    private RecyclerViewOnTouchItemHelperListener listener;

    public RecyclerViewOnTouchItemHelper( RecyclerViewOnTouchItemHelperListener listener) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.listener = listener ;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (listener != null)
            listener.onSwiped(viewHolder,direction,viewHolder.getAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }
}
