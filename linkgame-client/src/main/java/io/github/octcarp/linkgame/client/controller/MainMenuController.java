package io.github.octcarp.linkgame.client.controller;

import io.github.octcarp.linkgame.client.net.LobbyData;
import io.github.octcarp.linkgame.client.net.LoginData;
import io.github.octcarp.linkgame.client.utils.AlertPopper;
import io.github.octcarp.linkgame.client.utils.SceneSwitcher;
import io.github.octcarp.linkgame.common.packet.SimpStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import static java.lang.Thread.sleep;

public class MainMenuController {
    @FXML
    private Label lblPlayerId;

    @FXML
    public void initialize() {
        lblPlayerId.setText(LoginData.getInstance().getCurrentPlayer().id());
    }

    @FXML
    public void handleStartMatchAction(ActionEvent actionEvent) {
        SceneSwitcher.getInstance().switchScene("lobby");
        LobbyData.getInstance().enterLobby();
    }

    @FXML
    public void handleGameHistoryAction(ActionEvent actionEvent) {
    }

    @FXML
    public void handleLogoutAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("You will be redirected to the login page.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                LoginData.getInstance().logout();
            }
        });
    }

    public void handleLogoutResult(SimpStatus status) {
        if (status != SimpStatus.OK) {
            AlertPopper.popError("Logout",
                    "Logout failed", "Please try again.");
            return;
        }
        SceneSwitcher.getInstance().switchScene("login");
    }
}
