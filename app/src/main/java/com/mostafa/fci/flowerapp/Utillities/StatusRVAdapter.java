package com.mostafa.fci.flowerapp.Utillities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mostafa.fci.flowerapp.Models.StatusOrder;
import com.mostafa.fci.flowerapp.R;

import java.util.ArrayList;

public class StatusRVAdapter extends RecyclerView.Adapter<StatusRVAdapter.RVHolder> {


    Context context;
    ArrayList<StatusOrder> arrayList;

    public StatusRVAdapter(Context context, ArrayList<StatusOrder> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @Override
    public StatusRVAdapter.RVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        StatusRVAdapter.RVHolder holder;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.status_child_recyclerview, parent, false);
        holder = new StatusRVAdapter.RVHolder(view);
        return holder;

    }


    @Override
    public void onBindViewHolder(StatusRVAdapter.RVHolder holder, int position) {
        StatusOrder  statusOrder;
        statusOrder = arrayList.get(position);
        holder.orderId.setText(statusOrder.getOrderId());
        holder.payment.setText(statusOrder.getPayment());
        holder.phone.setText(statusOrder.getUserPhone());
        holder.address.setText(statusOrder.getUserAddress());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    /**
     * RecyclerView.ViewHolder
     **/

    public static class RVHolder extends RecyclerView.ViewHolder {

        TextView orderId , payment, phone, address;

        public RVHolder(View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderidStatusChildItem);
            payment = itemView.findViewById(R.id.paymentStatusChildItem);
            phone = itemView.findViewById(R.id.phoneStatusChildItem);
            address = itemView.findViewById(R.id.addressStatusChildItem);
        }

    }
}


