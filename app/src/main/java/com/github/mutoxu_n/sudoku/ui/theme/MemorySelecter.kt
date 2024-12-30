package com.github.mutoxu_n.sudoku.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.mutoxu_n.sudoku.MemoryType
import com.github.mutoxu_n.sudoku.R

@Composable
fun MemoryManager(
    modifier: Modifier = Modifier,
    onLoadMemory: (MemoryType)->Unit = {},
    onSaveMemory: (MemoryType)->Unit = {},
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    Button(
        modifier = modifier,
        onClick = { showDialog = true }
    ) {
        Text(stringResource(R.string.button_memory))
    }

    if (showDialog) {
        MemoryManagerDialog(
            onDismiss = { showDialog = false },
            onLoadMemory = {
                onLoadMemory(it)
                showDialog = false
            },
            onSaveMemory = {
                onSaveMemory(it)
                showDialog = false
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MemoryManagerDialog(
    onDismiss: () -> Unit = {},
    onLoadMemory: (MemoryType) -> Unit = {},
    onSaveMemory: (MemoryType) -> Unit = {},
) {
    var isLoad by rememberSaveable { mutableStateOf(true) }
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var memoryType by rememberSaveable { mutableStateOf(MemoryType.MEMORY_1) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(stringResource(R.string.dialog_memory_title)) },
        text = {
            Column {
                Text(text = stringResource(R.string.dialog_memory_text_operation_type))
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = 0, count = 2,
                        ),
                        onClick = { isLoad = true },
                        selected = isLoad,
                        label = {
                            Text(
                                text = stringResource(R.string.button_load),
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = null,
                            )
                        }
                    )

                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = 1, count = 2,
                        ),
                        onClick = { isLoad = false },
                        selected = !isLoad,
                        label = {
                            Text(
                                text = stringResource(R.string.button_save),
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = null,
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.size(8.dp))
                if(isLoad) Text(stringResource(R.string.dialog_memory_text_load))
                else Text(stringResource(R.string.dialog_memory_text_save))

                ExposedDropdownMenuBox(
                    modifier = Modifier.fillMaxWidth(),
                    expanded = isExpanded,
                    onExpandedChange = { isExpanded = !isExpanded },
                ) {
                    OutlinedTextField(
                        modifier = Modifier.menuAnchor(),
                        readOnly = true,
                        value = stringResource(memoryType.stringId),
                        onValueChange = {},
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                        },
                        supportingText = {
                            if(isLoad) Text(text = stringResource(R.string.dialog_memory_caution_load))
                            else Text(text = stringResource(R.string.dialog_memory_caution_save))
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false }
                    ) {
                        MemoryType.entries.forEach { selection ->
                            if(selection == MemoryType.CURRENT) return@forEach

                            DropdownMenuItem(
                                text = { Text(text = stringResource(selection.stringId)) },
                                onClick = {
                                    memoryType = selection
                                    isExpanded = false
                                }
                            )
                        }

                    }
                }

            }
        },
        confirmButton = {
            Row {
                Button(onClick = {
                    if(isLoad) onLoadMemory(memoryType)
                    else onSaveMemory(memoryType)
                }) {
                    Text(stringResource(R.string.button_ok))
                }
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(stringResource(R.string.button_close))
            }
        }

    )
}

@Preview
@Composable
fun MemorySelectorPreview() {
    MaterialTheme {
        Surface {
            MemoryManagerDialog()
        }
    }
}
