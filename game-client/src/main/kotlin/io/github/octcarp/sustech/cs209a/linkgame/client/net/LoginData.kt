package io.github.octcarp.sustech.cs209a.linkgame.client.net

import io.github.octcarp.sustech.cs209a.linkgame.client.controller.LoginController
import io.github.octcarp.sustech.cs209a.linkgame.client.controller.MainMenuController
import io.github.octcarp.sustech.cs209a.linkgame.client.utils.SceneSwitcher
import io.github.octcarp.sustech.cs209a.linkgame.common.model.Player
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.Request
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.RequestType
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.SimpStatus

object LoginData {
    var currentPlayer: Player? = null
        private set

    fun playerRegister(player: Player) {
        Request(RequestType.REGISTER).apply {
            data = player
        }.also(ClientService::sendRequest)
    }

    fun rePlayerRegister(status: SimpStatus) {
        (SceneSwitcher.getController("login") as LoginController).handleRegisterResult(status)
    }

    fun playerLogin(player: Player) {
        currentPlayer = player
        Request(RequestType.LOGIN).apply {
            data = currentPlayer
        }.also(ClientService::sendRequest)
    }

    fun rePlayerLogin(status: SimpStatus) {
        when (status) {
            SimpStatus.OK -> currentPlayer?.id?.let {
                ClientService.myId = it
                SceneSwitcher.updateGlobalTitle()
            }

            else -> currentPlayer = null
        }
        (SceneSwitcher.getController("login") as LoginController).handleLoginResult(status)
    }

    fun logout() {
        currentPlayer?.let {
            Request(RequestType.LOGOUT).apply {
                data = it
            }.also(ClientService::sendRequest)
        }
    }

    fun reLogout(status: SimpStatus) {
        currentPlayer = null
        SceneSwitcher.updateGlobalTitle()
//        if (status == SimpStatus.OK) {
//            currentPlayer = null
//        }
        (SceneSwitcher.getController("main-menu") as MainMenuController).handleLogoutResult(status)
    }
}
