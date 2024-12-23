package com.github.mutoxu_n.sudoku.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.mutoxu_n.sudoku.ui.theme.SudokuTheme

private val boarderS = 1.dp
private val boarderM = 3.dp

@Composable
fun SudokuUi(
    board: SudokuBoard,
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(modifier = Modifier.size(boarderM))
        for(y in 0..<3) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.size(boarderM))
                for(x in 0..<3) {
                    val l = mutableListOf<SudokuCell>()
                    for (y2 in 0..<3) {
                        for (x2 in 0..<3) {
                            l.add(board.board[y * 3 + y2][x * 3 + x2])
                        }
                    }

                    SudokuBlockUi(l.toList())
                    if(x != 2)
                        Spacer(modifier = Modifier.size(boarderM))
                }
                Spacer(modifier = Modifier.size(boarderM))
            }
        }
    }
}


@Composable
fun SudokuBlockUi(
    block: List<SudokuCell>,
) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        for(y in 0..<3) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                for (x in 0..<3) {
                    SudokuCellUi(cell = block[y * 3 + x])
                    if(x != 2)
                        Spacer(modifier = Modifier.size(boarderS))
                }
            }
            if(y != 2)
                Spacer(modifier = Modifier.size(boarderS))
        }
        Spacer(modifier = Modifier.size(boarderM))
    }
}

@Composable
fun SudokuCellUi(
    cell: SudokuCell,
) {
    val width = LocalConfiguration.current.screenWidthDp.dp / 10

    Box(
        modifier = Modifier
            .width(width)
            .aspectRatio(1f)
            .background(
                when(cell.type) {
                    SudokuCell.SudokuCellType.IMMUTABLE -> MaterialTheme.colorScheme.primaryContainer
                    else -> MaterialTheme.colorScheme.surface
                }
            )
            .padding(1.dp)
        ,
        contentAlignment = Alignment.Center,
    ) {
        when(cell.type) {
            SudokuCell.SudokuCellType.MEMO -> {
                Text(
                    text = cell.memoString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline,
                )
            }

            SudokuCell.SudokuCellType.FIXED -> {
                Text(
                    text = cell.number.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold,
                )
            }

            SudokuCell.SudokuCellType.IMMUTABLE -> {
                Text(
                    text = cell.number.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                )
            }

            else -> {}
        }
    }
}

@Preview
@Composable
fun SudokuUiPreview() {
    val board = SudokuBoard(".1...4..7.9...5..2..6....48....4.73.....9...428..7..1...9......86.......3..82....")
    board.put(0, 0, 5, isMemo = false)
    board.put(0, 1, 4, isMemo = true)
    board.put(0, 1, 7, isMemo = true)

    SudokuTheme {
        Box(contentAlignment = Alignment.Center) {
            SudokuUi(
                board
            )
        }
    }
}