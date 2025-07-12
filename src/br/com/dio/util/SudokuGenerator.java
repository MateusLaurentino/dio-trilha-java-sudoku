package br.com.dio.util;

import java.util.*;

public class SudokuGenerator {

    private static final int SIZE = 9;
    private static final int SUBGRID = 3;
    private static final int CELLS_REMOVE = 40;

    private int[][] solution = new int[SIZE][SIZE];
    private int[][] puzzle = new int[SIZE][SIZE];

    public Map<String, String> generate() {
        fillDiagonalBlocks();
        solveBoard(solution, 0, 0);
        copyBoard(solution, puzzle);
        removeNumbers(puzzle);
        return buildMapFromPuzzle(puzzle, solution);
    }

    private void fillDiagonalBlocks() {
        for (int i = 0; i < SIZE; i += SUBGRID) {
            fillBlock(solution, i, i);
        }
    }

    private void fillBlock(int[][] board, int row, int col) {
        List<Integer> nums = shuffledNumbers();
        for (int i = 0; i < SUBGRID; i++) {
            for (int j = 0; j < SUBGRID; j++) {
                board[row + i][col + j] = nums.remove(0);
            }
        }
    }

    private List<Integer> shuffledNumbers() {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= SIZE; i++) list.add(i);
        Collections.shuffle(list);
        return list;
    }

    private boolean solveBoard(int[][] board, int row, int col) {
        if (row == SIZE) return true;
        if (col == SIZE) return solveBoard(board, row + 1, 0);
        if (board[row][col] != 0) return solveBoard(board, row, col + 1);

        List<Integer> nums = shuffledNumbers();
        for (int num : nums) {
            if (isSafe(board, row, col, num)) {
                board[row][col] = num;
                if (solveBoard(board, row, col + 1)) return true;
                board[row][col] = 0;
            }
        }
        return false;
    }

    private boolean isSafe(int[][] board, int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num) return false;
        }

        int boxRow = row - row % SUBGRID;
        int boxCol = col - col % SUBGRID;

        for (int i = 0; i < SUBGRID; i++) {
            for (int j = 0; j < SUBGRID; j++) {
                if (board[boxRow + i][boxCol + j] == num) return false;
            }
        }

        return true;
    }

    private void copyBoard(int[][] from, int[][] to) {
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(from[i], 0, to[i], 0, SIZE);
        }
    }

    private void removeNumbers(int[][] board) {
        List<int[]> positions = new ArrayList<>();
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                positions.add(new int[]{i, j});

        Collections.shuffle(positions);

        int removed = 0;
        for (int[] pos : positions) {
            if (removed >= CELLS_REMOVE) break;

            int r = pos[0], c = pos[1];
            int backup = board[r][c];
            board[r][c] = 0;

            int[][] testBoard = new int[SIZE][SIZE];
            copyBoard(board, testBoard);
            if (!hasUniqueSolution(testBoard)) {
                board[r][c] = backup;
            } else {
                removed++;
            }
        }
    }

    private boolean hasUniqueSolution(int[][] board) {
        return countSolutions(board, 0, 0, 0) == 1;
    }

    private int countSolutions(int[][] board, int row, int col, int count) {
        if (count > 1) return count;
        if (row == SIZE) return count + 1;
        if (col == SIZE) return countSolutions(board, row + 1, 0, count);
        if (board[row][col] != 0) return countSolutions(board, row, col + 1, count);

        for (int num = 1; num <= SIZE; num++) {
            if (isSafe(board, row, col, num)) {
                board[row][col] = num;
                count = countSolutions(board, row, col + 1, count);
                board[row][col] = 0;
            }
        }

        return count;
    }

    private Map<String, String> buildMapFromPuzzle(int[][] puzzle, int[][] solution) {
        Map<String, String> result = new LinkedHashMap<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                boolean isFixed = puzzle[i][j] != 0;
                int number = isFixed ? puzzle[i][j] : solution[i][j];
                result.put(i + "," + j, number + "," + isFixed);
            }
        }
        return result;
    }
}
