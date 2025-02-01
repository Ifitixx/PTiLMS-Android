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
import androidx.compose.material3.Button
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
fun SignUpScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var formState by remember { mutableStateOf(SignupForm()) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var selectedRole by remember { mutableStateOf<UserRole?>(null) }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    // Observe authResource for success/failure
    val authResource by authViewModel.authResource.collectAsStateWithLifecycle()

    LaunchedEffect(authResource) {
        when (authResource) {
            is Resource.Success -> {
                val initialScreen =
                    if (selectedRole == UserRole.STUDENT) Screen.StudentHome else Screen.LecturerHome
                authViewModel.setInitialDashboardScreen(initialScreen)
                navController.navigate(Screen.Dashboard.route) {
                    popUpTo(Screen.Auth.route) { inclusive = true }
                }
            }

            is Resource.Error -> {
                formState = formState.copy(isLoading = false)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = authResource.message ?: "Sign up failed. Please try again.",
                        duration = SnackbarDuration.Short
                    )
                }
            }

            is Resource.Loading -> {
                formState = formState.copy(isLoading = true)
            }

            is Resource.Idle -> {
                formState = formState.copy(isLoading = false)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Sign Up",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

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
                            },
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Text("Student", modifier = Modifier.align(Alignment.CenterVertically))

                        Spacer(modifier = Modifier.width(16.dp))

                        RadioButton(
                            selected = selectedRole == UserRole.LECTURER,
                            onClick = {
                                selectedRole = UserRole.LECTURER
                            },
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Text("Lecturer", modifier = Modifier.align(Alignment.CenterVertically))
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Username input
                    CustomOutlinedTextField(
                        value = formState.username,
                        onValueChange = {
                            formState = formState.copy(username = it)
                            usernameError = null
                        },
                        label = "Username",
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                        isError = usernameError != null
                    )
                    if (usernameError != null) {
                        Text(
                            text = usernameError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Email input
                    CustomOutlinedTextField(
                        value = formState.email,
                        onValueChange = {
                            formState = formState.copy(email = it)
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
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Password input
                    CustomOutlinedTextField(
                        value = formState.password,
                        onValueChange = {
                            formState = formState.copy(password = it)
                            passwordError = null
                        },
                        label = "Password",
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next,
                        isError = passwordError != null
                    )
                    if (passwordError != null) {
                        Text(
                            text = passwordError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Confirm Password input
                    CustomOutlinedTextField(
                        value = formState.confirmPassword,
                        onValueChange = {
                            formState = formState.copy(confirmPassword = it)
                            confirmPasswordError = null
                        },
                        label = "Confirm Password",
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                        isError = confirmPasswordError != null || formState.password != formState.confirmPassword
                    )
                    if (confirmPasswordError != null) {
                        Text(
                            text = confirmPasswordError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    if (formState.password != formState.confirmPassword && formState.confirmPassword.isNotBlank()) {
                        Text(
                            text = "Passwords do not match",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            usernameError = null
                            emailError = null
                            passwordError = null
                            confirmPasswordError = null
                            formState = formState.copy(errorMessage = null) // Clear previous error
                            if (selectedRole == null) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Please select a role.",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            } else {
                                val usernameValidation =
                                    ValidationUtils.validateUsername(formState.username)
                                val emailValidation =
                                    ValidationUtils.validateEmail(formState.email)
                                val passwordValidation =
                                    ValidationUtils.validatePassword(formState.password)

                                when (usernameValidation) {
                                    is Resource.Error -> usernameError = usernameValidation.message
                                    is Resource.Success -> usernameError = null
                                    is Resource.Loading -> {
                                        println("Username validation is in Loading state unexpectedly.")
                                    }
                                    is Resource.Idle -> {
                                        println("Username validation is in Idle state unexpectedly.")
                                    }
                                }
                                when (emailValidation) {
                                    is Resource.Error -> emailError = emailValidation.message
                                    is Resource.Success -> emailError = null
                                    is Resource.Loading -> {
                                        println("Email validation is in Loading state unexpectedly.")
                                    }
                                    is Resource.Idle -> {
                                        println("Email validation is in Idle state unexpectedly.")
                                    }
                                }
                                when (passwordValidation) {
                                    is Resource.Error -> passwordError = passwordValidation.message
                                    is Resource.Success -> passwordError = null
                                    is Resource.Loading -> {
                                        println("Password validation is in Loading state unexpectedly.")
                                    }
                                    is Resource.Idle -> {
                                        println("Password validation is in Idle state unexpectedly.")
                                    }
                                }

                                if (usernameValidation is Resource.Success &&
                                    emailValidation is Resource.Success &&
                                    passwordValidation is Resource.Success &&
                                    formState.password == formState.confirmPassword
                                ) {
                                    formState = formState.copy(isLoading = true)
                                    authViewModel.signup(
                                        email = formState.email,
                                        password = formState.password,
                                        confirmPassword = formState.confirmPassword,
                                        username = formState.username,
                                        role = selectedRole!!,
                                        onSuccess = {
                                            formState = formState.copy(isLoading = false)
                                        },
                                        onFailure = {
                                            formState = formState.copy(
                                                errorMessage = it,
                                                isLoading = false
                                            )
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = it,
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Sign Up")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = { navController.navigate(Screen.Login.route) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Already have an account? Login")
            }

            if (formState.isLoading) {
                LoadingIndicator(modifier = Modifier.testTag("LoadingIndicator"))
            }
        }
    }
}