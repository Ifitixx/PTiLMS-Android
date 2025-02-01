package com.pizzy.ptilms.viewmodel

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pizzy.ptilms.data.local.StorageHelper
import com.pizzy.ptilms.data.model.CourseEntity
import com.pizzy.ptilms.data.model.CourseWithDepartment
import com.pizzy.ptilms.data.repository.CourseRepository
import com.pizzy.ptilms.data.repository.DepartmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import android.database.sqlite.SQLiteException
import android.webkit.MimeTypeMap
import android.widget.Toast
import java.io.File

@HiltViewModel
class CourseDetailsViewModel @Inject constructor(
    application: Application,
    private val courseRepository: CourseRepository,
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow<CourseDetailsUiState>(CourseDetailsUiState.Loading)
    val uiState: StateFlow<CourseDetailsUiState> = _uiState.asStateFlow()

    private val _isSaving: MutableState<Boolean> = mutableStateOf(false)
    val isSaving: State<Boolean> = _isSaving

    private val _selectedPdfUri: MutableState<Uri?> = mutableStateOf(null)
    val selectedPdfUri: State<Uri?> = _selectedPdfUri

    private val _selectedImageUri: MutableState<Uri?> = mutableStateOf(null)
    val selectedImageUri: State<Uri?> = _selectedImageUri

    private val _selectedVideoUri: MutableState<Uri?> = mutableStateOf(null)
    val selectedVideoUri: State<Uri?> = _selectedVideoUri

    private fun handleDatabaseError(e: SQLiteException) {
        _uiState.value = CourseDetailsUiState.Error(
            message = "Database error: Please try again later.",
            retryAction = {
                // You can add a retry action here if needed
            }
        )
    }

    private fun handleNetworkError(e: IOException) {
        _uiState.value = CourseDetailsUiState.Error(
            message = "Network error: Please check your internet connection and try again.",
            retryAction = {
                // You can add a retry action here if needed
            }
        )
    }

    private fun handleGenericError(e: Exception) {
        _uiState.value = CourseDetailsUiState.Error(
            message = "An unexpected error occurred. Please try again later.",
            retryAction = {
                // You can add a retry action here if needed
            }
        )
    }

    fun loadCourseDetails(courseId: Int) {
        viewModelScope.launch {
            _uiState.value = CourseDetailsUiState.Loading
            try {
                courseRepository.getCourseWithDepartmentById(courseId).collectLatest { courseWithDepartment ->
                    _uiState.value = if (courseWithDepartment != null) {
                        CourseDetailsUiState.Success(courseWithDepartment)
                    } else {
                        CourseDetailsUiState.NotFound("Course not found")
                    }
                }
            } catch (e: IOException) {
                handleNetworkError(e)
            } catch (e: SQLiteException) {
                handleDatabaseError(e)
            } catch (e: Exception) {
                handleGenericError(e)
            }
        }
    }

    suspend fun saveCourseDetails(course: CourseEntity) {
        _uiState.value = CourseDetailsUiState.Saving
        _isSaving.value = true
        try {
            courseRepository.updateCourse(course)
            _uiState.value = CourseDetailsUiState.SaveSuccess
            loadCourseDetails(course.courseId)
        } catch (e: Exception) {
            _uiState.value = CourseDetailsUiState.SaveError("Failed to save course details.")
        } finally {
            _isSaving.value = false
        }
    }

    fun createFilePickerIntent(fileType: String): Intent {
        return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = when (fileType) {
                "pdf" -> "application/pdf"
                "image" -> "image/*"
                "video" -> "video/*"
                else -> "*/*"
            }
            addCategory(Intent.CATEGORY_OPENABLE)
        }
    }

    fun openFileInDirectory(context: Context, filePath: String) {
        val file = File(filePath)
        val uri = Uri.fromFile(file)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, getMimeType(file))
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "No app found to open this file", Toast.LENGTH_SHORT).show()
        }
    }

    fun savePdfUriForCourse(courseId: Int, uri: Uri) {
        viewModelScope.launch {
            try {
                courseRepository.updatePdfPath(courseId, uri.toString())
                loadCourseDetails(courseId)
            } catch (e: Exception) {
                handleGenericError(e)
            }
        }
    }

    fun saveImageUriForCourse(courseId: Int, uri: Uri) {
        viewModelScope.launch {
            try {
                courseRepository.updateImagePath(courseId, uri.toString())
                loadCourseDetails(courseId)
            } catch (e: Exception) {
                handleGenericError(e)
            }
        }
    }

    fun saveVideoUriForCourse(courseId: Int, uri: Uri) {
        viewModelScope.launch {
            try {
                courseRepository.updateVideoPath(courseId, uri.toString())
                loadCourseDetails(courseId)
            } catch (e: Exception) {
                handleGenericError(e)
            }
        }
    }

    fun getMimeType(file: File): String {
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.absolutePath)
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: "*/*"
    }

    fun getRequestCodeForFileType(fileType: String): Int {
        return when (fileType) {
            "pdf" -> REQUEST_PDF_FILE
            "image" -> REQUEST_IMAGE_FILE
            "video" -> REQUEST_VIDEO_FILE
            else -> 0
        }
    }

    fun handleFileSelectionResult(requestCode: Int, data: Intent?) {
        val selectedUri = data?.data
        if (selectedUri != null) {
            try {
                getApplication<Application>().contentResolver.takePersistableUriPermission(
                    selectedUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                when (requestCode) {
                    REQUEST_PDF_FILE -> _selectedPdfUri.value = selectedUri
                    REQUEST_IMAGE_FILE -> _selectedImageUri.value = selectedUri
                    REQUEST_VIDEO_FILE -> _selectedVideoUri.value = selectedUri
                }
            } catch (e: SecurityException) {
                _uiState.value = CourseDetailsUiState.Error("Error: Permission denied to access URI.")
            }
        } else {
            _uiState.value = CourseDetailsUiState.Error("Error: No file selected.")
        }
    }

    companion object {
        const val REQUEST_PDF_FILE = 1
        const val REQUEST_IMAGE_FILE = 2
        const val REQUEST_VIDEO_FILE = 3
    }
}