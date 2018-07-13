package com.mostafa.fci.flowerapp.Services;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mostafa.fci.flowerapp.Models.StatusOrder;
import com.mostafa.fci.flowerapp.Models.UserInfo;
import com.mostafa.fci.flowerapp.Utillities.Constant;
import com.mostafa.fci.flowerapp.interfaces.FetchDerviryOrders;
import com.mostafa.fci.flowerapp.interfaces.FetchFlowers;
import com.mostafa.fci.flowerapp.interfaces.FetchOrders;
import com.mostafa.fci.flowerapp.interfaces.FetchUser;
import com.mostafa.fci.flowerapp.Models.Flower;
import com.mostafa.fci.flowerapp.Models.Order;
import com.mostafa.fci.flowerapp.Models.RoomDatabase;
import com.mostafa.fci.flowerapp.Models.UserOrder;

import java.util.ArrayList;

public class DBServices {


    // Listener for orders , flowers or user Name
    private static ArrayList<FetchOrders> fetchOrders = new ArrayList<>();
    private static ArrayList<FetchFlowers> fetchFlowers = new ArrayList<>();
    private static ArrayList<FetchUser> fetchUserList = new ArrayList<>();
    private static ArrayList<FetchDerviryOrders> fetchOrdersDerviryList = new ArrayList<>();
    // firebase root nodes and child nodes
    private static DatabaseReference root = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://flowerapp-89831.firebaseio.com/");
    private static DatabaseReference usersChild  = root.child("users");
    private static DatabaseReference flowersChild  = root.child("flowers");
    private static DatabaseReference userChild = usersChild.child(AuthServices.getUid());
    private static DatabaseReference ordersChild = usersChild.child(AuthServices.getUid()).child("orders");

    /**
     *  listener for data change in Firebase Database
     */
    // when orders data change
    private static ValueEventListener ordersDataChange = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ArrayList<Order> ordersList = new ArrayList<>();
            ArrayList<StatusOrder> ordersStatusList = new ArrayList<>();
            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
               Order order = snapshot.getValue(Order.class);

               if (order.isStatus()) {
                   StatusOrder statusOrder = new StatusOrder();
                   statusOrder.setOrderId(order.getId());
                   statusOrder.setPayment(order.getPayment());
                   ordersStatusList.add(statusOrder);
               }else {
                   ordersList.add(order);
               }
            }
            if (fetchOrders.size() > 0) {
                for (FetchOrders dataListener : fetchOrders) {
                    dataListener.onCompleted(ordersList);
                }
            }

            if (fetchOrdersDerviryList.size() > 0 ){
                for (FetchDerviryOrders dataListener : fetchOrdersDerviryList) {
                    dataListener.onCompleted(ordersStatusList);
                }
            }
            // end of if statement

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    // when flowers data change
    private static ValueEventListener flowersDataChange = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ArrayList<Flower> flowersList = new ArrayList<>();
            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                Flower flower = snapshot.getValue(Flower.class);
                flowersList.add(flower);
            }
            if (fetchFlowers.size() > 0) {
                for (FetchFlowers dataListener : fetchFlowers) {
                    dataListener.onCompleted(flowersList);
                }
            }
            // end of if statement

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    // when flowers data change
    private static ValueEventListener userChange = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (fetchUserList.size() > 0) {
                UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                for (FetchUser data : fetchUserList) {
                    if (userInfo != null)
                        data.onCompleted(userInfo);
                }
            }
            // end of if statement

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    /**
     * method for sign i , sign up ,sign out , set listener for flowers data or orders data
     * */
    //
    public static void pushOrder(final Context context , Order order){
        DatabaseReference orderChild = ordersChild.push();
        order.setId(orderChild.getKey());
        orderChild.setValue(order);
        UserOrder userOrder = new UserOrder();
        userOrder.setUserid(AuthServices.getUid());
        userOrder.setOrderid(order.getId());
        userOrder.setOrder(order);
        RoomDatabase.getDatabase(context).daoDatabase().insertOnlySingleOrder(userOrder);

    }

    //remove order from firbase
    public static void removeOrder(Order order) {
        ordersChild.child(order.getId()).removeValue();
    }
        // set new User in firebase database
    public static void pushUser(String phone,String name){
        usersChild.child(AuthServices.getUid()).child("phone").setValue(phone);
        usersChild.child(AuthServices.getUid()).child("name").setValue(name);
        usersChild.child(AuthServices.getUid()).child("address").setValue("No ADDRESS");
        // set device token
        String recent_token = FirebaseInstanceId.getInstance().getToken();
        Constant.device_token = recent_token;
        if (Constant.device_token != "")
            usersChild.child(AuthServices.getUid()).child("device_token").setValue(Constant.device_token);
    }

    // set device token to database
    public static  void setDeviceTokenSignIn(){
        String recent_token = FirebaseInstanceId.getInstance().getToken();
        Constant.device_token = recent_token;
        if (Constant.device_token != "")
            usersChild.child(AuthServices.getUid()).child("device_token").setValue(Constant.device_token);
    }

    // set device token to database
    public static  void setDeviceToken(){
        if (Constant.device_token != "")
            usersChild.child(AuthServices.getUid()).child("device_token").setValue(Constant.device_token);
    }
    // set address in firebase database
    public static void pushAddress(String address){
        usersChild.child(AuthServices.getUid()).child("address").setValue(address);
    }
    // listener for flowers data
    public static void setOnFetchFlowers(FetchFlowers fetchData){
        flowersChild.addValueEventListener(flowersDataChange);
        fetchFlowers.add(fetchData);
    }

    // get user name
    public static void getUserName(FetchUser fetchUser){
        userChild.addValueEventListener(userChange);
        fetchUserList.add(fetchUser);
    }
    // listener for orders data
    public static void setOnFetchOrders(FetchOrders fetchData){
        ordersChild.addValueEventListener(ordersDataChange);
        fetchOrders.add(fetchData);
    }
    // listener for orders Derviry data
    public static void setOnFetchOrdersDerviry(FetchDerviryOrders fetchData){
        ordersChild.addValueEventListener(ordersDataChange);
        fetchOrdersDerviryList.add(fetchData);
    }
}
