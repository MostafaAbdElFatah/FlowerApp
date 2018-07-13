package com.mostafa.fci.flowerapp.Utillities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mostafa.fci.flowerapp.Activities.SignInActivity;
import com.mostafa.fci.flowerapp.Models.Dialog;
import com.mostafa.fci.flowerapp.Models.Flower;
import com.mostafa.fci.flowerapp.Models.Network;
import com.mostafa.fci.flowerapp.interfaces.OnItemClickListener;
import com.mostafa.fci.flowerapp.Models.RoomDatabase;
import com.mostafa.fci.flowerapp.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class FlowerRVAdapter extends RecyclerView.Adapter<FlowerRVAdapter.RVHolder> {


    Context context;
    ArrayList<Flower> arrayList;
    OnItemClickListener listener;
    private StorageReference storageReference;

    public FlowerRVAdapter(Context context, ArrayList<Flower> arrayList , OnItemClickListener listener) {
        this.arrayList = arrayList;
        this.listener = listener;
        this.context = context;
        storageReference = FirebaseStorage.getInstance().getReference();

    }



    @Override
    public RVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RVHolder holder;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flower_child_recyclerview, parent, false);
        holder = new RVHolder(view , listener);
        return holder;

    }


    @Override
    public void onBindViewHolder(final RVHolder holder, int position) {
        Flower flower;
        flower = arrayList.get(position);
        holder.flowerName.setText(flower.getName());
        holder.flowerPrice.setText(flower.getPrice() + "$");
        holder.flowerCategory.setText(flower.getCategory());

        if(Network.isOnLine(context)) {
            storageReference.child( "flowers/"+flower.getPhoto() )
                    .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get()
                            .load(uri)
                            .placeholder(R.drawable.loading)
                            .into(holder.flowerImage);
                }
            });

        }else{
            Dialog.show(context);
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    /**
     * RecyclerView.ViewHolder
     * **/

    public static class RVHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        int type;
        ImageView flowerImage;
        TextView flowerName, flowerPrice, flowerCategory;
        OnItemClickListener listener;
        public RVHolder(View itemView,OnItemClickListener clickListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.listener = clickListener;
            flowerImage = itemView.findViewById(R.id.imageChildFlower);
            flowerName = itemView.findViewById(R.id.flowerNameChildItem);
            flowerPrice = itemView.findViewById(R.id.flowerPriceChildItem);
            flowerCategory = itemView.findViewById(R.id.flowerCategoryChildItem);
        }


        @Override
        public void onClick(View v) {
            if (this.listener != null)
                this.listener.onItemClick(v,this.getLayoutPosition());
        }
    }


}
