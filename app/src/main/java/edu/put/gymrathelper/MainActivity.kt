// MainActivity.kt
package edu.put.gymrathelper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import edu.gymrathelper.database.Account
import edu.put.gymrathelper.database.Training
import edu.put.gymrathelper.ui.AddTrainingScreen
import edu.put.gymrathelper.ui.LoginScreen
import edu.put.gymrathelper.ui.MainScreen
import edu.put.gymrathelper.ui.RegistrationScreen
import edu.put.gymrathelper.ui.TrainingDetailScreen
import edu.put.gymrathelper.ui.ViewExerciseDataScreen
import edu.put.gymrathelper.ui.ViewTrainingsScreen
import edu.put.gymrathelper.ui.addTraining.TrainingViewModel
import edu.put.gymrathelper.ui.theme.GymRatHelperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GymRatHelperTheme(darkTheme = false) {
                var currentScreen by rememberSaveable { mutableStateOf("login") }
                val dbHandler = DatabaseHandler(this)
                var currentUser by rememberSaveable { mutableStateOf<Account?>(null) }
                var selectedTraining by rememberSaveable { mutableStateOf<Training?>(null) }

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
                            onViewExerciseDataClick = { currentScreen = "viewExerciseData"},
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
                    "viewTrainings" -> currentUser?.let {
                        ViewTrainingsScreen(
                            onBackClick = { currentScreen = "main" },
                            onTrainingClick = { training ->
                                selectedTraining = training
                                currentScreen = "trainingDetail"
                            },
                            dbHandler = dbHandler,
                            userId = it.id
                        )
                    }
                    "trainingDetail" -> selectedTraining?.let { training ->
                        TrainingDetailScreen(
                            trainingId = training.id.toLong(),
                            dbHandler = dbHandler,
                            onDeleteClick = {
                                dbHandler.removeTraining(training)
                                currentScreen = "viewTrainings"
                            },
                            onBackClick = { currentScreen = "viewTrainings" }
                        )
                    }
                    "viewExerciseData" -> ViewExerciseDataScreen(
                        dbUrl = "https://gymrathelper-default-rtdb.europe-west1.firebasedatabase.app/exercises.json",
                        onBackClick = { currentScreen = "main" }
                    )
                }
            }
        }
    }
}

