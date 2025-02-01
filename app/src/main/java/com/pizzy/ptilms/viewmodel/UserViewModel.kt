package com.pizzy.ptilms.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pizzy.ptilms.data.DataStoreManager
import com.pizzy.ptilms.data.model.UserEntity
import com.pizzy.ptilms.data.model.UserRole
import com.pizzy.ptilms.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dataStoreManager: DataStoreManager // Added DataStoreManager
) : ViewModel() {

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser

    fun loadCurrentUserByUserId(userId: String) {
        viewModelScope.launch {
            _currentUser.value = userRepository.getUserByUserId(userId)
        }
    }

    fun isUserLecturer(): Boolean {
        return _currentUser.value?.role == UserRole.LECTURER.name
    }

    fun isUserStudent(): Boolean {
        return _currentUser.value?.role == UserRole.STUDENT.name
    }

    fun getCurrentUserRole(): UserRole? {
        return when (_currentUser.value?.role) {
            UserRole.LECTURER.name -> UserRole.LECTURER
            UserRole.STUDENT.name -> UserRole.STUDENT
            UserRole.ADMIN.name -> UserRole.ADMIN
            else -> null
        }
    }

    fun loadCurrentUser() {
        viewModelScope.launch {
            val userId = dataStoreManager.userId.firstOrNull()
            if (userId != null) {
                _currentUser.value = userRepository.getUserByUserId(userId)
            } else {
                _currentUser.value = userRepository.getCurrentUser().firstOrNull()
            }
        }
    }
}