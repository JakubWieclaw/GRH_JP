package edu.put.gymrathelper.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.gymrathelper.database.Account
import edu.put.gymrathelper.DatabaseHandler
import edu.put.gymrathelper.database.Training
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTrainingScreen(
    onCancel: () -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    dbHandler: DatabaseHandler,
    currentUser: Account
) {
    var trainingType by rememberSaveable { mutableStateOf("") }
    var exercises by rememberSaveable { mutableStateOf(listOf<ExerciseInput>()) }
    var stopwatchTime by rememberSaveable { mutableStateOf(0L) }
    var isStopwatchRunning by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Stopwatch logic
    LaunchedEffect(isStopwatchRunning) {
        if (isStopwatchRunning) {
            while (true) {
                delay(1000L)
                stopwatchTime += 1000L
            }
        }
    }

    // UI Components
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Button that comes back to the main screen
        TopAppBar(
            title = { Text("Add Training") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )
        OutlinedTextField(
            value = trainingType,
            onValueChange = { trainingType = it },
            label = { Text("Type of Training") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Stopwatch
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Duration: ${stopwatchTime / 1000} seconds")
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { isStopwatchRunning = !isStopwatchRunning }) {
                Text(if (isStopwatchRunning) "Pause" else "Start")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Add Exercise Button
        Button(onClick = {
            exercises = exercises + ExerciseInput("", "")
        }) {
            Text("Add Exercise")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Exercise Input Fields
        exercises.forEachIndexed { index, exerciseInput ->
            ExerciseInputField(
                exercise = exerciseInput,
                onExerciseChange = { newExercise ->
                    exercises = exercises.toMutableList().apply {
                        set(index, newExercise)
                    }
                },
                onRemove = {
                    exercises = exercises.toMutableList().apply {
                        removeAt(index)
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onCancel) {
                Text("Cancel")
            }
            Button(onClick = {
                val training = exercises.getOrNull(0)?.name?.let {
                    Training(
                        type = trainingType,
                        date = System.currentTimeMillis(),
                        totalTime = stopwatchTime,
                        exercise1 = it,
                        weight1 = exercises.getOrNull(0)?.weight,
                        exercise2 = exercises.getOrNull(1)?.name,
                        weight2 = exercises.getOrNull(1)?.weight,
                        exercise3 = exercises.getOrNull(2)?.name,
                        weight3 = exercises.getOrNull(2)?.weight,
                        exercise4 = exercises.getOrNull(3)?.name,
                        weight4 = exercises.getOrNull(3)?.weight,
                        exercise5 = exercises.getOrNull(4)?.name,
                        weight5 = exercises.getOrNull(4)?.weight,
                        exercise6 = exercises.getOrNull(5)?.name,
                        weight6 = exercises.getOrNull(5)?.weight,
                        exercise7 = exercises.getOrNull(6)?.name,
                        weight7 = exercises.getOrNull(6)?.weight,
                        exercise8 = exercises.getOrNull(7)?.name,
                        weight8 = exercises.getOrNull(7)?.weight,
                        exercise9 = exercises.getOrNull(8)?.name,
                        weight9 = exercises.getOrNull(8)?.weight,
                        exercise10 = exercises.getOrNull(9)?.name,
                        weight10 = exercises.getOrNull(9)?.weight
                    )
                }
                coroutineScope.launch(Dispatchers.IO) {
                    if (training != null) {
                        dbHandler.insertTraining(training)
                    }
                    withContext(Dispatchers.Main) {
                        onSave()
                    }
                }
            }) {
                Text("Save")
            }
        }
    }
}

@Composable
fun ExerciseInputField(
    exercise: ExerciseInput,
    onExerciseChange: (ExerciseInput) -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = exercise.name,
            onValueChange = { onExerciseChange(exercise.copy(name = it)) },
            label = { Text("Exercise") },
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        OutlinedTextField(
            value = exercise.weight,
            onValueChange = { onExerciseChange(exercise.copy(weight = it)) },
            label = { Text("Weight") },
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = onRemove) {
            Icon(Icons.Default.Delete, contentDescription = "Remove Exercise")
        }
    }
}

data class ExerciseInput(
    val name: String,
    val weight: String
)
