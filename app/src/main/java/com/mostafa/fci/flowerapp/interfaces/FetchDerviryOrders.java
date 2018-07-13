package com.mostafa.fci.flowerapp.interfaces;
import com.mostafa.fci.flowerapp.Models.StatusOrder;

import java.util.ArrayList;

public interface FetchDerviryOrders {
    void onCompleted(ArrayList<StatusOrder> orders);

}
