package io.github.octcarp.linkgame.common.module;

public record GridPos(int row, int col) {
    public GridPos {
        if (row < 0 || col < 0) {
            System.err.printf("Invalid grid position: (%d, %d)\n", row, col);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GridPos pos) {
            return row == pos.row && col == pos.col;
        }
        return false;
    }
}
