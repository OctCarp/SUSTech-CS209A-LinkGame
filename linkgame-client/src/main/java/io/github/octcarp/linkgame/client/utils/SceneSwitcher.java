package io.github.octcarp.linkgame.client.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SceneSwitcher {

    private static SceneSwitcher instance = null;
    private Stage primaryStage;
    private final Map<String, Scene> scenes = new HashMap<>();

    private SceneSwitcher() {
    }

    public static SceneSwitcher getInstance() {
        if (instance == null) {
            instance = new SceneSwitcher();
        }
        return instance;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void switchScene(String sceneFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + sceneFxml));
            Parent root = loader.load();
            scenes.put(sceneFxml, new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        primaryStage.setScene(scenes.get(sceneFxml));
    }
}
