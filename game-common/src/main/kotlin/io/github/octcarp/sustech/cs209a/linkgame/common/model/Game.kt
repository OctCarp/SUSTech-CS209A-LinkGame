package io.github.octcarp.sustech.cs209a.linkgame.common.model

import java.io.Serializable
import kotlin.random.Random

class Game : Serializable {
    // row length
    var row: Int = 0
        private set

    // col length
    var col: Int = 0
        private set

    // board content
    var board: Array<IntArray>

    constructor() {
        board = Array<IntArray>(0) { IntArray(0) }
    }

    constructor(row: Int, col: Int) {
        this.row = row
        this.col = col
        this.board = setupBoard(row, col)
    }

    constructor(board: Array<IntArray>) {
        this.board = board
        this.row = board.size
        this.col = board[0].size
    }

    fun deepCopy(): Game {
        val newBoard = board.map { it.copyOf() }.toTypedArray()
        return Game(newBoard)
    }

    fun shuffleBoard() {
        val random = Random.Default
        for (i in board.indices) {
            for (j in board[i].indices) {
                val newI = random.nextInt(board.size)
                val newJ = random.nextInt(board[0].size)
                val temp = board[i][j]
                board[i][j] = board[newI][newJ]
                board[newI][newJ] = temp
            }
        }
    }

    fun judge(row1: Int, col1: Int, row2: Int, col2: Int): List<GridPos>? {
        if ((board[row1][col1] != board[row2][col2]) || (row1 == row2 && col1 == col2)) {
            return null
        }

        // one line
        if (isDirectlyConnected(row1, col1, row2, col2, board)) {
            return getDirectPath(row1, col1, row2, col2, board)
        }

        // two lines
        if ((row1 != row2) && (col1 != col2)) {
            if (board[row1][col2] == 0 && isDirectlyConnected(row1, col1, row1, col2, board)
                && isDirectlyConnected(row1, col2, row2, col2, board)
            ) {
                val path1 = getDirectPath(row1, col1, row1, col2, board)
                val path2 = getDirectPath(row1, col2, row2, col2, board)
                if (path1 != null && path2 != null) {
                    return path1 + GridPos(row1, col2) + path2
                }
            }
            if (board[row2][col1] == 0 && isDirectlyConnected(row2, col2, row2, col1, board)
                && isDirectlyConnected(row2, col1, row1, col1, board)
            ) {
                val path1 = getDirectPath(row1, col1, row2, col1, board)
                val path2 = getDirectPath(row2, col1, row2, col2, board)
                if (path1 != null && path2 != null) {
                    return path1 + GridPos(row2, col1) + path2
                }
            }
        }

        // three lines
        if (row1 != row2) for (i in board[0].indices) {
            if (board[row1][i] == 0 && board[row2][i] == 0 &&
                isDirectlyConnected(row1, col1, row1, i, board) && isDirectlyConnected(row1, i, row2, i, board)
                && isDirectlyConnected(row2, col2, row2, i, board)
            ) {
                val path1 = getDirectPath(row1, col1, row1, i, board)
                val path2 = getDirectPath(row1, i, row2, i, board)
                val path3 = getDirectPath(row2, col2, row2, i, board)
                if (path1 != null && path2 != null && path3 != null) {
                    return path1 + GridPos(row1, i) + path2 + GridPos(row2, i) + path3
                }
            }
        }
        if (col1 != col2) for (j in board.indices) {
            if (board[j][col1] == 0 && board[j][col2] == 0 &&
                isDirectlyConnected(row1, col1, j, col1, board) && isDirectlyConnected(j, col1, j, col2, board)
                && isDirectlyConnected(row2, col2, j, col2, board)
            ) {
                val path1 = getDirectPath(row1, col1, j, col1, board)
                val path2 = getDirectPath(j, col1, j, col2, board)
                val path3 = getDirectPath(row2, col2, j, col2, board)
                if (path1 != null && path2 != null && path3 != null) {
                    return path1 + GridPos(j, col1) + path2 + GridPos(j, col2) + path3
                }
            }
        }

        return null
    }

    // judge the validity of an operation
    private fun isDirectlyConnected(row1: Int, col1: Int, row2: Int, col2: Int, board: Array<IntArray>): Boolean {
        if (row1 == row2) {
            val minCol = minOf(col1, col2)
            val maxCol = maxOf(col1, col2)
            return (minCol + 1..<maxCol).none { board[row1][it] != 0 }
        }

        if (col1 == col2) {
            val minRow = minOf(row1, row2)
            val maxRow = maxOf(row1, row2)
            return (minRow + 1..<maxRow).none { board[it][col1] != 0 }
        }

        return false
    }

    private fun getDirectPath(
        row1: Int,
        col1: Int,
        row2: Int,
        col2: Int,
        board: Array<IntArray>
    ): List<GridPos>? {
        val path = mutableListOf<GridPos>()

        if (row1 != row2 && col1 != col2) {
            return null
        }

        val rowStep = row2.compareTo(row1)
        val colStep = col2.compareTo(col1)

        var currentRow = row1 + rowStep
        var currentCol = col1 + colStep

        while (currentRow != row2 || currentCol != col2) {
            if (board[currentRow][currentCol] != 0) {
                return null
            }
            path.add(GridPos(currentRow, currentCol))
            currentRow += rowStep
            currentCol += colStep
        }

        return path
    }

    fun clearGrids(row1: Int, col1: Int, row2: Int, col2: Int) {
        if (board[row1][col1] > 0) {
            board[row1][col1] = 0
            board[row2][col2] = 0
        }
    }

    fun gameFinished() = board.none { row -> row.any { it != 0 } }

    companion object {
        // randomly initialize the game board
        private const val GRID_TYPE_NUM = 12

        fun setupBoard(row: Int, col: Int): Array<IntArray> {
            val gridNum = row * col
            require(gridNum % 2 == 0) { "The number of grids must be even." }

            val pieces = mutableListOf<Int>()
            val numPairs = gridNum / 2
            val piecesPerType = numPairs / GRID_TYPE_NUM

            repeat(GRID_TYPE_NUM) { i ->
                repeat(piecesPerType) {
                    pieces.add(i + 1)
                    pieces.add(i + 1)
                }
            }

            while (pieces.size < gridNum) {
                val extraPiece = Random.nextInt(GRID_TYPE_NUM) + 1
                pieces.add(extraPiece)
                pieces.add(extraPiece)
            }

            pieces.shuffle()

            return Array(row) { i ->
                IntArray(col) { j ->
                    pieces[i * col + j]
                }
            }
        }
    }
}
