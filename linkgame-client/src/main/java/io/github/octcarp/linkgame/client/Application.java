package io.github.octcarp.linkgame.client;

import io.github.octcarp.linkgame.client.utils.SceneSwitcher;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;


public class Application extends javafx.application.Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // set window icon
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(Application.class.getResource(
                "/img/main_icon.png")).toExternalForm()));

        // show login scene
        SceneSwitcher.getInstance().setPrimaryStage(primaryStage);
        SceneSwitcher.getInstance().switchScene("login");  // Default login scene
        primaryStage.setTitle("LinkGame");
        primaryStage.show();

        // handle the close event
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            showExitConfirmation(primaryStage);
        });
    }

    private void showExitConfirmation(Stage primaryStage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Exit");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.setContentText("Your match step will be lost.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                MyExit();
            }
        });
    }

    private void MyExit() {
        Platform.exit();
    }
}