package com.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.renderscript.ScriptGroup;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class search extends AppCompatActivity {

    private String jsonURL = "https://www.themealdb.com/api/json/v1/1/search.php?s=";

    private TextView food_search;
    private Button search_button;

    public static LinearLayout search_layout;

    public static ListView listView;

    public static Context context;

    public static ArrayList<Meal> search_meal= new ArrayList<Meal>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search_layout = (LinearLayout)findViewById(R.id.meal_details);

        search_button = (Button)findViewById(R.id.button_search);

        food_search = (EditText)findViewById(R.id.meal_search);

        listView = (ListView) findViewById(R.id.list_meals);

        context = (Context) getApplicationContext();


        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(search.this);
                dialog.setContentView(R.layout.progress_dialog);
                dialog.setCancelable(false);

                dialog.show();

                search_meal.clear();

                String new_url = jsonURL + food_search.getText().toString();
                listMeals process = new listMeals(new_url, getApplicationContext());
                process.execute();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                }, 5000);
            }
        });
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
            Intent intent = new Intent(search.this, Fav.class);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.home){
            Intent intent = new Intent(search.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}