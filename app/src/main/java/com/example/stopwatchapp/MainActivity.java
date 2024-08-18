package com.example.stopwatchapp;

import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView timerText;
    private Button startButton, pauseButton, resetButton;
    private Handler handler;
    private long startTime, timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L, updateTime = 0L;
    private Runnable updateTimerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerText = findViewById(R.id.timer_text);
        startButton = findViewById(R.id.start_button);
        pauseButton = findViewById(R.id.pause_button);
        resetButton = findViewById(R.id.reset_button);

        handler = new Handler();

        updateTimerThread = new Runnable() {
            public void run() {
                timeInMilliseconds = System.currentTimeMillis() - startTime;
                updateTime = timeSwapBuff + timeInMilliseconds;

                int secs = (int) (updateTime / 1000);
                int mins = secs / 60;
                secs = secs % 60;
                int milliseconds = (int) (updateTime % 1000);
                timerText.setText(String.format("%02d:%02d:%02d.%03d", mins, secs, milliseconds / 10, milliseconds % 10));
                handler.postDelayed(this, 0);
            }
        };

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = System.currentTimeMillis();
                handler.postDelayed(updateTimerThread, 0);
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSwapBuff += timeInMilliseconds;
                handler.removeCallbacks(updateTimerThread);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = 0L;
                timeInMilliseconds = 0L;
                timeSwapBuff = 0L;
                updateTime = 0L;
                timerText.setText("00:00:00.000");
                handler.removeCallbacks(updateTimerThread);
            }
        });
    }
}
