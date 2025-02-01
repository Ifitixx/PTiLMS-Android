package com.pizzy.ptilms.viewmodel

import com.pizzy.ptilms.data.model.CourseWithDepartmentAndLevel

sealed class CourseUiState {
    object Loading : CourseUiState()
    data class Success(val data: List<CourseWithDepartmentAndLevel>) : CourseUiState()
    data class Error(val message: String) : CourseUiState()
    object Empty : CourseUiState()
}