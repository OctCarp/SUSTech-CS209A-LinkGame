package io.github.octcarp.sustech.cs209a.linkgame.client.net

import io.github.octcarp.sustech.cs209a.linkgame.client.controller.MatchBoardController
import io.github.octcarp.sustech.cs209a.linkgame.client.utils.SceneSwitcher
import io.github.octcarp.sustech.cs209a.linkgame.common.model.Game
import io.github.octcarp.sustech.cs209a.linkgame.common.model.GridPos
import io.github.octcarp.sustech.cs209a.linkgame.common.model.Match
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.Request
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.RequestType

object MatchData {
    var match: Match? = null

    fun initBoard(size: GridPos) {
        Request(RequestType.SELECT_BOARD).apply {
            data = size
        }.also(ClientService::sendRequest)
    }

    fun shuffleBoard() = ClientService.sendRequestOnlyType(RequestType.SHUFFLE_BOARD)

    fun reStartMatch(match: Match) {
        SceneSwitcher.switchScene("match-board")
        this.match = match
        (SceneSwitcher.getController("match-board") as MatchBoardController).initMatch(match)
    }

    fun reConnectToMatch(match: Match) {
        this.match = match
        SceneSwitcher.switchScene("match-board")
        reSyncMatch(match)
    }

    fun reSyncMatch(match: Match) {
        this.match = match
        (SceneSwitcher.getController("match-board") as MatchBoardController).updateMatchByData(match)
    }

    fun reSyncBoard(game: Game) {
        match?.game = game
        (SceneSwitcher.getController("match-board") as MatchBoardController).updateBoard(game)
    }

    fun reMatchFinished(match: Match) {
        this.match = match
        (SceneSwitcher.getController("match-board") as MatchBoardController).matchFinished(match)
    }

    fun exitMatch(): Boolean {
        return LoginData.currentPlayer?.let {
            Request(RequestType.EXIT_MATCH).apply {
                data = it.id
            }.also(ClientService::sendRequest)
            true
        } == true
    }

    fun afterGameFinished() {
    }

    fun sendMove(move: List<GridPos>?) {
        move?.let {
            Request(RequestType.TURN_MOVE).apply {
                data = move
            }.also(ClientService::sendRequest)
        }
    }
}
