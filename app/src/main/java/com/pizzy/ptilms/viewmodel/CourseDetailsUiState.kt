package com.pizzy.ptilms.viewmodel

import com.pizzy.ptilms.data.model.CourseWithDepartment

sealed class CourseDetailsUiState {
    object Loading : CourseDetailsUiState()
    data class Success(val courseWithDepartment: CourseWithDepartment) : CourseDetailsUiState()
    data class NotFound(val message: String) : CourseDetailsUiState()
    data class Error(val message: String, val retryAction: (() -> Unit)? = null) : CourseDetailsUiState()
    object Saving : CourseDetailsUiState()
    object SaveSuccess : CourseDetailsUiState()
    data class SaveError(val message: String) : CourseDetailsUiState()
}