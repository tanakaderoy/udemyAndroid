package com.tanaka.memorableplaces;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView placesListView;
    ArrayList<String> places = new ArrayList<>();
    ArrayList<LatLng> locations = new ArrayList<>();
    ArrayAdapter<String> adapter;
    String serializedStringLatitudes = "";
    String serializedStringLongitudes = "";
    String serializedStringPlaces = "";
    SharedPreferences sharedPreferences;
    ArrayList<String> latitudes = new ArrayList<>();
    ArrayList<String> longitudes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        placesListView = findViewById(R.id.placesListView);

        sharedPreferences = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String serialLongitudes = sharedPreferences.getString("longitudes", "");
        String serialLatitudes = sharedPreferences.getString("latitudes", "");
        String placesSerial = sharedPreferences.getString("placesSerial", "");

        places.clear();
        latitudes.clear();
        longitudes.clear();
        locations.clear();
        try {



    places = (ArrayList<String>) ObjectSerializer.deserialize(placesSerial);
    latitudes = (ArrayList<String>) ObjectSerializer.deserialize(serialLatitudes);
    longitudes = (ArrayList<String>) ObjectSerializer.deserialize(serialLongitudes);

        } catch (IOException e) {
            e.printStackTrace();
        }
        if(places != null){
        if(places.size()>0 && latitudes.size()>0 && longitudes.size()>0) {
            if (longitudes.size() == latitudes.size() ) {
                for (int i = 0; i < latitudes.size(); i++) {
                    locations.add(new LatLng(Double.valueOf(latitudes.get(i)), Double.valueOf(longitudes.get(i))));
                }
            }
        }
        }else{
            places = new ArrayList<>();
            locations = new ArrayList<>();
            places.add("Add a new place...");
            locations.add(new LatLng(0, 0));
        }


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, places);
        placesListView.setAdapter(adapter);
        placesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                if (i == 0) {
                    intent.putExtra("place", "zoom");

                } else {
                    intent.putExtra("place", places.get(i));
                    intent.putExtra("latLng", locations.get(i-1));
                }
                if (Build.VERSION.SDK_INT > 20) {
                    System.out.println(intent.getStringExtra("place"));

                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
                    startActivityForResult(intent, 1, options.toBundle());
                } else {

                    System.out.println(intent.getStringExtra("place"));

                    startActivityForResult(intent, 1);
                }

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getExtras() != null) {
                    if (data.getStringExtra("place") != null) {
                        System.out.println(data.getStringExtra("place"));
                        places.add(data.getStringExtra("place"));
                        //sharedPreferences.edit().putString("places", serializedStringPlaces).apply();
                        try {

                            serializedStringPlaces = ObjectSerializer.serialize(places);
                            sharedPreferences.edit().putString("placesSerial", serializedStringPlaces).apply();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    if (data.getExtras().getParcelable("latLng") != null) {
                        System.out.println(data.getExtras().getParcelable("latLng") + "data back");
                        locations.add((LatLng) data.getExtras().getParcelable("latLng"));
                        //sharedPreferences.edit().putString("locations", serializedStringLatitudes).apply();

                        try {

                            for (LatLng coord: locations){
latitudes = new ArrayList<>();
longitudes = new ArrayList<>();
                                latitudes.add(String.valueOf(coord.latitude));
                                longitudes.add(String.valueOf(coord.longitude));
                                serializedStringLatitudes = ObjectSerializer.serialize(latitudes);
                                serializedStringLongitudes = ObjectSerializer.serialize(longitudes);

                            }

                            sharedPreferences.edit().putString("latitudes", serializedStringLatitudes).apply();

                            sharedPreferences.edit().putString("longitudes",serializedStringLongitudes).apply();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    adapter.notifyDataSetChanged();

                }

            }
        }
    }
}
