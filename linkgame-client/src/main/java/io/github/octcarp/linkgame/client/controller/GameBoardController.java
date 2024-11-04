package io.github.octcarp.linkgame.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.Objects;

public class GameBoardController {

    @FXML
    private Label scoreLabel;

    @FXML
    private GridPane gameBoard;

    public static Game game;

    int[] position = new int[3];

    @FXML
    public void initialize() {
        createGameBoard();
    }

    public void createGameBoard() {

        gameBoard.getChildren().clear();

        for (int row = 0; row < game.row; row++) {
            for (int col = 0; col < game.col; col++) {
                if (game.board[row][col] == 0) {
                    continue;
                }
                Button button = new Button();
                button.setPrefSize(40, 40);
                ImageView imageView = addContent(game.board[row][col]);
                imageView.setFitWidth(30);
                imageView.setFitHeight(30);
                imageView.setPreserveRatio(true);
                button.setGraphic(imageView);
                int finalRow = row;
                int finalCol = col;
                button.setOnAction(_ -> handleButtonPress(finalRow, finalCol));
                gameBoard.add(button, col, row);
            }
        }


    }

    private void handleButtonPress(int row, int col) {
        System.out.println("Button pressed at: " + row + ", " + col);
        if (position[0] == 0) {
            position[1] = row;
            position[2] = col;
            position[0] = 1;
        } else {
            boolean change = game.judge(position[1], position[2], row, col);
            position[0] = 0;
            if (change) {
                System.out.printf("Link grid: (%d, %d) and (%d, %d)\n", position[1], position[2], row, col);
                game.clearGrids(position[1], position[2], row, col);
                createGameBoard();
            }
        }
    }

    @FXML
    private void handleReset() {
        createGameBoard();
    }

    public ImageView addContent(int content) {
        return switch (content) {
            case 12 -> new ImageView(imageCarambola);
            case 1 -> new ImageView(imageApple);
            case 2 -> new ImageView(imageMango);
            case 3 -> new ImageView(imageBlueberry);
            case 4 -> new ImageView(imageCherry);
            case 5 -> new ImageView(imageGrape);
            case 6 -> new ImageView(imageKiwi);
            case 7 -> new ImageView(imageOrange);
            case 8 -> new ImageView(imagePeach);
            case 9 -> new ImageView(imagePear);
            case 10 -> new ImageView(imagePineapple);
            case 11 -> new ImageView(imageWatermelon);
            default -> null;
        };
    }

    public static Image imageApple = new Image(Objects.requireNonNull(Game.class.getResource("img/fruits/apple.png")).toExternalForm());
    public static Image imageMango = new Image(Objects.requireNonNull(Game.class.getResource("img/fruits/mango.png")).toExternalForm());
    public static Image imageBlueberry = new Image(Objects.requireNonNull(Game.class.getResource("img/fruits/blueberry.png")).toExternalForm());
    public static Image imageCherry = new Image(Objects.requireNonNull(Game.class.getResource("img/fruits/cherry.png")).toExternalForm());
    public static Image imageGrape = new Image(Objects.requireNonNull(Game.class.getResource("img/fruits/grape.png")).toExternalForm());
    public static Image imageCarambola = new Image(Objects.requireNonNull(Game.class.getResource("img/fruits/carambola.png")).toExternalForm());
    public static Image imageKiwi = new Image(Objects.requireNonNull(Game.class.getResource("img/fruits/kiwi.png")).toExternalForm());
    public static Image imageOrange = new Image(Objects.requireNonNull(Game.class.getResource("img/fruits/orange.png")).toExternalForm());
    public static Image imagePeach = new Image(Objects.requireNonNull(Game.class.getResource("img/fruits/peach.png")).toExternalForm());
    public static Image imagePear = new Image(Objects.requireNonNull(Game.class.getResource("img/fruits/pear.png")).toExternalForm());
    public static Image imagePineapple = new Image(Objects.requireNonNull(Game.class.getResource("img/fruits/pineapple.png")).toExternalForm());
    public static Image imageWatermelon = new Image(Objects.requireNonNull(Game.class.getResource("img/fruits/watermelon.png")).toExternalForm());

}
