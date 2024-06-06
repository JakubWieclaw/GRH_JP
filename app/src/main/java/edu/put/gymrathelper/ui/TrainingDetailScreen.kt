package edu.put.gymrathelper.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.put.gymrathelper.DatabaseHandler
import edu.put.gymrathelper.database.Training
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingDetailScreen(
    trainingId: Long,
    dbHandler: DatabaseHandler,
    onDeleteClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val training by produceState<Training?>(initialValue = null, trainingId) {
        withContext(Dispatchers.IO) {
            value = dbHandler.getTrainingById(trainingId)
        }
    }

    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    val hours = (training?.totalTime ?: 0) / 3600
    val minutes = ((training?.totalTime ?: 0) % 3600) / 60
    val seconds = (training?.totalTime ?: 0) % 60

    val formattedTotalTime = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)

    if (training != null) {


        Column {
            TopAppBar(
                title = { Text("Training Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {training?.let {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            val formattedDate = dateFormat.format(it.date)
                            Text("Date: $formattedDate", style = MaterialTheme.typography.bodySmall)
                            Text("Type: ${training!!.type}", style = MaterialTheme.typography.bodyLarge)
                            Text("Total Time: $formattedTotalTime", style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Exercises:", style = MaterialTheme.typography.bodyMedium)
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                            training!!.exercises.forEach { exercise ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(exercise.name, style = MaterialTheme.typography.labelMedium)
                                    Text(exercise.weight, style = MaterialTheme.typography.labelMedium)
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        onDeleteClick()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors()
                            ) {
                                Text("Delete Training", color = Color.White)
                            }
                        }
                    }
                }
                }
            }
        }
    } else {
        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
    }
}

