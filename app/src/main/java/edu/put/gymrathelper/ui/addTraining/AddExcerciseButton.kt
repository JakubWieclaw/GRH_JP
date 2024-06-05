package edu.put.gymrathelper.ui.addTraining

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

@Composable
fun AddExerciseButton(onAddExercise: () -> Unit) {
    Button(onClick = onAddExercise) {
        Text("Add Exercise")
    }
}