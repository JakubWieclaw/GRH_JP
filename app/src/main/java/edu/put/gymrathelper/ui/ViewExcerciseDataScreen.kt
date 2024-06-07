package edu.put.gymrathelper.ui

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.put.gymrathelper.ui.firebaseExcercises.ExerciseCard
import edu.put.gymrathelper.ui.firebaseExcercises.ExerciseDetailScreen
import edu.put.gymrathelper.ui.firebaseExcercises.FirebaseExercise
import edu.put.gymrathelper.ui.firebaseExcercises.NetworkClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewExerciseDataScreen(dbUrl: String, onBackClick: () -> Unit) {
    var firebaseExercises by remember { mutableStateOf<List<FirebaseExercise>>(emptyList()) }
    var selectedExercise by remember { mutableStateOf<FirebaseExercise?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val fetchedExercises = withContext(Dispatchers.IO) {
                NetworkClient.fetchExercises(dbUrl)
            }
            firebaseExercises = fetchedExercises
            isLoading = false
        }
    }

    BackHandler{
        onBackClick()
    }

    if (selectedExercise != null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(selectedExercise!!.name) },
                    navigationIcon = {
                        IconButton(onClick = { selectedExercise = null }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) {
            ExerciseDetailScreen(exercise = selectedExercise!!, onBackClick = { selectedExercise = null })
        }
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Exercises") },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                if (isLoading) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn {
                        items(firebaseExercises) { exercise ->
                            ExerciseCard(exercise = exercise, onClick = { selectedExercise = exercise })
                        }
                    }
                }
            }
        }
    }
}