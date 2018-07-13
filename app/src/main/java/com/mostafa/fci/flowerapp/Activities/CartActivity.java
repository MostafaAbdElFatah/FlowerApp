package com.mostafa.fci.flowerapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mostafa.fci.flowerapp.Models.Dialog;
import com.mostafa.fci.flowerapp.Models.UserInfo;
import com.mostafa.fci.flowerapp.interfaces.FetchOrders;
import com.mostafa.fci.flowerapp.interfaces.FetchUser;
import com.mostafa.fci.flowerapp.Models.Network;
import com.mostafa.fci.flowerapp.Models.Order;
import com.mostafa.fci.flowerapp.Models.RoomDatabase;
import com.mostafa.fci.flowerapp.Models.UserOrder;
import com.mostafa.fci.flowerapp.R;
import com.mostafa.fci.flowerapp.Services.AuthServices;
import com.mostafa.fci.flowerapp.Services.DBServices;
import com.mostafa.fci.flowerapp.Utillities.OrderRVAdapter;
import com.mostafa.fci.flowerapp.interfaces.NotifyDataChanged;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button btn;
    TextView totalPriceOrders;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Order> ordersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.orderRecyclerViewCartActivity);
        totalPriceOrders = findViewById(R.id.totalPriceOrdersCartActivity);
        btn = findViewById(R.id.placeAddressCartActivity);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        final TextView navUsername = headerView.findViewById(R.id.name_nav_header);
        DBServices.getUserName(new FetchUser() {
            @Override
            public void onCompleted(UserInfo user) {
                if (user.getName() != "") {

                    navUsername.setText(user.getName());
                }
            }
        });

        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");
        btn.setTypeface(typeface);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layoutCartActivity);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        } else {
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        }
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new OrderRVAdapter(CartActivity.this, ordersList, new NotifyDataChanged() {
            @Override
            public void onDataChange() {
                totalPriceOrders.setText("Total Orders Price:0$");
            }
        });
        recyclerView.setAdapter(adapter);

        DBServices.setOnFetchOrders(new FetchOrders() {
            @Override
            public void onCompleted(ArrayList<Order> orders) {
                if (orders != null && orders.size() > 0) {
                    RoomDatabase.getDatabase(CartActivity.this).daoDatabase().deleteAllOrder();
                    for (Order order : orders) {
                        RoomDatabase.getDatabase(CartActivity.this).daoDatabase()
                                .insertOnlySingleOrder(new UserOrder(order.getId()
                                        , AuthServices.getUid(), order));
                    }
                    getOrdersList();

                }else {
                    RoomDatabase.getDatabase(CartActivity.this).daoDatabase().deleteAllOrder();
                    ordersList.clear();
                    updateDisplay();
                    getOrdersList();
                }
            }
        });

        getOrdersList();

    }

    private void getOrdersList() {
        List<UserOrder> list = RoomDatabase.getDatabase(CartActivity.this)
                .daoDatabase().fetchAllOrders();
        if (list.size() > 0) {
            ordersList.clear();
            for (UserOrder userOrder:list) {
                if (userOrder.getUserid().equals(AuthServices.getUid())) {
                    Order order = userOrder.getOrder();
                    ordersList.add(order);
                }
            }
            updateDisplay();
        }else {
            if(!Network.isOnLine(CartActivity.this)) {
                Dialog.show(CartActivity.this);
            }
        }
    }


    protected void updateDisplay(){
        adapter.notifyDataSetChanged();
        double total = 0;
        for (Order order:ordersList) {
            total += order.getTotalPrice();
        }
        // to set two digit after point
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        double orderPrice = Double.valueOf(twoDForm.format( total));
        totalPriceOrders.setText("Total Orders Price : " + String.valueOf(orderPrice) +"$" );
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layoutCartActivity);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            Intent intent = new Intent(CartActivity.this,FlowerActivity.class);
            startActivity(intent);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Home) {
            // Handle the camera action
            Intent intent = new Intent(CartActivity.this,FlowerActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_cart) {

        } else if (id == R.id.nav_orders) {
            Intent intent = new Intent(CartActivity.this,OrderStatusActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_signOut) {
            AuthServices.signOut();
            Intent intent = new Intent(CartActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layoutCartActivity);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void placeOrderAddressBtnClicked(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);

        final EditText addressEditText = new EditText(CartActivity.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        addressEditText.setLayoutParams(layoutParams);
        builder.setTitle("One More Step!")
                .setMessage("Enter Your Address")
                .setView(addressEditText)
                .setIcon(R.drawable.ic_shopping_cart_black_24dp);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!addressEditText.getText().toString().trim().equals("")) {
                    DBServices.pushAddress(addressEditText.getText().toString().trim());
                    Toast.makeText(CartActivity.this,"Thank you ,Place address"
                            ,Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("No",null);
        builder.show();
    }

    public  void removeOrderBtnClicked(View view){

    }
}
