// MainActivity.kt
package edu.put.gymrathelper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import edu.put.gymrathelper.ui.LoginScreen
import edu.put.gymrathelper.ui.RegistrationScreen
import edu.put.gymrathelper.ui.theme.GymRatHelperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GymRatHelperTheme {
                var currentScreen by remember { mutableStateOf("login") }
                val dbHandler = DatabaseHandler(this)

                when (currentScreen) {
                    "login" -> LoginScreen(onRegisterClick = { currentScreen = "register" }, dbHandler)
                    "register" -> RegistrationScreen(onRegistrationSuccess = { currentScreen = "login" }, dbHandler)
                }
            }
        }
    }
}
