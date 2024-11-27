package io.github.octcarp.sustech.cs209a.linkgame.client.utils

import javafx.application.Platform
import javafx.scene.control.Alert

object AlertPopper {
    var tryExit = false

    fun popError(type: String, header: String, content: String) {
        Platform.runLater {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "$type Error"
            alert.headerText = header
            alert.contentText = content
            alert.showAndWait()
        }
    }

    fun popInfo(type: String, header: String, content: String) {
        Platform.runLater {
            val alert = Alert(Alert.AlertType.INFORMATION)
            alert.title = "$type Information"
            alert.headerText = header
            alert.contentText = content
            alert.showAndWait()
        }
    }

    fun popWarning(type: String, header: String, content: String) {
        Platform.runLater {
            val alert = Alert(Alert.AlertType.WARNING)
            alert.title = "$type Warning"
            alert.headerText = header
            alert.contentText = content
            alert.showAndWait()
        }
    }

    fun popNetErrAndToLogin() {
        if (tryExit) {
            return
        }
        Platform.runLater {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Network Error"
            alert.headerText = "Network Error"
            alert.contentText = "Please check your network, or server is down"
            alert.showAndWait()
            SceneSwitcher.switchScene("login")
        }
    }
}
