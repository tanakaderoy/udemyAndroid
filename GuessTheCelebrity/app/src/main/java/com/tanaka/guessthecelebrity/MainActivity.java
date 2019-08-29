package com.tanaka.guessthecelebrity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> celebURLs = new ArrayList<>();
    ArrayList<String> celebNames = new ArrayList<>();
    ImageView celebImageView;
    int chosenCelebrity = 0;
    Button button;
    Button button2;
    Button button3;
    Button button4;
    String[] answers = new String[4];
    int locationOfCorrectAnswer = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        celebImageView = findViewById(R.id.celebImageView);
        button = findViewById(R.id.answerButton);
        button2 = findViewById(R.id.answerButton2);
        button3 = findViewById(R.id.answerButton3);
        button4 = findViewById(R.id.answerButton4);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        DownloadTask task = new DownloadTask();
        String result = null;
        try {
            result = task.execute("http://www.posh24.se/kandisar").get();
            String[] splitResult = result.split("<div class=\"listedArticles\">");
            Log.i("res", result);

            Pattern p = Pattern.compile("img src=\"(.*?)\"");
            Matcher m = p.matcher(splitResult[0]);

            while (m.find()) {
                System.out.println(m.group(1));
                celebURLs.add(m.group(1));
            }

            p = Pattern.compile("alt=\"(.*?)\"");
            m = p.matcher(splitResult[0]);

            while (m.find()) {
                System.out.println(m.group(1));
                celebNames.add(m.group(1));
            }

        setup();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void celebChosen(View view) {
        if (view.getTag().toString().equals(Integer.toString(locationOfCorrectAnswer))) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            setup();
        } else {
            Toast.makeText(this, "False! it was" + celebNames.get(chosenCelebrity), Toast.LENGTH_SHORT).show();
            setup();
        }

    }

    public void setup() {

            Random random = new Random();
            chosenCelebrity = random.nextInt(celebURLs.size());





            ImageDownloader downloader = new ImageDownloader();

        Bitmap downloadedImage = null;
        try {
            downloadedImage = downloader.execute(celebURLs.get(chosenCelebrity)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        celebImageView.setImageBitmap(downloadedImage);
            locationOfCorrectAnswer = random.nextInt(4);
            int incorrectAnswer;
            for (int i = 0; i < 4; i++) {
                if (i == locationOfCorrectAnswer) {
                    answers[i] = celebNames.get(chosenCelebrity);
                } else {
                    incorrectAnswer = random.nextInt(celebNames.size());
                    while (incorrectAnswer == chosenCelebrity) {
                        incorrectAnswer = random.nextInt(celebNames.size());
                    }

                    answers[i] = celebNames.get(incorrectAnswer);

                }
            }

            button.setText(answers[0]);
            button2.setText(answers[1]);
            button3.setText(answers[2]);
            button4.setText(answers[3]);


    }


    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;


            } catch (Exception e) {
                e.printStackTrace();
                return "Failed";
            }


        }
    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
    }
}
