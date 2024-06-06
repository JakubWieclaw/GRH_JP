package edu.put.gymrathelper.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.put.gymrathelper.database.Training
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingDetailScreen(
    training: Training,
    onDeleteClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column {
        TopAppBar(
            title = { Text("Training Details") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Date: ${Date(training.date)}")
            Text("Type: ${training.type}")
            Text("Total Time: ${training.totalTime}")
            Spacer(modifier = Modifier.height(8.dp))
            Text("Exercises:")
            training.exercises.forEach { exercise ->
                Text("${exercise.name} - ${exercise.weight}")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onDeleteClick) {
                Text("Delete Training")
            }
        }
    }
}
