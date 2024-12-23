package com.github.mutoxu_n.sudoku
import androidx.annotation.RawRes

enum class Difficulty(@RawRes val dataId: Int) {
    EASY(R.raw.easy),
    MEDIUM(R.raw.medium),
    HARD(R.raw.hard),
}