package io.github.octcarp.linkgame.client.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    private static final int GRID_TYPE_NUM = 12;

    // row length
    int row;

    // col length
    int col;

    // board content
    int[][] board;

    public Game(int[][] board) {
        this.board = board;
        this.row = board.length;
        this.col = board[0].length;
    }

    // randomly initialize the game board
    public static int[][] SetupBoard(int row, int col) {
        final int gridNum = row * col;
        if (gridNum % 2 != 0) {
            throw new IllegalArgumentException("The number of grids must be even.");
        }

        List<Integer> pieces = new ArrayList<>();
        int numPairs = gridNum / 2;
        int piecesPerType = numPairs / GRID_TYPE_NUM;

        for (int i = 1; i <= GRID_TYPE_NUM; i++) {
            for (int j = 0; j < piecesPerType; j++) {
                pieces.add(i);
                pieces.add(i);
            }
        }

        while (pieces.size() < gridNum) {
            int extraPiece = (int) (Math.random() * GRID_TYPE_NUM) + 1;
            pieces.add(extraPiece);
            pieces.add(extraPiece);
        }

        Collections.shuffle(pieces);

        int[][] board = new int[row][col];

        int index = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                board[i][j] = pieces.get(index++);
            }
        }
        return board;
    }

    // judge the validity of an operation
    public boolean judge(int row1, int col1, int row2, int col2) {
        if ((board[row1][col1] != board[row2][col2]) || (row1 == row2 && col1 == col2)) {
            return false;
        }

        // one line
        if (isDirectlyConnected(row1, col1, row2, col2, board)) {
            return true;
        }

        // two lines
        if ((row1 != row2) && (col1 != col2)) {
            if (board[row1][col2] == 0 && isDirectlyConnected(row1, col1, row1, col2, board)
                    && isDirectlyConnected(row1, col2, row2, col2, board))
                return true;
            if (board[row2][col1] == 0 && isDirectlyConnected(row2, col2, row2, col1, board)
                    && isDirectlyConnected(row2, col1, row1, col1, board))
                return true;
        }

        // three lines
        if (row1 != row2)
            for (int i = 0; i < board[0].length; i++) {
                if (board[row1][i] == 0 && board[row2][i] == 0 &&
                        isDirectlyConnected(row1, col1, row1, i, board) && isDirectlyConnected(row1, i, row2, i, board)
                        && isDirectlyConnected(row2, col2, row2, i, board)) {
                    return true;
                }
            }
        if (col1 != col2)
            for (int j = 0; j < board.length; j++) {
                if (board[j][col1] == 0 && board[j][col2] == 0 &&
                        isDirectlyConnected(row1, col1, j, col1, board) && isDirectlyConnected(j, col1, j, col2, board)
                        && isDirectlyConnected(row2, col2, j, col2, board)) {
                    return true;
                }
            }

        return false;
    }

    // judge whether
    private boolean isDirectlyConnected(int row1, int col1, int row2, int col2, int[][] board) {
        if (row1 == row2) {
            int minCol = Math.min(col1, col2);
            int maxCol = Math.max(col1, col2);
            for (int col = minCol + 1; col < maxCol; col++) {
                if (board[row1][col] != 0) {
                    return false;
                }
            }
            return true;
        } else if (col1 == col2) {
            int minRow = Math.min(row1, row2);
            int maxRow = Math.max(row1, row2);
            for (int row = minRow + 1; row < maxRow; row++) {
                if (board[row][col1] != 0) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean clearGrids(int row1, int col1, int row2, int col2) {
        if (board[row1][col1] > 0) {
            board[row1][col1] = 0;
            board[row2][col2] = 0;
            return true;
        }
        return false;
    }
}