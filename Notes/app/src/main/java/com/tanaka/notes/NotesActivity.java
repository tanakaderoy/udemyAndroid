package com.tanaka.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

public class NotesActivity extends AppCompatActivity {
    Intent recievedIntent;
    int index;
    String note;
    EditText notesEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        notesEditText = findViewById(R.id.noteEditText);

        recievedIntent = getIntent();
        note = recievedIntent.getStringExtra("note");
        index = recievedIntent.getIntExtra("index",-2);
        if (note != null) {
            notesEditText.setText(note);
        }else{
            notesEditText.setText("");
        }




    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

//            getIntent().removeExtra("note");
//            getIntent().removeExtra("index");

        if(notesEditText.getText().toString().length()> 0){
            note = notesEditText.getText().toString();
            Log.i("noteValue",note);
        }else{
            note = "-1";
        }
        Log.i("Called", "OnoptionsSelected");
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                intent.putExtra("note", note);
                intent.putExtra("index",index);

                setResult(RESULT_OK, intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
