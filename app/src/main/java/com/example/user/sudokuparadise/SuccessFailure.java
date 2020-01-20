package com.example.user.sudokuparadise;

public class SuccessFailure {
    private String id;
    private String sudokuPuzzleString;
    private String levelOfDifficulty;
    private boolean timedChallenge;
    private long timeLimitInMillis;
    private long timeTakenToSolve;  // initialized to -1 if failure instance
    private String timeStamp;

    public SuccessFailure(String id, String sudokuPuzzleString, String levelOfDifficulty, boolean timedChallenge, long timeLimitInMillis, long timeTakenToSolve, String timeStamp) {
        this.id = id;
        this.sudokuPuzzleString = sudokuPuzzleString;
        this.levelOfDifficulty = levelOfDifficulty;
        this.timedChallenge = timedChallenge;
        this.timeLimitInMillis = timeLimitInMillis;
        this.timeTakenToSolve = timeTakenToSolve;
        this.timeStamp = timeStamp;
    }

    public String getId() {
        return id;
    }

    public String getSudokuPuzzleString() {
        return sudokuPuzzleString;
    }

    public String getLevelOfDifficulty() {
        return levelOfDifficulty;
    }

    public boolean isTimedChallenge() {
        return timedChallenge;
    }

    public long getTimeLimitInMillis() {
        return timeLimitInMillis;
    }

    public long getTimeTakenToSolve() {
        return timeTakenToSolve;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
}
