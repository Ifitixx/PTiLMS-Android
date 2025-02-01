package com.pizzy.ptilms.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pizzy.ptilms.data.model.CourseWithDepartment
import com.pizzy.ptilms.viewmodel.UiState

@Composable
fun MyCoursesList(
    uiState: UiState<List<CourseWithDepartment>>,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (uiState) {
            is UiState.Loading -> {
                // Show a loading indicator
                CircularProgressIndicator()
            }

            is UiState.Success -> {
                // Display the list of courses
                if (uiState.data.isEmpty()) {
                    Text(text = "No courses found")
                } else {
                    LazyColumn {
                        items(uiState.data) { courseWithDepartment -> // Corrected variable name
                            // Display each course
                            Text(text = courseWithDepartment.courseEntity.courseTitle) // Corrected line
                        }
                    }
                }
            }

            is UiState.Empty -> {
                // Show a message that there are no courses
                Text(text = "No courses found")
            }

            is UiState.Error -> {
                // Show an error message
                Text(text = "Error: ${uiState.message}")
            }
        }
    }
}