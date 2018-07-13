package com.mostafa.fci.flowerapp.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;


// this class to save order in database

@Entity
public class UserOrder {
    @PrimaryKey @NonNull
    String orderid;
    @ColumnInfo
    String userid;
    @TypeConverters(OrderConverter.class)
    Order order;


    public UserOrder() {
    }
    @Ignore
    public UserOrder(@NonNull String orderid, String userid, Order order) {
        this.orderid = orderid;
        this.userid = userid;
        this.order = order;
    }

    @NonNull
    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(@NonNull String orderid) {
        this.orderid = orderid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(@NonNull String userid) {
        this.userid = userid;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
