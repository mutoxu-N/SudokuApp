package com.github.mutoxu_n.sudoku

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.mutoxu_n.sudoku.game.SudokuBoard
import com.github.mutoxu_n.sudoku.game.SudokuCell
import com.github.mutoxu_n.sudoku.game.SudokuUi
import com.github.mutoxu_n.sudoku.ui.theme.SudokuTheme

class GameActivity : ComponentActivity() {
    companion object {
        private const val ARG_PROBLEM = "problem"

        fun launch(
            context: Context,
            problem: String,
        ) {
            val args = Bundle()
            args.putString(ARG_PROBLEM, problem)

            val intent = Intent(context, GameActivity::class.java)
            intent.putExtras(args)

            context.startActivity(intent)
        }
    }

    private var cursorX: Int by mutableIntStateOf(-1)
    private var cursorY: Int by mutableIntStateOf(-1)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val problem = intent.getStringExtra(ARG_PROBLEM)!!
        val board = SudokuBoard(problem)
        setContent {
            SudokuTheme {
                Screen(
                    board = board,
                    cursorX = cursorX,
                    cursorY = cursorY,
                    onCellClicked = { x, y -> onCellClicked(x, y) },
                    onNumberClicked = { number -> onNumberClicked(number) }
                )
            }
        }
    }

    private fun onCellClicked(x: Int, y: Int) {
        cursorX = x
        cursorY = y
    }

    private fun onNumberClicked(number: Int) {
    }
}

@Composable
private fun Screen(
    board: SudokuBoard,
    cursorX: Int = -1,
    cursorY: Int = -1,
    onCellClicked: (Int, Int) -> Unit = { _, _ -> },
    onNumberClicked: (Int) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        SudokuUi(
            board = board,
            cursorX = cursorX,
            cursorY = cursorY,
            onCellClicked = { x, y -> onCellClicked(x, y) }
        )

        Spacer(Modifier.size(5.dp))

        NumberPad(
            cell = board.board[cursorY][cursorX],
            onNumberClicked = { number -> onNumberClicked(number) },
        )
    }
}

@Composable
private fun NumberPad(
    cell: SudokuCell? = null,
    onNumberClicked: (Int) -> Unit = {},
) {
    val width = LocalConfiguration.current.screenWidthDp.dp / 10
    val l = cell?.memo()

    Column(
        modifier = Modifier
            .background(
                if(cell == null) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.primaryContainer
            )
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline,
            )
        ,
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for(i in 1..9) {
                Box(
                    modifier = Modifier
                        .width(width)
                        .aspectRatio(1f)
                        .clickable {
                            onNumberClicked(i)
                        }
                    ,
                    contentAlignment = Alignment.Center,
                ) {
                    if(cell == null) {
                        Text(
                            text = i.toString(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.outline,
                        )

                    } else {
                        when(cell.type) {
                            SudokuCell.SudokuCellType.MEMO -> {
                                if(l!![i-1]){
                                    Text(
                                        text = i.toString(),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        fontWeight = FontWeight.Bold,
                                    )

                                } else {
                                    Text(
                                        text = i.toString(),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.outline,
                                    )
                                }
                            }

                            SudokuCell.SudokuCellType.FIXED -> {
                                if(i == cell.number){
                                    Text(
                                        text = i.toString(),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.error,
                                        fontWeight = FontWeight.Bold,
                                    )

                                } else {
                                    Text(
                                        text = i.toString(),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.outline,
                                    )
                                }
                            }

                            else -> {
                                Text(
                                    text = i.toString(),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.outline,
                                )
                            }
                        }

                    }
                }
            }
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ScreenPreview() {
    val board = SudokuBoard(".1...4..7.9...5..2..6....48....4.73.....9...428..7..1...9......86.......3..82....")
    board.put(0, 0, 5, isMemo = false)
    board.put(0, 1, 4, isMemo = true)
    board.put(0, 1, 7, isMemo = true)

    SudokuTheme {
        Screen(
            board,
            cursorX = 0,
            cursorY = 2,
        )
    }
}
