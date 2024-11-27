package io.github.octcarp.sustech.cs209a.linkgame.client.controller

import io.github.octcarp.sustech.cs209a.linkgame.client.net.ClientService
import io.github.octcarp.sustech.cs209a.linkgame.client.net.MatchData
import io.github.octcarp.sustech.cs209a.linkgame.client.utils.ImageLoader
import io.github.octcarp.sustech.cs209a.linkgame.client.utils.SceneSwitcher
import io.github.octcarp.sustech.cs209a.linkgame.common.model.Game
import io.github.octcarp.sustech.cs209a.linkgame.common.model.GridPos
import io.github.octcarp.sustech.cs209a.linkgame.common.model.Match
import io.github.octcarp.sustech.cs209a.linkgame.common.model.Match.MatchStatus
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.image.ImageView
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.util.Duration
import java.util.function.Consumer

class MatchBoardController {
    // Status banner
    @FXML
    private lateinit var lblYourName: Label

    @FXML
    private lateinit var lblYourScore: Label

    @FXML
    private lateinit var lblOppName: Label

    @FXML
    private lateinit var lblOppScore: Label

    @FXML
    private lateinit var lblCurPlayer: Label

    // Select board size
    @FXML
    private lateinit var vbSelectSize: VBox

    @FXML
    private lateinit var lblSelectBoardSize: Label

    @FXML
    private lateinit var cbBoardSize: ChoiceBox<String?>

    @FXML
    private lateinit var btnConfirmSize: Button

    // Game board
    @FXML
    private lateinit var vbBoard: HBox

    @FXML
    private lateinit var lblSelectedPoints: Label

    @FXML
    private lateinit var gpGameBoard: GridPane

    @FXML
    private lateinit var lblJudgeResult: Label

    @FXML
    private lateinit var btnShuffle: Button

    private var match: Match? = null

    private val position = IntArray(3)

    private var myId: String? = null

    private var finished = false

    @FXML
    private fun initialize() {
        myId = ClientService.myId
        vbBoard.isVisible = false
    }

    @FXML
    private fun handleSelectSize(actionEvent: ActionEvent) {
        val size = cbBoardSize.getValue()
        if (size == null) {
            return
        }
        val row = size.split("×".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].toInt()
        val col = size.split("×".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].toInt()

        MatchData.initBoard(GridPos(row, col))

        vbSelectSize.isVisible = false
        vbSelectSize.isManaged = false
    }

    @FXML
    private fun handleButtonPress(row: Int, col: Int) {
        if (position[0] == 0) {
            position[1] = row
            position[2] = col
            position[0] = 1
            lblSelectedPoints.text = "($row,$col)"
        } else {
            position[0] = 0
            val selectedPoints = lblSelectedPoints.text
            lblSelectedPoints.text = "$selectedPoints -> ($row,$col)"

            val move: MutableList<GridPos> = ArrayList<GridPos>()
            move.add(GridPos(position[1], position[2]))
            move.add(GridPos(row, col))

            MatchData.sendMove(move)
        }
    }

    @FXML
    private fun handleShuffle() {
        MatchData.shuffleBoard()
    }

    @FXML
    private fun handleExit(actionEvent: ActionEvent) {
        if (!finished) {
            Alert(Alert.AlertType.CONFIRMATION).apply {
                title = "Exit Match"
                headerText = "Are you sure you want to exit the match?"
                contentText = "All progress will be lost."
            }.showAndWait().ifPresent(Consumer { response: ButtonType ->
                if (response == ButtonType.OK) {
                    MatchData.exitMatch()
                    SceneSwitcher.switchScene("main-menu")
                }
            })
        } else {
            SceneSwitcher.switchScene("main-menu")
        }
    }

    fun initMatch(match: Match) {
        Platform.runLater {
            this.match = match
            if (myId == match.p1) {
                lblYourName.text = match.p1
                lblOppName.text = match.p2
            } else {
                lblYourName.text = match.p2
                lblOppName.text = match.p1
            }
            val select = match.whoChoseSize == myId
            if (select) {
                lblSelectBoardSize.text = "Please select the board size"
            } else {
                lblSelectBoardSize.text = "Wait For your opponent to select the board size"
                cbBoardSize.isVisible = false
                btnConfirmSize.isVisible = false
            }
        }
    }

