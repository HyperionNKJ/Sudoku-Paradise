package com.example.user.sudokuparadise;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private Context mContext;
    private List<SuccessFailure> successFailureList;

    public RecyclerViewAdapter(Context context, List<SuccessFailure> successFailureList){
        this.mContext = context;
        this.successFailureList = successFailureList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_cardview, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final SuccessFailure successFailureInstance = successFailureList.get(position);

        // display level of difficulty
        final String levelOfDifficulty = successFailureInstance.getLevelOfDifficulty();
        holder.mLevelOfDifficulty.setText(levelOfDifficulty);
        switch (levelOfDifficulty) {
            case "Easy": holder.mLevelOfDifficulty.setTextColor(Color.parseColor("#FF079D17")); break;
            case "Medium": holder.mLevelOfDifficulty.setTextColor(Color.parseColor("#FFFA8F37")); break;
            case "Hard": holder.mLevelOfDifficulty.setTextColor(Color.parseColor("#FFEF0606")); break;
            case "Master": holder.mLevelOfDifficulty.setTextColor(Color.parseColor("#FF34128F")); break;
        }
        // display timed challenge and time limit
        final boolean timedChallenge = successFailureInstance.isTimedChallenge();
        final long timeLimit = successFailureInstance.getTimeLimitInMillis();
        if (timedChallenge) {
            holder.mTimedChallenge.setBackgroundResource(R.drawable.tick_icon);
            holder.mTimedChallenge.setText("");
            holder.mTimeLimit.setText(convertMillisToString(timeLimit));
        } else {
            holder.mTimedChallenge.setText("---");
            holder.mTimedChallenge.setBackgroundResource(0);
            holder.mTimeLimit.setText("---");
        }
        // display time taken to solve (-1 = failure instance) and final result (success/failure)
        final long timeTakenToSolve = successFailureInstance.getTimeTakenToSolve();
        if (timeTakenToSolve == -1) {
            holder.mTimeTakenToSolve.setText("---");
            holder.mResult.setText("FAILURE");
            holder.mResult.setTextColor(Color.parseColor("#FFCC0000"));
        } else {
            holder.mTimeTakenToSolve.setText(convertMillisToString(timeTakenToSolve));
            holder.mResult.setText("SUCCESS");
            holder.mResult.setTextColor(Color.parseColor("#FF669900"));
        }
        // display time stamp
        final String timeStamp = successFailureInstance.getTimeStamp();
        holder.mTimeStamp.setText(timeStamp);

        // when an entry is pressed, dialog pops up with game info and button to retry puzzle
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = LayoutInflater.from(mContext);
                final View dialogView = inflater.inflate(R.layout.history_dialog, null);
                alertBuilder.setView(dialogView);

                TextView hLevelOfDifficulty = dialogView.findViewById(R.id.hDialog_levelOfDifficulty);
                TextView hTimedChallenge = dialogView.findViewById(R.id.hDialog_timedChallenge);
                TextView hTimedLimit = dialogView.findViewById(R.id.hDialog_timeLimit);
                TextView hTimeTaken = dialogView.findViewById(R.id.hDialog_timeTaken);
                TextView hTimeStamp = dialogView.findViewById(R.id.hDialog_timeStamp);
                TextView hResult = dialogView.findViewById(R.id.hDialog_result);
                Button hRetry = dialogView.findViewById(R.id.hDialog_retry);
                ImageView hClose = dialogView.findViewById(R.id.hDialog_close);

                hLevelOfDifficulty.setText(levelOfDifficulty);
                hTimedChallenge.setText(convertYesNo(timedChallenge));
                hTimedLimit.setText(convertMillisToStringWithSeconds(timeLimit));
                hTimeTaken.setText(convertMillisToStringWithSeconds(timeTakenToSolve));
                hTimeStamp.setText(timeStamp);
                hResult.setText(determineResult(timeTakenToSolve));

                final AlertDialog alertDialog = alertBuilder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();

                hRetry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("levelOfDifficulty", levelOfDifficulty);
                        bundle.putBoolean("timedChallenge", timedChallenge);
                        bundle.putLong("timeLimitInMillis", timeLimit);
                        bundle.putString("sudokuString", successFailureInstance.getSudokuPuzzleString());
                        Intent intent = new Intent(dialogView.getContext(), GameModeActivity.class);
                        intent.putExtras(bundle);
                        dialogView.getContext().startActivity(intent);
                        alertDialog.dismiss();
                        ((Activity) mContext).finish();
                    }
                });
                hClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
            private String convertYesNo(boolean value) {
                if (value) {
                    return "Yes";
                } else {
                    return "No";
                }
            }
            private String determineResult(long timeTaken) {
                if (timeTaken != -1) {
                    return "Puzzle was solved. Success!";
                } else {
                    return "Exceeded time limit. Failure...";
                }
            }
            private String convertMillisToStringWithSeconds(long timeInMillis) {
                if (timeInMillis == Long.MAX_VALUE) {   // if no time restriction (timedChallenge = false)
                    return "No time limit";
                }
                if (timeInMillis == -1) {   // if failure instance
                    return "Was not solved";
                }
                int hours = (int) (timeInMillis / 1000) / 3600;
                int minutes = (int) (timeInMillis / 1000) % 3600 / 60;
                int seconds = (int) (timeInMillis / 1000) % 3600 % 60;
                if (hours == 0) {
                    return String.format(Locale.getDefault(), "%dm %02ds", minutes, seconds);
                }
                return String.format(Locale.getDefault(),"%dh %02dm %02ds", hours, minutes, seconds);
            }
        });
    }

    private String convertMillisToString(long timeInMillis) {
        int hours = (int) (timeInMillis / 1000) / 3600;
        int minutes = (int) (timeInMillis / 1000) % 3600 / 60;
        if (hours == 0) {
            return String.format(Locale.getDefault(), "%d min", minutes);
        }
        return String.format(Locale.getDefault(),"%dh %dm", hours, minutes);
    }

   @Override
    public int getItemCount() {
        return successFailureList.size();
    }

    // for swipe delete
    public void deleteSuccessFailureInstance(int pos) {
        SuccessFailure successFailure = successFailureList.get(pos);
        successFailureList.remove(pos);
        this.notifyItemRemoved(pos);
        if (successFailureList.isEmpty()) {
            TextView mEmptyListMessage = ((Activity)mContext).findViewById(R.id.history_emptyListMessage);
            mEmptyListMessage.setVisibility(View.VISIBLE);
        }

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Users/" + userId + "/" + "SuccessesAndFailures");
        dataRef.child(successFailure.getId()).removeValue();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mLevelOfDifficulty;
        public TextView mTimedChallenge;
        public TextView mTimeLimit;
        public TextView mTimeTakenToSolve;
        public TextView mTimeStamp;
        public TextView mResult;
        public CardView mCardView;

        public ViewHolder(View view) {
            super(view);
            mLevelOfDifficulty = view.findViewById(R.id.history_levelOfDifficulty);
            mTimedChallenge = view.findViewById(R.id.history_timedChallenge);
            mTimeLimit = view.findViewById(R.id.history_timeLimit);
            mTimeTakenToSolve = view.findViewById(R.id.history_timeTakenToSolve);
            mTimeStamp = view.findViewById(R.id.history_timeStamp);
            mResult = view.findViewById(R.id.history_result);
            mCardView = view.findViewById(R.id.history_cardView);
        }
    }
}
