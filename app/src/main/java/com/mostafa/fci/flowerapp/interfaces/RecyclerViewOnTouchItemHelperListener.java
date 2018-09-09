package com.mostafa.fci.flowerapp.interfaces;

import android.support.v7.widget.RecyclerView;

public interface RecyclerViewOnTouchItemHelperListener {
    void onSwiped(RecyclerView.ViewHolder viewHolder,int direction , int position);
}
