package io.github.octcarp.sustech.cs209a.linkgame.client.net

import io.github.octcarp.sustech.cs209a.linkgame.client.utils.SceneSwitcher
import io.github.octcarp.sustech.cs209a.linkgame.common.model.Game
import io.github.octcarp.sustech.cs209a.linkgame.common.model.Match
import io.github.octcarp.sustech.cs209a.linkgame.common.model.MatchRecord
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.Response
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.ResponseType
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.SimpStatus
import java.io.IOException
import java.io.ObjectInputStream
import java.net.Socket

class ServerHandlerThread(private val socket: Socket, private val ois: ObjectInputStream) : Runnable {
    override fun run() {
        try {
            while (true) {
                val oin = ois.readObject()
                if (oin !is Response) {
                    continue
                }
                val response = oin
                when (response.type) {
                    ResponseType.LOGIN_RESULT -> {
                        val status = response.data as SimpStatus
                        LoginData.rePlayerLogin(status)
                    }

                    ResponseType.REGISTER_RESULT -> {
                        val status = response.data as SimpStatus
                        LoginData.rePlayerRegister(status)
                    }

                    ResponseType.LOGOUT_RESULT -> {
                        val status = response.data as SimpStatus
                        LoginData.reLogout(status)
                    }

                    ResponseType.GET_MATCH_RECORD_RESULT -> {
                        val matches = (response.data as List<MatchRecord>)
                        RecordData.reSyncRecord(matches.toMutableList())
                    }

                    ResponseType.ALL_WAITING_PLAYERS -> {
                        val players = (response.data as List<String>)
                        LobbyData.reAllWaitingPlayers(players.toMutableList())
                    }

                    ResponseType.START_MATCH -> {
                        val match = response.data as Match
                        MatchData.reStartMatch(match)
                    }

                    ResponseType.SYNC_MATCH -> {
                        val match = response.data as Match
                        MatchData.reSyncMatch(match)
                    }

                    ResponseType.SYNC_BOARD -> {
                        val game = response.data as Game
                        MatchData.reSyncBoard(game)
                    }

                    ResponseType.RECONNECT_SUCCESS -> {
                        val match = response.data as Match
                        MatchData.reConnectToMatch(match)
                    }

                    ResponseType.MATCH_FINISHED -> {
                        val match = response.data as Match
                        MatchData.reMatchFinished(match)
                    }

                    ResponseType.NO_MATCH_TO_RECONNECT -> {
                        LobbyData.reNoReconnect()
                    }

                    ResponseType.WAITING_OPPONENT -> TODO()
                    ResponseType.OPP_DISCONNECTED -> TODO()
                    ResponseType.OPP_RECONNECTED -> TODO()
                    ResponseType.ERROR_MESSAGE -> TODO()
                }
            }
        } catch (e: IOException) {
            SceneSwitcher.netErrAndReturn();
            throw RuntimeException(e)
        } catch (e: ClassNotFoundException) {
            throw RuntimeException(e)
        } catch (e: ClassCastException) {
            throw RuntimeException(e)
        } finally {
            ClientService.disconnect()
        }
    }
}
