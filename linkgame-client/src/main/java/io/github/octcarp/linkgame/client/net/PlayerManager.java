package io.github.octcarp.linkgame.client.net;

import io.github.octcarp.linkgame.common.module.PlayerRecord;

public class PlayerManager {
    private static PlayerManager instance = null;
    private PlayerRecord currentPlayer;

    private PlayerManager() {
    }

    public static PlayerManager getInstance() {
        if (instance == null) {
            instance = new PlayerManager();
        }
        return instance;
    }

    private boolean playerCheckAndReg(PlayerRecord player) {
        // Todo: send player info to server and check if the player is valid
        return true;
    }

    public boolean playerLogin(String name, String passwd) {
        PlayerRecord playerRecord = new PlayerRecord(name, passwd);
        if (playerCheckAndReg(playerRecord)) {
            setCurrentPlayer(playerRecord);
            return true;
        }
        return false;
    }

    public void logout() {
        // TODO: send logout request to server
        currentPlayer = null;
    }

    private void setCurrentPlayer(PlayerRecord player) {
        currentPlayer = player;
    }

    public PlayerRecord getCurrentPlayer() {
        return currentPlayer;
    }
}
