package com.tanaka.sharedpreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static com.tanaka.sharedpreferences.ObjectSerializer.deserialize;
import static com.tanaka.sharedpreferences.ObjectSerializer.serialize;

public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
//       sharedPreferences.edit().putString("userName", "Tanaka").apply();
//       String userName =  sharedPreferences.getString("userName","default");
//       Toast.makeText(this, userName, Toast.LENGTH_SHORT).show();
        ArrayList<String > friends = new ArrayList<>();
//        friends.add("Kirubel");
//        friends.add("Tanaka");
//        friends.add("linc");
        String serializedString = "";
        try {

            serializedString = ObjectSerializer.serialize(friends);
            //sharedPreferences.edit().putString("friends", serializedString).apply();
            System.out.println(serializedString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String newString = sharedPreferences.getString("friends", serializedString);
        try {
           System.out.println( ObjectSerializer.deserialize(newString));
           friends = ((ArrayList<String>) ObjectSerializer.deserialize(newString));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(friends.toString());
        //sharedPreferences.edit().pu

    }
}
