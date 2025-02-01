package com.pizzy.ptilms.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pizzy.ptilms.data.model.CourseWithDepartmentAndLevel

@Composable
fun CourseItemLecturer(
    course: CourseWithDepartmentAndLevel,
    onCourseClick: (CourseWithDepartmentAndLevel) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onCourseClick(course) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = course.courseEntity.courseTitle, style = MaterialTheme.typography.titleMedium) // Corrected
            Text(text = "Course ID: ${course.courseEntity.courseId}", style = MaterialTheme.typography.bodyMedium) // Corrected
            Text(text = "Department: ${course.departmentEntity.name}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Level: ${course.levelEntity.name}", style = MaterialTheme.typography.bodyMedium)
            // Add more course details here if needed
        }
    }
}