package com.example.foodapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class loadMeals extends AsyncTask<Void, Void, Void> {
    String Url;
    String data = "";
    List<String> id;
    List<String> name;
    List<String> thumbnail;
    List<Bitmap> thumbImg;

    public loadMeals(String Url)
    {
        this.Url = Url;
    }

    @Override
    protected Void doInBackground(Void... voids){

        id = new ArrayList<String>();
        name = new ArrayList<String>();
        thumbnail = new ArrayList<String>();
        thumbImg = new ArrayList<Bitmap>();

        try {
            URL url = new URL(Url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while(line != null) {
                line = bufferedReader.readLine();
                data = data + line;
            }

            JSONObject object = new JSONObject(data);
            JSONArray jsonArray = object.getJSONArray("meals");

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String nid = jsonObject.getString("idMeal");
                String nname = jsonObject.getString("strMeal");
                String nthumb = jsonObject.getString("strMealThumb");
                id.add(nid);
                name.add(nname);
                thumbnail.add(nthumb);

                URL thumbUrl = new URL(thumbnail.get(i));
                HttpURLConnection connection = (HttpURLConnection) thumbUrl.openConnection();
                InputStream input = connection.getInputStream();
                thumbImg.add(BitmapFactory.decodeStream(input));
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        for (int i = 0; i < name.size(); i++)
        {
            Meal meal = new Meal(id.get(i), name.get(i), thumbImg.get(i));
            MainActivity.fav_food_list.add(meal);
        }
    }
}