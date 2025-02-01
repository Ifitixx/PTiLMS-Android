package com.pizzy.ptilms.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pizzy.ptilms.data.model.CourseWithDepartmentAndLevel
import com.pizzy.ptilms.navigation.Screen
import com.pizzy.ptilms.viewmodel.UiState

@Composable
fun CoursesList(
    uiState: UiState<List<CourseWithDepartmentAndLevel>>,
    navController: NavHostController,
    searchQuery: String
) {
    Column(modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            is UiState.Loading -> {
                Text(
                    text = "Loading...",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            is UiState.Error -> {
                Text(
                    text = "Error: ${uiState.message}",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            is UiState.Success -> {
                val coursesWithDepartmentAndLevel = uiState.data
                val filteredCourses = coursesWithDepartmentAndLevel.filter {
                    it.courseEntity.courseTitle.contains(searchQuery, ignoreCase = true) ||
                            it.courseEntity.courseCode.contains(searchQuery, ignoreCase = true) ||
                            it.departmentEntity.name.contains(searchQuery, ignoreCase = true) ||
                            it.levelEntity.name.contains(searchQuery, ignoreCase = true)
                }
                if (filteredCourses.isEmpty()) {
                    Text(
                        text = "No courses available.",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredCourses) { courseWithDepartmentAndLevel ->
                            CourseListItem(
                                courseWithDepartment = courseWithDepartmentAndLevel,
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

            is UiState.Empty -> {
                Text(
                    text = "No data available.",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}