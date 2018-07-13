package com.mostafa.fci.flowerapp.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.mostafa.fci.flowerapp.Models.UserInfo;
import com.mostafa.fci.flowerapp.interfaces.FetchDerviryOrders;
import com.mostafa.fci.flowerapp.interfaces.FetchUser;
import com.mostafa.fci.flowerapp.Models.StatusOrder;
import com.mostafa.fci.flowerapp.R;
import com.mostafa.fci.flowerapp.Services.AuthServices;
import com.mostafa.fci.flowerapp.Services.DBServices;
import com.mostafa.fci.flowerapp.Utillities.StatusRVAdapter;
import java.util.ArrayList;

public class OrderStatusActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    UserInfo userInfo=null;
    ArrayList<StatusOrder> ordersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        // init views
        Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.orderRecyclerViewCartActivity);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        final TextView navUsername = headerView.findViewById(R.id.name_nav_header);
        DBServices.getUserName(new FetchUser() {
            @Override
            public void onCompleted(UserInfo user) {
                userInfo = user;
                if (user.getName() != "") {

                    navUsername.setText(user.getName());
                    if (ordersList.size() > 0)
                        updateUI();

                }
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layoutCartActivity);
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
        adapter = new StatusRVAdapter(OrderStatusActivity.this,ordersList);
        recyclerView.setAdapter(adapter);

        DBServices.setOnFetchOrdersDerviry(new FetchDerviryOrders() {
            @Override
            public void onCompleted(ArrayList<StatusOrder> orders) {
                if (orders.size() > 0) {
                    ordersList.clear();
                    ordersList.addAll(orders);
                    if (userInfo != null)
                        updateUI();
                }
            }
        });


    }

    private void updateUI() {

        for (StatusOrder statusOrder :ordersList) {
            if (userInfo != null)
            statusOrder.setUserPhone(userInfo.getPhone());
            statusOrder.setUserAddress(userInfo.getAddress());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layoutCartActivity);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
            AuthServices.signOut();
            Intent intent = new Intent(OrderStatusActivity.this,FlowerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Home) {
            // Handle the camera action
            Intent intent = new Intent(OrderStatusActivity.this,FlowerActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_cart) {
            Intent intent = new Intent(OrderStatusActivity.this,CartActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_orders) {

        } else if (id == R.id.nav_signOut) {
            AuthServices.signOut();
            Intent intent = new Intent(OrderStatusActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layoutCartActivity);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
