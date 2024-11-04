package io.github.octcarp.linkgame.client.controller;

import io.github.octcarp.linkgame.client.net.PlayerManager;
import io.github.octcarp.linkgame.client.utils.SceneSwitcher;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class LoginController {

    @FXML
    public TextField nameField;

    @FXML
    public TextField passwdField;

    @FXML
    public void clickLoginBtn(MouseEvent mouseEvent) {
        System.out.println("Login button clicked");
        String name = nameField.getText();
        String passwd = passwdField.getText();
        if (name.isEmpty() || passwd.isEmpty()) {
            System.out.println("Name or password is empty");
            return;
        }
        boolean loginSuccess =  PlayerManager.getInstance().playerLogin(name, passwd);
        if (loginSuccess) {
            SceneSwitcher.getInstance().switchScene("main-menu");
        }
    }
}
