package io.github.octcarp.linkgame.client.net;

import io.github.octcarp.linkgame.common.module.PlayerRecord;

public class MatchManager {

    private static MatchManager instance = null;

    private boolean selectBoardSize;

    private PlayerRecord opponent;

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
        PlayerRecord currentPlayer = PlayerManager.getInstance().getCurrentPlayer();
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

    public PlayerRecord getOpponent() {
        return opponent;
    }

    public boolean getSelectBoardSize() {
        return selectBoardSize;
    }
}
