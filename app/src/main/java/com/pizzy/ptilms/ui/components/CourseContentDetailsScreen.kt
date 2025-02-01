package com.pizzy.ptilms.ui.components

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pizzy.ptilms.viewmodel.CourseDetailsUiState
import com.pizzy.ptilms.viewmodel.CourseDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseContentDetailsScreen(
    navController: NavController,
    courseId: Int,
    viewModel: CourseDetailsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(courseId) {
        viewModel.loadCourseDetails(courseId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Course Content") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            when (val currentState = uiState) {
                is CourseDetailsUiState.Success -> {
                    val courseWithDepartment = currentState.courseWithDepartment
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Text("Course Name: ${courseWithDepartment.courseEntity.courseTitle}")
                        }

                        // FilePickerComponents for uploading content
                        item {
                            FilePickerComposable(
                                fileType = "pdf",
                                onFileSelected = { uri ->
                                    viewModel.savePdfUriForCourse(
                                        courseId,
                                        uri
                                    )
                                }
                            )
                        }
                        item {
                            FilePickerComposable(
                                fileType = "image",
                                onFileSelected = { uri ->
                                    viewModel.saveImageUriForCourse(
                                        courseId,
                                        uri
                                    )
                                }
                            )
                        }
                        item {
                            FilePickerComposable(
                                fileType = "video",
                                onFileSelected = { uri ->
                                    viewModel.saveVideoUriForCourse(
                                        courseId,
                                        uri
                                    )
                                }
                            )
                        }

                        // Display PDF
                        courseWithDepartment.courseEntity.pdfPath?.let { pdfPath ->
                            item {
                                PdfViewerScreen(
                                    modifier = Modifier.height(300.dp),
                                    pdfUri = Uri.parse(pdfPath)
                                )
                            }
                        }

                        // Display Image
                        courseWithDepartment.courseEntity.imagePath?.let { imagePath ->
                            item {
                                AsyncImage(
                                    model = Uri.parse(imagePath),
                                    contentDescription = "Course Image",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                )
                            }
                        }

                        // Display Video
                        courseWithDepartment.courseEntity.videoPath?.let { videoPath ->
                            item {
                                VideoPlayer(
                                    uri = Uri.parse(videoPath),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(300.dp)
                                )
                            }
                        }
                    }
                }

                is CourseDetailsUiState.Error -> {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Error loading course content: ${currentState.message}")
                    }
                }
                is CourseDetailsUiState.NotFound -> {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Course content not found: ${currentState.message}")
                    }
                }

                else -> {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Loading...")
                    }
                }
            }
        }
    }
}