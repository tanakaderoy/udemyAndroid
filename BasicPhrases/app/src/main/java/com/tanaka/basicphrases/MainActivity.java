package com.tanaka.basicphrases;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    boolean isPlaying = true;

    public void playPhrase(View view) {
        Button button = (Button) view;
        Log.i("Button",button.getTag().toString());
       playSound(button.getTag().toString());

    }
    private void playSound(String nameOfFile){
        MediaPlayer mediaPlayer = MediaPlayer.create(this, getResources().getIdentifier(nameOfFile, "raw",getPackageName()));
        mediaPlayer.start();
    }
}
