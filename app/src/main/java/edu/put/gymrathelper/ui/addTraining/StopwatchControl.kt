package edu.put.gymrathelper.ui.addTraining

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StopwatchControl(elapsedTime: Long, isRunning: Boolean, onToggle: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("Duration: ${elapsedTime / 1000} seconds")
        Spacer(modifier = Modifier.width(16.dp))
        Button(onClick = { onToggle() }) {
            Text(if (isRunning) "Pause" else "Start")
        }
    }
}