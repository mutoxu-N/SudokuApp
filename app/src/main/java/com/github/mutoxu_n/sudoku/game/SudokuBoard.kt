package com.github.mutoxu_n.sudoku.game

import androidx.compose.runtime.mutableStateListOf

class SudokuBoard(private val problem: String) {
    private val board = mutableStateListOf<SudokuCell>()

    init {
        board.addAll(List(81) { SudokuCell(-1, -1, -1) })
        reset()
    }

    fun getCell(x: Int, y: Int): SudokuCell {
        return board[y*9 + x]
    }

    fun reset() {
        for (y in 0 until 9) {
            for (x in 0 until 9) {
                val c = problem[y * 9 + x]
                board[y*9 + x] = SudokuCell(
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
                if (x != posX && board[posY*9 + x].number == value)
                    return false

            // check column
            for (y in 0..<9)
                if (y != posY && board[y*9 + posX].number == value)
                    return false

            // check block
            val blockX = posX / 3
            val blockY = posY / 3
            for (x in blockX * 3..<blockX * 3 + 3)
                for (y in blockY * 3..<blockY * 3 + 3)
                    if (!(x == posX && y == posY) && board[y*9 + x].number == value)
                        return false
        }

        board[posY*9 + posX].setData(value, isMemo)

        return board.count { it.number == 0 } == 0
    }

    fun sync(board: SudokuBoard) {
        for (i in 0 until 81) {
            this.board[i].write(
                board.getCell(i % 9, i / 9).type,
                board.getCell(i % 9, i / 9).data,
            )
        }
    }
}
