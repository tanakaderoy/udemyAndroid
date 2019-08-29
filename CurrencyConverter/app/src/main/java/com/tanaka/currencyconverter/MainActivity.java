package com.tanaka.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void convert(View view){

        EditText editText = (EditText) findViewById(R.id.currencyEditText);

        double rate = 0.82;

        double usd = Double.valueOf(editText.getText().toString());

        double pound = usd * rate;

        DecimalFormat f = new DecimalFormat("##.00");

        Toast.makeText(this, "$"+ f.format(usd) + " is equal to £" + f.format(pound), Toast.LENGTH_SHORT).show();

    }
}
