package io.github.octcarp.sustech.cs209a.linkgame.client.controller

import io.github.octcarp.sustech.cs209a.linkgame.client.net.LobbyData
import io.github.octcarp.sustech.cs209a.linkgame.client.net.LoginData
import io.github.octcarp.sustech.cs209a.linkgame.client.utils.AlertPopper
import io.github.octcarp.sustech.cs209a.linkgame.client.utils.SceneSwitcher
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.SimpStatus
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.Label
import java.util.function.Consumer

class MainMenuController {
    @FXML
    private lateinit var lblPlayerId: Label

    @FXML
    private fun initialize() {
        lblPlayerId.text = LoginData.currentPlayer?.id ?: "Unknown"
    }

    @FXML
    private fun handleStartMatchAction(actionEvent: ActionEvent) {
        SceneSwitcher.switchScene("lobby")
        LobbyData.enterLobby()
    }

    @FXML
    private fun handleGameHistoryAction(actionEvent: ActionEvent) {
        SceneSwitcher.switchScene("match-record")
    }

    @FXML
    private fun handleLogoutAction(actionEvent: ActionEvent) {
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = "Logout"
        alert.headerText = "Are you sure you want to logout?"
        alert.contentText = "You will be redirected to the login page."
        alert.showAndWait().ifPresent(Consumer { response: ButtonType? ->
            if (response == ButtonType.OK) {
                LoginData.logout()
            }
        })
    }

    @FXML
    private fun handleReconnectAction(actionEvent: ActionEvent) {
        LobbyData.reconnectMatch()
    }

    fun handleLogoutResult(status: SimpStatus) {
        Platform.runLater {
            if (status != SimpStatus.OK) {
                AlertPopper.popError(
                    "Logout",
                    "Logout failed", "Please try again."
                )
                return@runLater
            }
            SceneSwitcher.switchScene("login")
        }
    }

    fun handleNoReconnect() {
        Platform.runLater {
            AlertPopper.popError(
                "Reconnect", "Reconnect failed",
                "No match to reconnect"
            )
        }
    }
}
