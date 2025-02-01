package com.pizzy.ptilms.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pizzy.ptilms.data.model.AssignmentEntity
import com.pizzy.ptilms.data.repository.AssignmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AssignmentViewModel @Inject constructor(
    private val assignmentRepository: AssignmentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<AssignmentEntity>>>(UiState.Empty)
    val uiState: StateFlow<UiState<List<AssignmentEntity>>> = _uiState.asStateFlow()

    private val _addAssignmentUiState = MutableStateFlow<UiState<Nothing>>(UiState.Empty)
    val addAssignmentUiState: StateFlow<UiState<Nothing>> = _addAssignmentUiState.asStateFlow()

    private val _deleteAssignmentUiState = MutableStateFlow<UiState<Nothing>>(UiState.Empty)
    val deleteAssignmentUiState: StateFlow<UiState<Nothing>> = _deleteAssignmentUiState.asStateFlow()

    private val _updateAssignmentUiState = MutableStateFlow<UiState<Nothing>>(UiState.Empty)
    val updateAssignmentUiState: StateFlow<UiState<Nothing>> = _updateAssignmentUiState.asStateFlow()

    private val _assignmentDetailUiState = MutableStateFlow<UiState<AssignmentEntity>>(UiState.Loading)
    val assignmentDetailUiState: StateFlow<UiState<AssignmentEntity>> = _assignmentDetailUiState.asStateFlow()

    fun getAssignmentsByCourseId(courseId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                assignmentRepository.getAssignmentsByCourseId(courseId.toInt())
                    .collectLatest { assignmentList ->
                        _uiState.value = if (assignmentList.isEmpty()) {
                            UiState.Empty
                        } else {
                            UiState.Success(assignmentList)
                        }
                    }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "An unknown error occurred", e)
            }
        }
    }

    fun addAssignment(title: String, description: String, dueDate: Date, courseId: Int) {
        viewModelScope.launch {
            _addAssignmentUiState.value = UiState.Loading
            try {
                val assignment = AssignmentEntity(
                    title = title,
                    description = description,
                    dueDate = dueDate,
                    courseId = courseId.toString()
                )
                assignmentRepository.insertAssignment(assignment)
                _addAssignmentUiState.value = UiState.Empty
            } catch (e: Exception) {
                _addAssignmentUiState.value = UiState.Error(e.message ?: "An unknown error occurred", e)
            }
        }
    }

    fun getAssignmentById(assignmentId: Int) {
        viewModelScope.launch {
            _assignmentDetailUiState.value = UiState.Loading
            try {
                val assignment = assignmentRepository.getAssignmentById(assignmentId)
                _assignmentDetailUiState.value = assignment?.let { UiState.Success(it) } ?: UiState.Empty
            } catch (e: Exception) {
                _assignmentDetailUiState.value = UiState.Error(e.message ?: "An unknown error occurred", e)
            }
        }
    }

    fun deleteAssignment(assignment: AssignmentEntity) {
        viewModelScope.launch {
            _deleteAssignmentUiState.value = UiState.Loading
            try {
                assignmentRepository.deleteAssignment(assignment)
                _deleteAssignmentUiState.value = UiState.Empty
            } catch (e: Exception) {
                _deleteAssignmentUiState.value = UiState.Error(e.message ?: "Delete assignment error", e)
            }
        }
    }

    fun updateAssignment(assignment: AssignmentEntity) {
        viewModelScope.launch {
            _updateAssignmentUiState.value = UiState.Loading
            try {
                assignmentRepository.updateAssignment(assignment)
                _updateAssignmentUiState.value = UiState.Empty
            } catch (e: Exception) {
                _updateAssignmentUiState.value = UiState.Error(e.message ?: "Update assignment error", e)
            }
        }
    }
}