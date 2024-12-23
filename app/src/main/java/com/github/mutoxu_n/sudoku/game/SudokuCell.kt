package com.github.mutoxu_n.sudoku.game

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class SudokuCell(
    val x: Int,
    val y: Int,
    value: Int,
) {
    enum class SudokuCellType {
        EMPTY,
        MEMO,
        FIXED,
        IMMUTABLE,
    }

    var type by mutableStateOf(SudokuCellType.EMPTY)
        private set

    private var data by mutableIntStateOf(0)

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
        if(type == SudokuCellType.FIXED && isMemo) {
            data = 1 shl data
        }

        if(isMemo) {
            data = data xor (1 shl value)
            type = SudokuCellType.MEMO

        } else {
            Log.d("setData", "value: $value, number: $number")
            if(type == SudokuCellType.FIXED && number == value){
                reset()

            } else {
                data = value
                type = SudokuCellType.FIXED
            }

        }
    }

    fun reset() {
        if(type == SudokuCellType.IMMUTABLE) return
        data = 0
        type = SudokuCellType.EMPTY

    }

    fun memo(): Array<Boolean> {
        var d = data shr 1
        val l = Array(9) {false}
        for(i in 0..<9) {
            if(d % 2 == 1) {
                l[i] = true
            }
            d = d shr 1
        }
        return l
    }

    fun memoString(): String {
        val l = memo()
        var s = ""
        for(i in 0..<9) {
            if(l[i]) s += (i + 1).toString()
        }
        return s
    }

}
