// MainActivity.kt
package edu.put.gymrathelper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import edu.gymrathelper.database.Account
import edu.put.gymrathelper.database.Training
import edu.put.gymrathelper.ui.AddTrainingScreen
import edu.put.gymrathelper.ui.LoginScreen
import edu.put.gymrathelper.ui.MainScreen
import edu.put.gymrathelper.ui.RegistrationScreen
import edu.put.gymrathelper.ui.TrainingDetailScreen
import edu.put.gymrathelper.ui.ViewTrainingsScreen
import edu.put.gymrathelper.ui.addTraining.TrainingViewModel
import edu.put.gymrathelper.ui.theme.GymRatHelperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GymRatHelperTheme {
                var currentScreen by remember { mutableStateOf("login") }
                val dbHandler = DatabaseHandler(this)
                var currentUser by remember { mutableStateOf<Account?>(null) }
                var selectedTraining by remember { mutableStateOf<Training?>(null) }

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
                            onViewTrainingsClick = { currentScreen = "viewTrainings" },
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
                        trainingViewModel = TrainingViewModel(),
                        currentUser = currentUser!!
                    )
                    "viewTrainings" -> ViewTrainingsScreen(
                        onBackClick = { currentScreen = "main" },
                        onTrainingClick = { training ->
                            selectedTraining = training
                            currentScreen = "trainingDetail"
                        },
                        dbHandler = dbHandler
                    )
                    "trainingDetail" -> selectedTraining?.let { training ->
                        TrainingDetailScreen(
                            training = training,
                            onDeleteClick = {
                                dbHandler.removeTraining(training)
                                currentScreen = "viewTrainings"
                            },
                            onBackClick = { currentScreen = "viewTrainings" }
                        )
                    }
                }
            }
        }
    }
}
