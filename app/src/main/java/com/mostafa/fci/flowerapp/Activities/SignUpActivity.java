package com.mostafa.fci.flowerapp.Activities;

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


public class SignUpActivity extends AppCompatActivity {


    Button signUpBtn;
    EditText emailEditText , nameEditText , phoneEditText , passEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailEditText = findViewById(R.id.emailSignUpEditText);
        nameEditText  = findViewById(R.id.nameSignUpEditText);
        phoneEditText = findViewById(R.id.phoneSignUpEditText);
        passEditText  = findViewById(R.id.passSignUpEditText);
        signUpBtn     = findViewById(R.id.signUpActivityBtn);

        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");
        signUpBtn.setTypeface(typeface);

        AuthServices.setOnAuthStateChanged(new AuthStateChanged() {
            @Override
            public void onAuthStateChanged() {
                if (AuthServices.isAuth()) {
                    Intent page = new Intent(SignUpActivity.this, FlowerActivity.class);
                    startActivity(page);
                }
            }
        });




    }


    public void signUpBtnClicked(View view) {
        String email = emailEditText.getText().toString().trim();
        String pass  = passEditText.getText().toString();
        String name = nameEditText.getText().toString().trim();
        String phone  = phoneEditText.getText().toString().trim();
        if (email.equals("") || pass.equals("")){
            Toast.makeText(SignUpActivity.this,"Fill the fields"
                    ,Toast.LENGTH_LONG).show();
            return;
        }
        Map<String,String> hashMap = new HashMap<>();
        hashMap.put("email" ,email);
        hashMap.put("pass"  ,pass);
        hashMap.put("name"  ,name);
        hashMap.put("phone" ,phone);

        if(Network.isOnLine(SignUpActivity.this)) {
            AuthServices.signUpFirebase(SignUpActivity.this, hashMap);
        }else {
            Dialog.show(SignUpActivity.this);
        }
    }

}
