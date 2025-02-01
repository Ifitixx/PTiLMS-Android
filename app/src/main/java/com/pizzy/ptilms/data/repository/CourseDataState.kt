package com.pizzy.ptilms.data.repository

import com.pizzy.ptilms.data.model.CourseEntity

sealed class CourseDataState {
    object Loading : CourseDataState()
    data class Success(val courses: List<CourseEntity>) : CourseDataState()
    data class Error(val message: String) : CourseDataState()
}