package com.tanaka.timerdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    SeekBar seekBar;
    TextView textView;
    //ImageView imageView;
    Button button;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekBar = findViewById(R.id.seekBar);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);

        seekBar.setMax(600);
        seekBar.setProgress(30);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                updateTimer(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


//        final Handler handler = new Handler();
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                Log.i("Hey it's us", "A second has passed by");
//                handler.postDelayed(this,1000);
//            }
//        };
//        handler.post(runnable);
    }

    Boolean counterIsActive = false;

    public void stopTimer() {
        textView.setText("0:30");
        seekBar.setProgress(30);
        seekBar.setEnabled(true);
        countDownTimer.cancel();
        button.setText("Go!");
        counterIsActive = false;

    }

    public void startTimer(View view) {
        if (counterIsActive) {

            stopTimer();
        } else {
            counterIsActive = true;
            seekBar.setEnabled(false);
            button.setText("Stop");


            countDownTimer = new CountDownTimer(seekBar.getProgress() * 1000 + 100, 1000) {
                @Override
                public void onTick(long milliSecondsUntilDone) {
                    updateTimer((int) milliSecondsUntilDone / 1000);
                }

                @Override
                public void onFinish() {
                    Log.i("Finished", " second has passed by");
                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.dragon);
                    mediaPlayer.start();
                    stopTimer();

                }
            };
            countDownTimer.start();
        }
    }

    public void updateTimer(int secondsLeft) {
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - (minutes * 60);
        String secondsString = Integer.toString(seconds);
        for (int x = 0; x < 10; x++) {
            if (secondsString.equals(Integer.toString(x))) {
                secondsString = "0" + x;
            }
        }
        textView.setText(minutes + ":" + secondsString);

    }
}
