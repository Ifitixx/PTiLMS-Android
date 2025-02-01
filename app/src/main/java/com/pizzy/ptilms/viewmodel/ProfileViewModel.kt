package com.pizzy.ptilms.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pizzy.ptilms.data.model.UserEntity
import com.pizzy.ptilms.data.repository.UserRepository
import com.pizzy.ptilms.ui.profile.ProfileFormEvent
import com.pizzy.ptilms.ui.profile.ProfileFormState
import com.pizzy.ptilms.ui.profile.updateState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userUiState = MutableStateFlow<UiState<UserEntity>>(UiState.Loading)
    val userUiState: StateFlow<UiState<UserEntity>> = _userUiState.asStateFlow()

    private val _formState = MutableStateFlow(ProfileFormState())
    val formState: StateFlow<ProfileFormState> = _formState.asStateFlow()

    fun loadUser(userId: String) {
        viewModelScope.launch {
            _userUiState.value = UiState.Loading
            try {
                val user = userRepository.getUserById(userId)
                if (user != null) {
                    _userUiState.value = UiState.Success(user)
                    // Initialize form state with user data
                    _formState.update {
                        it.copy(
                            username = user.username,
                            phoneNumber = user.phoneNumber ?: "",
                            dateOfBirth = user.dateOfBirth?.toString() ?: "",
                            sex = user.sex ?: "",
                            role = user.role,
                            profilePictureUri = if (user.profilePictureUrl != null) Uri.parse(user.profilePictureUrl) else null
                        )
                    }
                } else {
                    _userUiState.value = UiState.Empty
                }
            } catch (e: Exception) {
                _userUiState.value = UiState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun onFormEvent(event: ProfileFormEvent) {
        _formState.update { it.updateState(event) }
    }

    fun submitForm() {
        val currentState = _formState.value
        if (currentState.isValid()) {
            viewModelScope.launch {
                _formState.update { it.copy(isLoading = true, errorMessage = null) }
                try {
                    val user = (userUiState.value as UiState.Success<UserEntity>).data
                    val updatedUser = user.copy(
                        username = currentState.username,
                        phoneNumber = currentState.phoneNumber,
                        dateOfBirth = currentState.parsedDateOfBirth(),
                        sex = currentState.sex,
                        profilePictureUrl = currentState.profilePictureUri?.toString()
                    )
                    userRepository.updateUser(updatedUser)
                    _userUiState.value = UiState.Success(updatedUser)
                    _formState.update { it.copy(isLoading = false) }
                } catch (e: Exception) {
                    _formState.update { it.copy(isLoading = false, errorMessage = e.message) }
                }
            }
        } else {
            _formState.update { it.copy(errorMessage = "Please fill all the required fields") }
        }
    }
}