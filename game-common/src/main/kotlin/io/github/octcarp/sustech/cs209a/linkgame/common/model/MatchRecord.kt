package io.github.octcarp.sustech.cs209a.linkgame.common.model

import java.io.Serializable

data class MatchRecord(
    val p1: String,
    val p2: String,
    val p1Score: Int,
    val p2Score: Int,
    val endTime: String,
    val result: String
) : Serializable {

    override fun toString(): String = "$p1 ($p1Score) vs $p2 ($p2Score) | $endTime | $result"
}
