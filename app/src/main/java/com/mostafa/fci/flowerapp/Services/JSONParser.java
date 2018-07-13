package com.mostafa.fci.flowerapp.Services;


import com.mostafa.fci.flowerapp.Models.Flower;
import com.mostafa.fci.flowerapp.Utillities.Constant;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;


public class JSONParser {

    // get Json Object and return Array of Objects
    public static ArrayList<Flower> parse(String content){
        if (content == "" || content == null){
            return null;
        }


        ArrayList<Flower> flowersList = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(content);
            for(int i=0; i<jsonArray.length(); i++) {

               JSONObject jsonObject = jsonArray.getJSONObject(i);

               Flower flower = new Flower();
               flower.setProductId(jsonObject.getInt(Constant.ID));
               flower.setName(jsonObject.getString(Constant.NAME));
               flower.setCategory(jsonObject.getString(Constant.CATEGORY));
               flower.setPrice(jsonObject.getDouble(Constant.PRICE));
               flower.setInstructions(jsonObject.getString(Constant.INSTRUCTIONS));
               flower.setPhoto(jsonObject.getString(Constant.PHOTO));

               flowersList.add(flower);

            }
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return flowersList;

    }

}
