package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import static android.app.Notification.EXTRA_TEXT;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class ExerciseActivity extends AppCompatActivity {

    private TextView textRep1, textRep2, textRep3;

    private CircularSeekBar circle;
    private TextView[] boxes;
    private int setNum = 1;

    boolean going = true;

    private int timePeriod = 1000;

    private static final int REQUEST_CODE = 101;


    private int repAmt = 4;
    private int repsleftover = 4;



    private long lastTime = System.currentTimeMillis();

    float progressB = 100;

    private Timer timer;

    private TextView repsLeft;

    private SingletonData singletonData;

    private Bluetooth bluetoothEMS;
    private PowerManager.WakeLock wakeLock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "MyApp::MyWakelockTag");
        wakeLock.acquire();

        findViewById(R.id.constraintLayout).setRotation(180);

        singletonData = SingletonData.getInstance();
        repAmt = singletonData.getCount();


        Singleton mySingleton = Singleton.getInstanceBLE(this);
        bluetoothEMS = mySingleton.getMyBLEObject();

        timePeriod = singletonData.getInterval()*1000;
        textRep1 = findViewById(R.id.textRep1);
        textRep2 = findViewById(R.id.textRep2);
        textRep3 = findViewById(R.id.textRep3);

        boxes = new TextView[]{textRep1, textRep2, textRep3};

        repsleftover = repAmt;

        highlightBox(setNum);

        circle = findViewById(R.id.circularSeekBar);
        circle.setProgress(100);

        circle.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                Log.d("d",progress+"");
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });

        repsLeft = findViewById(R.id.repsLeftText);
        repsLeft.setText(repsleftover+"");


        TimerTask task = new TimerTask() {
            public void run() {
                if (System.currentTimeMillis() - lastTime>timePeriod){
                    if (going){
                        repsleftover--;
                        circle.setProgress((float)(100*((double)repsleftover)/repAmt));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                repsLeft.setText(repsleftover+"");
                            }
                        });
                        if (repsleftover ==1){
                            bluetoothEMS.startAdd("set<"+setNum+">:last");

                        }else if (repsleftover>1){
                            bluetoothEMS.startAdd("set<"+setNum+">:tempo");

                        }
                        progressB = circle.getProgress();
                        if (repsleftover==0){
                            Intent intent = new Intent(ExerciseActivity.this,RestActivity.class);
                            startActivityForResult(intent,REQUEST_CODE);
                            going = false;
                        }

                    }
                    lastTime = System.currentTimeMillis();
                }
            }
        };


        timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, 90);

    }

    private void highlightBox( int index) {
        for (TextView box : boxes) {
            box.setBackgroundResource(R.drawable.rounded_background);
            box.setClickable(false);
        }

        boxes[index-1].setBackgroundResource(R.drawable.highlighted_background);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            circle.setProgress(100);

            repsleftover = repAmt;
            setNum++;
            going = true;
            repsLeft.setText(repAmt+"");
            circle.setProgress(100);
            if (setNum>3){
                timer.cancel();
                bluetoothEMS.startAdd("reps:done");
                Intent intent = new Intent(ExerciseActivity.this, exerciseDoneActivity.class);
                startActivity(intent);
                finish();
            }else{
                highlightBox(setNum);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.onPause();

        // Release the wake lock when activity is paused
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
        timer.cancel();
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

}

