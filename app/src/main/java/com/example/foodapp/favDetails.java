package com.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class favDetails extends AppCompatActivity {

    private String jsonURL = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=";

    public static TextView id, name, category, instruction, country, tags, source, ingred;

    public static ImageView thumbnail;

    public Button remove_from_fav;

    private String fid = "";

    int checkAvail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_details);

        Dialog dialog = new Dialog(favDetails.this);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.setCancelable(false);

        dialog.show();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        fid = intent.getStringExtra("id");


        id = (TextView)findViewById(R.id.meal_id);
        name = (TextView)findViewById(R.id.meal_name);
        category = (TextView)findViewById(R.id.meal_category);
        instruction = (TextView)findViewById(R.id.meal_inst);
        country = (TextView)findViewById(R.id.meal_country);
        tags = (TextView)findViewById(R.id.meal_tag);
        source = (TextView)findViewById(R.id.source);
        ingred = (TextView)findViewById(R.id.meal_ingred);

        thumbnail = (ImageView) findViewById(R.id.thumbnail);

        favMeals process = new favMeals(jsonURL + fid);
        process.execute();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 3000);


        remove_from_fav = (Button) findViewById(R.id.remove_from_fav);

        remove_from_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < MainActivity.fav_food_list.size(); i++) {
                    if (MainActivity.fav_food_list.get(i).getId().equals(id.getText().toString())) {
                        checkAvail = i;
                        break;
                    }
                }
                MainActivity.fav_food_list.remove(checkAvail);
                saveToFile();
                Intent intent = new Intent(favDetails.this, Fav.class);
                startActivity(intent);
                finish();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, Fav.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}