package com.pizzy.ptilms.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pizzy.ptilms.data.model.CourseWithDepartment
import com.pizzy.ptilms.data.model.CourseWithDepartmentAndLevel
import com.pizzy.ptilms.navigation.Screen
import com.pizzy.ptilms.ui.components.CourseItemMyCourses
import com.pizzy.ptilms.viewmodel.CourseUiState
import com.pizzy.ptilms.viewmodel.CourseViewModel

@Composable
fun MyCoursesScreen(
    navController: NavHostController,
    uiState: CourseUiState,
    courseViewModel: CourseViewModel = hiltViewModel()
) {
    // Trigger initial course loading
    LaunchedEffect(key1 = Unit) {
        courseViewModel.fetchMyCourses()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when (uiState) {
            is CourseUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is CourseUiState.Error -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Error: ${uiState.message}",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }

            is CourseUiState.Success -> {
                if (uiState.data.isEmpty()) {
                    Text(
                        text = "No courses available for you at this time.",
                        modifier = Modifier.align(Alignment.Center),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.data) { courseWithDepartmentAndLevel ->
                            CourseItemMyCourses(
                                courseWithDepartment = courseWithDepartmentAndLevel.toCourseWithDepartment(),
                                onCourseClick = { selectedCourse ->
                                    navController.navigate(
                                        Screen.CourseDetailsScreen.createRoute(
                                            selectedCourse.courseEntity.courseId
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }

            is CourseUiState.Empty -> {
                Text(
                    text = "No data available.",
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

// Extension function to convert CourseWithDepartmentAndLevel to CourseWithDepartment
fun CourseWithDepartmentAndLevel.toCourseWithDepartment(): CourseWithDepartment {
    return CourseWithDepartment(
        courseEntity = this.courseEntity,
        departmentEntity = this.departmentEntity
    )
}