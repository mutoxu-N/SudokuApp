package com.github.mutoxu_n.sudoku.Sudoku

class SudokuCell(value: Int) {
    enum class SudokuCellType {
        EMPTY,
        MEMO,
        FIXED,
    }

    var type: SudokuCellType = SudokuCellType.EMPTY
    var data: Int = 0
        private set

    val number: Int
        get() = if (type == SudokuCellType.MEMO) 0 else data

    init {
        if(value != 0) {
            type = SudokuCellType.FIXED
            data = value
        }
    }

    fun setData(value: Int, isMemo: Boolean = true) {
        data =
            if (isMemo) data xor (1 shl value)
            else value
    }

}
