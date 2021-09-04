package com.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.chrono.MinguoChronology;
import java.util.ArrayList;

public class meal_details extends AppCompatActivity {

    private String jsonURL = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=";

    public static TextView id, name, category, instruction, country, tags, source, ingred;

    public static ImageView thumbnail;

    public Button add_to_fav;

    private String fid = "";

    Bitmap img;

    boolean checkAlready = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);

        Intent intent = getIntent();

        fid = intent.getStringExtra("id");

        System.out.println("ID : " + fid);


        id = (TextView)findViewById(R.id.meal_id);
        name = (TextView)findViewById(R.id.meal_name);
        category = (TextView)findViewById(R.id.meal_category);
        instruction = (TextView)findViewById(R.id.meal_inst);
        country = (TextView)findViewById(R.id.meal_country);
        tags = (TextView)findViewById(R.id.meal_tag);
        source = (TextView)findViewById(R.id.source);
        ingred = (TextView)findViewById(R.id.meal_ingred);

        thumbnail = (ImageView) findViewById(R.id.thumbnail);

        searchMeal process = new searchMeal(jsonURL + fid);
        process.execute();


        add_to_fav = (Button) findViewById(R.id.add_to_fav);

        add_to_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img = ((BitmapDrawable)thumbnail.getDrawable()).getBitmap();

                if(id.getText().toString() != null && name.getText().toString() != null &&  img != null) {
                    Meal meal = new Meal(id.getText().toString(), name.getText().toString(), img);

                    for (int i = 0; i < MainActivity.fav_food_list.size(); i++) {
                        if (MainActivity.fav_food_list.get(i).getId().equals(id.getText().toString())) {
                            checkAlready = false;
                            break;
                        }
                    }
                    if (checkAlready) {
                        MainActivity.fav_food_list.add(meal);
                        saveToFile();
                        Toast.makeText(getApplicationContext(), "Added to Favourite", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Already in Favourite", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid Meal", Toast.LENGTH_LONG).show();
                }
            }
        });

        source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!source.getText().toString().equals("Not Available")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(source.getText().toString()));
                    startActivity(browserIntent);
                }
            }
        });

    }

    public void saveToFile()
    {
        String keys = "";
        for(int i = 0; i < MainActivity.fav_food_list.size(); i++)
        {
            keys = keys + MainActivity.fav_food_list.get(i).getId() + "\n";
        }
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(new File(getFilesDir() + MainActivity.FILE_NAME));
            fos.write(keys.getBytes());
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fos != null)
            {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.fav)
        {
            Intent intent = new Intent(meal_details.this, Fav.class);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.home){
            Intent intent = new Intent(meal_details.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        if(id == R.id.search_meal){
            Intent intent = new Intent(meal_details.this, search.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}