package edu.put.gymrathelper.ui.addTraining

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.savedstate.SavedStateRegistryOwner


@Composable
fun Stopwatch(key: String) {
    val viewModelStoreOwner = LocalViewModelStoreOwner.current
    val savedStateRegistryOwner = viewModelStoreOwner as SavedStateRegistryOwner

    val viewModel: StopwatchViewModel = viewModel(
        factory = StopwatchViewModelFactory(savedStateRegistryOwner),
        key = key
    )

    val elapsedTime by viewModel.elapsedTime.observeAsState(0L)
    val isRunning by viewModel.isRunning.observeAsState(false)

    LaunchedEffect(isRunning, key) {
        if (isRunning) {
            viewModel.startTimer()
        } else {
            viewModel.stopTimer()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text(
                text = "Stopwatch",
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = String.format(
                    "%02d:%02d:%02d",
                    elapsedTime / 3600000,
                    (elapsedTime % 3600000) / 60000,
                    (elapsedTime % 60000) / 1000
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = { viewModel.toggleTimer() }) {
                    Text(if (isRunning) "Pause" else "Start")
                }
                Button(onClick = { viewModel.resetTimer() }) {
                    Text("Reset")
                }
            }
        }
    }
}
