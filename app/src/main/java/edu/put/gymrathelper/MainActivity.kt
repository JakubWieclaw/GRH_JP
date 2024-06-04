// MainActivity.kt
package edu.put.gymrathelper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import edu.gymrathelper.database.Account
import edu.put.gymrathelper.ui.AddTrainingScreen
import edu.put.gymrathelper.ui.LoginScreen
import edu.put.gymrathelper.ui.MainScreen
import edu.put.gymrathelper.ui.RegistrationScreen
import edu.put.gymrathelper.ui.theme.GymRatHelperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GymRatHelperTheme {
                var currentScreen by remember { mutableStateOf("login") }
                val dbHandler = DatabaseHandler(this)
                var currentUser by remember { mutableStateOf<Account?>(null) }

                when (currentScreen) {
                    "login" -> LoginScreen(
                        onLoginClick = { account ->
                            currentUser = account as Account?
                            currentScreen = "main"
                        },
                        onRegisterClick = { currentScreen = "register" },
                        dbHandler
                    )
                    "register" -> RegistrationScreen(
                        onRegistrationSuccess = { currentScreen = "login" },
                        dbHandler
                    )
                    "main" -> currentUser?.let { user ->
                        MainScreen(
                            onAddTrainingClick = { currentScreen = "addTraining" },
                            onViewTrainingsClick = { /* Navigate to View Trainings Screen */ },
                            onViewExerciseDataClick = { /* Navigate to View Exercise Data Screen */ },
                            onLogoutClick = { currentScreen = "login" },
                            dbHandler = dbHandler,
                            currentUser = user
                        )
                    }
                    "addTraining" -> AddTrainingScreen(
                        onCancel = { currentScreen = "main" },
                        onSave = { currentScreen = "main" },
                        onBack = { currentScreen = "main" },
                        dbHandler = dbHandler,
                        currentUser = currentUser!!
                    )
                }
            }
        }
    }
}
