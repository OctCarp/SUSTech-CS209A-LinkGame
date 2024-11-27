package io.github.octcarp.sustech.cs209a.linkgame.server.net

import io.github.octcarp.sustech.cs209a.linkgame.common.model.GridPos
import io.github.octcarp.sustech.cs209a.linkgame.common.model.Player
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.*
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket

class ClientHandlerThread(private val socket: Socket) : Runnable {
    private var player: Player? = null
    private var ois: ObjectInputStream? = null
    private var oos: ObjectOutputStream? = null

    init {
        try {
            oos = ObjectOutputStream(socket.getOutputStream())
            ois = ObjectInputStream(socket.getInputStream())
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    override fun run() {
        while (true) {
            try {
                val oin = ois!!.readObject()
                if (oin !is Request) {
                    throw RuntimeException("Invalid request")
                }
                val typeIn = oin.type
                when (typeIn) {
                    RequestType.LOGIN -> {
                        val player = oin.data as Player
                        val status = PlayersManager.playerCanLogin(player)

                        if (status == SimpStatus.OK) {
                            this.player = player
                            PlayersManager.addPlayerThread(player.id, this)
                        }

                        val response = Response(ResponseType.LOGIN_RESULT)
                        response.data = status
                        sendResponse(response)
                    }

                    RequestType.REGISTER -> {
                        val newPlayer = oin.data as Player
                        val status = PlayersManager.registerPlayer(newPlayer)

                        val response = Response(ResponseType.REGISTER_RESULT)
                        response.data = status
                        sendResponse(response)
                    }


                    RequestType.GET_MATCH_RECORD -> {
                        val matches = RecordManager.matchRecordList
                        val response = Response(ResponseType.GET_MATCH_RECORD_RESULT)
                        response.data = matches
                        sendResponse(response)
                    }

                    RequestType.SHUTDOWN -> {
                        handleClientDisconnect()
                    }

                    else -> {
                        player?.let { player ->
                            when (typeIn) {
                                RequestType.LOGOUT -> {
                                    val status = PlayersManager.playerLogout(player.id)

                                    val response = Response(ResponseType.LOGOUT_RESULT)
                                    response.data = status
                                    sendResponse(response)
                                }

                                RequestType.ENTER_LOBBY -> {
                                    LobbyManager.enterLobby(player.id, this)
                                }

                                RequestType.START_WAITING -> {
                                    LobbyManager.startWaiting(player.id)
                                }

                                RequestType.STOP_WAITING -> {
                                    LobbyManager.stopWaiting(player.id)
                                }

                                RequestType.SELECT_BOARD -> {
                                    val pos = oin.data as GridPos
                                    MatchManager.selectBoard(player.id, pos)
                                }

                                RequestType.SHUFFLE_BOARD -> {
                                    MatchManager.shuffleBoard(player.id)
                                }

                                RequestType.EXIT_LOBBY -> {
                                    LobbyManager.exitLobby(player.id)
                                }

                                RequestType.JOIN_PLAYER -> {
                                    val oppId = oin.data as String
                                    LobbyManager.joinPlayer(player.id, oppId)
                                }

                                RequestType.RECONNECT_MATCH -> {
                                    val hasMatch = MatchManager.reconnectMatch(player.id)
                                    if (!hasMatch) {
                                        val response = Response(ResponseType.NO_MATCH_TO_RECONNECT)
                                        sendResponse(response)
                                    }
                                }

                                RequestType.EXIT_MATCH -> {
                                    MatchManager.playerDisconnected(player.id)
                                }

                                RequestType.TURN_MOVE -> {
                                    val (start, end) = (oin.data as List<GridPos>).let { it.first() to it.last() }
                                    MatchManager.judgeTurnMove(player.id, start, end)
                                }

                                else -> {}
                            }
                        } ?: run {
                            val response = Response(ResponseType.ERROR_MESSAGE)
                            response.data = "Please login first"
                            sendResponse(response)
                        }
                    }
                }
            } catch (_: IOException) {
                handleClientDisconnect()
                break
            } catch (e: ClassNotFoundException) {
                throw RuntimeException(e)
            }
        }

        cleanUp()
    }

    fun sendResponse(response: Response) {
        try {
            oos!!.writeObject(response)
            oos!!.flush()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    private fun handleClientDisconnect() {
        println("Client:" + socket.getRemoteSocketAddress() + " disconnected")

        player?.let { player ->
            with(PlayersManager) {
                playerLogout(player.id)
                playerDisconnect(player.id)
            }
            LobbyManager.playerDisconnected(player.id)
            MatchManager.playerDisconnected(player.id)
        }
    }

    private fun cleanUp() {
        try {
            ois?.close()
            oos?.close()
            socket.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
