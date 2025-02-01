package com.pizzy.ptilms.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pizzy.ptilms.data.model.DepartmentEntity
import com.pizzy.ptilms.data.model.LevelEntity
import com.pizzy.ptilms.data.repository.DepartmentRepository
import com.pizzy.ptilms.data.repository.LevelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectionViewModel @Inject constructor(
    private val departmentRepository: DepartmentRepository,
    private val levelRepository: LevelRepository
) : ViewModel() {

    private val _selectedDepartment = MutableStateFlow<DepartmentEntity?>(null)
    val selectedDepartment: StateFlow<DepartmentEntity?> = _selectedDepartment.asStateFlow()

    private val _selectedLevel = MutableStateFlow<LevelEntity?>(null)
    val selectedLevel: StateFlow<LevelEntity?> = _selectedLevel.asStateFlow()

    fun selectDepartment(department: DepartmentEntity) {
        _selectedDepartment.value = department
    }

    fun selectLevel(level: LevelEntity) {
        _selectedLevel.value = level
    }

    fun clearSelection() {
        _selectedDepartment.value = null
        _selectedLevel.value = null
    }

    suspend fun getDepartmentId(departmentName: String): Int? {
        return departmentRepository.getDepartmentByName(departmentName)?.id
    }

    suspend fun getLevelId(levelName: String): Int? {
        return levelRepository.getLevelByName(levelName)?.id
    }
    fun getDepartmentName(): String? {
        return _selectedDepartment.value?.name
    }

    fun getLevelName(): String? {
        return _selectedLevel.value?.name
    }
}