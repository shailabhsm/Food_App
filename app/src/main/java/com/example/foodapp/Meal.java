package com.example.foodapp;

import android.graphics.Bitmap;

public class Meal {

    private String name;
    private String id;
    private Bitmap image;

    public Meal(String id, String name, Bitmap image)
    {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
