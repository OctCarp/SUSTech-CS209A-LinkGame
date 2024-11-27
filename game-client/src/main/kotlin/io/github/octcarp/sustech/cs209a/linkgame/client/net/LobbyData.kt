package io.github.octcarp.sustech.cs209a.linkgame.client.net

import io.github.octcarp.sustech.cs209a.linkgame.client.controller.LobbyController
import io.github.octcarp.sustech.cs209a.linkgame.client.controller.MainMenuController
import io.github.octcarp.sustech.cs209a.linkgame.client.utils.SceneSwitcher
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.Request
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.RequestType

object LobbyData {
    private var isWaiting = false

    fun enterLobby() = ClientService.sendRequestOnlyType(RequestType.ENTER_LOBBY)

    fun exitLobby() = ClientService.sendRequestOnlyType(RequestType.EXIT_LOBBY)

    fun toggleWaiting() {
        ClientService.sendRequestOnlyType(if (isWaiting) RequestType.STOP_WAITING else RequestType.START_WAITING)
        isWaiting = !isWaiting
    }

    fun reconnectMatch() = ClientService.sendRequestOnlyType(RequestType.RECONNECT_MATCH)

    fun reAllWaitingPlayers(players: List<String>) {
        (SceneSwitcher.getController("lobby") as LobbyController).updateWaitingPlayers(players)
    }

    fun joinPlayer(oppId: String) {
        Request(RequestType.JOIN_PLAYER).apply {
            data = oppId
        }.also(ClientService::sendRequest)
    }

    fun reNoReconnect() {
        (SceneSwitcher.getController("main-menu") as MainMenuController).handleNoReconnect()
    }
}
