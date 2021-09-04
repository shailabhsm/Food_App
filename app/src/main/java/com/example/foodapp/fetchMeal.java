package com.example.foodapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;

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

public class fetchMeal extends AsyncTask<Void, Void, Void> {
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
    Context context;

    public fetchMeal(String Url, Context context)
    {
        this.Url = Url;
        this.context = context;
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
                if(!(jsonObject.get(ing).equals("null") || jsonObject.get(ing).equals(null) || jsonObject.get(ing).equals("")))
                {
                    in_list += jsonObject.get(ing) + " - " + jsonObject.get(inm) + "\n";
                }
            }
            ingredients += in_list;

            URL thumbUrl = new URL(thumbnail);
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
            MainActivity.id.setText(this.id);
        else
            MainActivity.id.setText("Not Available");
        if(!this.name.equals("null"))
            MainActivity.name.setText(this.name);
        else
            MainActivity.name.setText("Not Available");
        if(!this.category.equals("null"))
            MainActivity.category.setText(this.category);
        else
            MainActivity.category.setText("Not Available");
        if(!this.country.equals("null"))
            MainActivity.country.setText(this.country);
        else
            MainActivity.country.setText("Not Available");
        if(!this.instruction.equals("null"))
            MainActivity.instruction.setText(this.instruction);
        else
            MainActivity.instruction.setText("Not Available");
        if(!this.tags.equals("null"))
            MainActivity.tags.setText(this.tags);
        else
            MainActivity.tags.setText("Not Available");
        if(!(this.source.equals("null") || this.source.equals("")))
            MainActivity.source.setText(this.source);
        else
            MainActivity.source.setText("Not Available");
        if(!this.ingredients.equals("null"))
            MainActivity.ingred.setText(this.ingredients);
        else
            MainActivity.ingred.setText("Not Available");
        if(this.thumbImg != null)
            MainActivity.thumbnail.setImageBitmap(thumbImg);
    }
}
