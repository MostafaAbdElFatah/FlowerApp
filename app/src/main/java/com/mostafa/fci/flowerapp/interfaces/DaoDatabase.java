package com.mostafa.fci.flowerapp.interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.mostafa.fci.flowerapp.Models.Flower;
import com.mostafa.fci.flowerapp.Models.UserOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FCI on 2018-06-22.
 */
@Dao
public interface DaoDatabase {

    @Insert
    void insertOnlySingleFlower(Flower flower);
    @Insert
    void insertMultipleFlowers(List<Flower> flowersList);
    @Query ("SELECT * FROM Flower WHERE productId = :flowerId")
    Flower fetchOneBooksbyFlowerId(int flowerId);
    @Query ("SELECT * FROM Flower")
    List<Flower> fetchAllFlowers();
    @Update
    void updateFlower(Flower flower);
    @Delete
    void deleteBook(Flower flower);
    @Query("DELETE FROM Flower")
    void deleteAllFlower();

    /**
     * Order methods
     * */

    @Insert
    void insertOnlySingleOrder(UserOrder userOrder);
    @Insert
    void insertMultipleOrders(List<UserOrder> userOrders);
    @Query ("SELECT * FROM `UserOrder` WHERE userid = :id")
    UserOrder fetchOneBooksbyOrderId(String id);
    @Query ("SELECT * FROM `UserOrder`")
    List<UserOrder> fetchAllOrders();
    @Update
    void updateOrder(UserOrder userOrder);
    @Delete
    void deleteOrder(UserOrder userOrder);
    @Query("DELETE FROM `UserOrder`")
    void deleteAllOrder();

}
