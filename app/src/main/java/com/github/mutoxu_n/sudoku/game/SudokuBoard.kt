package com.github.mutoxu_n.sudoku.game

class SudokuBoard(private val problem: String) {
    val board = Array(9) { Array(9) { SudokuCell(0) } }

    init {
        reset()
    }

    fun reset() {
        for (i in 0 until 9) {
            for (j in 0 until 9) {
                val c = problem[i * 9 + j]
                board[i][j] = SudokuCell(c.digitToIntOrNull() ?: 0)
            }
        }
    }

    fun put(posX: Int, posY: Int, value: Int, isMemo: Boolean = true): Boolean {
//        // check row
//        for(x in 0..<9)
//            if(board[posY][x].number == value)
//                return false
//
//        // check column
//        for(y in 0..<9)
//            if(board[y][posX].number == value)
//                return false
//
//        // check block
//        val blockX = posX / 3
//        val blockY = posY / 3
//        for(x in blockX * 3..<blockX * 3 + 3)
//            for(y in blockY * 3..<blockY * 3 + 3)
//                if(board[y][x].number == value)
//                    return false

        board[posY][posX].setData(value, isMemo)
        return true
    }


}