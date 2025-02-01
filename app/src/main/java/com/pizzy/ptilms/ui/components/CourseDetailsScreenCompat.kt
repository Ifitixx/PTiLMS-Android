package com.pizzy.ptilms.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pizzy.ptilms.viewmodel.CourseDetailsUiState
import com.pizzy.ptilms.viewmodel.CourseDetailsViewModel

// Fallback for API levels below 33
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailsScreenCompat(
    navController: NavController,
    courseId: Int,
    viewModel: CourseDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(courseId) {
        viewModel.loadCourseDetails(courseId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Course Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (val currentState = uiState) {
                is CourseDetailsUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is CourseDetailsUiState.Success -> {
                    val courseWithDepartment = currentState.courseWithDepartment
                    Text(
                        text = "Course: ${courseWithDepartment.courseEntity.courseTitle}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Course Code: ${courseWithDepartment.courseEntity.courseCode}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Department: ${courseWithDepartment.departmentEntity.name}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            navController.navigate("courseContent/$courseId")
                        }
                    ) {
                        Text("See Course Content")
                    }
                }

                is CourseDetailsUiState.NotFound -> {
                    Text(text = currentState.message)
                }

                is CourseDetailsUiState.Error -> {
                    Text(
                        text = "Error: ${currentState.message}",
                        color = Color.Red
                    )
                }

                is CourseDetailsUiState.Saving -> {
                    CircularProgressIndicator()
                    Text("Saving course details, please wait...")
                }

                is CourseDetailsUiState.SaveSuccess -> {
                    Text("Course details saved successfully!")
                }

                is CourseDetailsUiState.SaveError -> {
                    Text(
                        text = "Error saving course details: ${currentState.message}",
                        color = Color.Red
                    )
                }
            }
        }
    }
}