package com.pizzy.ptilms.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pizzy.ptilms.data.DataStoreManager
import com.pizzy.ptilms.data.model.CourseEntity
import com.pizzy.ptilms.data.model.CourseWithDepartment
import com.pizzy.ptilms.data.model.CourseWithDepartmentAndLevel
import com.pizzy.ptilms.data.model.UserRole
import com.pizzy.ptilms.data.repository.CourseRepository
import com.pizzy.ptilms.data.repository.DepartmentRepository
import com.pizzy.ptilms.data.repository.LevelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseViewModel @Inject constructor(
    private val courseRepository: CourseRepository,
    private val dataStoreManager: DataStoreManager,
    private val departmentRepository: DepartmentRepository,
    private val levelRepository: LevelRepository
) : ViewModel() {

    // Use CourseUiState for operations that return a list of courses
    private val _uiState = MutableStateFlow<CourseUiState>(CourseUiState.Loading)
    val uiState: StateFlow<CourseUiState> = _uiState.asStateFlow()

    // Use UiState for operations that return a list of CourseWithDepartmentAndLevel
    private val _uiStateWithDepartment =
        MutableStateFlow<UiState<List<CourseWithDepartmentAndLevel>>>(UiState.Loading)
    val uiStateWithDepartment: StateFlow<UiState<List<CourseWithDepartmentAndLevel>>> =
        _uiStateWithDepartment.asStateFlow()

    // Use UiState for operations that return a list of CourseWithDepartment
    private val _uiStateWithDepartmentOnly =
        MutableStateFlow<UiState<List<CourseWithDepartment>>>(UiState.Loading)
    val uiStateWithDepartmentOnly: StateFlow<UiState<List<CourseWithDepartment>>> =
        _uiStateWithDepartmentOnly.asStateFlow()

    // Use CourseUiState for operations that return a list of courses
    private val _myCoursesUiState = MutableStateFlow<CourseUiState>(CourseUiState.Loading)
    val myCoursesUiState: StateFlow<CourseUiState> = _myCoursesUiState.asStateFlow()

    // Use SimpleUiState for operations that don't return a list of courses
    private val _addCourseUiState = MutableStateFlow<SimpleUiState>(SimpleUiState.Loading)
    val addCourseUiState: StateFlow<SimpleUiState> = _addCourseUiState.asStateFlow()

    init {
        fetchMyCourses()
    }

    fun fetchMyCourses() {
        viewModelScope.launch {
            _myCoursesUiState.value = CourseUiState.Loading
            try {
                val currentUserId = dataStoreManager.userId.firstOrNull()
                val userRoleString = dataStoreManager.userRole.firstOrNull()

                if (currentUserId == null || userRoleString == null) {
                    _myCoursesUiState.value = CourseUiState.Error("User ID or Role not found")
                    return@launch
                }

                val userRole = try {
                    UserRole.valueOf(userRoleString.uppercase())
                } catch (e: IllegalArgumentException) {
                    _myCoursesUiState.value = CourseUiState.Error("Invalid User Role")
                    return@launch
                }

                when (userRole) {
                    UserRole.LECTURER -> {
                        courseRepository.getCoursesByLecturer(currentUserId.toLong())
                            .catch { e ->
                                _myCoursesUiState.value = CourseUiState.Error(
                                    e.message ?: "Failed to fetch my courses"
                                )
                            }
                            .collectLatest { coursesWithDepartmentAndLevel ->
                                _myCoursesUiState.value =
                                    CourseUiState.Success(coursesWithDepartmentAndLevel)
                            }
                    }

                    UserRole.STUDENT -> {
                        courseRepository.getMyCourses(currentUserId)
                            .catch { e ->
                                _myCoursesUiState.value = CourseUiState.Error(
                                    e.message ?: "Failed to fetch my courses"
                                )
                            }
                            .map { coursesWithDepartment ->
                                coursesWithDepartment.mapNotNull { courseWithDepartment ->
                                    val levelEntity =
                                        levelRepository.getLevelById(courseWithDepartment.courseEntity.levelId) ?: return@mapNotNull null
                                    CourseWithDepartmentAndLevel(
                                        courseEntity = courseWithDepartment.courseEntity,
                                        departmentEntity = courseWithDepartment.departmentEntity,
                                        levelEntity = levelEntity
                                    )
                                }
                            }
                            .collectLatest { coursesWithDepartmentAndLevel ->
                                _myCoursesUiState.value =
                                    CourseUiState.Success(coursesWithDepartmentAndLevel)
                            }
                    }

                    UserRole.ADMIN -> {
                        _myCoursesUiState.value = CourseUiState.Empty
                    }
                }
            } catch (e: Exception) {
                _myCoursesUiState.value =
                    CourseUiState.Error(e.message ?: "Failed to fetch my courses")
            }
        }
    }

    fun fetchCourses(departmentId: Int, levelId: Int) {
        viewModelScope.launch {
            _uiState.value = CourseUiState.Loading
            try {
                courseRepository.getCoursesByDepartmentAndLevel(departmentId, levelId)
                    .catch { e ->
                        _uiState.value = CourseUiState.Error(
                            e.message ?: "Failed to fetch courses"
                        )
                    }
                    .collectLatest { coursesWithDepartmentAndLevel ->
                        _uiState.value = CourseUiState.Success(coursesWithDepartmentAndLevel)
                    }
            } catch (e: Exception) {
                _uiState.value = CourseUiState.Error(
                    e.message ?: "Failed to fetch courses"
                )
            }
        }
    }

    fun fetchCoursesByName(departmentName: String, levelLevel: String) {
        viewModelScope.launch {
            _uiState.value = CourseUiState.Loading
            try {
                val departmentId = departmentRepository.getDepartmentByName(departmentName)?.id
                val levelId = levelRepository.getLevelByName(levelLevel)?.id

                if (departmentId != null && levelId != null) {
                    fetchCourses(departmentId, levelId)
                } else {
                    _uiState.value = CourseUiState.Error("Department or Level not found")
                }
            } catch (e: Exception) {
                _uiState.value = CourseUiState.Error(e.message ?: "An unknown error occurred"
                )
            }
        }
    }

    fun fetchCoursesWithDepartment() {
        viewModelScope.launch {
            _uiStateWithDepartment.value = UiState.Loading
            try {
                val courses = courseRepository.getAllCoursesWithDepartment()
                _uiStateWithDepartment.value = if (courses.isEmpty()) {
                    UiState.Empty
                } else {
                    UiState.Success(courses)
                }
            } catch (e: Exception) {
                _uiStateWithDepartment.value = UiState.Error(
                    e.message ?: "Failed to fetch courses with department",
                    e
                )
            }
        }
    }

    fun fetchCoursesWithDepartmentOnly() {
        viewModelScope.launch {
            _uiStateWithDepartmentOnly.value = UiState.Loading
            try {
                val courses = courseRepository.getAllCoursesWithDepartmentOnly()
                _uiStateWithDepartmentOnly.value = if (courses.isEmpty()) {
                    UiState.Empty
                } else {
                    UiState.Success(courses)
                }
            } catch (e: Exception) {
                _uiStateWithDepartmentOnly.value = UiState.Error(
                    e.message ?: "Failed to fetch courses with department",
                    e
                )
            }
        }
    }

    fun addCourse(
        courseTitle: String,
        courseDescription: String,
        courseCode: String,
        departmentId: Int,
        levelId: Int,
        format: String,
        category: String,
        lecturerId: Long?,
        instructor: String,
        units: Int,
        isDepartmental: Boolean
    ) {
        viewModelScope.launch {
            _addCourseUiState.value = SimpleUiState.Loading
            val newCourse = CourseEntity(
                courseTitle = courseTitle,
                courseDescription = courseDescription,
                courseCode = courseCode,
                departmentId = departmentId,
                levelId = levelId,
                format = format,
                category = category,
                lecturerId = lecturerId,
                instructor = instructor,
                units = units,
                isDepartmental = isDepartmental
            )
            try {
                courseRepository.insertCourse(newCourse)
                _addCourseUiState.value = SimpleUiState.Success
            } catch (e: Exception) {
                _addCourseUiState.value =
                    SimpleUiState.Error(e.message ?: "Failed to add course", e)
            }
        }
    }
}