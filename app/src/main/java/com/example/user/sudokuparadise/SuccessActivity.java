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

public class SuccessActivity extends AppCompatActivity {
    private String sudokuPuzzleString;
    private String levelOfDifficulty;
    private boolean timedChallenge;
    private long timeLimitInMillis;
    private long timeTakenToSolve;
    private String completionDateAndTime;
    private TextView difficultyLevel;
    private CheckBox timedChallengeYes;
    private CheckBox timedChallengeNo;
    private TextView timeLimit;
    private TextView timeTaken;
    private TextView completedOnDate;
    private TextView completedOnTime;
    private Button returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        // retrieve data from GameModeActivity
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            sudokuPuzzleString = bundle.getString("sudokuPuzzleString");
            levelOfDifficulty = bundle.getString("levelOfDifficulty");
            timedChallenge = bundle.getBoolean("timedChallenge");
            timeLimitInMillis = bundle.getLong("timeLimitInMillis");
            timeTakenToSolve = bundle.getLong("timeTakenToSolve");
            completionDateAndTime = bundle.getString("completionDateAndTime");
        }

        // add success data into Firebase database
        addSuccessInstanceIntoFirebase();

        // initialize
        difficultyLevel = findViewById(R.id.success_difficultyLevel);
        timedChallengeYes = findViewById(R.id.success_timedChallengeYes);
        timedChallengeNo = findViewById(R.id.success_timedChallengeNo);
        timeLimit = findViewById(R.id.success_timeLimit);
        timeTaken = findViewById(R.id.success_timeTaken);
        completedOnDate = findViewById(R.id.success_completedOnDate);
        completedOnTime = findViewById(R.id.success_completedOnTime);
        returnButton = findViewById(R.id.success_returnToMainPage);

        // show data on screen
        difficultyLevel.setText(levelOfDifficulty);
        switch (levelOfDifficulty) {
            case "Easy": difficultyLevel.setTextColor(Color.parseColor("#FF079D17")); break;
            case "Medium": difficultyLevel.setTextColor(Color.parseColor("#FFFA8F37")); break;
            case "Hard": difficultyLevel.setTextColor(Color.parseColor("#FFEF0606")); break;
            case "Master": difficultyLevel.setTextColor(Color.parseColor("#FF34128F")); break;
        }
        if (timedChallenge) {
            timedChallengeYes.setChecked(true);
            timeLimit.setText(convertMillisToString(timeLimitInMillis));
        } else {
            timedChallengeNo.setChecked(true);
            timeLimit.setText("---");
        }
        timeTaken.setText(convertMillisToString(timeTakenToSolve));
        String[] output = completionDateAndTime.split(" ");
        completedOnDate.setText(output[0]);
        completedOnTime.setText(output[1]);

        returnButton.setText(Html.fromHtml("<big><b> CLAIM REWARDS </b></big> <br> (Press to return to main page)"));
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuccessActivity.this, MainActivity.class);
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

    private void addSuccessInstanceIntoFirebase() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Users/" + userId + "/" + "SuccessesAndFailures");
        String successId = dataRef.push().getKey();
        SuccessFailure success = new SuccessFailure(successId, sudokuPuzzleString, levelOfDifficulty, timedChallenge, timeLimitInMillis, timeTakenToSolve, completionDateAndTime);
        dataRef.child(successId).setValue(success);
    }
}
