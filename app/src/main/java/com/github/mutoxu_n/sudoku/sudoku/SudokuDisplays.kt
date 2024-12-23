package com.github.mutoxu_n.sudoku.sudoku

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.mutoxu_n.sudoku.ui.theme.SudokuTheme

@Composable
fun SudokuUi(
    board: SudokuBoard,
) {
    Column {
        for(y in 0..<3) {
            Row {
                for(x in 0..<3) {
                    val l = mutableListOf<SudokuCell>()
                    for (y2 in 0..<3) {
                        for (x2 in 0..<3) {
                            l.add(board.board[y * 3 + y2][x * 3 + x2])
                        }
                    }

                    SudokuBlockUi(l.toList())
                }
            }
        }
    }
}


@Composable
fun SudokuBlockUi(
    block: List<SudokuCell>,
) {
    Column {
        for(y in 0..<3) {
            Row {
                for (x in 0..<3) {
                    SudokuCellUi(cell = block[y * 3 + x])
                }
            }
        }
    }
}

@Composable
fun SudokuCellUi(
    cell: SudokuCell,
) {

    val w = LocalConfiguration.current.screenWidthDp.dp
    val width = w / 9

    Box(
        modifier = Modifier
            .width(width)
            .aspectRatio(1f),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = cell.number.toString(),
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SudokuUiPreview() {
    val board = SudokuBoard(".1...4..7.9...5..2..6....48....4.73.....9...428..7..1...9......86.......3..82....")

    SudokuTheme {
        SudokuUi(
            board
        )
    }
}