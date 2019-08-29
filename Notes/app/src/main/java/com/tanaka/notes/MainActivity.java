package com.tanaka.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    ListView notesListView;
    ArrayList<String> notesArray = new ArrayList<>();
    String notesSerializedString = "";
    ArrayAdapter<String> adapter;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String newString = sharedPreferences.getString("notes", "Error");
        notesListView = findViewById(R.id.notesListView);

        if (!newString.equals("Error")) {

            try {
                notesArray = ((ArrayList<String>) ObjectSerializer.deserialize(newString));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            notesArray.add("Example Note");
        }
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, notesArray);
        notesListView.setAdapter(adapter);
        notesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("Item", notesArray.get(i));

                intent.putExtra("note", notesArray.get(i));
                intent.putExtra("index", i);
                startActivityForResult(intent, 1);
                return false;
            }
        });
        intent = new Intent(getApplicationContext(), NotesActivity.class);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        Log.i("requestCode", "" + requestCode);
        System.out.println("above" + data.getExtras().toString());
        int index = data.getIntExtra("index", -1);
        String note = data.getStringExtra("note");
        switch (requestCode) {
            //long press to edit

            case 1:
                notesArray.set(index,note);
                adapter.notifyDataSetChanged();
                return;
                // add new
            case 2:

                if (!note.equals("-1")) {
                    notesArray.add(note);
                    adapter.notifyDataSetChanged();
                }
                return;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
        try {

            notesSerializedString = ObjectSerializer.serialize(notesArray);
            sharedPreferences.edit().putString("notes", notesSerializedString).apply();
            System.out.println(notesSerializedString);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.addNote:
                Log.i("itemSelected", "addNote");
                intent = new Intent(getApplicationContext(), NotesActivity.class);
                startActivityForResult(intent, 2);
                return true;
            default:
                return false;
        }
    }
}
