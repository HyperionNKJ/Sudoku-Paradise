package com.example.user.sudokuparadise;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

public class SolverModeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private Spinner[][] cell_spinners = new Spinner[9][9];
    private TextView[][] cell_textViews = new TextView[9][9];
    private Button solveButton;
    private Button unsolveButton;
    private Button resetButton;
    private Button hintButton;
    private Button hintCancelButton;
    private Button tutorialButton;
    private Button tipsButton;
    private Button quitButton;
    private TextView errorMessage;
    private TextView finalMessage;
    private TextView timeMessage;
    private TextView hintMessage;
    private TableLayout mTableSpinnerLayout;
    private TableLayout mTableTextViewLayout;
    private Sudoku sudoku = new Sudoku();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solver_mode);

        solveButton = (Button)findViewById(R.id.button_solve);
        unsolveButton = (Button)findViewById(R.id.button_unsolve);
        hintButton = (Button)findViewById(R.id.button_hint);
        hintCancelButton = (Button)findViewById(R.id.button_hintCancel);
        resetButton = (Button)findViewById(R.id.button_reset);
        tutorialButton = (Button)findViewById(R.id.button_tutorial);
        tipsButton = (Button)findViewById(R.id.button_tips);
        quitButton = (Button)findViewById(R.id.button_quit);
        unsolveButton.setVisibility(View.INVISIBLE);
        hintCancelButton.setVisibility(View.INVISIBLE);
        errorMessage = (TextView)findViewById(R.id.textView_errorMessage);
        finalMessage= (TextView)findViewById(R.id.textView_finalMessage);
        timeMessage = (TextView)findViewById(R.id.textView_timeMessage);
        hintMessage = (TextView)findViewById(R.id.textView_hintMessage);

        mTableSpinnerLayout = (TableLayout)findViewById(R.id.tableSpinnerLayout);
        mTableTextViewLayout = (TableLayout)findViewById(R.id.tableTextViewLayout);
        mTableTextViewLayout.setVisibility(View.INVISIBLE);

        cell_spinners[0][0] = (Spinner) findViewById(R.id.cell_00);     cell_textViews[0][0] = (TextView) findViewById(R.id.tcell_00);
        cell_spinners[0][1] = (Spinner) findViewById(R.id.cell_01);     cell_textViews[0][1] = (TextView) findViewById(R.id.tcell_01);
        cell_spinners[0][2] = (Spinner) findViewById(R.id.cell_02);     cell_textViews[0][2] = (TextView) findViewById(R.id.tcell_02);
        cell_spinners[0][3] = (Spinner) findViewById(R.id.cell_03);     cell_textViews[0][3] = (TextView) findViewById(R.id.tcell_03);
        cell_spinners[0][4] = (Spinner) findViewById(R.id.cell_04);     cell_textViews[0][4] = (TextView) findViewById(R.id.tcell_04);
        cell_spinners[0][5] = (Spinner) findViewById(R.id.cell_05);     cell_textViews[0][5] = (TextView) findViewById(R.id.tcell_05);
        cell_spinners[0][6] = (Spinner) findViewById(R.id.cell_06);     cell_textViews[0][6] = (TextView) findViewById(R.id.tcell_06);
        cell_spinners[0][7] = (Spinner) findViewById(R.id.cell_07);     cell_textViews[0][7] = (TextView) findViewById(R.id.tcell_07);
        cell_spinners[0][8] = (Spinner) findViewById(R.id.cell_08);     cell_textViews[0][8] = (TextView) findViewById(R.id.tcell_08);
        cell_spinners[1][0] = (Spinner) findViewById(R.id.cell_10);     cell_textViews[1][0] = (TextView) findViewById(R.id.tcell_10);
        cell_spinners[1][1] = (Spinner) findViewById(R.id.cell_11);     cell_textViews[1][1] = (TextView) findViewById(R.id.tcell_11);
        cell_spinners[1][2] = (Spinner) findViewById(R.id.cell_12);     cell_textViews[1][2] = (TextView) findViewById(R.id.tcell_12);
        cell_spinners[1][3] = (Spinner) findViewById(R.id.cell_13);     cell_textViews[1][3] = (TextView) findViewById(R.id.tcell_13);
        cell_spinners[1][4] = (Spinner) findViewById(R.id.cell_14);     cell_textViews[1][4] = (TextView) findViewById(R.id.tcell_14);
        cell_spinners[1][5] = (Spinner) findViewById(R.id.cell_15);     cell_textViews[1][5] = (TextView) findViewById(R.id.tcell_15);
        cell_spinners[1][6] = (Spinner) findViewById(R.id.cell_16);     cell_textViews[1][6] = (TextView) findViewById(R.id.tcell_16);
        cell_spinners[1][7] = (Spinner) findViewById(R.id.cell_17);     cell_textViews[1][7] = (TextView) findViewById(R.id.tcell_17);
        cell_spinners[1][8] = (Spinner) findViewById(R.id.cell_18);     cell_textViews[1][8] = (TextView) findViewById(R.id.tcell_18);
        cell_spinners[2][0] = (Spinner) findViewById(R.id.cell_20);     cell_textViews[2][0] = (TextView) findViewById(R.id.tcell_20);
        cell_spinners[2][1] = (Spinner) findViewById(R.id.cell_21);     cell_textViews[2][1] = (TextView) findViewById(R.id.tcell_21);
        cell_spinners[2][2] = (Spinner) findViewById(R.id.cell_22);     cell_textViews[2][2] = (TextView) findViewById(R.id.tcell_22);
        cell_spinners[2][3] = (Spinner) findViewById(R.id.cell_23);     cell_textViews[2][3] = (TextView) findViewById(R.id.tcell_23);
        cell_spinners[2][4] = (Spinner) findViewById(R.id.cell_24);     cell_textViews[2][4] = (TextView) findViewById(R.id.tcell_24);
        cell_spinners[2][5] = (Spinner) findViewById(R.id.cell_25);     cell_textViews[2][5] = (TextView) findViewById(R.id.tcell_25);
        cell_spinners[2][6] = (Spinner) findViewById(R.id.cell_26);     cell_textViews[2][6] = (TextView) findViewById(R.id.tcell_26);
        cell_spinners[2][7] = (Spinner) findViewById(R.id.cell_27);     cell_textViews[2][7] = (TextView) findViewById(R.id.tcell_27);
        cell_spinners[2][8] = (Spinner) findViewById(R.id.cell_28);     cell_textViews[2][8] = (TextView) findViewById(R.id.tcell_28);
        cell_spinners[3][0] = (Spinner) findViewById(R.id.cell_30);     cell_textViews[3][0] = (TextView) findViewById(R.id.tcell_30);
        cell_spinners[3][1] = (Spinner) findViewById(R.id.cell_31);     cell_textViews[3][1] = (TextView) findViewById(R.id.tcell_31);
        cell_spinners[3][2] = (Spinner) findViewById(R.id.cell_32);     cell_textViews[3][2] = (TextView) findViewById(R.id.tcell_32);
        cell_spinners[3][3] = (Spinner) findViewById(R.id.cell_33);     cell_textViews[3][3] = (TextView) findViewById(R.id.tcell_33);
        cell_spinners[3][4] = (Spinner) findViewById(R.id.cell_34);     cell_textViews[3][4] = (TextView) findViewById(R.id.tcell_34);
        cell_spinners[3][5] = (Spinner) findViewById(R.id.cell_35);     cell_textViews[3][5] = (TextView) findViewById(R.id.tcell_35);
        cell_spinners[3][6] = (Spinner) findViewById(R.id.cell_36);     cell_textViews[3][6] = (TextView) findViewById(R.id.tcell_36);
        cell_spinners[3][7] = (Spinner) findViewById(R.id.cell_37);     cell_textViews[3][7] = (TextView) findViewById(R.id.tcell_37);
        cell_spinners[3][8] = (Spinner) findViewById(R.id.cell_38);     cell_textViews[3][8] = (TextView) findViewById(R.id.tcell_38);
        cell_spinners[4][0] = (Spinner) findViewById(R.id.cell_40);     cell_textViews[4][0] = (TextView) findViewById(R.id.tcell_40);
        cell_spinners[4][1] = (Spinner) findViewById(R.id.cell_41);     cell_textViews[4][1] = (TextView) findViewById(R.id.tcell_41);
        cell_spinners[4][2] = (Spinner) findViewById(R.id.cell_42);     cell_textViews[4][2] = (TextView) findViewById(R.id.tcell_42);
        cell_spinners[4][3] = (Spinner) findViewById(R.id.cell_43);     cell_textViews[4][3] = (TextView) findViewById(R.id.tcell_43);
        cell_spinners[4][4] = (Spinner) findViewById(R.id.cell_44);     cell_textViews[4][4] = (TextView) findViewById(R.id.tcell_44);
        cell_spinners[4][5] = (Spinner) findViewById(R.id.cell_45);     cell_textViews[4][5] = (TextView) findViewById(R.id.tcell_45);
        cell_spinners[4][6] = (Spinner) findViewById(R.id.cell_46);     cell_textViews[4][6] = (TextView) findViewById(R.id.tcell_46);
        cell_spinners[4][7] = (Spinner) findViewById(R.id.cell_47);     cell_textViews[4][7] = (TextView) findViewById(R.id.tcell_47);
        cell_spinners[4][8] = (Spinner) findViewById(R.id.cell_48);     cell_textViews[4][8] = (TextView) findViewById(R.id.tcell_48);
        cell_spinners[5][0] = (Spinner) findViewById(R.id.cell_50);     cell_textViews[5][0] = (TextView) findViewById(R.id.tcell_50);
        cell_spinners[5][1] = (Spinner) findViewById(R.id.cell_51);     cell_textViews[5][1] = (TextView) findViewById(R.id.tcell_51);
        cell_spinners[5][2] = (Spinner) findViewById(R.id.cell_52);     cell_textViews[5][2] = (TextView) findViewById(R.id.tcell_52);
        cell_spinners[5][3] = (Spinner) findViewById(R.id.cell_53);     cell_textViews[5][3] = (TextView) findViewById(R.id.tcell_53);
        cell_spinners[5][4] = (Spinner) findViewById(R.id.cell_54);     cell_textViews[5][4] = (TextView) findViewById(R.id.tcell_54);
        cell_spinners[5][5] = (Spinner) findViewById(R.id.cell_55);     cell_textViews[5][5] = (TextView) findViewById(R.id.tcell_55);
        cell_spinners[5][6] = (Spinner) findViewById(R.id.cell_56);     cell_textViews[5][6] = (TextView) findViewById(R.id.tcell_56);
        cell_spinners[5][7] = (Spinner) findViewById(R.id.cell_57);     cell_textViews[5][7] = (TextView) findViewById(R.id.tcell_57);
        cell_spinners[5][8] = (Spinner) findViewById(R.id.cell_58);     cell_textViews[5][8] = (TextView) findViewById(R.id.tcell_58);
        cell_spinners[6][0] = (Spinner) findViewById(R.id.cell_60);     cell_textViews[6][0] = (TextView) findViewById(R.id.tcell_60);
        cell_spinners[6][1] = (Spinner) findViewById(R.id.cell_61);     cell_textViews[6][1] = (TextView) findViewById(R.id.tcell_61);
        cell_spinners[6][2] = (Spinner) findViewById(R.id.cell_62);     cell_textViews[6][2] = (TextView) findViewById(R.id.tcell_62);
        cell_spinners[6][3] = (Spinner) findViewById(R.id.cell_63);     cell_textViews[6][3] = (TextView) findViewById(R.id.tcell_63);
        cell_spinners[6][4] = (Spinner) findViewById(R.id.cell_64);     cell_textViews[6][4] = (TextView) findViewById(R.id.tcell_64);
        cell_spinners[6][5] = (Spinner) findViewById(R.id.cell_65);     cell_textViews[6][5] = (TextView) findViewById(R.id.tcell_65);
        cell_spinners[6][6] = (Spinner) findViewById(R.id.cell_66);     cell_textViews[6][6] = (TextView) findViewById(R.id.tcell_66);
        cell_spinners[6][7] = (Spinner) findViewById(R.id.cell_67);     cell_textViews[6][7] = (TextView) findViewById(R.id.tcell_67);
        cell_spinners[6][8] = (Spinner) findViewById(R.id.cell_68);     cell_textViews[6][8] = (TextView) findViewById(R.id.tcell_68);
        cell_spinners[7][0] = (Spinner) findViewById(R.id.cell_70);     cell_textViews[7][0] = (TextView) findViewById(R.id.tcell_70);
        cell_spinners[7][1] = (Spinner) findViewById(R.id.cell_71);     cell_textViews[7][1] = (TextView) findViewById(R.id.tcell_71);
        cell_spinners[7][2] = (Spinner) findViewById(R.id.cell_72);     cell_textViews[7][2] = (TextView) findViewById(R.id.tcell_72);
        cell_spinners[7][3] = (Spinner) findViewById(R.id.cell_73);     cell_textViews[7][3] = (TextView) findViewById(R.id.tcell_73);
        cell_spinners[7][4] = (Spinner) findViewById(R.id.cell_74);     cell_textViews[7][4] = (TextView) findViewById(R.id.tcell_74);
        cell_spinners[7][5] = (Spinner) findViewById(R.id.cell_75);     cell_textViews[7][5] = (TextView) findViewById(R.id.tcell_75);
        cell_spinners[7][6] = (Spinner) findViewById(R.id.cell_76);     cell_textViews[7][6] = (TextView) findViewById(R.id.tcell_76);
        cell_spinners[7][7] = (Spinner) findViewById(R.id.cell_77);     cell_textViews[7][7] = (TextView) findViewById(R.id.tcell_77);
        cell_spinners[7][8] = (Spinner) findViewById(R.id.cell_78);     cell_textViews[7][8] = (TextView) findViewById(R.id.tcell_78);
        cell_spinners[8][0] = (Spinner) findViewById(R.id.cell_80);     cell_textViews[8][0] = (TextView) findViewById(R.id.tcell_80);
        cell_spinners[8][1] = (Spinner) findViewById(R.id.cell_81);     cell_textViews[8][1] = (TextView) findViewById(R.id.tcell_81);
        cell_spinners[8][2] = (Spinner) findViewById(R.id.cell_82);     cell_textViews[8][2] = (TextView) findViewById(R.id.tcell_82);
        cell_spinners[8][3] = (Spinner) findViewById(R.id.cell_83);     cell_textViews[8][3] = (TextView) findViewById(R.id.tcell_83);
        cell_spinners[8][4] = (Spinner) findViewById(R.id.cell_84);     cell_textViews[8][4] = (TextView) findViewById(R.id.tcell_84);
        cell_spinners[8][5] = (Spinner) findViewById(R.id.cell_85);     cell_textViews[8][5] = (TextView) findViewById(R.id.tcell_85);
        cell_spinners[8][6] = (Spinner) findViewById(R.id.cell_86);     cell_textViews[8][6] = (TextView) findViewById(R.id.tcell_86);
        cell_spinners[8][7] = (Spinner) findViewById(R.id.cell_87);     cell_textViews[8][7] = (TextView) findViewById(R.id.tcell_87);
        cell_spinners[8][8] = (Spinner) findViewById(R.id.cell_88);     cell_textViews[8][8] = (TextView) findViewById(R.id.tcell_88);

        ArrayAdapter<CharSequence> adapterForCell = ArrayAdapter.createFromResource(this,
                R.array.cell_options, R.layout.spinnercell_item);
        adapterForCell.setDropDownViewResource(R.layout.spinnercell_item);

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                cell_spinners[r][c].setOnItemSelectedListener(this);
                cell_spinners[r][c].setAdapter(adapterForCell);
                cell_textViews[r][c].setOnClickListener(this); // for hint feature
                cell_textViews[r][c].setClickable(false);
            }
        }

        solveButtonClickListener();
        unsolveButtonClickListener();
        resetButtonClickListener();
        hintButtonClickListener();
        hintCancelButtonClickListener();
        tutorialButtonClickListener();
        tipsButtonClickListener();
        quitButtonClickListener();
    }

    // called whenever cell_spinners[r][c].setSelection() is executed
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String input = parent.getSelectedItem().toString();
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (parent.getId() == cell_spinners[r][c].getId()) {
                    if (!input.equals("")) {
                        sudoku.setCell(r, c, Integer.parseInt(input));
                    } else {
                        sudoku.setCell(r, c, 0);
                    }
                    if (sudoku.checkTableViolation()) {
                        setButtonsVisibility(View.INVISIBLE,View.INVISIBLE,View.INVISIBLE,View.INVISIBLE);
                        errorMessage.setText(R.string.message_inputError);
                    } else {
                        setButtonsVisibility(View.VISIBLE,View.INVISIBLE,View.VISIBLE,View.INVISIBLE);
                        errorMessage.setText("");
                    }
                    return;
                }
            }
        }
    }

    @Override // for hint feature
    public void onClick(View v) {
        int row = 0;
        int column = 0;
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                cell_textViews[r][c].setClickable(false);
                if (cell_textViews[r][c].getId() == v.getId()) {
                    row = r;
                    column = c;
                }
            }
        }
        hintMessage.setText("");
        int hintDigit = sudoku.solveCell(row,column);
        if (hintDigit != 0) {
            cell_spinners[row][column].setSelection(hintDigit);
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    if (!cell_spinners[r][c].getSelectedItem().toString().equals("")) {
                        cell_spinners[r][c].setEnabled(false); // to prevent user from changing original puzzle, rendering hint useless
                    }
                }
            }
        } else {
            Toast.makeText(SolverModeActivity.this, "There is no solution to this puzzle!", Toast.LENGTH_SHORT).show();
        }
        setTableLayoutsVisibility(View.VISIBLE,View.INVISIBLE);
        setButtonsVisibility(View.VISIBLE,View.INVISIBLE,View.VISIBLE,View.INVISIBLE);
    }

    public void solveButtonClickListener() {
        solveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long timeStart = System.nanoTime();
                boolean isSolved = sudoku.solvePuzzle();
                long timeEnd = System.nanoTime();
                if (isSolved) {
                    showTableInTextViewLayout(sudoku.getPuzzleSolution());
                    String time = "Solve time:\n" + String.format("%.3f",(timeEnd - timeStart)/1000000.00) + "ms";
                    timeMessage.setText(time);
                    finalMessage.setTextColor(Color.GREEN);
                    finalMessage.setTextSize(18);
                    finalMessage.setText(R.string.message_solved);
                    setButtonsVisibility(View.INVISIBLE,View.VISIBLE,View.INVISIBLE,View.INVISIBLE);
                } else {
                    showTableInTextViewLayout(sudoku.getPuzzle());
                    finalMessage.setTextColor(Color.RED);
                    finalMessage.setTextSize(15);
                    finalMessage.setText(R.string.message_cannotBeSolved);
                    setButtonsVisibility(View.INVISIBLE,View.INVISIBLE,View.INVISIBLE,View.INVISIBLE);
                }
            }
        });
    }

    public void unsolveButtonClickListener() {
        unsolveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTableLayoutsVisibility(View.VISIBLE,View.INVISIBLE);
                finalMessage.setText("");
                timeMessage.setText("");
                setButtonsVisibility(View.VISIBLE,View.INVISIBLE,View.VISIBLE,View.INVISIBLE);
            }
        });
    }

    public void resetButtonClickListener() {
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SolverModeActivity.this);
                builder.setMessage("Are you sure you want to empty all the cells?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setTableLayoutsVisibility(View.VISIBLE,View.INVISIBLE);
                                for (int r = 0; r < 9; r++) {
                                    for (int c = 0; c < 9; c++) {
                                        cell_spinners[r][c].setSelection(0); // Also initializes sudoku.puzzle to '0' because of onItemSelected listener!
                                        cell_spinners[r][c].setEnabled(true);
                                        cell_textViews[r][c].setClickable(false);
                                    }
                                }
                                setButtonsVisibility(View.VISIBLE,View.INVISIBLE,View.VISIBLE,View.INVISIBLE);
                                finalMessage.setText("");
                                timeMessage.setText("");
                                hintMessage.setText("");
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("Reset?");
                alert.show();
            }
        });
    }

    public void hintButtonClickListener() {
        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[][] puzzle = sudoku.getPuzzle();
                for (int r = 0; r < 9; r++) {
                    for (int c = 0; c < 9; c++) {
                        if (puzzle[r][c] == 0) {
                            cell_textViews[r][c].setClickable(true);
                        }
                    }
                }
                showTableInTextViewLayout(puzzle);
                hintMessage.setText(R.string.message_hint);
                setButtonsVisibility(View.INVISIBLE,View.INVISIBLE,View.INVISIBLE,View.VISIBLE);
            }
        });
    }

    public void hintCancelButtonClickListener() {
        hintCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int r = 0; r < 9; r++) {
                    for (int c = 0; c < 9; c++) {
                        cell_textViews[r][c].setClickable(false);
                    }
                }
                setTableLayoutsVisibility(View.VISIBLE,View.INVISIBLE);
                hintMessage.setText("");
                setButtonsVisibility(View.VISIBLE,View.INVISIBLE,View.VISIBLE,View.INVISIBLE);
            }
        });
    }

    private void tutorialButtonClickListener() {
        tutorialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SolverModeActivity.this);
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

    private void tipsButtonClickListener() {
        tipsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(SolverModeActivity.this);
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

                PagerAdapter adapter = new PagerAdapter(SolverModeActivity.this,pagerLayoutArray);
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

    private void quitButtonClickListener() {
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SolverModeActivity.this);
                builder.setMessage("Return to main page?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("Quit?");
                alert.show();
            }
        });
    }

    private void showTableInTextViewLayout(int[][] tableToShow) {
        setTableLayoutsVisibility(View.INVISIBLE,View.VISIBLE);
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int digitToShow = tableToShow[r][c];
                if (digitToShow != 0) {
                    cell_textViews[r][c].setText(String.valueOf(digitToShow));
                } else {
                    cell_textViews[r][c].setText("");
                }
            }
        }
    }

    private void setButtonsVisibility(int solveVisibility, int unsolveVisibility, int hintVisibility, int hintCancelVisibility) {
        solveButton.setVisibility(solveVisibility);
        unsolveButton.setVisibility(unsolveVisibility);
        hintButton.setVisibility(hintVisibility);
        hintCancelButton.setVisibility(hintCancelVisibility);
    }

    private void setTableLayoutsVisibility(int spinnerLayoutVisibility, int textViewLayoutVisibility) {
        mTableSpinnerLayout.setVisibility(spinnerLayoutVisibility);
        mTableTextViewLayout.setVisibility(textViewLayoutVisibility);
    }

    public void onNothingSelected(AdapterView<?> parent) {}
}