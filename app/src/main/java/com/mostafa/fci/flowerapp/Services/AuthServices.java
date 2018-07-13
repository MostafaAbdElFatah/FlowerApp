package com.mostafa.fci.flowerapp.Services;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mostafa.fci.flowerapp.interfaces.AuthStateChanged;

import java.util.ArrayList;
import java.util.Map;

public class AuthServices {

    private static ProgressDialog dialog = null;

    final private static FirebaseAuth auth = FirebaseAuth.getInstance();

    private static ArrayList<AuthStateChanged> onAuthStateChangedList = new ArrayList<>();

    private static FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){
                    DBServices.setDeviceTokenSignIn();
                    for (AuthStateChanged state: onAuthStateChangedList) {
                        state.onAuthStateChanged();
                    }
                    if (dialog !=null){
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }

                }

            }
        };

    
    /**
     *      Sign Up to firebase
     * */
    public static void signUpFirebase(final Context context , Map<String, String> hashMap){

        dialog = new ProgressDialog(context);
        dialog.setMessage("Sign Up ......, please wait.");
        dialog.show();

        /**
         * authentication
         * **/

        // [START create_user_with_email]
        String email = hashMap.get("email");
        String pass  = hashMap.get("pass");
        final String phone = hashMap.get("phone");
        final String name = hashMap.get("name");

        auth.createUserWithEmailAndPassword(email , pass)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = auth.getCurrentUser();
                            DBServices.pushUser(phone,name);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context , "Authentication failed.",
                                    Toast.LENGTH_LONG).show();
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }

                        // [END_EXCLUDE]
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context , "ERROR:" + e.toString(),
                        Toast.LENGTH_LONG).show();
            }
        });
        // [END create_user_with_email]
    }

    /**
     *      Sign In
     * */
    public static void signInFirebase(final Context context , Map<String, String> hashMap) {

        dialog = new ProgressDialog(context);
        dialog.setMessage("Sign In ......, please wait.");
        dialog.show();
        /**
         * authentication
         * **/
        String email = hashMap.get("email");
        String pass  = hashMap.get("pass");

        auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = auth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }

                        // [END_EXCLUDE]
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,"ERROR:"+e.toString(),Toast.LENGTH_LONG).show();
            }
        });
        // [END create_user_with_email]
    }



    public static void setOnAuthStateChanged(AuthStateChanged authStateChanged){
        auth.addAuthStateListener(authStateListener);
        onAuthStateChangedList.add(authStateChanged);
    }

    public static boolean isAuth (){
        if ( auth.getCurrentUser() == null )
            return false;
        else
            return true;
    }
    public static String getUid(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String id = user.getUid();
        return id;
    }

    public static void signOut(){
        auth.signOut();
    }

}
