package io.github.octcarp.sustech.cs209a.linkgame.client.controller

import io.github.octcarp.sustech.cs209a.linkgame.client.net.ClientService
import io.github.octcarp.sustech.cs209a.linkgame.client.net.LobbyData
import io.github.octcarp.sustech.cs209a.linkgame.client.utils.SceneSwitcher
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.layout.HBox
import javafx.util.Callback

class LobbyController {
    @FXML
    private lateinit var lvWaitingPlayer: ListView<String>

    @FXML
    private lateinit var btnWait: Button

    @FXML
    private fun initialize() {
        lvWaitingPlayer.cellFactory = Callback { `_`: ListView<String> ->
            object : ListCell<String>() {
                private val lblId = Label()
                private val btnJoin = Button("Join")
                private val hbItem = HBox(lblId, btnJoin)

                init {
                    hbItem.spacing = 100.0
                    hbItem.alignment = Pos.CENTER
                }

                override fun updateItem(item: String?, empty: Boolean) {
                    super.updateItem(item, empty)
                    val playerId = getItem()
                    if (empty || playerId == null) {
                        text = null
                        graphic = null
                    } else {
                        lblId.text = playerId
                        if (playerId == ClientService.myId) {
                            btnJoin.isDisable = true
                        } else {
                            btnJoin.isDisable = false
                            btnJoin.onAction = EventHandler { event: ActionEvent ->
                                println("Joining player: $playerId")
                                LobbyData.joinPlayer(playerId)
                            }
                        }
                        graphic = hbItem
                    }
                }
            }
        }
    }

    @FXML
    private fun handleWaitAction(actionEvent: ActionEvent) {
        LobbyData.toggleWaiting()
    }

    @FXML
    private fun handleExitLobby(actionEvent: ActionEvent) {
        LobbyData.exitLobby()
        SceneSwitcher.switchScene("main-menu")
    }

    fun updateWaitingPlayers(playerList: List<String>) {
        val curId = ClientService.myId
        Platform.runLater {
            btnWait.text = if (playerList.contains(curId)) "Stop Waiting" else "Start Waiting"

            lvWaitingPlayer.getItems().clear()

            val players = FXCollections.observableArrayList<String>(playerList)
            lvWaitingPlayer.items = players
        }
    }
}
