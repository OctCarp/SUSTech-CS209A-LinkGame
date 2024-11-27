package io.github.octcarp.sustech.cs209a.linkgame.common.model

import java.io.Serializable
import kotlin.random.Random

class Match : Serializable {
    var p1: String? = null
        private set
    var p2: String? = null
        private set

    var p1Score: Int = 0
        private set
    var p2Score: Int = 0
        private set

    var whoChoseSize: String? = null
        private set

    var curTurn: String? = null
        private set

    var lastPath: List<GridPos>? = null

    var game: Game? = null
        private set


    enum class MatchStatus {
        INIT,
        RUN,
        P1_DIS,
        P2_DIS,
        FINISHED
    }

    var status: MatchStatus = MatchStatus.INIT

    //    private MatchStatus status;
    constructor()

    constructor(p1: String, p2: String) {
        this.p1 = p1
        this.p2 = p2
        this.p1Score = 0
        this.p2Score = 0
        val choose = Random.nextInt(2) + 1
        this.whoChoseSize = if (choose == 1) p1 else p2
        this.curTurn = whoChoseSize
        this.lastPath = null
        this.game = Game()
        this.status = MatchStatus.INIT;
    }

    constructor(p1: String, p2: String, boardSize: GridPos) {
        this.p1 = p1
        this.p2 = p2
        this.p1Score = 0
        this.p2Score = 0
        val choose = Random.nextInt(2) + 1
        this.whoChoseSize = if (choose == 1) p1 else p2
        this.curTurn = whoChoseSize
        this.lastPath = null
        this.game = Game(boardSize.col, boardSize.row)
    }

    constructor(
        p1: String?,
        p2: String,
        p1Score: Int,
        p2Score: Int,
        whoChoseSize: String,
        curTurn: String,
        lastPath: List<GridPos>?,
        status: MatchStatus,
        copy: Game
    ) {
        this.p1 = p1
        this.p2 = p2
        this.p1Score = p1Score
        this.p2Score = p2Score
        this.whoChoseSize = whoChoseSize
        this.curTurn = curTurn
        this.lastPath = lastPath
        this.status = status
        this.game = copy
    }

    fun copy(): Match {
        return Match(p1, p2!!, p1Score, p2Score, whoChoseSize!!, curTurn!!, lastPath, status, game!!.copy())
    }

    fun initGame(row: Int, col: Int) {
        this.game!!.board = Game.setupBoard(row, col)
        status = MatchStatus.RUN
    }

    fun incP1Score(increment: Int) {
        p1Score += increment
    }

    fun incP2Score(increment: Int) {
        p2Score += increment
    }

    fun switchTurn() {
        curTurn = if (curTurn == p1) p2 else p1
    }
}
