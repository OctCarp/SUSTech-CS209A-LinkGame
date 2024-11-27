package io.github.octcarp.sustech.cs209a.linkgame.common.model

import java.io.Serializable

data class GridPos(
    val row: Int,
    val col: Int
) : Serializable {

    override fun equals(obj: Any?): Boolean {
        if (this === obj) {
            return true
        }
        if (obj == null || javaClass != obj.javaClass) {
            return false
        }
        val pos = obj as GridPos
        return row == pos.row && col == pos.col
    }

    init {
        if (row < 0 || col < 0) {
            System.err.printf("Invalid grid position: (%d, %d)\n", row, col)
        }
    }

    override fun hashCode(): Int {
        var result = row
        result = 31 * result + col
        return result
    }
}
