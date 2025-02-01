package com.pizzy.ptilms.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pizzy.ptilms.data.model.AnnouncementEntity
import com.pizzy.ptilms.data.model.AssignmentEntity
import com.pizzy.ptilms.data.repository.AnnouncementRepository
import com.pizzy.ptilms.data.repository.AssignmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val announcementRepository: AnnouncementRepository,
    private val assignmentRepository: AssignmentRepository
) : ViewModel() {

    private val _homeUiState = MutableStateFlow<UiState<HomeData>>(UiState.Loading)
    val homeUiState: StateFlow<UiState<HomeData>> = _homeUiState.asStateFlow()

    init {
        fetchHomeData()
    }

    fun fetchHomeData() {
        viewModelScope.launch {
            _homeUiState.value = UiState.Loading
            try {
                val announcementsFlow = announcementRepository.getRecentAnnouncements(5)
                    .catch { e ->
                        _homeUiState.value = UiState.Error(e.message ?: "Failed to fetch announcements", e)
                    }
                val assignmentsFlow = assignmentRepository.getRecentAssignments(5)
                    .catch { e ->
                        _homeUiState.value = UiState.Error(e.message ?: "Failed to fetch assignments", e)
                    }

                combine(announcementsFlow, assignmentsFlow) { announcements, assignments ->
                    HomeData(announcements, assignments)
                }.collect { homeData ->
                    _homeUiState.value = if (homeData.announcements.isEmpty() && homeData.assignments.isEmpty()){
                        UiState.Empty
                    }else{
                        UiState.Success(homeData)
                    }
                }
            } catch (e: Exception) {
                _homeUiState.value = UiState.Error(e.message ?: "Failed to fetch home data", e)
            }
        }
    }

    fun fetchAllHomeData() {
        viewModelScope.launch {
            _homeUiState.value = UiState.Loading
            try {
                val announcementsFlow = announcementRepository.getAnnouncements()
                    .catch { e ->
                        _homeUiState.value = UiState.Error(e.message ?: "Failed to fetch announcements", e)
                    }
                val assignmentsFlow = assignmentRepository.getAllAssignments()
                    .catch { e ->
                        _homeUiState.value = UiState.Error(e.message ?: "Failed to fetch assignments", e)
                    }

                combine(announcementsFlow, assignmentsFlow) { announcements, assignments ->
                    HomeData(announcements, assignments)
                }.collect { homeData ->
                    _homeUiState.value = if (homeData.announcements.isEmpty() && homeData.assignments.isEmpty()){
                        UiState.Empty
                    }else{
                        UiState.Success(homeData)
                    }
                }
            } catch (e: Exception) {
                _homeUiState.value = UiState.Error(e.message ?: "Failed to fetch home data", e)
            }
        }
    }
}

data class HomeData(
    val announcements: List<AnnouncementEntity>,
    val assignments: List<AssignmentEntity>
)