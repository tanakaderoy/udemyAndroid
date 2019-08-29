package com.tanaka.downloadjson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    Button fetchWeatherButton;
    TextView textView;
    String city = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.cityEditText);
        fetchWeatherButton = findViewById(R.id.fetchWeatherButton);
        textView = findViewById(R.id.weatherTextView);
        textView.setText(" ");






    }

    public void getWeather(String city) {
        DownloadTask task = new DownloadTask();
        String result = null;
        try {
            result = task.execute("http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=17c7d7bc3e2290f6a84d92a4fce8c3a2&units=imperial").get();


        } catch(Exception e) {
            e.printStackTrace();
        }
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(editText.getWindowToken(),0);
    }

    public void fetchWeather(View view) {
        if(!editText.getText().toString().isEmpty()){
            city = editText.getText().toString();
            getWeather(city);
        }

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

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("JSON", s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                String temp = ""+ jsonObject.getJSONObject("main").getDouble("temp");
                Log.i("Weather", weatherInfo);
                JSONArray array = new JSONArray(weatherInfo);
                String weatherText = "";
                for(int i=0; i<array.length();i++){
                    JSONObject jsonPart = array.getJSONObject(i);
                    Log.i("main",jsonPart.getString("main"));
                    Log.i("description",jsonPart.getString("description"));

                    weatherText += (jsonPart.getString("main")+": "+ jsonPart.getString("description") + "\n");



                }
                System.out.println(weatherText);
                textView.setText(weatherText + "\n" + "Temp: "+ temp + "F°");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
