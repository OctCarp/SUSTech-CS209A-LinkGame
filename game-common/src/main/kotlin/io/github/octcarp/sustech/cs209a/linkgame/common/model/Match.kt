package io.github.octcarp.sustech.cs209a.linkgame.common.model

import java.io.Serializable
import kotlin.random.Random

class Match : Serializable {
    lateinit var p1: String
        private set
    lateinit var p2: String
        private set

    var p1Score: Int = 0
        private set
    var p2Score: Int = 0
        private set

    lateinit var whoChoseSize: String
        private set

    lateinit var curTurn: String
        private set

    var lastPath: List<GridPos>? = null

    lateinit var game: Game


    enum class MatchStatus {
        INIT,
        RUN,
        P1_DIS,
        P2_DIS,
        FINISHED
    }

    var status: MatchStatus = MatchStatus.INIT

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

    constructor(
        p1: String,
        p2: String,
        p1Score: Int,
        p2Score: Int,
        whoChoseSize: String,
        curTurn: String,
        lastPath: List<GridPos>?,
        status: MatchStatus,
        game: Game
    ) {
        this.p1 = p1
        this.p2 = p2
        this.p1Score = p1Score
        this.p2Score = p2Score
        this.whoChoseSize = whoChoseSize
        this.curTurn = curTurn
        this.lastPath = lastPath
        this.status = status
        this.game = game
    }

    fun deepCopy() = Match(
        p1 = p1.toString(),
        p2 = p2.toString(),
        p1Score = p1Score,
        p2Score = p2Score,
        whoChoseSize = whoChoseSize.toString(),
        curTurn = curTurn.toString(),
        lastPath = lastPath,
        status = status,
        game = game.deepCopy()
    )

    fun initGame(row: Int, col: Int) {
        this.game.board = Game.setupBoard(row, col)
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
