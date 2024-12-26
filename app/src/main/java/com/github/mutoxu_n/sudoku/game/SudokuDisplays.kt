package com.github.mutoxu_n.sudoku.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
    cursorX: Int = -1,
    cursorY: Int = -1,
    onCellClicked: (Int, Int) -> Unit = { _, _ -> },
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
                            l.add(board.getCell(x*3+x2, y*3+y2))
                        }
                    }

                    SudokuBlockUi(
                        l.toList(),
                        cursorX,
                        cursorY,
                        onCellClicked = { xx, yy -> onCellClicked(xx, yy)},
                    )
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
    cursorX: Int = -1,
    cursorY: Int = -1,
    onCellClicked: (Int, Int) -> Unit = { _, _ -> },
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
                    SudokuCellUi(
                        cell = block[y * 3 + x],
                        cursorX,
                        cursorY,
                        onCellClicked = { xx, yy -> onCellClicked(xx, yy)},
                    )
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
    cursorX: Int = -1,
    cursorY: Int = -1,
    onCellClicked: (Int, Int) -> Unit = { _, _ -> },
) {
    val width = LocalConfiguration.current.screenWidthDp.dp / 10

    val modifier =
        if(cursorX == cell.x && cursorY == cell.y && cell.type != SudokuCell.SudokuCellType.IMMUTABLE) {
            Modifier
                .width(width)
                .aspectRatio(1f)
                .background(
                    when(cell.type) {
                        SudokuCell.SudokuCellType.IMMUTABLE -> MaterialTheme.colorScheme.primaryContainer
                        else -> MaterialTheme.colorScheme.surface
                    }
                )
                .padding(1.dp)
                .clickable {
                    onCellClicked(cell.x, cell.y)
                }
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.error,
                )
        } else {
            Modifier
                .width(width)
                .aspectRatio(1f)
                .background(
                    when(cell.type) {
                        SudokuCell.SudokuCellType.IMMUTABLE -> MaterialTheme.colorScheme.primaryContainer
                        else -> MaterialTheme.colorScheme.surface
                    }
                )
                .padding(1.dp)
                .clickable {
                    onCellClicked(cell.x, cell.y)
                }
        }


    Box(
        modifier = modifier,
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
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
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
                board,
                cursorX = 1,
                cursorY = 4,
            )
        }
    }
}

@Preview
@Composable
fun SudokuIconPreview() {
    val board = SudokuBoard(
    "........." +
            "........." +
            "........." +
            "...5....." +
            ".....4..." +
            "...2....." +
            "........." +
            "........." +
            "........."
    )
    board.put(3, 4, 9, isMemo = false)
    board.put(5, 3, 3, isMemo = false)

    board.put(4, 5, 7, isMemo = true)
    board.put(4, 5, 8, isMemo = true)

    board.put(5, 5, 1, isMemo = true)
    board.put(5, 5, 8, isMemo = true)

    board.put(4, 4, 6, isMemo = false)

    board.put(4, 3, 1, isMemo = true)
    board.put(4, 3, 7, isMemo = true)


    SudokuTheme {
        Box(contentAlignment = Alignment.Center) {
            SudokuUi(
                board,
                cursorX = -1,
                cursorY = -1,
            )
        }
    }
}