package com.mostafa.fci.flowerapp.Models;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;

public class OrderConverter {
    @TypeConverter
    public Order storedStringToOrder(String value) {
        Gson json = new Gson();
        Order order = json.fromJson(value,Order.class);
        return order;
    }

    @TypeConverter
    public String orderToString(Order order) {
        Gson json = new Gson();
        return json.toJson(order);
    }
}