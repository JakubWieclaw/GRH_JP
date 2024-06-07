package edu.put.gymrathelper.ui

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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

    BackHandler{
        onBackClick()
    }

    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    val hours = (training?.totalTime ?: 0) / 3600
    val minutes = ((training?.totalTime ?: 0) % 3600) / 60
    val seconds = (training?.totalTime ?: 0) % 60

    val formattedTotalTime = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)

    var showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete this training?") },
            confirmButton = {
                Button(onClick = {
                    showDialog.value = false
                    CoroutineScope(Dispatchers.IO).launch {
                        onDeleteClick()
                    }
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Surface(color = MaterialTheme.colorScheme.background) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Training Details") },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { paddingValues ->
            if (training != null) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    training?.let {
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    val formattedDate = dateFormat.format(it.date)
                                    Text("Date: $formattedDate", style = MaterialTheme.typography.titleMedium)
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text("Type: ${it.type}", style = MaterialTheme.typography.titleMedium)
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text("Time: $formattedTotalTime", style = MaterialTheme.typography.titleMedium)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Exercises:", style = MaterialTheme.typography.titleMedium)
                                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                                    it.exercises.forEach { exercise ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(exercise.name, style = MaterialTheme.typography.bodyMedium)
                                            Text(exercise.weight, style = MaterialTheme.typography.bodyMedium)
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(
                                        onClick = { showDialog.value = true },
                                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                                    ) {
                                        Text("Delete Training", color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
