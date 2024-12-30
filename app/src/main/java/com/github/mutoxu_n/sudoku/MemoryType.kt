package com.github.mutoxu_n.sudoku

import androidx.annotation.StringRes

enum class MemoryType(
    val preferenceId: String,
    @StringRes val stringId: Int,
) {
    CURRENT("current", 0),
    MEMORY_1("memory_1", R.string.memory_1),
    MEMORY_2("memory_2", R.string.memory_2),
    MEMORY_3("memory_3", R.string.memory_3),
    MEMORY_4("memory_4", R.string.memory_4),
    MEMORY_5("memory_5", R.string.memory_5),
}