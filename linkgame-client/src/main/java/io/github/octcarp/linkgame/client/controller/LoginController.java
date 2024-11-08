package io.github.octcarp.linkgame.client.controller;

import io.github.octcarp.linkgame.client.net.LoginData;
import io.github.octcarp.linkgame.client.utils.AlertPopper;
import io.github.octcarp.linkgame.client.utils.SceneSwitcher;
import io.github.octcarp.linkgame.common.module.Player;
import io.github.octcarp.linkgame.common.packet.SimpStatus;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    private TextField fieldLogId;
    @FXML
    private PasswordField fieldLogPasswd;

    @FXML
    private TextField fieldRegId;
    @FXML
    private PasswordField fieldRegPasswd;
    @FXML
    private PasswordField fieldRegPasswdConfirm;

    @FXML
    public void initialize() {
        fieldLogId.setText("");
        fieldLogPasswd.setText("");
    }

    @FXML
    public void handleLoginAction(ActionEvent actionEvent) {
        String name = fieldLogId.getText();
        String passwd = fieldLogPasswd.getText();
        if (name.isEmpty() || passwd.isEmpty()) {
            AlertPopper.popWarning("Login", "Please fill in all fields",
                    "ID and password cannot be empty");
        }
        LoginData.getInstance().playerLogin(new Player(name, passwd));
    }

    public void handleLoginResult(SimpStatus status) {
        Platform.runLater(() -> {
            if (status == SimpStatus.OK) {
                SceneSwitcher.getInstance().switchScene("main-menu");
            } else {
                String type = "Login";
                switch (status) {
                    case UNAUTHORIZED -> AlertPopper.popError(type,
                            "Password is wrong", "Please check your password");
                    case NOT_FOUND -> AlertPopper.popError(type,
                            "ID not found", "Please check your ID");
                    default -> AlertPopper.popError(type,
                            "Login failed", "Please try again");
                }
            }
        });
    }

    @FXML
    public void handleRegisterAction(ActionEvent actionEvent) {
        String name = fieldRegId.getText();
        String passwd = fieldRegPasswd.getText();
        String passwdConfirm = fieldRegPasswdConfirm.getText();
        if (name.isEmpty() || passwd.isEmpty() || passwdConfirm.isEmpty()) {
            AlertPopper.popError("Register", "Please fill in all fields",
                    "ID and password cannot be empty");
            return;
        }

        if (!passwd.equals(passwdConfirm)) {
            AlertPopper.popWarning("Register", "Password not match",
                    "Please confirm your password");
            return;
        }
        LoginData.getInstance().playerRegister(new Player(name, passwd));
    }

    public void handleRegisterResult(SimpStatus status){
        Platform.runLater(() -> {
            if (status == SimpStatus.OK) {
                AlertPopper.popInfo("Register Success", "Your ID is '" + fieldRegId.getText() + "'",
                        "You can try to login now");
            } else {
                switch (status) {
                    case FORBIDDEN -> AlertPopper.popError("Register", "ID is reserved",
                            "Please try another ID");
                    case CONFLICT -> AlertPopper.popError("Register", "ID already exists",
                            "Please try another ID");
                    default -> AlertPopper.popError("Register", "Register failed",
                            "Please try again");
                }
            }
        });
    }
}
