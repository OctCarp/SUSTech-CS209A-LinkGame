package io.github.octcarp.sustech.cs209a.linkgame.client.controller

import io.github.octcarp.sustech.cs209a.linkgame.client.net.LoginData
import io.github.octcarp.sustech.cs209a.linkgame.client.utils.AlertPopper
import io.github.octcarp.sustech.cs209a.linkgame.client.utils.SceneSwitcher
import io.github.octcarp.sustech.cs209a.linkgame.common.model.Player
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.SimpStatus
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField

class LoginController {
    @FXML
    private lateinit var fieldLogId: TextField

    @FXML
    private lateinit var fieldLogPasswd: PasswordField

    @FXML
    private lateinit var fieldRegId: TextField

    @FXML
    private lateinit var fieldRegPasswd: PasswordField

    @FXML
    private lateinit var fieldRegPasswdConfirm: PasswordField

    @FXML
    private fun initialize() {
        fieldLogId.text = ""
        fieldLogPasswd.text = ""
    }

    @FXML
    private fun handleLoginAction(actionEvent: ActionEvent) {
        val name = fieldLogId.text
        val passwd = fieldLogPasswd.text
        if (name.isEmpty() || passwd.isEmpty()) {
            AlertPopper.popWarning(
                "Login", "Please fill in all fields",
                "ID and password cannot be empty"
            )
        }
        LoginData.playerLogin(Player(name, passwd))
    }

    @FXML
    private fun handleRegisterAction(actionEvent: ActionEvent) {
        val name = fieldRegId.text
        val passwd = fieldRegPasswd.text
        val passwdConfirm = fieldRegPasswdConfirm.text
        if (name.isEmpty() || passwd.isEmpty() || passwdConfirm.isEmpty()) {
            AlertPopper.popError(
                "Register",
                "Please fill in all fields",
                "ID and password cannot be empty"
            )
            return
        }

        if (passwd != passwdConfirm) {
            AlertPopper.popWarning(
                "Register",
                "Password not match",
                "Please confirm your password"
            )
            return
        }
        LoginData.playerRegister(Player(name, passwd))
    }

    fun handleLoginResult(status: SimpStatus) {
        Platform.runLater {
            when (status) {
                SimpStatus.OK -> SceneSwitcher.switchScene("main-menu")

                SimpStatus.UNAUTHORIZED -> AlertPopper.popError(
                    "Login",
                    "Password is wrong",
                    "Please check your password"
                )

                SimpStatus.NOT_FOUND -> AlertPopper.popError(
                    "Login",
                    "ID not found",
                    "Please check your ID"
                )

                SimpStatus.CONFLICT -> AlertPopper.popError(
                    "Login",
                    "ID already login",
                    "Please try another later"
                )

                else -> AlertPopper.popError(
                    "Login",
                    "Login failed",
                    "Please try again"
                )
            }
        }
    }

    fun handleRegisterResult(status: SimpStatus) {
        Platform.runLater {
            when (status) {
                SimpStatus.OK -> AlertPopper.popInfo(
                    "Register Success",
                    "Your ID is '${fieldRegId.text}'",
                    "You can try to login now"
                )

                SimpStatus.FORBIDDEN -> AlertPopper.popError(
                    "Register",
                    "ID is reserved",
                    "Please try another ID"
                )

                SimpStatus.CONFLICT -> AlertPopper.popError(
                    "Register",
                    "ID already exists",
                    "Please try another ID"
                )

                else -> AlertPopper.popError(
                    "Register",
                    "Register failed",
                    "Please try again"
                )
            }
        }
    }

}
