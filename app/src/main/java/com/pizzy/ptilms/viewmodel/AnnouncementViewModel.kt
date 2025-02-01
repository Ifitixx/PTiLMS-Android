package com.pizzy.ptilms.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pizzy.ptilms.data.model.AnnouncementEntity
import com.pizzy.ptilms.data.model.UserEntity
import com.pizzy.ptilms.data.repository.AnnouncementRepository
import com.pizzy.ptilms.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AnnouncementViewModel @Inject constructor(
    private val announcementRepository: AnnouncementRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<AnnouncementEntity>>>(UiState.Empty)
    val uiState: StateFlow<UiState<List<AnnouncementEntity>>> = _uiState.asStateFlow()

    private val _addAnnouncementUiState = MutableStateFlow<UiState<Nothing>>(UiState.Empty)
    val addAnnouncementUiState: StateFlow<UiState<Nothing>> = _addAnnouncementUiState.asStateFlow()

    private val _deleteAnnouncementUiState = MutableStateFlow<UiState<Nothing>>(UiState.Empty)
    val deleteAnnouncementUiState: StateFlow<UiState<Nothing>> = _deleteAnnouncementUiState.asStateFlow()

    private val _updateAnnouncementUiState = MutableStateFlow<UiState<Nothing>>(UiState.Empty)
    val updateAnnouncementUiState: StateFlow<UiState<Nothing>> = _updateAnnouncementUiState.asStateFlow()

    private val _currentUser = MutableStateFlow<UiState<UserEntity>>(UiState.Loading)
    val currentUser: StateFlow<UiState<UserEntity>> = _currentUser.asStateFlow()

    private val _announcementDetailUiState = MutableStateFlow<UiState<AnnouncementEntity>>(UiState.Loading)
    val announcementDetailUiState: StateFlow<UiState<AnnouncementEntity>> = _announcementDetailUiState.asStateFlow()

    init {
        fetchCurrentUser()
    }

    private fun fetchCurrentUser() {
        viewModelScope.launch {
            userRepository.getCurrentUser()
                .catch { e ->
                    _currentUser.value = UiState.Error(e.message ?: "Failed to fetch current user", e)
                }
                .collectLatest { user ->
                    if (user != null) {
                        _currentUser.value = UiState.Success(user)
                    } else {
                        _currentUser.value = UiState.Error("User not found")
                    }
                }
        }
    }

    fun getAnnouncementsByCourseId(courseId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            announcementRepository.getAnnouncementsByCourseId(courseId)
                .catch { e ->
                    _uiState.value = UiState.Error(e.message ?: "Fetch announcements error", e)
                }
                .collectLatest { announcements ->
                    _uiState.value = if (announcements.isEmpty()) {
                        UiState.Empty
                    } else {
                        UiState.Success(announcements)
                    }
                }
        }
    }

    fun getAnnouncements() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            announcementRepository.getAnnouncements()
                .catch { e ->
                    _uiState.value = UiState.Error(e.message ?: "Fetch all announcements error", e)
                }
                .collectLatest { announcements ->
                    _uiState.value = if (announcements.isEmpty()) {
                        UiState.Empty
                    } else {
                        UiState.Success(announcements)
                    }
                }
        }
    }

    fun addAnnouncement(title: String, content: String, courseId: String) {
        viewModelScope.launch {
            _addAnnouncementUiState.value = UiState.Loading
            val user = (_currentUser.value as? UiState.Success)?.data
            if (user == null) {
                _addAnnouncementUiState.value = UiState.Error("User not found")
                return@launch
            }
            val newAnnouncement = AnnouncementEntity(
                title = title,
                content = content,
                courseId = courseId,
                lecturerId = user.userId,
                lecturerUsername = user.username,
                timestamp = Date().time
            )
            try {
                announcementRepository.addAnnouncement(newAnnouncement)
                _addAnnouncementUiState.value = UiState.Empty
            } catch (e: Exception) {
                _addAnnouncementUiState.value = UiState.Error(e.message ?: "Add announcement error", e)
            }
        }
    }

    fun getAnnouncementById(announcementId: String) {
        viewModelScope.launch {
            _announcementDetailUiState.value = UiState.Loading
            announcementRepository.getAnnouncementById(announcementId)
                .catch { e ->
                    _announcementDetailUiState.value = UiState.Error(e.message ?: "An unknown error occurred")
                }
                .collectLatest { announcement ->
                    _announcementDetailUiState.value = announcement?.let { UiState.Success(it) } ?: UiState.Empty
                }
        }
    }

    fun deleteAnnouncement(announcement: AnnouncementEntity) {
        viewModelScope.launch {
            _deleteAnnouncementUiState.value = UiState.Loading
            try {
                announcementRepository.deleteAnnouncement(announcement)
                _deleteAnnouncementUiState.value = UiState.Empty
            } catch (e: Exception) {
                _deleteAnnouncementUiState.value = UiState.Error(e.message ?: "Delete announcement error", e)
            }
        }
    }

    fun updateAnnouncement(announcement: AnnouncementEntity) {
        viewModelScope.launch {
            _updateAnnouncementUiState.value = UiState.Loading
            try {
                announcementRepository.updateAnnouncement(announcement)
                _updateAnnouncementUiState.value = UiState.Empty
            } catch (e: Exception) {
                _updateAnnouncementUiState.value = UiState.Error(e.message ?: "Update announcement error", e)
            }
        }
    }
}