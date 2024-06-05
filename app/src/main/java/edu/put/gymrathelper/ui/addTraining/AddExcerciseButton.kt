package edu.put.gymrathelper.ui.addTraining

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AddExerciseButton(onAddExercise: () -> Unit) {
    Button(onClick = onAddExercise) {
        Text("Add Exercise")
    }
}