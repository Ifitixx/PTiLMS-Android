package com.pizzy.ptilms.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pizzy.ptilms.data.model.UserRole
import com.pizzy.ptilms.navigation.Screen
import com.pizzy.ptilms.ui.components.CustomOutlinedTextField
import com.pizzy.ptilms.ui.components.LoadingIndicator
import com.pizzy.ptilms.utils.Resource
import com.pizzy.ptilms.utils.ValidationUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf<UserRole?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var roleError by remember { mutableStateOf<String?>(null) }

    val emailValidation = remember(email) { ValidationUtils.validateEmail(email) }

    val authResource by authViewModel.authResource.collectAsState()
    val initialScreen by authViewModel.initialDashboardScreen.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Handle authResource changes
    LaunchedEffect(authResource) {
        when (authResource) {
            is Resource.Success -> {
                // Navigate to the initial screen
                if (initialScreen != null) {
                    navController.navigate(initialScreen!!.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }

            is Resource.Error -> {
                val errorMessage = (authResource as Resource.Error).message
                if (errorMessage == "User not found") {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            "User not found. Please sign up.",
                            duration = SnackbarDuration.Short
                        )
                    }
                } else {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(errorMessage ?: "An error occurred")
                    }
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
                        text = "Login",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Role Selection
                    Text(
                        text = "Select Role",
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        RadioButton(
                            selected = selectedRole == UserRole.STUDENT,
                            onClick = {
                                selectedRole = UserRole.STUDENT
                                roleError = null
                            },
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Text("Student", modifier = Modifier.align(Alignment.CenterVertically))

                        Spacer(modifier = Modifier.width(16.dp))

                        RadioButton(
                            selected = selectedRole == UserRole.LECTURER,
                            onClick = {
                                selectedRole = UserRole.LECTURER
                                roleError = null
                            },
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Text("Lecturer", modifier = Modifier.align(Alignment.CenterVertically))
                    }
                    if (roleError != null) {
                        Text(
                            text = roleError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Email input
                    CustomOutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = null
                        },
                        label = "Email",
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                        isError = emailError != null
                    )
                    if (emailError != null) {
                        Text(
                            text = emailError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Password input
                    CustomOutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = null
                        },
                        label = "Password",
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                        isError = passwordError != null
                    )
                    if (passwordError != null) {
                        Text(
                            text = passwordError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            emailError = null
                            passwordError = null
                            roleError = null
                            if (selectedRole == null) {
                                roleError = "Please select a role."
                            } else {
                                val emailValidation = ValidationUtils.validateEmail(email)
                                if (emailValidation is Resource.Error) {
                                    emailError = emailValidation.message
                                } else {
                                    emailError = null
                                }
                                if (email.isBlank()) {
                                    emailError = "Please enter your email."
                                }
                                if (password.isBlank()) {
                                    passwordError = "Please enter your password."
                                }
                                if (emailValidation is Resource.Success && email.isNotBlank() && password.isNotBlank()) {
                                    authViewModel.login(
                                        email = email,
                                        password = password,
                                        selectedRole = selectedRole!!,
                                        onSuccess = {
                                            val initialScreen =
                                                if (selectedRole == UserRole.STUDENT) Screen.StudentHome else Screen.LecturerHome
                                            authViewModel.setInitialDashboardScreen(initialScreen)
                                        },
                                        onFailure = { errorMessage ->
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar(errorMessage)
                                            }
                                        }
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        enabled = authResource !is Resource.Loading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text("Login", modifier = Modifier.padding(8.dp))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(
                        onClick = {
                            navController.navigate(Screen.Auth.route)
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Don't have an account? Sign Up")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(
                        onClick = {
                            navController.navigate(Screen.ForgotPassword.route)
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Forgot Password?")
                    }
                }
            }
            if (authResource is Resource.Loading) {
                LoadingIndicator()
            }
        }
    }
}