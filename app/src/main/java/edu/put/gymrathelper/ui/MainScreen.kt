package edu.put.gymrathelper.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.gymrathelper.database.Account
import edu.put.gymrathelper.DatabaseHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun MainScreen(
    onAddTrainingClick: () -> Unit,
    onViewTrainingsClick: () -> Unit,
    onViewExerciseDataClick: () -> Unit,
    onLogoutClick: () -> Unit,
    dbHandler: DatabaseHandler,
    currentUser: Account
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDeleteAccountDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    BackHandler {
        showLogoutDialog = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Button(onClick = onAddTrainingClick, modifier = Modifier.fillMaxWidth()) {
            Text("Add New Training")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onViewTrainingsClick, modifier = Modifier.fillMaxWidth()) {
            Text("View Past Trainings")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onViewExerciseDataClick, modifier = Modifier.fillMaxWidth()) {
            Text("View Exercise Data")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.End),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { showLogoutDialog = true }) {
                Text("Logout")
            }

            Button(onClick = { showDeleteAccountDialog = true }) {
                Text("Delete Account")
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    onLogoutClick()
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Logout Confirmation") },
            text = { Text("Are you sure you want to logout?") }
        )
    }

    if (showDeleteAccountDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAccountDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteAccountDialog = false
                    coroutineScope.launch(Dispatchers.IO) {
                        dbHandler.removeAccount(currentUser)
                        withContext(Dispatchers.Main) {
                            onLogoutClick()
                        }
                    }
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAccountDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Delete Account Confirmation") },
            text = { Text("Are you sure you want to delete your account? This action cannot be undone.") }
        )
    }
}


