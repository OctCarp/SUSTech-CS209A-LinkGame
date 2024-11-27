package io.github.octcarp.sustech.cs209a.linkgame.client.controller

import io.github.octcarp.sustech.cs209a.linkgame.client.net.RecordData
import io.github.octcarp.sustech.cs209a.linkgame.client.utils.SceneSwitcher
import io.github.octcarp.sustech.cs209a.linkgame.common.model.MatchRecord
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.util.Callback


class MatchRecordController {
    @FXML
    private lateinit var lblTitle: Label

    @FXML
    private lateinit var spPlayerList: ScrollPane

    @FXML
    private lateinit var spPlayerRecord: ScrollPane

    @FXML
    private lateinit var lvPlayerList: ListView<String>

    @FXML
    private lateinit var lvPlayerRecord: ListView<MatchRecord>


    @FXML
    private fun initialize() {
        lvPlayerList.cellFactory = Callback { `_`: ListView<String> ->
            object : ListCell<String>() {
                private val lblId = Label()
                private val btnShowRecord = Button("Show")
                private val hbItem = HBox(lblId, btnShowRecord)

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
                        btnShowRecord.isDisable = false
                        btnShowRecord.onAction = EventHandler { event: ActionEvent ->
                            showPlayerRecord(playerId)
                        }
                        graphic = hbItem
                    }
                }
            }
        }

        spPlayerList.isManaged = false
        spPlayerRecord.isManaged = false

        RecordData.recordRequest()
    }

    @FXML
    private fun handleToMenu(actionEvent: ActionEvent) {
        SceneSwitcher.switchScene("main-menu")
    }

    @FXML
    private fun handleToPlayerList(actionEvent: ActionEvent) {
        lblTitle.text = "Recorded Players"
        showPlayerListPane()
    }

    @FXML
    private fun handleRefreshRecord(actionEvent: ActionEvent) {
        RecordData.recordRequest()
    }

    fun updateRecordPlayerList(playerList: List<String>) {
        Platform.runLater {
            showPlayerListPane()
            val players = FXCollections.observableArrayList<String?>(playerList)
            lvPlayerList.items = players
        }
    }

    private fun showPlayerRecord(playerId: String) {
        Platform.runLater {
            lblTitle.text = "Player $playerId's Record"
            val records = RecordData.getRecordByPlayer(playerId)
            val recordList = FXCollections.observableArrayList<MatchRecord?>(records)
            lvPlayerRecord.items = recordList
            showPlayerRecordPane()
        }
    }

    private fun showPlayerRecordPane() {
        spPlayerRecord.isManaged = true
        spPlayerRecord.isVisible = true
        spPlayerList.isManaged = false
        spPlayerList.isVisible = false
    }

    private fun showPlayerListPane() {
        spPlayerRecord.isManaged = false
        spPlayerRecord.isVisible = false
        spPlayerList.isManaged = true
        spPlayerList.isVisible = true
    }
}

