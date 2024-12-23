package com.github.mutoxu_n.sudoku.game

class SudokuCell(value: Int) {
    enum class SudokuCellType {
        EMPTY,
        MEMO,
        FIXED,
        IMMUTABLE,
    }

    var type: SudokuCellType = SudokuCellType.EMPTY
    var data: Int = 0
        private set

    val number: Int
        get() = if (type == SudokuCellType.EMPTY || type == SudokuCellType.MEMO) 0 else data

    init {
        if(value != 0) {
            type = SudokuCellType.IMMUTABLE
            data = value
        }
    }

    fun setData(value: Int, isMemo: Boolean = true) {
        if(type == SudokuCellType.IMMUTABLE) return

        data =
            if (isMemo) data xor (1 shl value)
            else value
    }
}
