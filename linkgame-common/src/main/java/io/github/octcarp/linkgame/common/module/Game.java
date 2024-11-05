package io.github.octcarp.linkgame.common.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    private static final int GRID_TYPE_NUM = 12;

    // row length
    private final int row;

    // col length
    private final int col;

    // board content
    int[][] board;

    public Game(int[][] board) {
        this.board = board;
        this.row = board.length;
        this.col = board[0].length;
    }

    // randomly initialize the game board
    public static int[][] setupBoard(int row, int col) {
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

    public void shuffleBoard() {
        ArrayList<Integer> pieces = new ArrayList<>();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                pieces.add(board[i][j]);
            }
        }
        Collections.shuffle(pieces);

        int index = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                board[i][j] = pieces.get(index++);
            }
        }
    }

    // judge the validity of an operation
    public List<GridPos> judge(int row1, int col1, int row2, int col2) {
        if ((board[row1][col1] != board[row2][col2]) || (row1 == row2 && col1 == col2)) {
            return null;
        }

        // one line
        List<GridPos> directPath = isDirectlyConnected(row1, col1, row2, col2, board);
        if (directPath != null) {
            return directPath;
        }

        // two lines
        if ((row1 != row2) && (col1 != col2)) {
            if (board[row1][col2] == 0) {
                List<GridPos> path1 = isDirectlyConnected(row1, col1, row1, col2, board);
                List<GridPos> path2 = isDirectlyConnected(row1, col2, row2, col2, board);
                if (path1 != null && path2 != null) {
                    path1.addLast(new GridPos(row1, col2));
                    path1.addAll(path2);
                    return path1;
                }
            }
            if (board[row2][col1] == 0) {
                List<GridPos> path1 = isDirectlyConnected(row1, col1, row2, col1, board);
                List<GridPos> path2 = isDirectlyConnected(row2, col1, row2, col2, board);
                if (path1 != null && path2 != null) {
                    path1.addLast(new GridPos(row2, col1));
                    path1.addAll(path2);
                    return path1;
                }
            }
        }

        // three lines
        if (row1 != row2)
            for (int i = 0; i < board[0].length; i++) {
                if (board[row1][i] == 0 && board[row2][i] == 0) {
                    List<GridPos> path1 = isDirectlyConnected(row1, col1, row1, i, board);
                    List<GridPos> path2 = isDirectlyConnected(row1, i, row2, i, board);
                    List<GridPos> path3 = isDirectlyConnected(row2, col2, row2, i, board);
                    if (path1 != null && path2 != null && path3 != null) {
                        path1.addLast(new GridPos(row1, i));
                        path1.addAll(path2);
                        path1.addLast(new GridPos(row2, i));
                        path1.addAll(path3);
                        return path1;
                    }
                }
            }
        if (col1 != col2)
            for (int j = 0; j < board.length; j++) {
                if (board[j][col1] == 0 && board[j][col2] == 0) {
                    List<GridPos> path1 = isDirectlyConnected(row1, col1, j, col1, board);
                    List<GridPos> path2 = isDirectlyConnected(j, col1, j, col2, board);
                    List<GridPos> path3 = isDirectlyConnected(row2, col2, j, col2, board);
                    if (path1 != null && path2 != null && path3 != null) {
                        path1.addLast(new GridPos(j, col1));
                        path1.addAll(path2);
                        path1.addLast(new GridPos(j, col2));
                        path1.addAll(path3);
                        return path1;
                    }
                }
            }

        return null;
    }

    // judge whether
    private List<GridPos> isDirectlyConnected(int row1, int col1, int row2, int col2, int[][] board) {
        List<GridPos> path = new ArrayList<>();
        if (row1 == row2) {
            if (col1 < col2) {
                for (int col = col1 + 1; col < col2; col++) {
                    if (board[row1][col] != 0) {
                        return null;
                    }
                    path.add(new GridPos(row1, col));
                }
            } else {
                for (int col = col1 - 1; col > col2; col--) {
                    if (board[row1][col] != 0) {
                        return null;
                    }
                    path.add(new GridPos(row1, col));
                }
            }
            return path;
        } else if (col1 == col2) {
            if (row1 < row2) {
                for (int row = row1 + 1; row < row2; row++) {
                    if (board[row][col1] != 0) {
                        return null;
                    }
                    path.add(new GridPos(row, col1));
                }
            } else {
                for (int row = row1 - 1; row > row2; row--) {
                    if (board[row][col1] != 0) {
                        return null;
                    }
                    path.add(new GridPos(row, col1));
                }
            }
            return path;
        }
        return null;
    }

    public void clearGrids(int row1, int col1, int row2, int col2) {
        if (board[row1][col1] > 0) {
            board[row1][col1] = 0;
            board[row2][col2] = 0;
        }
    }

    public boolean gameFinished() {
        for (int[] row : board) {
            for (int grid : row) {
                if (grid != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int[][] getBoard() {
        return board;
    }
}
