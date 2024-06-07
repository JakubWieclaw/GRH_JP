package edu.put.gymrathelper.ui.firebaseExcercises

import android.annotation.SuppressLint
import android.content.Intent
import android.webkit.WebView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import edu.put.gymrathelper.R
import android.net.Uri

@SuppressLint("SetJavaScriptEnabled", "UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailScreen(exercise: FirebaseExercise, onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { exercise.name },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(40.dp))

//            Text(
//                text = "Watch a movie how to do this correctly",
//                style = MaterialTheme.typography.displaySmall,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .wrapContentSize(Alignment.Center)
//            )
//            Spacer(modifier = Modifier.height(8.dp))


            // Display the video link as a clickable image
            val context = LocalContext.current
            val intent = remember { mutableStateOf(Intent(Intent.ACTION_VIEW)) }
            val videoUri = remember { mutableStateOf(Uri.parse(exercise.video)) }
            intent.value.data = videoUri.value
            Image(
                painter = painterResource(id = R.drawable.yt),
                contentDescription = "YouTube Logo",
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clickable { context.startActivity(intent.value) }
            )
            Spacer(modifier = Modifier.height(16.dp))

            AndroidView(factory = {
                WebView(it).apply {
                    settings.javaScriptEnabled = true
                    loadUrl(exercise.other_info)
                    settings.builtInZoomControls = true
                    settings.displayZoomControls = false
                }
            }, modifier = Modifier.fillMaxSize())
        }
    }
}

