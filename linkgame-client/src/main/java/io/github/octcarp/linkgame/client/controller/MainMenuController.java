package io.github.octcarp.linkgame.client.controller;

import io.github.octcarp.linkgame.client.net.PlayerManager;
import io.github.octcarp.linkgame.client.utils.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

public class MainMenuController {
    @FXML
    private Label lblPlayerId;

    @FXML
    public void initialize() {
        lblPlayerId.setText(PlayerManager.getInstance().getCurrentPlayer().id());
    }

    @FXML
    public void handleStartMatchAction(ActionEvent actionEvent) {
        SceneSwitcher.getInstance().switchScene("match-board.fxml");
    }

    @FXML
    public void handleGameHistoryAction(ActionEvent actionEvent) {
    }

    public void handleLogoutAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("You will be redirected to the login page.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                PlayerManager.getInstance().logout();
                SceneSwitcher.getInstance().switchScene("login.fxml");
            }
        });
    }
}
