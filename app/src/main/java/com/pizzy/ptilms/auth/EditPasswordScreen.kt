package com.pizzy.ptilms.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pizzy.ptilms.ui.components.LoadingIndicator
import com.pizzy.ptilms.utils.Resource

@Composable
fun EditPasswordScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    token: String // Added the token parameter
) {
    var formState by remember { mutableStateOf(EditPasswordFormState()) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val authResource by authViewModel.authResource.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = formState.newPassword,
            onValueChange = { formState = formState.copy(newPassword = it) },
            label = { Text("New Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = formState.confirmPassword,
            onValueChange = { formState = formState.copy(confirmPassword = it) },
            label = { Text("Confirm Password") },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        imageVector = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (formState.newPassword == formState.confirmPassword) {
                    authViewModel.resetPassword(
                        newPassword = formState.newPassword,
                        token = token, // Pass the token parameter
                        onSuccess = {
                            navController.popBackStack() // Navigate back after success
                        },
                        onFailure = {
                            formState = formState.copy(errorMessage = it)
                        }
                    )
                } else {
                    formState = formState.copy(errorMessage = "Passwords do not match.")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reset Password")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Show success/failure message
        if (authResource is Resource.Error) {
            Text(
                text = (authResource as Resource.Error).message ?: "An error occurred",
                color = MaterialTheme.colorScheme.error
            )
        }
        formState.errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }
        if (authResource is Resource.Success) {
            Text(
                text = "Password reset successfully.",
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Show loading indicator while resetting the password
        if (authResource is Resource.Loading) {
            LoadingIndicator(modifier = Modifier.testTag("LoadingIndicator"))
        }
    }
}