package com.pizzy.ptilms.viewmodel

import androidx.lifecycle.ViewModel
import com.pizzy.ptilms.data.repository.CourseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val courseRepository: CourseRepository
) : ViewModel() {


}