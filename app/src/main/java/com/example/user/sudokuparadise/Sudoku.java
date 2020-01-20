package com.example.user.sudokuparadise;

public class Sudoku {
    private String id = null;
    private int[][] puzzle = new int[9][9];
    private int[][] puzzleSolution = new int[9][9];
    public static final long solveTimeLimit = 6000000000L; // 6 seconds time limit before program timeout

    public Sudoku() {}

    // initialize puzzle based on given sudoku string. Then solves it
    public Sudoku(String sudokuString) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                puzzle[r][c] = Character.getNumericValue(sudokuString.charAt(r*9+c));
            }
        }
        solvePuzzle();
    }

    public void setCell(int row, int column, int value) {
        this.puzzle[row][column] = value;
    }

    public int[][] getPuzzle() {
        return puzzle;
    }

    public String getPuzzleString() {
        StringBuilder sb = new StringBuilder(81);
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                sb.append(puzzle[r][c]);
            }
        }
        return sb.toString();
    }

    public int[][] getPuzzleSolution() {
        return puzzleSolution;
    }

    public int solveCell(int row, int column) {
        boolean isSolved = solvePuzzle();
        if (isSolved) {
            return puzzleSolution[row][column];
        }
        return 0;
    }

    // returns true if solved, false if unsolvable. If true, puzzleSolution will be updated to solution.
    public boolean solvePuzzle() {
        int[][] tempPuzzle = new int[9][9];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                tempPuzzle[r][c] = puzzle[r][c];
            }
        }
        long timeStart = System.nanoTime();
        boolean isSolvedOrHasTimeout = solve(0, 0, timeStart);
        long timeElapsed = System.nanoTime() - timeStart;
        if (isSolvedOrHasTimeout && (timeElapsed < solveTimeLimit)) {
            puzzleSolution = puzzle;
            puzzle = tempPuzzle;
            return true;
        }
        puzzle = tempPuzzle;
        return false;
    }

    private boolean solve(int row, int column, long timeStart) {
        if (row == 9) {     // Anchor case
            return true;
        }
        if (puzzle[row][column] == 0) {     // "0" denotes empty cell
            for (int i = 1; i <= 9; i++) {
                if (!checkCellViolation(row,column,i)) {
                    puzzle[row][column] = i;
                    boolean isSolvable = solveTheRest(row,column,timeStart);
                    if (isSolvable) {
                        return true;
                    }
                }
            }
            puzzle[row][column] = 0; // Backtrack. Reset to empty cell
        } else {
            boolean isSolvable = solveTheRest(row,column,timeStart);
            if (isSolvable) {
                return true;
            }
        }
        return false;
    }

    private boolean solveTheRest(int row, int column, long timeStart) {
        if (System.nanoTime() - timeStart > solveTimeLimit) {
            return true; // Timeout. Program auto terminates
        }
        if (column != 8) {
            return solve(row,column+1,timeStart);
        } else {
            return solve(row+1,0,timeStart);
        }
    }

    // checks if entire table contains violation or not
    public boolean checkTableViolation() {
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (puzzle[row][column] != 0 && checkCellViolation(row,column,puzzle[row][column])) {
                    return true;
                }
            }
        }
        return false;
    }

    // checks if table[row][column] fits valueToCheck
    private boolean checkCellViolation(int row, int column, int valueToCheck) {
        // Checks for vertical and horizontal duplicates
        for (int j = 0; j < 9; j++) {
            if ((puzzle[row][j] == valueToCheck && j != column) || (puzzle[j][column] == valueToCheck && j != row)) {
                return true;
            }
        }
        // Checks for duplicate in 3x3 zone
        for (int r = row/3*3; r <= row/3*3 + 2; r++) {
            for (int c = column/3*3; c <= column/3*3 + 2; c++) {
                if (r == row && c == column) {
                    continue;
                }
                if (puzzle[r][c] == valueToCheck) {
                    return true;
                }
            }
        }
        return false;
    }
}
