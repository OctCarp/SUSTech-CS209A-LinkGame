package io.github.octcarp.linkgame.common.module;

import java.io.Serializable;
import java.util.Random;

public class Match implements Serializable {
    private final Player player1;

    private final Player player2;

    private int player1Score;

    private int player2Score;

    private int whoChoseSize;

    private int currentMove;

    private Game game;

    public Match(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.player1Score = 0;
        this.player2Score = 0;
        this.whoChoseSize = new Random().nextInt(2) + 1;
        this.currentMove = whoChoseSize;
    }

    public void initGame(int row, int col) {
        game = new Game(Game.setupBoard(row, col));
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public int getWhoChoseSize() {
        return whoChoseSize;
    }

    public int getCurrentMove() {
        return currentMove;
    }

    public Game getGame() {
        return game;
    }

    public void incPlayer1Score(int increment) {
        player1Score += increment;
    }

    public void incPlayer2Score(int increment) {
        player2Score += increment;
    }

    public void switchMove() {
        currentMove = currentMove == 1 ? 2 : 1;
    }
}
