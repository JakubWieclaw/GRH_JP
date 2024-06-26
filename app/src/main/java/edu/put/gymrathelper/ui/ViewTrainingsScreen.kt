package edu.put.gymrathelper.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import edu.put.gymrathelper.DatabaseHandler
import edu.put.gymrathelper.database.Training
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewTrainingsScreen(
    onBackClick: () -> Unit,
    onTrainingClick: (Training) -> Unit,
    dbHandler: DatabaseHandler,
    userId: Int
) {
    val trainings = remember { mutableStateOf(emptyList<Training>()) }

    BackHandler{
        onBackClick()
    }

    LaunchedEffect(key1 = trainings) {
        trainings.value = withContext(Dispatchers.IO) {
            dbHandler.getTrainingsByUserId(userId)
        }
    }

    Column {
        TopAppBar(
            title = { Text("Trainings") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )
        LazyColumn {
            items(trainings.value) { training ->
                TrainingListItem(
                    training = training,
                    onClick = { onTrainingClick(training) },
                    onDeleteClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            dbHandler.removeTraining(training)
                            trainings.value = dbHandler.getTrainingsByUserId(userId)
                        }
                    }
                )
            }
        }
    }
}