    fun updateMatchByData(match: Match) {
        this.match = match
        Platform.runLater {
            vbSelectSize.isVisible = false
            vbSelectSize.isManaged = false
            when (match.status) {
                MatchStatus.P1_DIS -> {
                    lblJudgeResult.text = "${match.p1} is disconnect"
                    vbBoard.isVisible = false
                    btnShuffle.isDisable = true
                    gpGameBoard.isVisible = false
                }

                MatchStatus.P2_DIS -> {
                    lblJudgeResult.text = "${match.p2} is disconnect"
                    vbBoard.isVisible = false
                    btnShuffle.isDisable = true
                    gpGameBoard.isVisible = false
                }

                MatchStatus.RUN -> {
                    btnShuffle.isDisable = false
                    vbBoard.isVisible = true
                    gpGameBoard.isVisible = true
                    updateMatch()
                }

                MatchStatus.INIT -> {}
                MatchStatus.FINISHED -> {}
            }
        }
    }

    private fun paintGameBoard(enableE: Boolean) {
        val enable = enableE && match!!.curTurn == myId
        gpGameBoard.children.clear()
        val game = match!!.game
        val board: Array<IntArray> = game!!.board
        for (row in 0 until game.row) {
            for (col in 0 until game.col) {
                val imageView = ImageLoader.addContent(board[row][col])!!

                imageView.isPreserveRatio = true
                if (board[row][col] == 0) {
                    imageView.fitWidth = 40.0
                    imageView.fitHeight = 40.0
                    gpGameBoard.add(imageView, col, row)
                } else {
                    imageView.fitWidth = 30.0
                    imageView.fitHeight = 30.0
                    gpGameBoard.add(imageView, col, row)

                    val button = Button().apply {
                        setPrefSize(40.0, 40.0)
                        graphic = imageView
                        isDisable = !enable
                    }

                    button.onAction = EventHandler { `_`: ActionEvent ->
                        handleButtonPress(row, col)
                    }
                    gpGameBoard.add(button, col, row)
                }
            }
        }
    }

    fun updateBoard(game: Game) {
        this.match!!.game!!.board = game.board
        Platform.runLater {
            paintGameBoard(true)
        }
    }

    private fun updateMatch() {
        with(match!!) {
            gpGameBoard.isVisible = true
            with(match) {
                if (myId == p1) {
                    lblYourName.text = p1
                    lblOppName.text = p2
                    lblYourScore.text = p1Score.toString()
                    lblOppScore.text = p2Score.toString()
                } else {
                    lblYourName.text = p2
                    lblOppName.text = p1
                    lblYourScore.text = p2Score.toString()
                    lblOppScore.text = p1Score.toString()
                }

                val myTurn = curTurn == myId

                paintGameBoard(true)

                btnShuffle.isDisable = !myTurn

                lblCurPlayer.text = if (curTurn == myId) "Your Turn" else "Opponent's Turn"

                lastPath?.let {
                    if (it.size >= 2) {
                        paintPath(it)
                        lblJudgeResult.text = if (myTurn) "Opponent's Right" else "Bingo!"
                    } else {
                        lblJudgeResult.text = if (myTurn) "Opponent's Wrong" else "No below 3 link found"
                    }
                }
            }
        }

    }

    private fun paintPath(path: List<GridPos>) {
        val game = match?.game ?: return
        if (path.isEmpty()) return

        val (start, end) = path.first() to path.last()
        if (path.size > 2) {
            path.windowed(3).forEach { (prev, current, next) ->
                ImageLoader.getDirectImgByPos(prev, current, next).let { dirImage ->
                    ImageView(dirImage).apply {
                        fitHeight = 40.0
                        fitWidth = 40.0
                        gpGameBoard.add(this, current.col, current.row)
                    }
                }
            }

            Timeline().apply {
                keyFrames.add(
                    KeyFrame(Duration.seconds(0.5), EventHandler { e: ActionEvent? ->
                        game.clearGrids(start.row, start.col, end.row, end.col)
                        paintGameBoard(true)
                    })
                )
                cycleCount = 1
            }.also(Timeline::play)
        } else {
            game.clearGrids(start.row, start.col, end.row, end.col)
            paintGameBoard(true)
        }
    }

    fun matchFinished(match: Match) {
        this.match = match
        finished = true
        Platform.runLater {
            paintGameBoard(false)
            btnShuffle.isDisable = true

            with(lblJudgeResult) {
                text = when {
                    lblYourScore.text.toInt() > lblOppScore.text.toInt() -> "You Win"
                    lblYourScore.text.toInt() < lblOppScore.text.toInt() -> "You Lose"
                    else -> "Draw"
                }
            }

            lblCurPlayer.text = "Game Finished"
        }
    }
}