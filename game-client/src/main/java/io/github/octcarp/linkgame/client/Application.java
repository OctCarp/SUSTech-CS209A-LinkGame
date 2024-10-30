package io.github.octcarp.linkgame.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

import static io.github.octcarp.linkgame.client.Game.SetupBoard;

public class Application extends javafx.application.Application {

    @Override
    public void start(Stage stage) throws IOException {

        int[] size = getBoardSizeFromUser();
        Controller.game = new Game(SetupBoard(size[0], size[1]));

        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("board.fxml"));
        VBox root = fxmlLoader.load();
        Controller controller = fxmlLoader.getController();
        controller.createGameBoard();

        Scene scene = new Scene(root);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        // TODO: handle the game logic

    }

    // let user choose board size
    private int[] getBoardSizeFromUser() {
        enum BoardSize {
            R4C4, R6C6, R8C8,
        }
        int sizeTypeId = (int) (Math.random() * BoardSize.values().length);
        return switch (BoardSize.values()[sizeTypeId]) {
            case BoardSize.R4C4 -> new int[]{4, 4};
            case BoardSize.R6C6 -> new int[]{6, 6};
            case BoardSize.R8C8 -> new int[]{8, 8};
        };
    }

    public static void main(String[] args) {
        launch();
    }
}