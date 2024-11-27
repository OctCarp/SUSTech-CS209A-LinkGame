package io.github.octcarp.sustech.cs209a.linkgame.server.net

import io.github.octcarp.sustech.cs209a.linkgame.common.model.GridPos
import io.github.octcarp.sustech.cs209a.linkgame.common.model.Match
import io.github.octcarp.sustech.cs209a.linkgame.common.model.Match.MatchStatus
import io.github.octcarp.sustech.cs209a.linkgame.common.model.MatchRes
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.Response
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.ResponseType
import io.github.octcarp.sustech.cs209a.linkgame.server.model.MatchInfo
import java.util.concurrent.ConcurrentHashMap

object MatchManager {
    private val matches = ConcurrentHashMap<String, MatchInfo>()

    fun createMatch(p1Id: String, p2Id: String, p1Thread: ClientHandlerThread, p2Thread: ClientHandlerThread) {
        require(p1Id != p2Id) { "Players must be different" }

        val match = Match(p1Id, p2Id)
        val matchInfo = MatchInfo(match, p1Id, p2Id, p1Thread, p2Thread)
        matches[p1Id] = matchInfo
        matches[p2Id] = matchInfo

        Response(ResponseType.START_MATCH).apply {
            data = match
        }.also { sendResponseToBothPlayers(matchInfo, it) }
    }

    fun selectBoard(playerId: String, boardSize: GridPos) {
        matches[playerId]?.let { matchInfo ->
            val match = matchInfo.match
            match.initGame(boardSize.row, boardSize.col)

            val finalMatch = match.copy()
            finalMatch.game!!.board = (match.game!!.board)

            Response(ResponseType.SYNC_MATCH).apply {
                data = finalMatch
            }.also { sendResponseToBothPlayers(matchInfo, it) }

            matchInfo.match = finalMatch
        }
    }

    fun judgeTurnMove(playerId: String, start: GridPos, end: GridPos) {
        matches[playerId]?.let { matchInfo ->
            if (playerId != matchInfo.match.curTurn) {
                return
            }

            matchInfo.judgeMoveAndUpdate(playerId, start, end)

            Response(ResponseType.SYNC_MATCH).apply {
                data = matchInfo.match.copy()
            }.also { sendResponseToBothPlayers(matchInfo, it) }

            with(matchInfo) {
                match.lastPath?.takeIf { it.size >= 2 }?.let {
                    match.game?.clearGrids(start.row, start.col, end.row, end.col)
                    if (match.game?.gameFinished() == true) {
                        gameFinish(this)
                    }
                }
            }
        }
    }

    private fun gameFinish(matchInfo: MatchInfo) {
        val finalMatch = matchInfo.match.copy()
        Response(ResponseType.MATCH_FINISHED).apply {
            data = finalMatch
        }.also { sendResponseToBothPlayers(matchInfo, it) }

        matchInfo.result = when {
            finalMatch.p1Score > finalMatch.p2Score -> MatchRes.P1_WIN
            finalMatch.p1Score < finalMatch.p2Score -> MatchRes.P2_WIN
            else -> MatchRes.DRAW
        }

        endMatch(matchInfo)
    }

    fun playerDisconnected(playerId: String) {
        matches[playerId]?.let { matchInfo ->
            val playerIndex = if (matchInfo.p1 == playerId) 1 else 2
            val oppThread = if (playerIndex == 1) matchInfo.p2Thread else matchInfo.p1Thread

            with(matchInfo.match) {
                when (status) {
                    MatchStatus.RUN -> {
                        status = if (playerIndex == 1) MatchStatus.P1_DIS else MatchStatus.P2_DIS

                        val finalMatch = this.copy()
                        Response(ResponseType.SYNC_MATCH).apply {
                            data = finalMatch
                        }.also {
                            oppThread.sendResponse(it)
                        }
                    }

                    MatchStatus.P1_DIS -> {
                        if (playerIndex == 2) {
                            matchInfo.result = MatchRes.ALL_ERROR
                            endMatch(matchInfo)
                        }
                    }

                    MatchStatus.P2_DIS -> {
                        if (playerIndex == 1) {
                            matchInfo.result = MatchRes.ALL_ERROR
                            endMatch(matchInfo)
                        }
                    }

                    MatchStatus.INIT -> {}
                    MatchStatus.FINISHED -> {}
                }
            }
        }
    }

    private fun endMatch(matchInfo: MatchInfo) {
        with(matchInfo) {
            matches.remove(p1)
            matches.remove(p2)
            RecordManager.addMatchRecordByInfo(this)
        }
    }

    fun shuffleBoard(playerId: String) {
        matches[playerId]?.takeIf { it.match.curTurn == playerId }?.let { matchInfo ->
            matchInfo.match.game!!.shuffleBoard()

            Response(ResponseType.SYNC_BOARD).apply {
                data = matchInfo.match.game!!.copy()
            }.also { sendResponseToBothPlayers(matchInfo, it) }
        }
    }

    fun reconnectMatch(playerId: String): Boolean {
        return matches[playerId]?.let { matchInfo ->
            val match = matchInfo.match
            if ((match.status == MatchStatus.P1_DIS && match.p1 == playerId) ||
                (match.status == MatchStatus.P2_DIS && match.p2 == playerId)
            ) {
                match.status = MatchStatus.RUN
            }

            val toReconnect = Response(ResponseType.RECONNECT_SUCCESS)
            toReconnect.data = match.copy()
            val toOpp = Response(ResponseType.SYNC_MATCH)
            toOpp.data = match.copy()

            if (match.p1 == playerId) {
                matchInfo.p1Thread = PlayersManager.getClientThreadByPlayerId(playerId)!!
                matchInfo.p1Thread.sendResponse(toReconnect)
                matchInfo.p2Thread.sendResponse(toOpp)
            } else {
                matchInfo.p2Thread = PlayersManager.getClientThreadByPlayerId(playerId)!!
                matchInfo.p2Thread.sendResponse(toReconnect)
                matchInfo.p1Thread.sendResponse(toOpp)
            }

            true
        } == true
    }

    private fun sendResponseToBothPlayers(matchInfo: MatchInfo, response: Response) {
        matchInfo.p1Thread.sendResponse(response)
        matchInfo.p2Thread.sendResponse(response)
    }
}
