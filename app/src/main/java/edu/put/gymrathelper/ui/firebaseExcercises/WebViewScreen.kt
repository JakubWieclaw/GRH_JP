package edu.put.gymrathelper.ui.firebaseExcercises

import android.annotation.SuppressLint
import android.webkit.WebView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(url: String, onBackClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = onBackClick, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Back")
        }
        AndroidView(factory = {
            WebView(it).apply {
                settings.javaScriptEnabled = true
                loadUrl(url)
            }
        }, modifier = Modifier.fillMaxSize())
    }
}