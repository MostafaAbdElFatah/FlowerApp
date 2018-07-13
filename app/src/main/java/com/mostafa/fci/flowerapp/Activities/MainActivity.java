package com.mostafa.fci.flowerapp.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.mostafa.fci.flowerapp.R;
import com.mostafa.fci.flowerapp.Services.AuthServices;
import com.mostafa.fci.flowerapp.Services.DBServices;
import com.mostafa.fci.flowerapp.interfaces.AuthStateChanged;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button signUpBtn , signInBtn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        textView = findViewById(R.id.textView);
        signInBtn = findViewById(R.id.signInBtn);
        signUpBtn = findViewById(R.id.signUpBtn);

        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");
        textView.setTypeface(typeface);
        signUpBtn.setTypeface(typeface);
        signInBtn.setTypeface(typeface);


        /**
         * authentication
         * **/

        AuthServices.setOnAuthStateChanged(new AuthStateChanged() {
            @Override
            public void onAuthStateChanged() {

                DBServices.setDeviceToken();
                //intent the user account
                if (AuthServices.isAuth()) {
                    Intent page = new Intent(MainActivity.this, FlowerActivity.class);
                    startActivity(page);
                }
            }
        });

    }

    public void singInBtnClicked(View view) {
        startActivity(new Intent(MainActivity.this,SignInActivity.class));
    }
    
    public void singUpBtnClicked(View view) {
        startActivity(new Intent(MainActivity.this,SignUpActivity.class));
    }


}
