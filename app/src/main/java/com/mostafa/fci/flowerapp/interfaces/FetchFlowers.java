package com.mostafa.fci.flowerapp.interfaces;

import com.mostafa.fci.flowerapp.Models.Flower;

import java.util.ArrayList;

public interface FetchFlowers  {
    void onCompleted(ArrayList<Flower> flowersList);
}
