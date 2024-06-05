package edu.put.gymrathelper.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import edu.put.gymrathelper.database.Exercise
import edu.put.gymrathelper.ui.addTraining.AddExerciseButton
import edu.put.gymrathelper.ui.addTraining.ExerciseInput
import edu.put.gymrathelper.ui.addTraining.ExerciseInputField
import edu.put.gymrathelper.ui.addTraining.Stopwatch
import edu.put.gymrathelper.ui.addTraining.TrainingTypeInput

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
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = { Text("Add Training") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        TrainingTypeInput(trainingType) { trainingType = it }
        Spacer(modifier = Modifier.height(16.dp))

        Stopwatch(key = "stopwatch_key")
        Spacer(modifier = Modifier.height(16.dp))

        AddExerciseButton { exercises = exercises + ExerciseInput("", "") }
        Spacer(modifier = Modifier.height(16.dp))

        exercises.forEachIndexed { index, exercise ->
            ExerciseInputField(
                exercise = exercise,
                onExerciseChange = { updatedExercise ->
                    exercises = exercises.toMutableList().also { it[index] = updatedExercise }
                },
                onRemove = {
                    exercises = exercises.toMutableList().also { it.removeAt(index) }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = onCancel) {
                Text("Cancel")
            }
            Button(onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    dbHandler.insertTraining(
                        Training(
                            type = trainingType,
                            date = System.currentTimeMillis(),
                            totalTime = 0L, // Replace with actual time
                            exercises = exercises.map { Exercise(it.name, it.weight) },
                        )
                    )
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
