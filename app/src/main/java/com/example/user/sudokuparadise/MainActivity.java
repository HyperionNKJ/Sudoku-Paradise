package com.example.user.sudokuparadise;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

public class MainActivity extends AppCompatActivity {
    private ImageView mImageGameMode;
    private ImageView mimageSolverMode;
    private ImageView mImageLogout;
    private ImageView mImageTutorial;
    private ImageView mImageTips;
    private ImageView mImageHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if user is logged in. If user is not logged in, direct user to login page
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        mImageGameMode = findViewById(R.id.imageView_gameMode);
        mimageSolverMode = findViewById(R.id.imageView_solverMode);
        mImageLogout = findViewById(R.id.imageLogout);
        mImageTutorial = findViewById(R.id.imageView_tutorial);
        mImageTips = findViewById(R.id.imageView_tips);
        mImageHistory = findViewById(R.id.imageView_history);

        imageLogoutClickListener();
        imageGameModeClickListener();
        imageSolverModeClickListener();
        imageTutorialClickListener();
        imageTipsClickListener();
        ImageHistoryClickListener();
    }

    private void imageLogoutClickListener() {
        mImageLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder a_builder = new AlertDialog.Builder(MainActivity.this);
                a_builder.setMessage("Are you sure?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Signout user and return user to LoginActivity
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                // Create title for alert message
                AlertDialog alert = a_builder.create();
                alert.setTitle("Logout");
                alert.show();
            }
        });
    }

    private void imageGameModeClickListener() {
        mImageGameMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.game_options_dialog, null);   //game_options_dialog is the pop-up layout
                dialogBuilder.setView(dialogView);

                final Spinner mSpinnerLOD = dialogView.findViewById(R.id.spinnerLOD);
                final Spinner mSpinnerTimeLimit = dialogView.findViewById(R.id.spinnerTimeLimit);
                final SwitchCompat mSwitchTimedChallenge = dialogView.findViewById(R.id.switchTimedChallenge);
                final Button mButtonGameOn = dialogView.findViewById(R.id.buttonGameOn);
                final TextView mTextViewTimeLimit = dialogView.findViewById(R.id.textViewTimeLimit);

                dialogBuilder.setCancelable(true);
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                ArrayAdapter<CharSequence> adapterForSpinnerLOD = ArrayAdapter.createFromResource(MainActivity.this,
                        R.array.level_of_difficulty, R.layout.spinnerlod_item);
                ArrayAdapter<CharSequence> adapterForSpinnerTimeLimit = ArrayAdapter.createFromResource(MainActivity.this,
                        R.array.time_limit, R.layout.spinnerlod_item);
                adapterForSpinnerLOD.setDropDownViewResource(R.layout.spinnerlod_item);
                adapterForSpinnerTimeLimit.setDropDownViewResource(R.layout.spinnerlod_item);
                mSpinnerLOD.setAdapter(adapterForSpinnerLOD);
                mSpinnerTimeLimit.setAdapter(adapterForSpinnerTimeLimit);

                mSwitchTimedChallenge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            mSpinnerTimeLimit.setVisibility(View.VISIBLE);
                            mTextViewTimeLimit.setVisibility(View.VISIBLE);
                        } else {
                            mSpinnerTimeLimit.setVisibility(View.INVISIBLE);
                            mTextViewTimeLimit.setVisibility(View.INVISIBLE);
                        }
                    }
                });

                mButtonGameOn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String levelOfDifficulty = mSpinnerLOD.getSelectedItem().toString();
                        boolean timedChallenge = mSwitchTimedChallenge.isChecked();
                        long timeLimitInMillis = Long.MAX_VALUE;
                        if (timedChallenge) {
                            switch (mSpinnerTimeLimit.getSelectedItem().toString()) {
                                case "5 min": timeLimitInMillis = 300000; break;
                                case "10 min": timeLimitInMillis = 600000; break;
                                case "20 min": timeLimitInMillis = 1200000; break;
                                case "30 min": timeLimitInMillis = 1800000; break;
                                case "45 min": timeLimitInMillis = 2700000; break;
                                case "1 hour": timeLimitInMillis = 3600000; break;
                                case "2 hour": timeLimitInMillis = 7200000; break;
                            }
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString("levelOfDifficulty", levelOfDifficulty);
                        bundle.putBoolean("timedChallenge", timedChallenge);
                        bundle.putLong("timeLimitInMillis", timeLimitInMillis);
                        Intent intent = new Intent(dialogView.getContext(),GameModeActivity.class);
                        intent.putExtras(bundle);
                        dialogView.getContext().startActivity(intent);
                        alertDialog.dismiss();
                    }
                });
            }
        });
    }

    private void imageSolverModeClickListener() {
        mimageSolverMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SolverModeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void imageTutorialClickListener() {
        mImageTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.tutorial_dialog, null);   //game_options_dialog is the pop-up layout
                dialogBuilder.setView(dialogView).setCancelable(true);
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
                ImageView closeButton = (ImageView)alertDialog.findViewById(R.id.close_button);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                TextView tutorialMessage = (TextView)alertDialog.findViewById(R.id.textView_tutorialMessage);
                tutorialMessage.setText(Html.fromHtml(" <h4> What is Sudoku? </h4>" +
                        "<p> Sudoku is a single-player puzzle game that consists of 81 cells divided into nine rows, nine columns and nine 3x3 regions, forming a 9x9 square grid. <br>Some of the cells are pre-filled with a digit from 1 to 9, while the rest are empty. <br>A well-formed Sudoku puzzle has only one solution with at least 17 pre-filled cells. </p>" +
                        "<h4> Rules and Objectives: </h4>" +
                        "<p> Complete the 9x9 grid by filling in all of the empty cells with a digit such that every row, column and 3x3 region has exactly ONE of every digit from 1 to 9. <br>In other words, no duplicates are allowed in any row, column or region. </p>" +
                        "<h4> Game Mode: </h4>" +
                        "<p> An environment for player to solve an arbitrarily chosen puzzle based on the level of difficulty indicated. <br>Time constraints can be introduced at the discretion of the player to make the game more exciting and challenging. </p>" +
                        "<u> Game Mode User Interface </u>" +
                        "<p> <b>\"Hint\"</b> - Stuck and unable to proceed on with the puzzle? Want to check your current input to see if you are on the right track? <br>Use this feature to select any cell you would like the answer to, and we will reveal its correct digit right before your eyes! <br>Press \"Cancel\" should you change your mind and not want any hint. </p>"+
                        "<p> <b>\"Reset\"</b> - Messed up the puzzle and want to start over again? <br>Use this feature to reset the puzzle to its original state. No extra time will be given! </p>" +
                        "<p> <b>\"Tutorial\"</b> - Unclear of the rules and objectives of Sudoku? Want to know the functionality of each button? <br>Use this feature to bring this page back up. </p>" +
                        "<p> <b>\"Tips\"</b> - Want to learn the fundamental techniques to solving Sudoku? Or are you looking for more advanced solving techniques to up your game? <br>Use this feature and we will teach you several techniques to solve almost any puzzle. </p>" +
                        "<p> <b>\"Timer\"</b> - Only for timed challenge. Timer shows you how much time you have left to solve the puzzle. Game over if it hits zero! </p>" +
                        "<p> <b>\"Pause\"</b> - Need to rest your eyes and mind? Want to take a breather from the timed Sudoku challenge? <br> Use this feature to pause the game. The timer countdown will be paused as well in the event of a timed challenge. <br>Press \"Resume\" to continue with the puzzle whenever you are ready! </p>" +
                        "<p> <b>\"Quit\"</b> - Tired for the day and want to exit game mode? <br>Press this and we will send you back to homepage. </p>" +
                        "<h4> Solver Mode: </h4>" +
                        "<p> Player can manually input a sudoku puzzle that he/she finds elsewhere, and opt to <br>1) have it solved by our solver; OR <br>2) attempt to solve it himself/herself, using our platform to receive assistance in the form of hints and cell validation. <br>Our Sudoku Solver is capable of solving any sudoku puzzle in an instant regardless of the difficulty. </p>" +
                        "<u> Solver Mode User Interface </u>" +
                        "<p> <b>\"Solve\"</b> - Want the complete solution to a Sudoku puzzle? <br>Copy the original puzzle into the 9x9 grid and press this button to execute the solver's algorithm. The solution would appear on screen if there is one, otherwise, you will be notified of its unsolvable nature. </p>" +
                        "<p> <b>\"Unsolve\"</b> - Only visible after you have pressed \"Solve\". Feel like challenging the puzzle yourself after seeing the solution? <br>Use this feature to bring the puzzle back to its pre-solved state. </p>" +
                        "<p> <b>\"Hint\"</b> - Stuck and unable to proceed on with the puzzle? Want to move on but do not wish to see the entire solution? <br>Use this feature to select any cell you would like the solution to, and we will reveal its correct digit for you. <br>Press \"Cancel\" should you change your mind and not want any hint. </p>" +
                        "<p> <b>\"Reset\"</b> - Messed up the puzzle and want to start over again? Want to harness the power of our solver to solve another puzzle? <br>Use this feature to reset all the cells to empty. </p>" +
                        "<p> <b>\"Tutorial\"</b> - Unclear of the rules and objectives of Sudoku? Want to know the functionality of each button? <br>Use this feature to bring this page back up. </p>" +
                        "<p> <b>\"Tips\"</b> - Want to learn the fundamental techniques to solving Sudoku? Or are you looking for more advanced solving techniques to up your game? <br>Use this feature and we will teach you several techniques to solve almost any puzzle. </p>" +
                        "<p> <b>\"Quit\"</b> - Tired for the day and want to exit solver mode? <br>Press this and we will send you back to homepage.</p>"));
                alertDialog.getWindow().setLayout(700, 1090);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });
    }

    private void imageTipsClickListener() {
        mImageTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.tips_dialog);
                ImageView closeButton = (ImageView)dialog.findViewById(R.id.close_button1);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                List<Integer> pagerLayoutArray = new ArrayList<>();
                pagerLayoutArray.add(R.layout.tips1_open_single);
                pagerLayoutArray.add(R.layout.tips2_naked_single);
                pagerLayoutArray.add(R.layout.tips3_hidden_single);
                pagerLayoutArray.add(R.layout.tips4_region_cross_hatching);
                pagerLayoutArray.add(R.layout.tips5_row_column_cross_hatching);
                pagerLayoutArray.add(R.layout.tips6_naked_pair);
                pagerLayoutArray.add(R.layout.tips7_naked_triple);
                pagerLayoutArray.add(R.layout.tips8_hidden_pair);
                pagerLayoutArray.add(R.layout.tips9_hidden_triple);
                pagerLayoutArray.add(R.layout.tips10_x_wing);

                PagerAdapter adapter = new PagerAdapter(MainActivity.this,pagerLayoutArray);
                AutoScrollViewPager pager = (AutoScrollViewPager) dialog.findViewById(R.id.tips_pager);
                pager.setAdapter(adapter);

                CirclePageIndicator pageIndicator = (CirclePageIndicator) dialog.findViewById(R.id.tips_page_indicator);
                pageIndicator.setViewPager(pager);
                pageIndicator.setFillColor(Color.parseColor("#FF00FF")); //fill colour of the selected circle
                pageIndicator.setStrokeColor(Color.parseColor("#000000")); //stroke or the circle's border colour
                pageIndicator.setPageColor(Color.parseColor("#C0C0C0")); //default fill colour of the circle
                pageIndicator.setRadius(7);
                pageIndicator.setCurrentItem(0);

                dialog.show();
                dialog.getWindow().setLayout(700,1135);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });
    }

    private void ImageHistoryClickListener() {
        mImageHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            }
        });
    }
}
