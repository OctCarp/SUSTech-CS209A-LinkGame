package io.github.octcarp.linkgame.client.net;

import io.github.octcarp.linkgame.common.module.Player;

public class MatchManager {

    private static MatchManager instance = null;

    private boolean selectBoardSize;

    private Player opponent;

    private int yourScore;

    private int oppScore;

    private MatchManager() {
    }

    public static MatchManager getInstance() {
        if (instance == null) {
            instance = new MatchManager();
        }
        return instance;
    }

    public void startMatch() {
        selectBoardSize = true;
    }

    public boolean exitMatch() {
        Player currentPlayer = PlayerManager.getInstance().getCurrentPlayer();
        // TODO: send exit match request to server
        return true;
    }

    public void afterGameFinished() {
    }

    public int getOppScore() {
        return oppScore;
    }

    public int getYourScore() {
        return yourScore;
    }

    public Player getOpponent() {
        return opponent;
    }

    public boolean getSelectBoardSize() {
        return selectBoardSize;
    }
}
