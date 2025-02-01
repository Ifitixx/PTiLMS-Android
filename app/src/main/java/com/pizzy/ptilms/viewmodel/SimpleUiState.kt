package com.pizzy.ptilms.viewmodel

sealed class SimpleUiState {
    object Loading : SimpleUiState()
    object Success : SimpleUiState()
    data class Error(val message: String, val exception: Throwable? = null) : SimpleUiState()
    object Empty : SimpleUiState()
}