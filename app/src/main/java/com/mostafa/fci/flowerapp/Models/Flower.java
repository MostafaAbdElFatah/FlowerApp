package com.mostafa.fci.flowerapp.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by FCI on 2018-06-25.
 */
@Entity
public class Flower implements Serializable {

    @PrimaryKey
    private int productId;
    @ColumnInfo
    private String name;
    @ColumnInfo
    private String category;
    @ColumnInfo
    private String instructions;
    @ColumnInfo
    private double price;
    @ColumnInfo
    private String photo;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] imagebyteArray;

    public Flower(){

    }


    public byte[] getImagebyteArray() {
        return imagebyteArray;
    }

    public void setImagebyteArray(byte[] imagebyteArray) {
        this.imagebyteArray = imagebyteArray;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
