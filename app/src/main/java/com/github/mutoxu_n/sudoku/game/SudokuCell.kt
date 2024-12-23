package com.github.mutoxu_n.sudoku.game

class SudokuCell(
    val x: Int,
    val y: Int,
    value: Int
) {
    enum class SudokuCellType {
        EMPTY,
        MEMO,
        FIXED,
        IMMUTABLE,
    }

    var type: SudokuCellType = SudokuCellType.EMPTY
        private set

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
        if(type == SudokuCellType.FIXED && isMemo) return

        if(isMemo) {
            data = data xor (1 shl value)
            type = SudokuCellType.MEMO
        } else {
            data = value
            type = SudokuCellType.FIXED
        }
    }

    fun memoString(): String {
        var d = data shr 1
        var s = ""
        for(i in 1..9) {
            if(d % 2 == 1) {
                s += "$i"
            }
            d = d shr 1
        }
        return s
    }
}
