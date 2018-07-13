package com.mostafa.fci.flowerapp.Services;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLManager {


    /**
     * get Data by AsyncTask with HttpURLConnection without Authentication
     * */
    public static String  fetchData(String uri){

        BufferedReader reader = null;
        try {

            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            StringBuilder stringBuilder = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line ;
            while ( (line = reader.readLine() ) != null ){
                stringBuilder.append(line+"\n");
            }

            return stringBuilder.toString();

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {

            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

        }
    }

}
