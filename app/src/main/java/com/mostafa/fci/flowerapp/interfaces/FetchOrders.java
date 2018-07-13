package com.mostafa.fci.flowerapp.interfaces;

import android.widget.ArrayAdapter;

import com.mostafa.fci.flowerapp.Models.Order;

import java.util.ArrayList;

public interface FetchOrders {
    void onCompleted(ArrayList<Order> orders);
}
