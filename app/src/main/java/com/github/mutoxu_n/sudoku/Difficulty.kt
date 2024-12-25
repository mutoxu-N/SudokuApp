package com.github.mutoxu_n.sudoku
import androidx.annotation.RawRes

enum class Difficulty(@RawRes val dataId: Int) {
    EASY(R.raw.easy),
    MEDIUM(R.raw.medium),
    HARD(R.raw.hard);

    fun stringId() = when(this) {
        EASY -> R.string.difficulty_easy
        MEDIUM -> R.string.difficulty_medium
        HARD -> R.string.difficulty_hard
    }
}