package io.github.octcarp.sustech.cs209a.linkgame.server.model

import io.github.octcarp.sustech.cs209a.linkgame.common.model.GridPos
import io.github.octcarp.sustech.cs209a.linkgame.common.model.Match
import io.github.octcarp.sustech.cs209a.linkgame.common.model.MatchRes
import io.github.octcarp.sustech.cs209a.linkgame.server.net.ClientHandlerThread
import java.io.Serializable

class MatchInfo(
    var match: Match,
    val p1: String,
    val p2: String,
    var p1Thread: ClientHandlerThread,
    var p2Thread: ClientHandlerThread
) : Serializable {

    var result: MatchRes? = null

    fun judgeMoveAndUpdate(playerId: String, start: GridPos, end: GridPos) {
        val path = match.game!!.judge(start.row, start.col, end.row, end.col)?.toMutableList()

        path?.let {
            it.add(0, start)
            it.add(end)
            if (p1 == playerId) {
                match.incP1Score(1)
            } else {
                match.incP2Score(1)
            }
        }

        match.switchTurn()
        match.lastPath = path?.toList() ?: emptyList()
    }
}
