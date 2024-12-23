@file:OptIn(ExperimentalMaterial3Api::class)

package com.github.mutoxu_n.sudoku

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TextSnippet
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val problem = intent.getStringExtra(ARG_PROBLEM)!!
        Log.i("GameActivity", "problem: $problem")
        val b = SudokuBoard(problem)

        setContent {
            SudokuTheme {
                val board by remember { mutableStateOf(b) }
                var cursorX by rememberSaveable { mutableIntStateOf(-1) }
                var cursorY by rememberSaveable { mutableIntStateOf(-1) }
                var isMemo by rememberSaveable { mutableStateOf(false) }

                Screen(
                    board = board,
                    cursorX = cursorX,
                    cursorY = cursorY,
                    isMemo = isMemo,
                    onCellClicked = { x, y ->
                        cursorX = x
                        cursorY = y
                    },
                    onNumberClicked = { x, y, n ->
                        board.put(x, y, n, isMemo)
                    },
                    onIsMemoClicked = {
                        isMemo = it
                    },
                )
            }
        }
    }
}

@Composable
private fun Screen(
    board: SudokuBoard,
    cursorX: Int = -1,
    cursorY: Int = -1,
    isMemo: Boolean = false,
    onCellClicked: (Int, Int) -> Unit = { _, _ -> },
    onNumberClicked: (Int, Int, Int) -> Unit = { _, _, _ -> },
    onIsMemoClicked: (Boolean) -> Unit = {},
) {
    val colorWrite =
        if(isMemo) MaterialTheme.colorScheme.outline
        else MaterialTheme.colorScheme.onSurface

    val colorMemo =
        if(isMemo) MaterialTheme.colorScheme.onSurface
        else MaterialTheme.colorScheme.outline

    Column(
        modifier = Modifier
            .fillMaxSize()
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        SingleChoiceSegmentedButtonRow {
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = 0, count = 2,
                ),
                onClick = { onIsMemoClicked(false) },
                selected = !isMemo,
                label = {
                    Text(
                        text = stringResource(R.string.button_write),
                        color = colorWrite,
                    )
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        tint = colorWrite,
                        contentDescription = null,
                    )
                }
            )
            SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = 1, count = 2,
                    ),
                onClick = { onIsMemoClicked(true) },
                selected = isMemo,
                label = {
                    Text(
                        text = stringResource(R.string.button_note),
                        color = colorMemo,
                    )
                },
                icon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.TextSnippet,
                        tint = colorMemo,
                        contentDescription = null,
                    )
                }
            )
        }

        Spacer(Modifier.size(5.dp))

        SudokuUi(
            board = board,
            cursorX = cursorX,
            cursorY = cursorY,
            onCellClicked = { x, y -> onCellClicked(x, y) }
        )

        Spacer(Modifier.size(5.dp))

        NumberPad(
            cell =
                if(cursorX == -1 || cursorY == -1) null
                else board.getCell(cursorX, cursorY),
            onNumberClicked = { x, y, number -> onNumberClicked(x, y, number) },
        )
    }
}

@Composable
private fun NumberPad(
    cell: SudokuCell?,
    onNumberClicked: (Int, Int, Int) -> Unit = { _, _, _ -> },
) {
    val width = LocalConfiguration.current.screenWidthDp.dp / 10
    val l = cell?.memo()

    Column(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.primaryContainer
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
                            cell?.let {
                                onNumberClicked(it.x, it.y, i)
                            }
                        }
                    ,
                    contentAlignment = Alignment.Center,
                ) {

                    val type = cell?.type ?: SudokuCell.SudokuCellType.EMPTY
                    when(type) {
                        SudokuCell.SudokuCellType.MEMO -> {
                            if(l != null && l[i-1]){
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
                            if(cell != null && i == cell.number){
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
            isMemo = false,
        )
    }
}
