package io.github.octcarp.linkgame.client.controller;

import io.github.octcarp.linkgame.client.net.PlayerManager;
import io.github.octcarp.linkgame.client.utils.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField passwdField;

    @FXML
    public void initialize() {
        nameField.setText("");
        passwdField.setText("");
    }

    @FXML
    public void handleLoginAction(ActionEvent actionEvent) {
        String name = nameField.getText();
        String passwd = passwdField.getText();
        if (name.isEmpty() || passwd.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Login Warning");
            alert.setHeaderText("Please fill in all fields");
            alert.setContentText("ID and password cannot be empty");
            alert.showAndWait();
            return;
        }

        boolean loginSuccess = PlayerManager.getInstance().playerLogin(name, passwd);
        if (loginSuccess) {
            SceneSwitcher.getInstance().switchScene("main-menu.fxml");
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText("Login failed");
            alert.setContentText("Please check your password, or ID already taken");
            alert.showAndWait();
        }
    }
}
