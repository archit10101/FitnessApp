package com.example.fitnessapp;

import android.app.Activity;
import android.util.Log;

public class SingletonData {

    private static SingletonData instance;

    int interval;

    int count;

    int rest;

    public SingletonData() {
        interval = 2;
        count = 12;
        rest = 3;
    }

    public static SingletonData getInstance() {
        if (instance == null) {
            instance = new SingletonData();
        }
        return instance;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        Log.d("count",count+"");

        this.count = count;
    }

    public void setRest(int rest) {
        Log.d("rest",rest+"");

        this.rest = rest;
    }

    public void setInterval(int interval) {
        Log.d("interval",interval+"");

        this.interval = interval;
    }

    public int getInterval() {

        return interval;
    }

    public int getRest() {
        return rest;
    }
}
