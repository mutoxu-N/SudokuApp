package com.github.mutoxu_n.sudoku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.mutoxu_n.sudoku.ui.theme.SudokuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SudokuTheme {
                Screen(
                    onLaunchButtonClicked = ::launchGame,
                )
            }
        }
    }

    private fun launchGame(difficulty: Difficulty) {
        val stream = resources.openRawResource(difficulty.dataId)
        val problem = choiceProblem(stream)
        GameActivity.launch(this@MainActivity, problem)
    }
}

@Composable
private fun Screen(
    onLaunchButtonClicked: (Difficulty) -> Unit = {},
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.displayLarge,
            )

            Spacer(modifier = Modifier.size(24.dp))

            // easy
            Button(
                modifier = Modifier
                    .width(180.dp)
                ,
                onClick = { onLaunchButtonClicked(Difficulty.EASY) },
            ) {
                Text(text = stringResource(R.string.difficulty_easy))
            }

            // medium
            Button(
                modifier = Modifier
                    .width(180.dp)
                ,
                onClick = { onLaunchButtonClicked(Difficulty.MEDIUM) },
            ) {
                Text(text = stringResource(R.string.difficulty_medium))
            }

            // hard
            Button(
                modifier = Modifier
                    .width(180.dp)
                ,
                onClick = { onLaunchButtonClicked(Difficulty.HARD) },
            ) {
                Text(text = stringResource(R.string.difficulty_hard))
            }
        }
    }
}

@Preview
@Composable
private fun ScreenPreview() {
    SudokuTheme {
        Screen()
    }
}