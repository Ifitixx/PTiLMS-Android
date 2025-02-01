package com.pizzy.ptilms.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pizzy.ptilms.data.DataStoreManager
import com.pizzy.ptilms.data.model.AnnouncementEntity
import com.pizzy.ptilms.data.model.AssignmentEntity
import com.pizzy.ptilms.data.model.CourseWithDepartment
import com.pizzy.ptilms.data.model.CourseWithDepartmentAndLevel
import com.pizzy.ptilms.data.model.DepartmentEntity
import com.pizzy.ptilms.data.model.LevelEntity
import com.pizzy.ptilms.data.model.UserRole
import com.pizzy.ptilms.data.repository.AnnouncementRepository
import com.pizzy.ptilms.data.repository.AssignmentRepository
import com.pizzy.ptilms.data.repository.CourseRepository
import com.pizzy.ptilms.data.repository.DepartmentRepository
import com.pizzy.ptilms.data.repository.LevelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val departmentRepository: DepartmentRepository,
    private val levelRepository: LevelRepository,
    private val courseRepository: CourseRepository,
    private val announcementRepository: AnnouncementRepository,
    private val assignmentRepository: AssignmentRepository,
) : ViewModel() {

    val userRole: Flow<String?> = dataStoreManager.userRole
    val username: Flow<String?> = dataStoreManager.username

    private val _homeUiState =
        MutableStateFlow<UiState<Pair<List<DepartmentEntity>, List<LevelEntity>>>>(UiState.Loading)
    val homeUiState: StateFlow<UiState<Pair<List<DepartmentEntity>, List<LevelEntity>>>> =
        _homeUiState

    private val _courseUiState =
        MutableStateFlow<UiState<List<CourseWithDepartmentAndLevel>>>(UiState.Loading)
    val courseUiState: StateFlow<UiState<List<CourseWithDepartmentAndLevel>>> = _courseUiState

    private val _announcements =
        MutableStateFlow<UiState<List<AnnouncementEntity>>>(UiState.Loading)
    val announcements: StateFlow<UiState<List<AnnouncementEntity>>> = _announcements

    private val _assignments =
        MutableStateFlow<UiState<List<AssignmentEntity>>>(UiState.Loading)
    val assignments: StateFlow<UiState<List<AssignmentEntity>>> = _assignments

    private val _myCourses = MutableStateFlow<UiState<List<CourseWithDepartment>>>(UiState.Loading)
    val myCourses: StateFlow<UiState<List<CourseWithDepartment>>> = _myCourses

    init {
        fetchHomeData()
        fetchAnnouncements()
        fetchAssignments()
        fetchMyCourses()
    }

    fun fetchHomeData() {
        loadHomeData()
    }

    fun fetchMyCourses() {
        loadMyCourses()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadMyCourses() {
        viewModelScope.launch {
            _myCourses.value = UiState.Loading
            try {
                val currentUserId = dataStoreManager.userId.firstOrNull()
                val userRoleString = dataStoreManager.userRole.firstOrNull()

                if (currentUserId == null || userRoleString == null) {
                    _myCourses.value = UiState.Error(
                        "User ID or Role not found",
                        Exception("User ID or Role not found")
                    )
                    return@launch
                }

                val userRole = try {
                    UserRole.valueOf(userRoleString.uppercase())
                } catch (e: IllegalArgumentException) {
                    _myCourses.value = UiState.Error("Invalid User Role", e)
                    return@launch
                }
                val currentUserIdInt = currentUserId.toIntOrNull() ?: 0
                when (userRole) {
                    UserRole.LECTURER -> {
                        courseRepository.getCoursesByLecturer(currentUserIdInt.toLong())
                            .catch { e ->
                                _myCourses.value = UiState.Error(
                                    e.message ?: "Failed to fetch my courses", e
                                )
                            }
                            .collectLatest { coursesWithDepartmentAndLevel ->
                                val coursesWithDepartment = coursesWithDepartmentAndLevel.map {
                                    CourseWithDepartment(it.courseEntity, it.departmentEntity)
                                }
                                _myCourses.value = if (coursesWithDepartment.isEmpty()) {
                                    UiState.Empty
                                } else {
                                    UiState.Success(coursesWithDepartment)
                                }
                            }
                    }

                    UserRole.STUDENT -> {
                        val coursesWithDepartment = courseRepository.getCoursesByStudent(currentUserId)
                        _myCourses.value = if (coursesWithDepartment.isEmpty()) {
                            UiState.Empty
                        } else {
                            UiState.Success(coursesWithDepartment)
                        }
                    }

                    UserRole.ADMIN -> {
                        _myCourses.value = UiState.Empty
                    }
                }
            } catch (e: Exception) {
                _myCourses.value =
                    UiState.Error(e.message ?: "Failed to fetch my courses", e)
            }
        }
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            combine(
                departmentRepository.getAllDepartments(),
                levelRepository.getAllLevels()
            ) { departments, levels ->
                Pair(departments, levels)
            }
                .onStart { _homeUiState.value = UiState.Loading }
                .catch { e ->
                    _homeUiState.value =
                        UiState.Error(e.message ?: "Error loading home data", e)
                }
                .collect { data ->
                    _homeUiState.value = UiState.Success(data)
                }
        }
    }

    fun loadCourses(departmentName: String, level: String) {
        viewModelScope.launch {
            val departmentId = departmentRepository.getDepartmentByName(departmentName)?.id
            val levelId = levelRepository.getLevelByName(level)?.id

            if (departmentId != null && levelId != null) {
                courseRepository.getCoursesByDepartmentAndLevel(departmentId, levelId)
                    .onStart { _courseUiState.value = UiState.Loading }
                    .catch { e ->
                        _courseUiState.value =
                            UiState.Error(e.message ?: "Error loading courses", e)
                    }
                    .collect { courses ->
                        _courseUiState.value = if (courses.isEmpty()) {
                            UiState.Empty
                        } else {
                            UiState.Success(courses)
                        }
                    }
            } else {
                _courseUiState.value = UiState.Error(
                    "Department or level not found",
                    Exception("Department or level not found")
                )
            }
        }
    }

    private fun fetchAnnouncements() {
        viewModelScope.launch {
            announcementRepository.getAnnouncements()
                .onStart { _announcements.value = UiState.Loading }
                .catch { e ->
                    _announcements.value =
                        UiState.Error(e.message ?: "Error loading announcements", e)
                }
                .collect { announcements ->
                    _announcements.value = if (announcements.isEmpty()) {
                        UiState.Empty
                    } else {
                        UiState.Success(announcements)
                    }
                }
        }
    }

    private fun fetchAssignments() {
        viewModelScope.launch {
            assignmentRepository.getAllAssignments()
                .onStart { _assignments.value = UiState.Loading }
                .catch { e ->
                    _assignments.value =
                        UiState.Error(e.message ?: "Error loading assignments", e)
                }
                .collect { assignments ->
                    _assignments.value = if (assignments.isEmpty()) {
                        UiState.Empty
                    } else {
                        UiState.Success(assignments)
                    }
                }
        }
    }
}