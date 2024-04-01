package com.example.fitnessapp;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class RestActivity extends AppCompatActivity {

    public static final String EXTRA_TEXT = "extra_text";


    int restTime = 10;
    private long lastTime = System.currentTimeMillis();


    private SingletonData singletonData;

    private Timer timer;

    private Bluetooth bluetoothEMS;


    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest);

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "MyApp::MyWakelockTag");
        wakeLock.acquire();


        findViewById(R.id.constraintLayout).setRotation(180);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Singleton mySingleton = Singleton.getInstanceBLE(this);
        bluetoothEMS = mySingleton.getMyBLEObject();

        TextView countDown = findViewById(R.id.countdownTextView);

        singletonData = SingletonData.getInstance();

        restTime = singletonData.getRest();
        countDown.setText(restTime+"");


        TimerTask task = new TimerTask() {
            public void run() {
                if (System.currentTimeMillis() - lastTime>1000){
                    restTime--;
                    if (restTime == 3){
                        bluetoothEMS.startAdd("rest:3");
                    }else if(restTime == 2){
                        bluetoothEMS.startAdd("rest:2");

                    }else if(restTime == 1){
                        bluetoothEMS.startAdd("rest:1");

                    }
                    if (restTime == 0){

                        cancel();
                        String newText = "m";
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra(EXTRA_TEXT, newText);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            countDown.setText(restTime + "");
                        }
                    });

                    lastTime = System.currentTimeMillis();
                }
            }
        };

        timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, 90);




    }
    @Override
    protected void onPause() {
        super.onPause();

        // Release the wake lock when activity is paused
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Re-acquire the wake lock when activity is resumed
        if (wakeLock != null && !wakeLock.isHeld()) {
            wakeLock.acquire();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
        timer.cancel();

    }
}