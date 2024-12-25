package com.github.mutoxu_n.sudoku

import android.app.Application
import com.github.mutoxu_n.sudoku.game.SudokuBoard
import com.github.mutoxu_n.sudoku.game.SudokuCell

class App: Application() {
    companion object {
        private const val TAG = "sudoku_app"
        private const val PREF_DIFFICULTY = "difficulty"
        private const val PREF_PROBLEM_ID = "problem_id"
        private const val PREF_BOARD_TYPE = "board_type_"
        private const val PREF_BOARD = "board_"

        private var instance: Application? = null

        fun savePreference(difficulty: Difficulty, problemId: Int, board: SudokuBoard){
            if(instance == null) return

            val sharedPreferences = instance!!.getSharedPreferences(TAG, MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(PREF_DIFFICULTY, difficulty.name)
            editor.putInt(PREF_PROBLEM_ID, problemId)
            for(i in 0 until 81) {
                editor.putString(PREF_BOARD_TYPE + i, board.getCell(i % 9, i / 9).type.name)
                editor.putInt(PREF_BOARD + i, board.getCell(i % 9, i / 9).data)
            }
            editor.apply()
        }

        fun clearPreference(){
            if(instance == null) return

            val sharedPreferences = instance!!.getSharedPreferences(TAG, MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
        }

        fun getBoardInPreference(): SudokuBoard? {
            if(instance == null) return null

            val sharedPreferences = instance!!.getSharedPreferences(TAG, MODE_PRIVATE)
            val diffRaw = sharedPreferences.getString(PREF_DIFFICULTY, null) ?: return null
            val difficulty = Difficulty.valueOf(diffRaw)

            val problemId = sharedPreferences.getInt(PREF_PROBLEM_ID, 0)
            val problem = getProblemFromId(
                instance!!.resources.openRawResource(difficulty.dataId),
                problemId,
            )

            if(problem.isEmpty()) return null
            val board = SudokuBoard(problem)

            for(i in 0 until 81) {
                val typeRaw = sharedPreferences.getString(PREF_BOARD_TYPE + i, null) ?: return null
                val type = SudokuCell.SudokuCellType.valueOf(typeRaw)
                val data = sharedPreferences.getInt(PREF_BOARD + i, 0)
                board.getCell(i % 9, i / 9).write(type, data)
            }

            return board
        }

        fun getDifficulty(): Difficulty? {
            if(instance == null) return null

            val sharedPreferences = instance!!.getSharedPreferences(TAG, MODE_PRIVATE)
            val diffRaw = sharedPreferences.getString(PREF_DIFFICULTY, null) ?: return null
            return Difficulty.valueOf(diffRaw)
        }

        fun getProblemId(): Int {
            if(instance == null) return 0

            val sharedPreferences = instance!!.getSharedPreferences(TAG, MODE_PRIVATE)
            return sharedPreferences.getInt(PREF_PROBLEM_ID, 0)
        }
    }

    init {
        if(instance == null) {
            instance = this
        }
    }
}