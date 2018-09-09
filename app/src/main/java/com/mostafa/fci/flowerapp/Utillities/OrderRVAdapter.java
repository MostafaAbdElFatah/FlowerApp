package com.mostafa.fci.flowerapp.Utillities;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mostafa.fci.flowerapp.Models.Order;
import com.mostafa.fci.flowerapp.Models.RoomDatabase;
import com.mostafa.fci.flowerapp.Models.UserOrder;
import com.mostafa.fci.flowerapp.R;
import com.mostafa.fci.flowerapp.Services.AuthServices;
import com.mostafa.fci.flowerapp.Services.DBServices;
import com.mostafa.fci.flowerapp.interfaces.NotifyDataChanged;
import com.mostafa.fci.flowerapp.interfaces.OnItemClickListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrderRVAdapter extends RecyclerView.Adapter<OrderRVAdapter.RVHolder> {


    private Context context;
    private ArrayList<Order> arrayList;
    private NotifyDataChanged dataChanged;
    private OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            showDialog(arrayList.get(position));
        }
    };

    public OrderRVAdapter(Context context, ArrayList<Order> arrayList , NotifyDataChanged dataChanged) {
        this.arrayList = arrayList;
        this.context = context;
        this.dataChanged = dataChanged;
    }


    @Override
    @NonNull
    public OrderRVAdapter.RVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        OrderRVAdapter.RVHolder holder;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_child_recyclerview, parent, false);
        holder = new OrderRVAdapter.RVHolder(view,this.listener);
        return holder;

    }


    @Override
    public void onBindViewHolder(@NonNull OrderRVAdapter.RVHolder holder, final int position) {
        final Order order;
        order = arrayList.get(position);
        holder.orderName.setText(order.getFlowerName());

        DecimalFormat twoDForm = new DecimalFormat("#.##");
        double orderPrice = Double.valueOf(twoDForm.format( order.getTotalPrice()));

        holder.orderPrice.setText( orderPrice  + "$");
        holder.orderQuantity.setText( "Quantity : "+ order.getQuantity() );
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(order);
            }
        });
    }

    public void removeItemPosition(Order order){
        int index = arrayList.indexOf(order);
        arrayList.remove(index);
        notifyItemRemoved(index);
        UserOrder userOrder = new UserOrder();
        userOrder.setOrderid(order.getId());
        userOrder.setOrder(order);
        userOrder.setUserid(AuthServices.getUid());
        RoomDatabase.getDatabase(context).daoDatabase().deleteOrder(userOrder);
        DBServices.removeOrder(order);
        if (dataChanged !=null)
            dataChanged.onDataChange();
    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    private void showDialog(final Order order){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("DELETE ORDER")
                .setMessage("Do you really want to delete Order")
                .setIcon(R.drawable.ic_delete_red_24dp);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeItemPosition(order);
            }
        });
        builder.setNegativeButton("No",null);
        builder.show();
    }
    /**
     * RecyclerView.ViewHolder
     **/

    public static class RVHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        ImageButton imageButton;
        TextView orderName, orderPrice, orderQuantity;
        OnItemClickListener listener;
        public RVHolder(View itemView,OnItemClickListener clickListener){
            super(itemView);
            this.listener = clickListener;
            orderName     = itemView.findViewById(R.id.orderNameChildItem);
            orderPrice    = itemView.findViewById(R.id.orderTotalPriceChildItem);
            imageButton   = itemView.findViewById(R.id.redBtn);
            orderQuantity = itemView.findViewById(R.id.orderQuantityChildItem);
        }

        @Override
        public boolean onLongClick(View v) {
            if (this.listener != null)
                this.listener.onItemClick(v,this.getLayoutPosition());
            return true;
        }
    }
}


