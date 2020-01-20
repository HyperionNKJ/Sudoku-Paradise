package com.example.user.sudokuparadise;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class FailureActivity extends AppCompatActivity {
    private String sudokuPuzzleString;
    private String levelOfDifficulty;
    private boolean timedChallenge;
    private long timeLimitInMillis;
    private String attemptedDateAndTime;
    private TextView difficultyLevel;
    private CheckBox timedChallengeYes;
    private TextView timeLimit;
    private TextView attemptedOnDate;
    private TextView attemptedOnTime;
    private Button returnToPuzzleButton;
    private Button returnToMainPageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failure);

        // retrieve data from GameModeActivity
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            sudokuPuzzleString = bundle.getString("sudokuPuzzleString");
            levelOfDifficulty = bundle.getString("levelOfDifficulty");
            timedChallenge = bundle.getBoolean("timedChallenge");
            timeLimitInMillis = bundle.getLong("timeLimitInMillis");
            attemptedDateAndTime = bundle.getString("failureDateAndTime");
        }

        // add failure data into Firebase database
        addFailureInstanceIntoFirebase();

        // initialize
        difficultyLevel = findViewById(R.id.failure_difficultyLevel);
        timedChallengeYes = findViewById(R.id.failure_timedChallengeYes);
        timeLimit = findViewById(R.id.failure_timeLimit);
        attemptedOnDate = findViewById(R.id.failure_attemptedOnDate);
        attemptedOnTime = findViewById(R.id.failure_attemptedOnTime);
        returnToPuzzleButton = findViewById(R.id.failure_returnToPuzzle);
        returnToMainPageButton = findViewById(R.id.failure_returnToMainPage);

        // show data on screen
        difficultyLevel.setText(levelOfDifficulty);
        switch (levelOfDifficulty) {
            case "Easy": difficultyLevel.setTextColor(Color.parseColor("#FF079D17")); break;
            case "Medium": difficultyLevel.setTextColor(Color.parseColor("#FFFA8F37")); break;
            case "Hard": difficultyLevel.setTextColor(Color.parseColor("#FFEF0606")); break;
            case "Master": difficultyLevel.setTextColor(Color.parseColor("#FF34128F")); break;
        }
        timedChallengeYes.setChecked(true);
        timeLimit.setText(convertMillisToString(timeLimitInMillis));
        String[] output = attemptedDateAndTime.split(" ");
        attemptedOnDate.setText(output[0]);
        attemptedOnTime.setText(output[1]);

        returnToPuzzleButton.setText(Html.fromHtml("<big><b> CONFRONT THE FUTURE </b></big> <br> (Press to continue solving the puzzle indefinitely)"));
        returnToMainPageButton.setText(Html.fromHtml("<big><b> RETURN HOME </b></big> <br> (Press to return to main page)"));
        returnToPuzzleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        returnToMainPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FailureActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private String convertMillisToString(long timeInMillis) {
        int hours = (int) (timeInMillis / 1000) / 3600;
        int minutes = (int) (timeInMillis / 1000) % 3600 / 60;
        int seconds = (int) (timeInMillis / 1000) % 3600 % 60;
        if (hours == 0) {
            return String.format(Locale.getDefault(), "%02dm %02ds", minutes, seconds);
        }
        return String.format(Locale.getDefault(),"%01dh %02dm %02ds", hours, minutes, seconds);
    }

    private void addFailureInstanceIntoFirebase() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Users/" + userId + "/" + "SuccessesAndFailures");
        String failureId = dataRef.push().getKey();
        SuccessFailure failure = new SuccessFailure(failureId, sudokuPuzzleString, levelOfDifficulty, timedChallenge, timeLimitInMillis, -1, attemptedDateAndTime);
        dataRef.child(failureId).setValue(failure);
    }
}
