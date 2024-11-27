package io.github.octcarp.sustech.cs209a.linkgame.server.net

import io.github.octcarp.sustech.cs209a.linkgame.common.packet.Response
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.ResponseType
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentSkipListSet

object LobbyManager {
    private val lobbyPlayerThreads = ConcurrentHashMap<String, ClientHandlerThread>()
    private val onlinePlayers = ConcurrentSkipListSet<String>()
    private val waitingPlayers = ConcurrentSkipListSet<String>()
    private val exceptPlayers = ConcurrentHashMap<String, String>()

    fun getClientThreadByPlayerId(playerId: String): ClientHandlerThread? {
        return lobbyPlayerThreads[playerId]
    }

    fun enterLobby(playerId: String, clientHandlerThread: ClientHandlerThread) {
        lobbyPlayerThreads[playerId] = clientHandlerThread

        exceptPlayers[playerId]?.let { oppId ->
            val response = Response(ResponseType.WAITING_OPPONENT, oppId)
            clientHandlerThread.sendResponse(response)
        }

        val clientResponse = Response(ResponseType.ALL_WAITING_PLAYERS, waitingPlayers.toList())
        clientHandlerThread.sendResponse(clientResponse)
    }

    fun startWaiting(playerId: String) {
        if (waitingPlayers.add(playerId)) {
            notifyAllLobbyPlayers()
        }
    }

    fun stopWaiting(playerId: String) {
        if (waitingPlayers.remove(playerId)) {
            notifyAllLobbyPlayers()
        }
    }

    fun exitLobby(playerId: String) {
        lobbyPlayerThreads.remove(playerId)
        if (waitingPlayers.remove(playerId)) {
            notifyAllLobbyPlayers()
        }
    }

    fun notifyAllLobbyPlayers() {
        val response = Response(ResponseType.ALL_WAITING_PLAYERS, waitingPlayers.toList())
        lobbyPlayerThreads.values.forEach {
            it.sendResponse(response)
        }
    }

    fun joinPlayer(playerId: String, oppId: String) {
        lobbyPlayerThreads[playerId]?.let { client ->
            lobbyPlayerThreads[oppId]?.let { oppClient ->
                stopWaiting(playerId)
                stopWaiting(oppId)
                MatchManager.createMatch(oppId, playerId, oppClient, client)
            }
        }
    }


    fun playerDisconnected(playerId: String) {
        lobbyPlayerThreads.remove(playerId)
        if (waitingPlayers.remove(playerId)) {
            notifyAllLobbyPlayers()
        }
    }
}
