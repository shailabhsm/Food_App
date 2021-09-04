package com.example.foodapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

public class MainActivity extends AppCompatActivity {

    public static TextView id, name, category, instruction, country, tags, source, ingred;
    public static ImageView thumbnail;
    public Button add_to_fav;
    private String jsonUrl = "https://www.themealdb.com/api/json/v1/1/random.php";
    public static ArrayList<Meal> fav_food_list = new ArrayList<Meal>();
    Bitmap img;
    boolean checkAlready = true;
    public static final String FILE_NAME = "/mealList.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.setCancelable(false);

        dialog.show();

        LoadFav();

        id = (TextView)findViewById(R.id.meal_id);
        name = (TextView)findViewById(R.id.meal_name);
        category = (TextView)findViewById(R.id.meal_category);
        instruction = (TextView)findViewById(R.id.meal_inst);
        country = (TextView)findViewById(R.id.meal_country);
        tags = (TextView)findViewById(R.id.meal_tag);
        source = (TextView)findViewById(R.id.source);
        ingred = (TextView)findViewById(R.id.meal_ingred);

        thumbnail = (ImageView) findViewById(R.id.thumbnail);

        add_to_fav = (Button) findViewById(R.id.add_to_fav);

        fetchMeal process = new fetchMeal(jsonUrl, getApplicationContext());
        process.execute();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 5000);

        add_to_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img = ((BitmapDrawable)thumbnail.getDrawable()).getBitmap();

                if(id.getText().toString() != null && name.getText().toString() != null &&  img != null) {
                    Meal meal = new Meal(id.getText().toString(), name.getText().toString(), img);

                    for(int i = 0; i < fav_food_list.size(); i++)
                    {
                        if(fav_food_list.get(i).getId().equals(id.getText().toString()))
                        {
                            checkAlready = false;
                            break;
                        }
                    }

                    if (checkAlready) {
                        fav_food_list.add(meal);
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
        for(int i = 0; i < fav_food_list.size(); i++)
        {
            keys = keys + fav_food_list.get(i).getId() + "\n";
        }
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(new File(getFilesDir() + FILE_NAME));
            fos.write(keys.getBytes());
            //System.out.println("FILE LOCATION : " + getFilesDir());
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

    public void loadFromFile()
    {
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(new File(getFilesDir() + FILE_NAME));
            System.out.println(getFilesDir() + FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String text;
            String jsonURL = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=";
            boolean CA = false;
            while((text = br.readLine()) != null) {
                for(int i = 0; i < fav_food_list.size(); i++)
                {
                    if(fav_food_list.get(i).getId().equals(text))
                    {
                        CA = true;
                        break;
                    }
                }
                if(!CA) {
                    loadMeals LM = new loadMeals(jsonURL + text);
                    LM.execute();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void LoadFav()
    {
        try {
            File root = new File(String.valueOf(getFilesDir()));
            if (!root.exists())
                root.mkdirs();
            File file = new File(getFilesDir() + FILE_NAME);
            if (!file.exists())
                file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadFromFile();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.search_meal){
            Intent intent = new Intent(MainActivity.this, search.class);
            startActivity(intent);
            finish();
        } else if(id == R.id.fav)
        {
            Intent intent = new Intent(MainActivity.this, Fav.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}