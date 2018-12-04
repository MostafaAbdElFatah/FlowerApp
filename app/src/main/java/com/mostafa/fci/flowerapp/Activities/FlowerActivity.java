package com.mostafa.fci.flowerapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mostafa.fci.flowerapp.Models.Dialog;
import com.mostafa.fci.flowerapp.Models.UserInfo;
import com.mostafa.fci.flowerapp.interfaces.FetchFlowers;
import com.mostafa.fci.flowerapp.interfaces.FetchUser;
import com.mostafa.fci.flowerapp.Models.Flower;
import com.mostafa.fci.flowerapp.Models.Network;
import com.mostafa.fci.flowerapp.Models.RoomDatabase;
import com.mostafa.fci.flowerapp.R;
import com.mostafa.fci.flowerapp.Services.AuthServices;
import com.mostafa.fci.flowerapp.Services.DBServices;
import com.mostafa.fci.flowerapp.Services.URLManager;
import com.mostafa.fci.flowerapp.Services.JSONParser;
import com.mostafa.fci.flowerapp.interfaces.OnItemClickListener;
import com.mostafa.fci.flowerapp.Utillities.FlowerRVAdapter;
import com.mostafa.fci.flowerapp.Utillities.URLs;
import java.util.ArrayList;
import java.util.List;

public class FlowerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    ProgressBar progressBar;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Flower> flowersList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower);


        Toolbar toolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progressBar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        recyclerView = findViewById(R.id.flowerRecyclerViewFlowerActivity);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        View headerView = navigationView.getHeaderView(0);
        final TextView navUsername = headerView.findViewById(R.id.name_nav_header);
        DBServices.getUserName(new FetchUser() {
            @Override
            public void onCompleted(UserInfo user) {
                if (user.getName() != "" || user.getName() != null) {
                    navUsername.setText(user.getName());
                }
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        } else {
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        }
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new FlowerRVAdapter(FlowerActivity.this,flowersList, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(FlowerActivity.this , DetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("flower", flowersList.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        if(!Network.isOnLine(FlowerActivity.this)) {
            Dialog.show(FlowerActivity.this);
        }
        this.getData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.getData();
    }

    protected void getData() {
        if(!Network.isOnLine(FlowerActivity.this)) {
            List<Flower> list = RoomDatabase.getDatabase(FlowerActivity.this)
                    .daoDatabase().fetchAllFlowers();
            if (list.size() > 0) {
                flowersList.clear();
                flowersList.addAll(list);
                adapter.notifyDataSetChanged();;
            }
        }else{
            progressBar.setVisibility(View.VISIBLE);
            DBServices.setOnFetchFlowers(new FetchFlowers() {
                @Override
                public void onCompleted(ArrayList<Flower> flowers) {
                    if (flowers != null && flowers.size() > 0) {
                        flowersList.clear();
                        flowersList.addAll(flowers);
                        RoomDatabase.getDatabase(FlowerActivity.this).daoDatabase().deleteAllFlower();
                        RoomDatabase.getDatabase(FlowerActivity.this)
                                .daoDatabase().insertMultipleFlowers(flowers);
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
        // end if statement
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Home) {
            // Handle the camera action
            AuthServices.signOut();
            Intent intent = new Intent(FlowerActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_cart) {
            Intent intent = new Intent(FlowerActivity.this,CartActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_orders) {
            Intent intent = new Intent(FlowerActivity.this,OrderStatusActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_signOut) {
            AuthServices.signOut();
            Intent intent = new Intent(FlowerActivity.this,SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            AuthServices.signOut();
            Intent intent = new Intent(FlowerActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

    }


}
