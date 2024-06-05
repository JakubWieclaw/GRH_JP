package edu.put.gymrathelper.ui.addTraining

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TrainingTypeInput(trainingType: String, onTypeChange: (String) -> Unit) {
    OutlinedTextField(
        value = trainingType,
        onValueChange = onTypeChange,
        label = { Text("Type of Training") },
        modifier = Modifier.fillMaxWidth()
    )
}