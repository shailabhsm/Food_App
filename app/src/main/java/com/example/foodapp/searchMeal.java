package com.example.foodapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;

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

public class searchMeal extends AsyncTask<Void, Void, Void> {
    String Url;
    String data = "";
    String id = "";
    String name = "";
    String category = "";
    String instruction = "";
    String tags = "";
    String country = "";
    String thumbnail = "";
    String source = "";
    String ingredients = "";
    String in_list = "";
    Bitmap thumbImg;


    public searchMeal(String Url)
    {
        this.Url = Url;
    }

    @Override
    protected Void doInBackground(Void... voids){

        try {
            URL url = new URL(Url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = bufferedReader.readLine();
            data = data + line;

            JSONObject object = new JSONObject(data);
            JSONArray jsonArray = object.getJSONArray("meals");

            JSONObject jsonObject = jsonArray.getJSONObject(0);

            id += jsonObject.get("idMeal");
            name += jsonObject.get("strMeal");
            category += jsonObject.get("strCategory");
            country += jsonObject.get("strArea");
            thumbnail += jsonObject.get("strMealThumb");
            tags += jsonObject.get("strTags");
            instruction += jsonObject.get("strInstructions");
            source += jsonObject.get("strSource");
            String ing, inm;
            for(int i = 1; i <= 20; i++)
            {
                ing = "strIngredient" + i;
                inm = "strMeasure" + i;
                //if(!(jsonObject.get(ing).equals("") || jsonObject.get(ing) == null))
                if(!(jsonObject.get(ing).equals("null") || jsonObject.get(ing).equals(null) || jsonObject.get(ing).equals("")))
                {
                    in_list += jsonObject.get(ing) + " - " + jsonObject.get(inm) + "\n";
                }
            }
            ingredients += in_list;

            URL thumbUrl = new URL(thumbnail + "/preview");
            HttpURLConnection connection = (HttpURLConnection) thumbUrl.openConnection();
            InputStream input = connection.getInputStream();
            thumbImg = BitmapFactory.decodeStream(input);

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


        if(!this.id.equals("null"))
            meal_details.id.setText(this.id);
        else
            meal_details.id.setText("Not Available");
        if(!this.name.equals("null"))
            meal_details.name.setText(this.name);
        else
            meal_details.name.setText("Not Available");
        if(!this.category.equals("null"))
            meal_details.category.setText(this.category);
        else
            meal_details.category.setText("Not Available");
        if(!this.country.equals("null"))
            meal_details.country.setText(this.country);
        else
            meal_details.country.setText("Not Available");
        if(!this.instruction.equals("null"))
            meal_details.instruction.setText(this.instruction);
        else

            meal_details.instruction.setText("Not Available");
        if(!this.tags.equals("null"))
            meal_details.tags.setText(this.tags);
        else
            meal_details.tags.setText("Not Available");
        if(!(this.source.equals("null") || this.source.equals("")))
            meal_details.source.setText(this.source);
        else
            meal_details.source.setText("Not Available");
        if(!this.ingredients.equals("null"))
            meal_details.ingred.setText(this.ingredients);
        else
            meal_details.ingred.setText("Not Available");
        if(this.thumbImg != null)
            meal_details.thumbnail.setImageBitmap(thumbImg);
    }
}
