package io.github.octcarp.sustech.cs209a.linkgame.server.net

import io.github.octcarp.sustech.cs209a.linkgame.common.model.Player
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.SimpStatus
import io.github.octcarp.sustech.cs209a.linkgame.server.utils.FileIO
import io.github.octcarp.sustech.cs209a.linkgame.server.utils.ServerConfig
import java.util.concurrent.ConcurrentHashMap

object PlayersManager {
    private val playerList = FileIO.readPlayerList()

    private val playerThreads = ConcurrentHashMap<String, ClientHandlerThread>()

    fun playerCanLogin(loginPlayer: Player): SimpStatus = when {
        playerThreads.containsKey(loginPlayer.id) -> SimpStatus.CONFLICT
        else -> playerList.find { it.id == loginPlayer.id }
            ?.let { player ->
                if (player.password == loginPlayer.password) {
                    SimpStatus.OK
                } else {
                    SimpStatus.UNAUTHORIZED
                }
            } ?: SimpStatus.NOT_FOUND
    }

    fun playerLogout(id: String): SimpStatus =
        playerThreads[id]?.let {
            playerThreads.remove(id)
            removePlayerThread(id)
            SimpStatus.OK
        } ?: SimpStatus.NOT_FOUND

    @Synchronized
    fun registerPlayer(newPlayer: Player): SimpStatus = when {
        ServerConfig.reservedIds.contains(newPlayer.id) -> SimpStatus.FORBIDDEN
        playerList.any { it.id == newPlayer.id } -> SimpStatus.CONFLICT
        else -> {
            playerList.add(newPlayer)
            if (FileIO.updatePlayerByList(playerList)) SimpStatus.OK
            else SimpStatus.FAILURE
        }
    }

    fun getClientThreadByPlayerId(id: String): ClientHandlerThread? = playerThreads[id]

    fun addPlayerThread(id: String, thread: ClientHandlerThread) {
        playerThreads[id] = thread
    }

    fun removePlayerThread(id: String) {
        playerThreads.remove(id)
    }

    fun playerDisconnect(id: String) {
        removePlayerThread(id)
    }

}
