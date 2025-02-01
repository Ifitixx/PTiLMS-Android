package com.pizzy.ptilms.viewmodel

sealed class UserUiState {
    object Loading : UserUiState()
    data class Success(val user: com.pizzy.ptilms.data.model.UserEntity) : UserUiState()
    data class Error(val message: String) : UserUiState()
}