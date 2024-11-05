package io.github.octcarp.linkgame.common.module;

public record GridPos(int x, int y) {
    public GridPos {
        if (x < 0 || y < 0) {
            System.err.printf("Invalid grid position: (%d, %d)\n", x, y);
        }
    }
}
