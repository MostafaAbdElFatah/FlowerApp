package com.mostafa.fci.flowerapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mostafa.fci.flowerapp.Models.Dialog;
import com.mostafa.fci.flowerapp.Models.UserInfo;
import com.mostafa.fci.flowerapp.interfaces.FetchOrders;
import com.mostafa.fci.flowerapp.interfaces.FetchUser;
import com.mostafa.fci.flowerapp.Models.Flower;
import com.mostafa.fci.flowerapp.Models.Network;
import com.mostafa.fci.flowerapp.Models.Order;
import com.mostafa.fci.flowerapp.Models.RoomDatabase;
import com.mostafa.fci.flowerapp.Models.UserOrder;
import com.mostafa.fci.flowerapp.R;
import com.mostafa.fci.flowerapp.Services.AuthServices;
import com.mostafa.fci.flowerapp.Services.DBServices;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    Toolbar toolbar;
    Flower flower = null;
    ImageView flowerImageView;
    AlertDialog dialog = null;
    private String payment = "";
    ElegantNumberButton quantityElNumBtn;
    CollapsingToolbarLayout collapsingTbLayout;
    private StorageReference storageReference;
    TextView  flowerCategory , flowerPrice ,flowerInstructions , titleToolBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        toolbar = findViewById(R.id.toolbarDetailsActivity);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout_DetailsActivity);
        NavigationView navigationView = findViewById(R.id.nav_view_DetailsAcivity);
        titleToolBar = findViewById(R.id.titleToobBar);
        quantityElNumBtn = findViewById(R.id.quantityBtn);
        flowerPrice = findViewById(R.id.flowerPriceDetailsActivity);
        flowerCategory = findViewById(R.id.flowerCategoryDetailsActivity);
        flowerImageView = findViewById(R.id.flowerImageDetailActivity);
        flowerInstructions = findViewById(R.id.flowerInstructionsDetailsActivity);
        collapsingTbLayout = findViewById(R.id.collapsingToolbar);

        storageReference = FirebaseStorage.getInstance().getReference();
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

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        } else {
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        }

        navigationView.setNavigationItemSelectedListener(this);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Intent intent = getIntent();
        flower = (Flower) intent.getExtras().getSerializable("flower");
        if (flower != null){
            titleToolBar.setText(flower.getName());

            flowerPrice.setText(flower.getPrice() + "$" );
            flowerCategory.setText(flower.getCategory());
            flowerInstructions.setText(flower.getInstructions());
            if(Network.isOnLine(DetailsActivity.this)) {
                storageReference.child( "flowers/"+flower.getPhoto() )
                        .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get()
                                .load(uri)
                                .placeholder(R.drawable.loading)
                                .into(flowerImageView);
                    }
                });

            }else{
                Dialog.show(DetailsActivity.this);
            }
        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Home) {
            // Handle the camera action
            Intent intent = new Intent(DetailsActivity.this,FlowerActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_cart) {
            Intent intent = new Intent(DetailsActivity.this,CartActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_orders) {
            Intent intent = new Intent(DetailsActivity.this,OrderStatusActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_signOut) {
            AuthServices.signOut();
            Intent intent = new Intent(DetailsActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout_DetailsActivity);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_DetailsActivity);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }



    private void updateUI() {
        Bitmap bitmap = BitmapFactory.decodeByteArray(flower.getImagebyteArray()
                ,0,flower.getImagebyteArray().length);
        flowerImageView.setImageBitmap(bitmap);
    }

    public void addToCartbtnClicked(View view) {
        if (payment != "") {
            Order order = new Order();
            order.setFlowerName(flower.getName());
            order.setPayment(payment);
            order.setQuantity(Integer.parseInt(quantityElNumBtn.getNumber()));
            double totalPrice = order.getQuantity() * flower.getPrice();
            order.setTotalPrice(totalPrice);
            DBServices.pushOrder(DetailsActivity.this, order);
            DBServices.setOnFetchOrders(new FetchOrders() {
                @Override
                public void onCompleted(ArrayList<Order> orders) {
                    if (orders != null && orders.size() > 0)
                        RoomDatabase.getDatabase(DetailsActivity.this).daoDatabase().deleteAllOrder();
                    for (Order order : orders) {
                        RoomDatabase.getDatabase(DetailsActivity.this).daoDatabase()
                                .insertOnlySingleOrder(new UserOrder(order.getId()
                                        , AuthServices.getUid(), order));
                    }
                }
            });
            payment = "" ;
            Toast.makeText(DetailsActivity.this, "Added to Cart",
                    Toast.LENGTH_LONG).show();
        }else
            showDialog();
    }

    int position = -1;
    public void payBtnClicked(View view) {
        final String[] pay = {"PayPal","Credit Card","With delivery"};

        if (dialog != null) {
            if (dialog.isShowing())
                return;
            else
                dialog = null;

        }
        if (dialog == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
            builder.setTitle("Choose your payment method?");
            builder.setSingleChoiceItems(pay, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setPositionPayment(which);

                }
            });
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (getPositionPayment() > -1 && getPositionPayment() < pay.length) {
                        payment = pay[getPositionPayment()];
                        dialog.dismiss();
                    }
                }
            });
            dialog = builder.create();
            dialog.show();
        }

    }

    private void setPositionPayment(int positonPayment){
        this.position = positonPayment;
    }

    private int getPositionPayment(){
        return this.position;
    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
        builder.setTitle("Payment !!!")
                .setMessage("Please Choose Payment Method")
                .setIcon(R.drawable.ic_shopping_cart_black_24dp)
                .setPositiveButton("OK",null)
                .show();
    }

}
