package com.mostafa.fci.flowerapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mostafa.fci.flowerapp.Models.Dialog;
import com.mostafa.fci.flowerapp.Models.Network;
import com.mostafa.fci.flowerapp.R;
import com.mostafa.fci.flowerapp.Services.AuthServices;
import com.mostafa.fci.flowerapp.Services.DBServices;
import com.mostafa.fci.flowerapp.interfaces.AuthStateChanged;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    Button signInBtn ;
    private ProgressDialog dialog;
    EditText emailEditText , passEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");
        emailEditText = findViewById(R.id.emailSignInEditText);
        passEditText  = findViewById(R.id.passSignInEditText);
        signInBtn = findViewById(R.id.signInActivityBtn);
        signInBtn.setTypeface(typeface);
        AuthServices.setOnAuthStateChanged(new AuthStateChanged() {
            @Override
            public void onAuthStateChanged() {
                if (AuthServices.isAuth()) {
                    Intent page = new Intent(SignInActivity.this, FlowerActivity.class);
                    startActivity(page);
                }
            }
        });
    }

    public void singInBtnClicked(View view) {
        String email = emailEditText.getText().toString().trim();
        String pass  = passEditText.getText().toString();
        if (email.equals("") || pass.equals("")){
            Toast.makeText(SignInActivity.this,"Write Email or Password"
                    ,Toast.LENGTH_LONG).show();
            return;
        }
        Map<String,String> hashMap = new HashMap<>();
        hashMap.put("email",email);
        hashMap.put("pass",pass);
        if(Network.isOnLine(SignInActivity.this)) {
            AuthServices.signInFirebase(SignInActivity.this, hashMap);
        }else {
            Dialog.show(SignInActivity.this);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SignInActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
