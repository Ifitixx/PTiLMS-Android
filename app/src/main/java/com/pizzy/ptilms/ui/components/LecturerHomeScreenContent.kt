package com.pizzy.ptilms.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.isEmpty
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pizzy.ptilms.data.model.*
import com.pizzy.ptilms.navigation.Screen
import com.pizzy.ptilms.viewmodel.UiState
import com.pizzy.ptilms.viewmodel.CourseUiState

@Composable
fun LecturerHomeScreenContent(
    padding: PaddingValues,
    homeUiState: UiState<Pair<List<DepartmentEntity>, List<LevelEntity>>>,
    courseUiState: CourseUiState, // Changed to CourseUiState
    announcementUiState: UiState<List<AnnouncementEntity>>,
    assignmentUiState: UiState<List<AssignmentEntity>>,
    selectedDepartment: DepartmentEntity?,
    selectedLevel: LevelEntity?,
    navController: NavHostController,
) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
    ) {
        when (homeUiState) {
            is UiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
            is UiState.Success -> {
                val (departments, levels) = homeUiState.data

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Welcome to Plearner, Lecturer!",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(16.dp)
                    )

                    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Lecturer Name", style = MaterialTheme.typography.titleMedium)
                            // Add more lecturer details here if needed
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Department Selection
                        Column(modifier = Modifier.clickable {
                            navController.navigate(Screen.DepartmentList.route)
                        }) {
                            Text(text = "Department", style = MaterialTheme.typography.labelMedium)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = selectedDepartment?.let { it.name } ?: "Select Department",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Icon(Icons.Filled.ArrowDropDown, contentDescription = "Select Department")
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Level Selection
                        Column(modifier = Modifier.clickable {
                            navController.navigate(Screen.LevelList.route)
                        }) {
                            Text(text = "Level", style = MaterialTheme.typography.labelMedium)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = selectedLevel?.let { it.name } ?: "Select Level",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Icon(Icons.Filled.ArrowDropDown, contentDescription = "Select Level")
                            }
                        }
                    }

                    TabRow(selectedTabIndex = selectedTabIndex) {
                        Tab(selected = selectedTabIndex == 0, onClick = { selectedTabIndex = 0 }) {
                            Text("Announcements")
                        }
                        Tab(selected = selectedTabIndex == 1, onClick = { selectedTabIndex = 1 }) {
                            Text("Assignments")
                        }
                        Tab(selected = selectedTabIndex == 2, onClick = { selectedTabIndex = 2 }) {
                            Text("Courses")
                        }
                    }

                    when (selectedTabIndex) {
                        0 -> AnnouncementSection(announcementUiState)
                        1 -> AssignmentSection(assignmentUiState)
                        2 -> CourseSection(
                            courseUiState = courseUiState,
                            selectedDepartment = selectedDepartment,
                            selectedLevel = selectedLevel,
                            navController = navController
                        )
                    }
                }
            }

            is UiState.Error -> {
                Text(
                    text = "Error loading data: ${homeUiState.message}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is UiState.Empty -> {
                Text(
                    text = "No data available",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun AnnouncementSection(announcementUiState: UiState<List<AnnouncementEntity>>) {
    when (announcementUiState) {
        is UiState.Loading -> CircularProgressIndicator()
        is UiState.Success -> {
            val announcements = announcementUiState.data
            if (announcements.isEmpty()) {
                Text(
                    text = "No announcements yet",
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                Column {
                    announcements.forEach { announcement ->
                        Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = announcement.title,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = announcement.content,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }

        is UiState.Error -> Text(text = "Error loading announcements: ${announcementUiState.message}")
        is UiState.Empty -> Text(text = "No announcements available")
    }
}

@Composable
fun AssignmentSection(assignmentUiState: UiState<List<AssignmentEntity>>) {
    when (assignmentUiState) {
        is UiState.Loading -> CircularProgressIndicator()
        is UiState.Success -> {
            val assignments = assignmentUiState.data
            if (assignments.isEmpty()) {
                Text(
                    text = "No assignments yet",
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                Column {
                    assignments.forEach { assignment ->
                        Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = assignment.title,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "Due Date: ${assignment.dueDate}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }

        is UiState.Error -> Text(text = "Error loading assignments: ${assignmentUiState.message}")
        is UiState.Empty -> Text(text = "No assignments available")
    }
}

@Composable
fun CourseSection(
    courseUiState: CourseUiState, // Changed to CourseUiState
    selectedDepartment: DepartmentEntity?,
    selectedLevel: LevelEntity?,
    navController: NavHostController
) {
    var showAllCourses by remember { mutableStateOf(false) }

    if (selectedDepartment != null && selectedLevel != null) {
        when (courseUiState) { // Changed to CourseUiState
            is CourseUiState.Loading -> CircularProgressIndicator() // Changed to CourseUiState
            is CourseUiState.Success -> { // Changed to CourseUiState
                val courses = courseUiState.data // Changed to CourseUiState
                if (courses.isEmpty()) {
                    Text(
                        text = "No courses available for the selected department and level",
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    LazyColumn {
                        items(if (showAllCourses) courses else courses.take(5)) { course ->
                            Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = course.courseEntity.courseTitle,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = course.courseEntity.courseDescription,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    TextButton(onClick = {
                                        navController.navigate(Screen.CourseDetailsScreen.createRoute(course.courseEntity.courseId))
                                    }) {
                                        Text(text = "View Details")
                                    }
                                }
                            }
                        }
                        if (!showAllCourses && courses.size > 5) {
                            item {
                                TextButton(onClick = { showAllCourses = true }) {
                                    Text(text = "View More Courses")
                                }
                            }
                        }
                    }
                }
            }
            is CourseUiState.Empty -> { // Changed to CourseUiState
                Text(
                    text = "No courses available",
                    color = MaterialTheme.colorScheme.error
                )
            }

            is CourseUiState.Error -> { // Changed to CourseUiState
                Text(
                    text = "Error loading courses: ${courseUiState.message}", // Changed to CourseUiState
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}