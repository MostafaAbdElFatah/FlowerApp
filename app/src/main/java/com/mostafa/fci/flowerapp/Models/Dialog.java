package com.mostafa.fci.flowerapp.Models;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class Dialog {

    private  static AlertDialog.Builder builder;

    public static void show(final Context context){
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Can't Connect to  Network");
        builder.setMessage("Please Connect to Internet , Network to get Flowers List .....");
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }

        });
        builder.setPositiveButton("Setting", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Open Setting Phone Activity
                 context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
            }

        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
