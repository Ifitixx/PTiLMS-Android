package com.pizzy.ptilms.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pizzy.ptilms.data.DataStoreManager
import com.pizzy.ptilms.data.model.UserEntity
import com.pizzy.ptilms.data.model.UserRole
import com.pizzy.ptilms.data.repository.UserRepository
import com.pizzy.ptilms.navigation.Screen
import com.pizzy.ptilms.network.AuthApiService
import com.pizzy.ptilms.network.model.PasswordResetRequest
import com.pizzy.ptilms.utils.HashUtils
import com.pizzy.ptilms.utils.Resource
import com.pizzy.ptilms.utils.ValidationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject
import java.util.concurrent.TimeUnit

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dataStoreManager: DataStoreManager,
    private val authApiService: AuthApiService
) : ViewModel() {

    private val _authResource = MutableStateFlow<Resource<Unit>>(Resource.Idle())
    val authResource: StateFlow<Resource<Unit>> = _authResource.asStateFlow()

    private val _userRole = MutableStateFlow<UserRole?>(null)
    val userRole: StateFlow<UserRole?> = _userRole.asStateFlow()

    private val _initialDashboardScreen = MutableStateFlow<Screen?>(null)
    val initialDashboardScreen: StateFlow<Screen?> = _initialDashboardScreen.asStateFlow()

    init {
        initializeUserState()
    }

    private fun initializeUserState() {
        viewModelScope.launch {
            val storedUserId = dataStoreManager.userId.firstOrNull()
            if (storedUserId != null) {
                val user = userRepository.getUserByUserId(storedUserId)
                if (user != null) {
                    _userRole.value = UserRole.valueOf(user.role.uppercase())
                }
            }
        }
    }

    fun setInitialDashboardScreen(screen: Screen) {
        _initialDashboardScreen.value = screen
    }

    fun signup(
        email: String,
        password: String,
        confirmPassword: String,
        username: String,
        role: UserRole,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit,
    ) {
        viewModelScope.launch {
            _authResource.value = Resource.Loading()
            try {
                // New validation using ValidationUtils
                val validationResults = listOf(
                    ValidationUtils.validateEmail(email),
                    ValidationUtils.validateUsername(username),
                    ValidationUtils.validatePassword(password)
                )

                val errorMessage = validationResults
                    .filterIsInstance<Resource.Error<*>>()
                    .joinToString("\n") { it.message ?: "" }

                if (errorMessage.isNotEmpty()) {
                    onFailure(errorMessage)
                    _authResource.value = Resource.Error(errorMessage)
                    return@launch
                }

                if (password != confirmPassword) {
                    onFailure("Passwords do not match")
                    _authResource.value = Resource.Error("Passwords do not match")
                    return@launch
                }

                // Check for existing username
                val usernameExists = userRepository.getUserByUsername(username) != null
                if (usernameExists) {
                    onFailure("Username already taken")
                    _authResource.value = Resource.Error("Username already taken")
                    return@launch
                }
                // Check for existing email
                val emailExists = userRepository.getUserByEmail(email) != null
                if (emailExists) {
                    onFailure("Email already registered")
                    _authResource.value = Resource.Error("Email already registered")
                    return@launch
                }

                val user = UserEntity(
                    userId = UUID.randomUUID().toString(),
                    email = email,
                    username = username,
                    role = role.name,
                    password = HashUtils.hashPassword(password)
                )

                userRepository.insertUser(user)
                dataStoreManager.updateUserId(user.userId)
                dataStoreManager.updateUsername(user.username)
                dataStoreManager.updateUserRole(user.role)
                dataStoreManager.setLoggedInState(true)
                onSuccess()
                _authResource.value = Resource.Success(Unit)
            } catch (e: Exception) {
                Timber.e(e, "Signup failed")
                onFailure("Registration failed")
                _authResource.value = Resource.Error("Registration failed")
            }
        }
    }

    fun login(
        email: String,
        password: String,
        selectedRole: UserRole,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit,
    ) {
        viewModelScope.launch {
            _authResource.value = Resource.Loading()
            try {
                val emailValidation = ValidationUtils.validateEmail(email)
                if (emailValidation is Resource.Error) {
                    onFailure(emailValidation.message!!)
                    _authResource.value = Resource.Error(emailValidation.message)
                    return@launch
                }

                val user = userRepository.getUserByEmail(email)
                if (user == null) {
                    onFailure("User not found")
                    _authResource.value = Resource.Error("User not found")
                    return@launch
                }

                if (!user.role.equals(selectedRole.name, ignoreCase = true)) {
                    onFailure("Incorrect role selected")
                    _authResource.value = Resource.Error("Incorrect role selected")
                    return@launch
                }

                if (!HashUtils.verifyPassword(password, user.password)) {
                    onFailure("Authentication failed")
                    _authResource.value = Resource.Error("Authentication failed")
                    return@launch
                }
                dataStoreManager.updateUserId(user.userId)
                dataStoreManager.updateUsername(user.username)
                dataStoreManager.updateUserRole(user.role)
                dataStoreManager.setLoggedInState(true)
                _userRole.value = UserRole.valueOf(user.role)
                onSuccess() // Changed to Unit
                _authResource.value = Resource.Success(Unit)
            } catch (e: Exception) {
                Timber.e(e, "Login failed")
                onFailure("Authentication error")
                _authResource.value = Resource.Error("Authentication error")
            }
        }
    }

    fun sendPasswordResetEmail(email: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            _authResource.value = Resource.Loading()
            try {
                if (ValidationUtils.validateEmail(email) is Resource.Error) {
                    _authResource.value = Resource.Error("Invalid email format.")
                    onFailure("Invalid email format.")
                    return@launch
                }

                // Check if the user exists in the database
                val user = userRepository.getUserByEmail(email)
                if (user == null) {
                    _authResource.value = Resource.Error("User not found.")
                    onFailure("User not found.")
                    return@launch
                }
                // Generate a reset token and set its expiry
                val token = UUID.randomUUID().toString()
                val expiry = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1) // 1 hour expiry

                // Update the user's reset token and expiry in the database
                userRepository.updateResetToken(email, token, expiry)

                // Make a network request to your backend API
                val request = PasswordResetRequest(email = email, token = token)
                val response = authApiService.sendPasswordResetEmail(request)

                if (response.isSuccessful && response.body() != null) {
                    _authResource.value = Resource.Success(Unit)
                    onSuccess()
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Failed to send reset email."
                    _authResource.value = Resource.Error(errorMessage)
                    onFailure(errorMessage)
                }
            } catch (e: Exception) {
                Timber.e(e, "Failed to send password reset email")
                _authResource.value = Resource.Error("Failed to send reset email: ${e.localizedMessage}")
                onFailure("Failed to send reset email: ${e.localizedMessage}")
            }
        }
    }

    fun resetPassword(newPassword: String, token: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            _authResource.value = Resource.Loading()
            try {
                val passwordValidation = ValidationUtils.validatePassword(newPassword)
                if (passwordValidation is Resource.Error) {
                    _authResource.value = passwordValidation
                    onFailure(passwordValidation.message!!)
                    return@launch
                }

                // Check if the token is valid and not expired
                val user = userRepository.getUserByResetToken(token)
                if (user == null) {
                    _authResource.value = Resource.Error("Invalid token.")
                    onFailure("Invalid token.")
                    return@launch
                }

                if (user.resetTokenExpiry == null || user.resetTokenExpiry < System.currentTimeMillis()) {
                    _authResource.value = Resource.Error("Token expired.")
                    onFailure("Token expired.")
                    return@launch
                }
                // Update the password
                userRepository.updatePassword(user.userId, HashUtils.hashPassword(newPassword))
                userRepository.clearResetToken(user.userId)

                _authResource.value = Resource.Success(Unit)
                onSuccess()
            } catch (e: Exception) {
                Timber.e(e, "Failed to reset password")
                _authResource.value = Resource.Error("Failed to reset password: ${e.localizedMessage}")
                onFailure("Failed to reset password: ${e.localizedMessage}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            dataStoreManager.clearAll()
        }
    }
}