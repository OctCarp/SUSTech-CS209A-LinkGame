package io.github.octcarp.linkgame.client.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class AlertPopper {
    public static void popError(String type, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(type + " Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void popInfo(String type, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(type + " Information");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void popWarning(String type, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(type + " Warning");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void popNetErrAndExit() {
        popError("Network", "Network Error", "Please check your network, or server is down");
        Platform.exit();
    }
}
