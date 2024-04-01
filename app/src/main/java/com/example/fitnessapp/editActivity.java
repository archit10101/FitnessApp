package com.example.fitnessapp;

import android.os.Bundle;
        import android.util.Log;
        import android.widget.SeekBar;
        import android.widget.TextView;

        import androidx.appcompat.app.AppCompatActivity;

public class editActivity extends AppCompatActivity {

    private SeekBar seekBar1, seekBar2, seekBar3;
    private TextView intervalText, countText, restText;

    int interval,count,rest = 1;

    private SingletonData singletonData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        singletonData = SingletonData.getInstance();

        interval = singletonData.getInterval();
        count = singletonData.getCount();
        rest = singletonData.getRest();


        seekBar1 = findViewById(R.id.seekBar1);
        seekBar1.setProgress(interval-1);
        seekBar2 = findViewById(R.id.seekBar2);
        seekBar2.setProgress(count-1);

        seekBar3 = findViewById(R.id.seekBar3);
        seekBar3.setProgress(rest-1);

        intervalText = findViewById(R.id.seekBar1Value);
        countText = findViewById(R.id.seekBar2Value);
        restText = findViewById(R.id.seekBar3Value);
        intervalText.setText((interval)+"");
        countText.setText((count)+"");
        restText.setText((rest)+"");


        // Set up SeekBar listeners
        seekBar1.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBar2.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBar3.setOnSeekBarChangeListener(seekBarChangeListener);
    }

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // Log the current value when SeekBar progress changes
            int value = progress ; // Adjust to range 1-10
            String seekBarId = getResources().getResourceEntryName(seekBar.getId());
            Log.d("SeekBar", seekBarId + ": " + value);

            interval = getValueFromSeekBar(seekBar1);
            count = getValueFromSeekBar(seekBar2);
            rest = getValueFromSeekBar(seekBar3);
            intervalText.setText(interval+"");
            singletonData.setInterval(interval);
            countText.setText(count+"");
            singletonData.setCount(count);

            restText.setText(rest+"");
            singletonData.setRest(rest);

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // Not needed for this example
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // Not needed for this example
        }
    };

    private int getValueFromSeekBar(SeekBar seekBar) {
        return seekBar.getProgress() + 1;
    }
}
