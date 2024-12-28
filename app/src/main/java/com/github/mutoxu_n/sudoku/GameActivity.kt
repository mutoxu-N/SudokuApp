@file:OptIn(ExperimentalMaterial3Api::class)

package com.github.mutoxu_n.sudoku

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TextSnippet
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.mutoxu_n.sudoku.game.SudokuBoard
import com.github.mutoxu_n.sudoku.game.SudokuCell
import com.github.mutoxu_n.sudoku.game.SudokuUi
import com.github.mutoxu_n.sudoku.ui.theme.SudokuTheme

class GameActivity : ComponentActivity() {
    companion object {
        private const val ARG_PROBLEM_ID = "problem_id"
        private const val ARG_DIFFICULTY = "difficulty"

        fun launch(
            context: Context,
            difficulty: Difficulty,
            problemId: Int,
        ) {
            val args = Bundle()
            args.putString(ARG_DIFFICULTY, difficulty.name)
            args.putInt(ARG_PROBLEM_ID, problemId)

            val intent = Intent(context, GameActivity::class.java)
            intent.putExtras(args)

            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val diffRaw = intent.getStringExtra(ARG_DIFFICULTY)!!
        val difficulty = Difficulty.valueOf(diffRaw)
        val problemId = intent.getIntExtra(ARG_PROBLEM_ID, 0)
        val problem = getProblemFromId(
            resources.openRawResource(difficulty.dataId),
            problemId,
        )

        Log.i("GameActivity", "problem: $problem")

        setContent {
            SudokuTheme {
                var showBackDialog by remember { mutableStateOf(false) }
                var showCompletedDialog by rememberSaveable { mutableStateOf(false) }

                // on back button pressed
                onBackPressedDispatcher.addCallback(
                    object: OnBackPressedCallback(true) {
                        override fun handleOnBackPressed() {
                            showBackDialog = true
                        }
                    }
                )

                if(problem.isNotEmpty()) {
                    val board by remember {
                        val b = App.getBoardInPreference() ?: SudokuBoard(problem)
                        App.savePreference(difficulty, problemId, b)
                        mutableStateOf(b)
                    }
                    var cursorX by rememberSaveable { mutableIntStateOf(-1) }
                    var cursorY by rememberSaveable { mutableIntStateOf(-1) }
                    var isMemo by rememberSaveable { mutableStateOf(false) }

                    Screen(
                        difficulty = difficulty,
                        problemId = problemId,
                        board = board,
                        cursorX = cursorX,
                        cursorY = cursorY,
                        isMemo = isMemo,
                        onCellClicked = { x, y ->
                            cursorX = x
                            cursorY = y
                        },
                        onNumberClicked = { x, y, n ->
                            showCompletedDialog = board.put(x, y, n, isMemo)
                            App.savePreference(difficulty, problemId, board)
                        },
                        onIsMemoClicked = {
                            isMemo = it
                        },
                        onBackClicked = {
                            showBackDialog = true
                        },
                        onResetClicked = {
                            board.reset()
                            App.savePreference(difficulty, problemId, board)
                            cursorX = -1
                            cursorY = -1
                        }
                    )
                } else {
                    AlertDialog(
                        onDismissRequest = {},
                        title = { Text(stringResource(
                            R.string.dialog_problem_not_found, problemId, difficulty.name
                        )) },
                        text = {},
                        confirmButton = {
                            TextButton(onClick = { finish() }) {
                                Text(stringResource(R.string.button_close))
                            }
                        }
                    )
                }

                if(showBackDialog) {
                    AlertDialog(
                        onDismissRequest = { showBackDialog = false },
                        title = { Text(stringResource(R.string.dialog_back_title)) },
                        text = { Text(stringResource(R.string.dialog_back_message)) },
                        confirmButton = {
                            TextButton(onClick = {
                                showBackDialog = false
                                App.clearPreference()
                                finish()
                            }) {
                                Text(
                                    stringResource(R.string.button_back),
                                    color = MaterialTheme.colorScheme.error,
                                )
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showBackDialog = false }) {
                                Text(stringResource(R.string.button_cancel))
                            }
                        }
                    )
                }

                if (showCompletedDialog) {
                    AlertDialog(
                        onDismissRequest = { showCompletedDialog = false },
                        title = { Text(stringResource(R.string.dialog_completed)) },
                        text = { Text(stringResource(R.string.dialog_completed_message)) },
                        confirmButton = {
                            TextButton(onClick = { showCompletedDialog = false }) {
                                Text(stringResource(R.string.button_close))
                            }
                        }
                    )
                }

            }
        }
    }
}

@Composable
private fun Screen(
    difficulty: Difficulty,
    problemId: Int,
    board: SudokuBoard,
    cursorX: Int = -1,
    cursorY: Int = -1,
    isMemo: Boolean = false,
    onCellClicked: (Int, Int) -> Unit = { _, _ -> },
    onNumberClicked: (Int, Int, Int) -> Unit = { _, _, _ -> },
    onIsMemoClicked: (Boolean) -> Unit = {},
    onBackClicked: () -> Unit = {},
    onResetClicked: () -> Unit = {},
) {
    var showResetDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(
                    R.string.topbar_title_difficulty,
                    stringResource(difficulty.stringId())
                )) },
                navigationIcon = {
                    IconButton(onClick = { onBackClicked() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                actions = {
                    OutlinedIconButton(onClick = { showResetDialog = true}) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                        )
                    }
                }
            )
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth(1f)
            ) {
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = 0, count = 2,
                    ),
                    onClick = { onIsMemoClicked(false) },
                    selected = !isMemo,
                    label = {
                        Text(
                            text = stringResource(R.string.button_write),
                        )
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Edit,
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
                        )
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.TextSnippet,
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

            Spacer(modifier = Modifier.size(16.dp))

            Text(
                stringResource(R.string.text_id_display, problemId),
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }

    if(showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text(stringResource(R.string.dialog_reset_title)) },
            text = { Text(stringResource(R.string.dialog_reset_message)) },
            confirmButton = {
                TextButton(onClick = {
                    showResetDialog = false
                    onResetClicked()
                }) {
                    Text(
                        stringResource(R.string.button_reset),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text(stringResource(R.string.button_cancel))
                }
            }
        )
    }
}

@Composable
private fun NumberPad(
    cell: SudokuCell?,
    onNumberClicked: (Int, Int, Int) -> Unit = { _, _, _ -> },
) {
    val l = cell?.memo()

    MultiChoiceSegmentedButtonRow(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(1f)
    ) {
        for(i in 1..9) {
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = i - 1, count = 9,
                ),
                checked = when (cell?.type) {
                    SudokuCell.SudokuCellType.MEMO -> {
                        if (l == null) false
                        else l[i - 1]
                    }

                    SudokuCell.SudokuCellType.FIXED -> {
                        cell.number == i
                    }

                    else -> false
                },
                enabled = when (cell?.type) {
                    SudokuCell.SudokuCellType.EMPTY
                        , SudokuCell.SudokuCellType.FIXED
                        , SudokuCell.SudokuCellType.MEMO -> true
                    else -> false
                },
                onCheckedChange = {
                    cell?.let {
                        onNumberClicked(it.x, it.y, i)
                    }
                },
                label = {
                    Text(
                        text = i.toString(),
                    )
                },
                icon = {}
            )
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
            difficulty = Difficulty.EASY,
            problemId = 0,
            board = board,
            cursorX = 0,
            cursorY = 2,
            isMemo = false,
        )
    }
}
