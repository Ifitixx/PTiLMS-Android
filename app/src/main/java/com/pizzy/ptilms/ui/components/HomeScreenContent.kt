package com.pizzy.ptilms.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pizzy.ptilms.data.model.AnnouncementEntity
import com.pizzy.ptilms.data.model.AssignmentEntity
import com.pizzy.ptilms.data.model.DepartmentEntity
import com.pizzy.ptilms.data.model.LevelEntity
import com.pizzy.ptilms.navigation.Screen
import com.pizzy.ptilms.viewmodel.UiState

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    homeUiState: UiState<Pair<List<DepartmentEntity>, List<LevelEntity>>>,
    announcementUiState: UiState<List<AnnouncementEntity>>,
    assignmentUiState: UiState<List<AssignmentEntity>>,
    selectedDepartment: DepartmentEntity?,
    selectedLevel: LevelEntity?,
    navController: NavHostController,
    onViewCoursesClicked: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        when (homeUiState) {
            is UiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
            is UiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text(
                            text = "Welcome to Plearner, Student!",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                    item {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Student Name",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "Student ID",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    item {
                        // Navigate to DepartmentListScreen
                        Button(onClick = { navController.navigate(Screen.DepartmentList.route) }) {
                            Text("Select Department and Level")
                        }
                    }
                    item {
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
                                            Card(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                            ) {
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
                    item {
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
                                            Card(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                            ) {
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
                    item {
                        if (selectedDepartment != null && selectedLevel != null) {
                            Button(onClick = onViewCoursesClicked) {
                                Text(text = "View My Courses")
                            }
                        }
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