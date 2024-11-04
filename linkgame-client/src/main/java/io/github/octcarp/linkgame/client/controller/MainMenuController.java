package io.github.octcarp.linkgame.client.controller;

import io.github.octcarp.linkgame.client.net.PlayerManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainMenuController {
    @FXML
    public Label playerId;

    public void initialize() {
        playerId.setText(PlayerManager.getInstance().getCurrentPlayer().id());
    }
}
