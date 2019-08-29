package com.tanaka.databasedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            SQLiteDatabase myDatabase = this.openOrCreateDatabase("Users", MODE_PRIVATE, null);
            //myDatabase.execSQL("Drop Table users");

            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS users(name VARCHAR, age INT(3), id  INTEGER PRimary key autoincrement)");
            myDatabase.execSQL("INSERT INTO users (name, age) VALUES ('Willian', 22)");
            myDatabase.execSQL("INSERT INTO users (name, age) VALUES ('Lamps', 28)");
            myDatabase.execSQL("INSERT INTO users (name, age) VALUES ('Kepa', 14)");
            myDatabase.execSQL("INSERT INTO users (name, age) VALUES ('Eddy', 28)");
            myDatabase.execSQL("INSERT INTO users (name, age) VALUES ('Terry', 14)");
            myDatabase.execSQL("INSERT INTO users (name, age) VALUES ('Eden', 28)");
            myDatabase.execSQL("INSERT INTO users (name, age) VALUES ('Luiz', 14)");
            myDatabase.execSQL("INSERT INTO users (name, age) VALUES ('Kepa', 28)");
            Cursor cursor = myDatabase.rawQuery("SELECT * FROM users", null);
            int nameIndex = cursor.getColumnIndex("name");
            int ageIndex = cursor.getColumnIndex("age");
            int idIndex = cursor.getColumnIndex("id");
            cursor.moveToFirst();
            while (cursor != null){
                String name = (cursor.getString(nameIndex));
                int age = (cursor.getInt(ageIndex));
                Log.i("name",name);
                Log.i("age",""+age);
                Log.i("id" ,""+cursor.getInt(idIndex));
                cursor.moveToNext();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
