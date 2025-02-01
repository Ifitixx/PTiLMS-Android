package com.pizzy.ptilms.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pizzy.ptilms.data.model.DepartmentEntity
import com.pizzy.ptilms.data.repository.DepartmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DepartmentViewModel @Inject constructor(
    private val departmentRepository: DepartmentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<DepartmentEntity>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<DepartmentEntity>>> = _uiState.asStateFlow()

    init {
        loadDepartments()
    }

    private fun loadDepartments() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = UiState.Loading
            try {
                val departments = departmentRepository.getAllDepartments().first()
                _uiState.value = UiState.Success(departments)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to load departments", e)
            }
        }
    }

    suspend fun getDepartmentByName(name: String): DepartmentEntity? {
        return departmentRepository.getDepartmentByName(name)
    }
}