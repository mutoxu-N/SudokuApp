package com.github.mutoxu_n.sudoku

import android.app.Application
import com.github.mutoxu_n.sudoku.game.SudokuBoard
import com.github.mutoxu_n.sudoku.game.SudokuCell

class App: Application() {
    companion object {
        private const val TAG = "sudoku_app"
        private const val PREF_DIFFICULTY = "difficulty"
        private const val PREF_PROBLEM_ID = "problem_id"
        private const val PREF_BOARD_TYPE = "board_type"
        private const val PREF_BOARD = "board"

        private var instance: Application? = null

        fun clearPreference(){
            if(instance == null) return

            val sharedPreferences = instance!!.getSharedPreferences(TAG, MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
        }

        fun savePreference(
            difficulty: Difficulty,
            problemId: Int,
            board: SudokuBoard,
            type: MemoryType = MemoryType.CURRENT,
        ){
            // save board to preference
            if(instance == null) return

            val sharedPreferences = instance!!.getSharedPreferences(TAG, MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("${type.preferenceId}_$PREF_DIFFICULTY", difficulty.name)
            editor.putInt("${type.preferenceId}_$PREF_PROBLEM_ID", problemId)
            for(i in 0 until 81) {
                editor.putString("${type.preferenceId}_${PREF_BOARD_TYPE}_$i", board.getCell(i % 9, i / 9).type.name)
                editor.putInt("${type.preferenceId}_${PREF_BOARD}_$i", board.getCell(i % 9, i / 9).data)
            }
            editor.apply()
        }

        fun getBoardInPreference(type: MemoryType = MemoryType.CURRENT): SudokuBoard? {
            // get board from preference

            if(instance == null) return null

            val sharedPreferences = instance!!.getSharedPreferences(TAG, MODE_PRIVATE)
            val difficulty = getDifficulty(type) ?: return null

            val problemId = getProblemId(type)
            val problem = getProblemFromId(
                instance!!.resources.openRawResource(difficulty.dataId),
                problemId,
            )

            if(problem.isEmpty()) return null
            val board = SudokuBoard(problem)
            if(
                type == MemoryType.CURRENT
                    && sharedPreferences.getString("${type.preferenceId}_${PREF_BOARD_TYPE}_0", null) == null
            ) {
                // if current data type is null, get from old data type
                for(i in 0 until 81) {
                    val typeRaw = sharedPreferences.getString("${PREF_BOARD_TYPE}_$i", null) ?: return null
                    val cellType = SudokuCell.SudokuCellType.valueOf(typeRaw)
                    val data = sharedPreferences.getInt("${PREF_BOARD}_$i", 0)
                    board.getCell(i % 9, i / 9).write(cellType, data)
                }

            } else {
                for(i in 0 until 81) {
                    val typeRaw = sharedPreferences.getString("${type.preferenceId}_${PREF_BOARD_TYPE}_$i", null) ?: return null
                    val cellType = SudokuCell.SudokuCellType.valueOf(typeRaw)
                    val data = sharedPreferences.getInt("${type.preferenceId}_${PREF_BOARD}_$i", 0)
                    board.getCell(i % 9, i / 9).write(cellType, data)
                }
            }

            return board
        }

        fun getDifficulty(type: MemoryType = MemoryType.CURRENT): Difficulty? {
            if(instance == null) return null

            val sharedPreferences = instance!!.getSharedPreferences(TAG, MODE_PRIVATE)
            var diffRaw = sharedPreferences.getString("${type.preferenceId}_$PREF_DIFFICULTY", null)

            // if current data type is null, get from old data type
            if(type == MemoryType.CURRENT && diffRaw == null)
                diffRaw = sharedPreferences.getString(PREF_DIFFICULTY, null)

            return if(diffRaw == null) null else Difficulty.valueOf(diffRaw)
        }

        fun getProblemId(type: MemoryType = MemoryType.CURRENT): Int {
            if(instance == null) return 0

            val sharedPreferences = instance!!.getSharedPreferences(TAG, MODE_PRIVATE)
            var ret =  sharedPreferences.getInt("${type.preferenceId}_$PREF_PROBLEM_ID", -1)

            // if current data type is null, get from old data type
            if(type == MemoryType.CURRENT && ret == -1)
                ret = sharedPreferences.getInt(PREF_PROBLEM_ID, -1)

            return if(ret == -1) 0 else ret
        }
    }

    init {
        if(instance == null) {
            instance = this
        }
    }
}