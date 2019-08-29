package com.tanaka.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Button goButton;
    TextView sumTextView;
    Button button0;
    Button button1;
    Button button2;
    Button button3;
    TextView resultTextView;
    TextView timerTextView;
    TextView scoreTextView;
    Button playAgainButton;
    int score = 0;
    int numberOfQuestions = 0;
    int a;
    int b;
    ArrayList<Integer> answers = new ArrayList<>();
    CountDownTimer countDownTimer = new CountDownTimer(5000, 1000 + 100) {
        @Override
        public void onTick(long l) {
            int seconds = (int) l / 1000;
            timerTextView.setText(seconds + "s");

        }

        @Override
        public void onFinish() {
            resultTextView.setText("Done!");
            playAgainButton.setVisibility(View.VISIBLE);
            button0.setEnabled(false);
            button1.setEnabled(false);
            button2.setEnabled(false);
            button3.setEnabled(false);


        }
    };

    int locationCorrectAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goButton = findViewById(R.id.goButton);
        playAgainButton = findViewById(R.id.playAgainButton);

        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        timerTextView = findViewById(R.id.timerTextView);

        //resultTextView.setText("");
        goButton.setVisibility(View.VISIBLE);

        sumTextView = findViewById(R.id.sumTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        resultTextView = findViewById(R.id.resultTextView);
        timerTextView.setVisibility(View.INVISIBLE);
        button0.setVisibility(View.INVISIBLE);
        button1.setVisibility(View.INVISIBLE);
        button2.setVisibility(View.INVISIBLE);
        button3.setVisibility(View.INVISIBLE);
        resultTextView.setVisibility(View.INVISIBLE);
        scoreTextView.setVisibility(View.INVISIBLE);
        sumTextView.setVisibility(View.INVISIBLE);
        playAgainButton.setVisibility(View.INVISIBLE);

        initialize();

    }

    public void newQuestion() {
        Random random = new Random();
        a = random.nextInt(21);
        b = random.nextInt(21);
        locationCorrectAnswer = random.nextInt(4);
        sumTextView.setText(a + " + " + b);
        answers.clear();
        for (int i = 0; i < 4; i++) {
            if (i == locationCorrectAnswer) {
                answers.add(a + b);
            }
            int wrongAnswer = random.nextInt(41);
            while (wrongAnswer == a + b) {
                wrongAnswer = random.nextInt(41);

            }
            answers.add(wrongAnswer);
            Log.i("answers", "" + answers.get(i));
        }
        System.out.println(answers);
        button0.setText(answers.get(0).toString());
        button1.setText(answers.get(1).toString());
        button2.setText(answers.get(2).toString());
        button3.setText(answers.get(3).toString());

    }

    public void start(View view) {
        goButton.setVisibility(View.INVISIBLE);
        timerTextView.setVisibility(View.VISIBLE);
        button0.setVisibility(View.VISIBLE);
        button1.setVisibility(View.VISIBLE);
        button2.setVisibility(View.VISIBLE);
        button3.setVisibility(View.VISIBLE);
        resultTextView.setVisibility(View.VISIBLE);
        scoreTextView.setVisibility(View.VISIBLE);
        sumTextView.setVisibility(View.VISIBLE);
       countDownTimer.start();



    }

    public void chooseAnswer(View view) {
        if (Integer.toString(locationCorrectAnswer).equals(view.getTag().toString())) {
            resultTextView.setText("Correct!");
            score++;
        } else {
            resultTextView.setText("Wrong! 😭");
        }
        numberOfQuestions++;
        newQuestion();
        scoreTextView.setText(score + "/" + numberOfQuestions);

    }

    public void initialize() {

        score = 0;
        numberOfQuestions = 0;
        timerTextView.setText("30s");
        resultTextView.setText(" ");
        newQuestion();
        scoreTextView.setText(score + "/" + numberOfQuestions);


    }

    public void playAgain(View view) {
        playAgainButton.setVisibility(View.INVISIBLE);
        button0.setEnabled(true);
        button1.setEnabled(true);
        button2.setEnabled(true);
        button3.setEnabled(true);
        countDownTimer.start();

        initialize();
    }
}
