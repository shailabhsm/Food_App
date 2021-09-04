package com.example.foodapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class displayLists extends ArrayAdapter<Meal> {
    //Context context;
    //ArrayList<Meal> mealArrayList = new ArrayList<Meal>();

    public displayLists(Context context, ArrayList<Meal> mealArrayList)
    {
        super(context, R.layout.listview,mealArrayList);

        //this.context = context;
        //this.mealArrayList = mealArrayList;
        //System.out.println(this.mealArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Meal meal = getItem(position);

        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview,parent,false);
        }

        ImageView thumb = (ImageView) convertView.findViewById(R.id.meal_img);
        TextView name = (TextView) convertView.findViewById(R.id.meal_name);

        thumb.setImageBitmap(meal.getImage());
        name.setText(meal.getName());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), meal_details.class);
                intent.putExtra("id", meal.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                search.context.startActivity(intent);
            }
        });

        return convertView;
    }
}
