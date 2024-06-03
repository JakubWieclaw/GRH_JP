package edu.put.gymrathelper.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import edu.gymrathelper.database.Account
import edu.put.gymrathelper.DatabaseHandler
import kotlinx.coroutines.*

@Composable
fun RegistrationScreen(onRegistrationSuccess: () -> Unit, dbHandler: DatabaseHandler) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Text(text = "Register")

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

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        errorMessage?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = {
                if (password == confirmPassword) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val account = Account(id = 0, username = username, passwordhash = password.hashCode().toString())
                        dbHandler.insertAccount(account)
                        withContext(Dispatchers.Main) {
                            onRegistrationSuccess()
                        }
                    }
                } else {
                    errorMessage = "Passwords do not match"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun RegistrationScreenPreview() {
//    GymRatHelperTheme {
//        RegistrationScreen(onRegistrationSuccess = {}, DatabaseHandler())
//    }
//}
