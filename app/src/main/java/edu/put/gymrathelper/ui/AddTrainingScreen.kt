package edu.put.gymrathelper.ui

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
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
import edu.put.gymrathelper.ui.addTraining.StopwatchViewModel
import edu.put.gymrathelper.ui.addTraining.StopwatchViewModelFactory
import edu.put.gymrathelper.ui.addTraining.TrainingTypeInput
import edu.put.gymrathelper.ui.addTraining.TrainingViewModel

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTrainingScreen(
    onCancel: () -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    dbHandler: DatabaseHandler,
    trainingViewModel: TrainingViewModel,
    currentUser: Account
) {
    var trainingType by rememberSaveable { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val showDialog = mutableStateOf(false)
    val confirmCancelDialog = mutableStateOf(false)
    val confirmSaveDialog = mutableStateOf(false)

    val viewModel: StopwatchViewModel = viewModel(factory = StopwatchViewModelFactory(LocalViewModelStoreOwner.current as androidx.savedstate.SavedStateRegistryOwner), key = "stopwatch_key")
    val elapsedTime by viewModel.elapsedTime.observeAsState(0L)

    val scrollState = rememberScrollState()

    BackHandler{
        onBack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
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

        TrainingTypeInput(trainingType) { trainingType = if (it.length <= 30) it else trainingType } // Restrict to 30 characters
        Spacer(modifier = Modifier.height(16.dp))

        Stopwatch(key = "stopwatch_key")
        Spacer(modifier = Modifier.height(16.dp))

        trainingViewModel.exercises.value.forEachIndexed { index, exercise ->
            ExerciseInputField(
                exercise = exercise,
                onExerciseChange = { updatedExercise ->
                    trainingViewModel.exercises.value = trainingViewModel.exercises.value.toMutableList().also { it[index] = updatedExercise }
                },
                onRemove = {
                    if (trainingViewModel.exercises.value.isNotEmpty() && index < trainingViewModel.exercises.value.size) {
                        trainingViewModel.exercises.value = trainingViewModel.exercises.value.toMutableList().also { it.removeAt(index) }
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            if (trainingViewModel.exercises.value.size < 10) {
                AddExerciseButton {
                    trainingViewModel.exercises.value += ExerciseInput("", "")
                }
            } else {
                Text("Maximum of 10 exercises reached")
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                confirmCancelDialog.value = true
            }) {
                Text("Cancel Training")
            }
            Button(onClick = {
                confirmSaveDialog.value = true
            }) {
                Text("Save Training")
            }
        }
    }
    ShowDialog(showDialog = showDialog)
    ShowCancelDialog(onConfirm = onCancel, showDialog = confirmCancelDialog, stopwatchViewModel = viewModel)
    ShowSaveDialog(onConfirm = {
        if (trainingViewModel.exercises.value.isNotEmpty() && trainingType.isNotEmpty()) {
            coroutineScope.launch(Dispatchers.IO) {
                dbHandler.insertTraining(
                    Training(
                        userId = currentUser.id,
                        type = trainingType,
                        date = System.currentTimeMillis(),
                        totalTime = elapsedTime/1000,
                        exercises = trainingViewModel.exercises.value.map {
                            Exercise(
                                it.name,
                                it.weight
                            )
                        },
                    )
                )
                withContext(Dispatchers.Main) {
                    trainingViewModel.exercises.value = emptyList()
                    viewModel.stopTimer()
                    onSave()
                }
            }
        }
        else {
            showDialog.value = true

        }
        viewModel.stopTimer()
        viewModel.resetTimer()
        viewModel.isRunning.value = false
    }, showDialog = confirmSaveDialog , stopwatchViewModel = viewModel)

}

@Composable
fun ShowDialog(showDialog: MutableState<Boolean>) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Error") },
            text = { Text("Training type and exercises are required") },
            confirmButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun ShowCancelDialog(onConfirm: () -> Unit, showDialog: MutableState<Boolean>, stopwatchViewModel: StopwatchViewModel) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Confirm Cancel") },
            text = { Text("Are you sure you want to cancel the training?") },
            confirmButton = {
                Button(onClick = {
                    showDialog.value = false
                    onConfirm()
                }) {
                    Text("Yes")
                }
                stopwatchViewModel.stopTimer()
                stopwatchViewModel.resetTimer()
                stopwatchViewModel.isRunning.value = false
            },
            dismissButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text("No")
                }
            }
        )
    }
}

@Composable
fun ShowSaveDialog(onConfirm: () -> Unit, showDialog: MutableState<Boolean>, stopwatchViewModel: StopwatchViewModel) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Confirm Save") },
            text = { Text("Are you sure you want to save the training?") },
            confirmButton = {
                Button(onClick = {
                    showDialog.value = false
                    onConfirm()
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text("No")
                }
            }
        )
    }
}

