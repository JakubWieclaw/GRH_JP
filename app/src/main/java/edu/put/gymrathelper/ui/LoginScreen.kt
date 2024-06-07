// LoginScreen.kt
package edu.put.gymrathelper.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import edu.put.gymrathelper.DatabaseHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LoginScreen(onLoginClick: (Any?) -> Unit, onRegisterClick: () -> Unit, dbHandler: DatabaseHandler) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    //var accountsExist by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    var showError by remember { mutableStateOf(false) }

    // Check if there are any accounts in the database
//    LaunchedEffect(Unit) {
//        coroutineScope.launch(Dispatchers.IO) {
//            accountsExist = dbHandler.getAllAccounts().isNotEmpty()
//        }
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        if (showError) {
            Snackbar(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                action = {
                    TextButton(onClick = { showError = false }) {
                        Text("Dismiss")
                    }
                }
            ) {
                Text("Invalid username or password")
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Text(text = "Login")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    val loginSuccessful = loginAttempt(username, password, dbHandler)
//                    withContext(Dispatchers.Main) {
                        if (loginSuccessful) {
                            // redirect to the main screen
                            onLoginClick(dbHandler.getAccountByLogin(username))
                        } else {
                            // show an error message
                            showError = true
                        }
//                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }


        Spacer(modifier = Modifier.height(16.dp))

        // if there are no accounts in the database, show the register button
//        if (!accountsExist) {
        Text(text = "Don't have an account?")
        Button(
            onClick = onRegisterClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }
//        }

    }

}

fun loginAttempt(username: String, password: String, dbHandler: DatabaseHandler): Boolean {
    return dbHandler.checkIfLoginAttemptIsCorrect(username, password)
}