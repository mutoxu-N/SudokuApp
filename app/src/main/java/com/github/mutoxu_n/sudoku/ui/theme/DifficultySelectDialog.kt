package com.github.mutoxu_n.sudoku.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.mutoxu_n.sudoku.Difficulty
import com.github.mutoxu_n.sudoku.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DifficultySelectDialog(
    onDismiss: () -> Unit,
    onConfirm: (Difficulty, Int) -> Unit,
) {
    var difficulty by remember { mutableStateOf(Difficulty.EASY) }
    var problemNum by remember { mutableIntStateOf(-1) }

    AlertDialog(
        title = { Text(text = stringResource(R.string.dialog_difficulty_title)) },
        text = {
            Column {
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                    ,
                ) {
                    Difficulty.entries.forEachIndexed { index, diff ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(index, Difficulty.entries.size),
                            onClick = { difficulty = diff },
                            selected = difficulty == diff,
                            label = { Text(text = stringResource(diff.stringId())) },
                        )
                    }
                }

                Spacer(Modifier.size(16.dp))

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = if(problemNum == -1) "" else problemNum.toString(),
                    onValueChange = {
                        if(it.isEmpty()) problemNum = -1
                        else {
                            val new = it.toIntOrNull()
                            if(new != null) problemNum = it.toInt()
                        }
                    },
                    label = { Text(text = stringResource(R.string.dialog_textfield_problem_id)) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done,
                    ),
                    isError = problemNum == -1,
                    trailingIcon = {
                        if(problemNum == -1) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                tint = MaterialTheme.colorScheme.error,
                                contentDescription = null,
                            )
                        }
                    },
                    supportingText = {
                        if(problemNum == -1) {
                            Text(
                                text = stringResource(R.string.dialog_error_empty)
                            )
                        }
                    }
                )
            }
        },

        onDismissRequest = { onDismiss() },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text(text = stringResource(R.string.button_cancel))
            }

        },
        confirmButton = {
            Button(
                onClick = { onConfirm(difficulty, problemNum) },
                enabled = problemNum != -1,
            ) {
                Text(text = stringResource(R.string.button_ok))
            }
        }
    )
}


@Preview
@Composable
fun DifficultySelectDialogPreview() {
    SudokuTheme {
        DifficultySelectDialog(
            onDismiss = {},
            onConfirm = { _, _ -> },
        )
    }
}