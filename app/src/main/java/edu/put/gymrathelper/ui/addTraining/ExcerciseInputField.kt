package edu.put.gymrathelper.ui.addTraining

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class ExerciseInput(val name: String, val weight: String)

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