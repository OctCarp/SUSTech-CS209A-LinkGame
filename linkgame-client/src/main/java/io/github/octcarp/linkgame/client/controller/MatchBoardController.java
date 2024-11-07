package io.github.octcarp.linkgame.client.controller;

import io.github.octcarp.linkgame.client.net.MatchManager;
import io.github.octcarp.linkgame.client.utils.ImageLoader;
import io.github.octcarp.linkgame.client.utils.SceneSwitcher;
import io.github.octcarp.linkgame.common.module.Game;
import io.github.octcarp.linkgame.common.module.GridPos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.util.Duration;

import java.util.List;

public class MatchBoardController {
    // Status banner
    @FXML
    private Label lblYourName;
    @FXML
    private Label lblYourScore;
    @FXML
    private Label lblOppName;
    @FXML
    private Label lblOppScore;
    @FXML
    private Label lblCurPlayer;

    // Select board size
    @FXML
    public VBox vbSelectSize;
    @FXML
    public Label lblSelectBoardSize;
    @FXML
    private ChoiceBox<String> cbBoardSize;
    @FXML
    private Button btnConfirmSize;

    // Game board
    @FXML
    private HBox vbBoard;
    @FXML
    private Label lblSelectedPoints;
    @FXML
    private GridPane gpGameBoard;
    @FXML
    private Label lblJudgeResult;

    public static Game game;

    int[] position = new int[3];

    @FXML
    public void initialize() {
        MatchManager.getInstance().startMatch();
        updateStatus();
        vbBoard.setVisible(false);
    }

    private void updateStatus() {
        lblYourName.setText("Player 1");
        lblYourScore.setText("0");
        lblOppName.setText("Player 2");
        lblOppScore.setText("0");
        if (MatchManager.getInstance().getSelectBoardSize()) {
            lblSelectBoardSize.setText("Please select the board size");
        } else {
            lblSelectBoardSize.setText("Wait For your opponent to select the board size");
            cbBoardSize.setVisible(false);
            btnConfirmSize.setVisible(false);
        }
    }

    @FXML
    public void handleSelectSize(ActionEvent actionEvent) {
        String size = cbBoardSize.getValue();
        if (size == null) {
            return;
        }
        int row = Integer.parseInt(size.split("×")[0]);
        int col = Integer.parseInt(size.split("×")[1]);
        int[][] board = Game.setupBoard(row, col);

        vbSelectSize.setVisible(false);
        vbSelectSize.setManaged(false);

        vbBoard.setVisible(true);
        game = new Game(board);
        paintGameBoard();
    }

    public void paintGameBoard() {
        gpGameBoard.getChildren().clear();
        int[][] board = game.getBoard();
        for (int row = 0; row < game.getRow(); row++) {
            for (int col = 0; col < game.getCol(); col++) {
                ImageView imageView = ImageLoader.addContent(board[row][col]);
                if (board[row][col] == 0) {
                    imageView.setFitWidth(40);
                    imageView.setFitHeight(40);
                    imageView.setPreserveRatio(true);
                    gpGameBoard.add(imageView, col, row);
                    continue;
                }

                imageView.setFitWidth(30);
                imageView.setFitHeight(30);
                imageView.setPreserveRatio(true);
                Button button = new Button();
                button.setPrefSize(40, 40);
                button.setGraphic(imageView);
                int finalRow = row;
                int finalCol = col;
                button.setOnAction(_ -> handleButtonPress(finalRow, finalCol));
                gpGameBoard.add(button, col, row);
            }
        }
    }

    @FXML
    private void handleButtonPress(int row, int col) {
        if (position[0] == 0) {
            position[1] = row;
            position[2] = col;
            position[0] = 1;
            lblSelectedPoints.setText("(" + row + "," + col + ")");
        } else {
            position[0] = 0;
            String selectedPoints = lblSelectedPoints.getText();
            lblSelectedPoints.setText(selectedPoints + " -> (" + row + "," + col + ")");

            List<GridPos> path = game.judge(position[1], position[2], row, col);
            if (path != null) {
                path.addFirst(new GridPos(position[1], position[2]));
                path.addLast(new GridPos(row, col));
                if (path.size() > 2) {
                    for (int i = 1; i < path.size() - 1; i++) {
                        GridPos current = path.get(i);
                        Image dirImage = ImageLoader.getDirectImgByPos(path.get(i - 1), path.get(i), path.get(i + 1));
                        ImageView imageView = new ImageView(dirImage);
                        imageView.setFitHeight(40);
                        imageView.setFitWidth(40);
                        gpGameBoard.add(imageView, current.col(), current.row());
                    }
                    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> {
                        game.clearGrids(position[1], position[2], row, col);
                        paintGameBoard();
                    }));
                    timeline.setCycleCount(1);
                    timeline.play();
                } else {
                    game.clearGrids(position[1], position[2], row, col);
                    paintGameBoard();
                }
            }
            if (path != null) {
                if (game.gameFinished()) {
                    lblJudgeResult.setText("Game Finished!");
                    MatchManager.getInstance().afterGameFinished();
                } else {
                    lblJudgeResult.setText("Bingo!");
                }
            } else {
                lblJudgeResult.setText("No below 3 link found");
            }
        }
    }

    @FXML
    private void handleShuffle() {
        game.shuffleBoard();
        paintGameBoard();
    }

    @FXML
    public void handleExit(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Match");
        alert.setHeaderText("Are you sure you want to exit the match?");
        alert.setContentText("All progress will be lost.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                System.out.println("Exit Match");
                SceneSwitcher.getInstance().switchScene("main-menu");
            }
        });

    }

}