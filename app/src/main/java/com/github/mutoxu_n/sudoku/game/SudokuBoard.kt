package com.github.mutoxu_n.sudoku.game

class SudokuBoard(private val problem: String) {
    val board = Array(9) { Array(9) { SudokuCell(-1, -1, -1) } }

    init {
        reset()
    }

    fun reset() {
        for (y in 0 until 9) {
            for (x in 0 until 9) {
                val c = problem[y * 9 + x]
                board[y][x] = SudokuCell(
                    x, y,
                    c.digitToIntOrNull() ?: 0
                )
            }
        }
    }

    fun put(posX: Int, posY: Int, value: Int, isMemo: Boolean = true): Boolean {
        if(!isMemo) {
            // check row
            for (x in 0..<9)
                if (x != posX && board[posY][x].number == value)
                    return false

            // check column
            for (y in 0..<9)
                if (y != posY && board[y][posX].number == value)
                    return false

            // check block
            val blockX = posX / 3
            val blockY = posY / 3
            for (x in blockX * 3..<blockX * 3 + 3)
                for (y in blockY * 3..<blockY * 3 + 3)
                    if (!(x == posX && y == posY) && board[y][x].number == value)
                        return false
        }

        board[posY][posX].setData(value, isMemo)
        return true
    }
}
