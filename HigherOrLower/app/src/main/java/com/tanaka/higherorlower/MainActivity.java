package com.tanaka.higherorlower;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    int number;

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generateRandomNumber();
        editText = (EditText) findViewById(R.id.guessEditText);
        keyBoardControl();
    }

    public void keyBoardControl(){
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // do your stuff here
                    guess();
                    System.out.println("done");
                }
                return true;
            }
        });
    }
    public void generateRandomNumber(){
        Random random = new Random();
        number = random.nextInt(20 - 1 + 1) + 1;
    }

    public void guess() {

        String toastText = "";




        int userNumber;
        if (editText.getText().toString().isEmpty()){
            userNumber = 0;
        } else{
            userNumber = Integer.valueOf(editText.getText().toString());
        }

        if (userNumber > number){
            toastText = "Lower";
        }else if(userNumber < number){
            toastText = "Higher";
        }else if (userNumber == number){
            toastText = "Bingo! Try again with new number";
            generateRandomNumber();
            editText.setText("");
        }
        Toast.makeText(this,""+toastText, Toast.LENGTH_SHORT).show();

    }
}
