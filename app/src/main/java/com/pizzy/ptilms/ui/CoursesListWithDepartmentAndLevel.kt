package com.pizzy.ptilms.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pizzy.ptilms.data.model.CourseWithDepartmentAndLevel
import com.pizzy.ptilms.ui.components.CourseListItem // Correct import: CourseListItem
import com.pizzy.ptilms.viewmodel.UiState

@Composable
fun CoursesListWithDepartmentAndLevel(
    uiState: UiState<List<CourseWithDepartmentAndLevel>>,
    navController: NavHostController
) {
    when (uiState) {
        is UiState.Success -> {
            val courses = uiState.data
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(courses.size) { index ->
                    CourseListItem( // Correct composable: CourseListItem
                        courseWithDepartment = courses[index],
                        onCourseClick = { selectedCourse ->
                            // Navigate to the course details screen
                            navController.navigate("courseDetails/${selectedCourse.courseEntity.courseId}")
                            Log.d("CourseListItem", "Clicked on: ${selectedCourse.courseEntity.courseTitle}")
                        }
                    )
                }
            }
        }
        is UiState.Empty -> {
            EmptyCoursesScreen()
        }
        else -> {
            // Handle other states if needed
        }
    }
}