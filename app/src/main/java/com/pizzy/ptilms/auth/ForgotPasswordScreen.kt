package com.pizzy.ptilms.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pizzy.ptilms.navigation.Screen
import com.pizzy.ptilms.ui.components.CustomOutlinedTextField
import com.pizzy.ptilms.ui.components.LoadingIndicator
import com.pizzy.ptilms.utils.Resource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var formState by remember { mutableStateOf(ForgotPasswordFormState()) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val authResource by authViewModel.authResource.collectAsState()

    LaunchedEffect(authResource) {
        when (authResource) {
            is Resource.Success -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Password reset email sent successfully!",
                        duration = androidx.compose.material3.SnackbarDuration.Short
                    )
                }
            }

            is Resource.Error -> {
                val errorMessage = (authResource as Resource.Error).message
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(errorMessage ?: "An error occurred")
                }
            }

            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Forgot Password",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Email input field
            CustomOutlinedTextField(
                value = formState.email,
                onValueChange = { formState = formState.copy(email = it) },
                label = "Enter your email",
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done,
                isError = false
            )
            if (formState.email.isBlank() && authResource is Resource.Error) {
                Text(
                    text = "Please enter your email.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.Start)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Send Reset Email Button
            Button(
                onClick = {
                    formState = formState.copy(errorMessage = null)
                    authViewModel.sendPasswordResetEmail(
                        email = formState.email,
                        onSuccess = {
                            formState = formState.copy(isEmailSent = true)
                        },
                        onFailure = { error ->
                            formState = formState.copy(errorMessage = error)
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Send Reset Email")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Show error message if reset fails
            if (authResource is Resource.Error) {
                Text(
                    text = (authResource as Resource.Error).message ?: "An error occurred",
                    color = MaterialTheme.colorScheme.error
                )
            }
            if (authResource is Resource.Success) {
                Text(
                    text = "Password reset email sent successfully!",
                    color = MaterialTheme.colorScheme.primary
                )
                Button(onClick = { navController.navigate(Screen.Login.route) }) {
                    Text("Go to Login")
                }
            }

            // Show loading indicator while requesting password reset
            if (authResource is Resource.Loading) {
                LoadingIndicator(modifier = Modifier.testTag("LoadingIndicator"))
            }
        }
    }
}