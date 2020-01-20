package com.example.user.sudokuparadise;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HistoryActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ArrayList<SuccessFailure> successFailureList;
    private RecyclerViewAdapter recyclerViewAdapter;
    private Spinner mSortSpinner;
    private RadioButton mAscendingOrder;
    private RadioGroup mSortOrder;
    private Button mClearHistory;
    private TextView mEmptyListMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        successFailureList = new ArrayList<>();
        mRecyclerView = findViewById(R.id.history_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        recyclerViewAdapter = new RecyclerViewAdapter(this, successFailureList);
        mRecyclerView.setAdapter(recyclerViewAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        ItemTouchHelper.Callback callback = new SwipeHelper(recyclerViewAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);

        getFirebaseData(new SuccessFailureCallback() {
            @Override
            public void onCallBack(SuccessFailure successFailureInstance) {
                successFailureList.add(successFailureInstance);
                recyclerViewAdapter.notifyDataSetChanged(); // Updates the List in adapter
            }
        });

        setUpSort();
        mEmptyListMessage = findViewById(R.id.history_emptyListMessage);
        mClearHistory = findViewById(R.id.history_clearHistory);
        mClearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
                builder.setMessage("Are you sure you want to COMPLETELY delete gameplay history?")
                        .setCancelable(true)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                clearHistory(); // delete all SuccessFailures from database and successFailureList (rv adapter's array)
                                recyclerViewAdapter.notifyDataSetChanged();
                                mEmptyListMessage.setVisibility(View.VISIBLE);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("Confirmation");
                alert.show();
            }
        });
    }

    private void getFirebaseData(final SuccessFailureCallback successFailureCallback) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Users/" + userId + "/" + "SuccessesAndFailures");
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {   // Detects changes at the database. Method called if changed. Methods will update successFailureList arraylist and inform adapter of change.
                if (!dataSnapshot.exists()) {
                    mEmptyListMessage.setVisibility(View.VISIBLE);
                }
                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                    String id = String.valueOf(dataSnap.child("id").getValue());
                    String sudokuPuzzleString = String.valueOf(dataSnap.child("sudokuPuzzleString").getValue());
                    String levelOfDifficulty = String.valueOf(dataSnap.child("levelOfDifficulty").getValue());
                    boolean timedChallenge = (boolean) dataSnap.child("timedChallenge").getValue();
                    long timeLimitInMillis = (long) dataSnap.child("timeLimitInMillis").getValue();
                    long timeTakenToSolve = (long) dataSnap.child("timeTakenToSolve").getValue();
                    String timeStamp = String.valueOf(dataSnap.child("timeStamp").getValue());

                    SuccessFailure successFailureInstance = new SuccessFailure(id,sudokuPuzzleString,levelOfDifficulty,timedChallenge,timeLimitInMillis,timeTakenToSolve,timeStamp);
                    successFailureCallback.onCallBack(successFailureInstance);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {} // Handle db error
        });
    }

    public interface SuccessFailureCallback {
        void onCallBack(SuccessFailure successFailureInstance);
    }

    private void setUpSort() {
        // configure spinner
        mSortSpinner = findViewById(R.id.history_sortSpinner);
        ArrayAdapter<CharSequence> adapterForSortSpinner = ArrayAdapter.createFromResource(this,
                R.array.sort_options, R.layout.spinner_sort_item);
        adapterForSortSpinner.setDropDownViewResource(R.layout.spinner_sort_dropdown);
        mSortSpinner.setAdapter(adapterForSortSpinner);

        mAscendingOrder = findViewById(R.id.history_sbAscending);
        mSortOrder = findViewById(R.id.history_sortRadioGroup);

        mSortOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                reverseArray();     // changing from ascending to descending or vice versa will reverse the list for user
                recyclerViewAdapter.notifyDataSetChanged();
            }
        });
        mSortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1: sortByDifficulty(); break;
                    case 2: sortByTimedChallenge(); break;
                    case 3: sortByTimeLimit(); break;
                    case 4: sortByTimeTaken(); break;
                    case 5: sortByDate(); break;
                    case 6: sortByResult(); break;
                }
                recyclerViewAdapter.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void sortByDifficulty() {
        Collections.sort(successFailureList, new Comparator<SuccessFailure>() {
            @Override
            public int compare(SuccessFailure o1, SuccessFailure o2) {
                if (mAscendingOrder.isChecked()) {
                    return getDifficultyScore(o1.getLevelOfDifficulty()) - getDifficultyScore(o2.getLevelOfDifficulty());
                } else {
                    return getDifficultyScore(o2.getLevelOfDifficulty()) - getDifficultyScore(o1.getLevelOfDifficulty());
                }
            }
            private int getDifficultyScore(String difficulty) {
                switch(difficulty) {
                    case "Easy": return 1;
                    case "Medium": return 2;
                    case "Hard": return 3;
                    default: return 4;
                }
            }
        });
    }

    private void sortByTimedChallenge() {
        Collections.sort(successFailureList, new Comparator<SuccessFailure>() {
            @Override
            public int compare(SuccessFailure o1, SuccessFailure o2) {
                if (mAscendingOrder.isChecked()) {
                    return getBooleanScore(o1.isTimedChallenge()) - getBooleanScore(o2.isTimedChallenge());
                } else {
                    return getBooleanScore(o2.isTimedChallenge()) - getBooleanScore(o1.isTimedChallenge());
                }
            }
            private int getBooleanScore(boolean timedChallenge) {
                if (timedChallenge) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
    }

    private void sortByTimeLimit() {
        Collections.sort(successFailureList, new Comparator<SuccessFailure>() {
            @Override
            public int compare(SuccessFailure o1, SuccessFailure o2) {
                int o1TimeLimit = convertToInt(o1.getTimeLimitInMillis());
                int o2TimeLimit = convertToInt(o2.getTimeLimitInMillis());
                if (mAscendingOrder.isChecked()) {
                    return o1TimeLimit - o2TimeLimit;
                } else {
                    return o2TimeLimit - o1TimeLimit;
                }
            }
            private int convertToInt(long timeLimit) {
                if (timeLimit != Long.MAX_VALUE) {
                    return (int) timeLimit;
                } else {
                    return Integer.MAX_VALUE;
                }
            }
        });
    }

    private void sortByTimeTaken() {
        Collections.sort(successFailureList, new Comparator<SuccessFailure>() {
            @Override
            public int compare(SuccessFailure o1, SuccessFailure o2) {
                int o1TimeTaken = convertToInt(o1.getTimeTakenToSolve());
                int o2TimeTaken = convertToInt(o2.getTimeTakenToSolve());
                if (mAscendingOrder.isChecked()) {
                    return o1TimeTaken - o2TimeTaken;
                } else {
                    return o2TimeTaken - o1TimeTaken;
                }
            }
            private int convertToInt(long timeTaken) {
                if (timeTaken != -1) {
                    return (int) timeTaken;
                } else {
                    return Integer.MAX_VALUE;   // -1 = failure instance = never solved puzzle = timeTakenToSolve is infinite
                }
            }
        });
    }

    private void sortByDate() {
        Collections.sort(successFailureList, new Comparator<SuccessFailure>() {
            @Override
            public int compare(SuccessFailure o1, SuccessFailure o2) {
                String o1TimeStamp = o1.getTimeStamp();
                String o2TimeStamp = o2.getTimeStamp();
                LocalDateTime o1DateTime = getLocalDateTime(o1TimeStamp);
                LocalDateTime o2DateTime = getLocalDateTime(o2TimeStamp);
                if (mAscendingOrder.isChecked()) {
                    return o1DateTime.compareTo(o2DateTime);    // uses Java API - LocalDateTime comparison
                } else {
                    return o2DateTime.compareTo(o1DateTime);
                }
            }
            private LocalDateTime getLocalDateTime(String timeStamp) {
                String[] output = timeStamp.split(" ");
                String date = output[0];
                String time = output[1];
                String[] dateSplit = date.split("/");
                int dayOfMonth = Integer.valueOf(dateSplit[0]);
                Month month = Month.of(Integer.valueOf(dateSplit[1]));
                int year = Integer.valueOf(dateSplit[2]);
                String[] timeSplit = time.split(":");
                int hour = Integer.valueOf(timeSplit[0]);
                int minute = Integer.valueOf(timeSplit[1]);
                int seconds = Integer.valueOf(timeSplit[2]);
                return LocalDateTime.of(year, month, dayOfMonth, hour, minute, seconds);
            }
        });
    }

    private void sortByResult() {
        Collections.sort(successFailureList, new Comparator<SuccessFailure>() {
            @Override
            public int compare(SuccessFailure o1, SuccessFailure o2) {
                long o1TimeTaken = o1.getTimeTakenToSolve();
                long o2TimeTaken = o2.getTimeTakenToSolve();
                if (mAscendingOrder.isChecked()) {      // timeTaken = -1 means failure instance
                    if (o1TimeTaken == -1 && o2TimeTaken != -1) {
                        return -1;
                    } else if (o1TimeTaken != -1 && o2TimeTaken == -1) {
                        return 1;
                    } else {
                        return 0;
                    }
                } else {
                    if (o1TimeTaken == -1 && o2TimeTaken != -1) {
                        return 1;
                    } else if (o1TimeTaken != -1 && o2TimeTaken == -1) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            }
        });
    }

    private void reverseArray() {
        Collections.reverse(successFailureList);
    }

    private void clearHistory() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Users/" + userId);
        dataRef.child("SuccessesAndFailures").removeValue();  // remove all data from root "SuccessesAndFailures" and below from database
        successFailureList.clear(); // remove all data from array
    }
}
