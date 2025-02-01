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
fun CourseItemWithDepartmentAndLevel(
    courseWithDepartmentAndLevel: CourseWithDepartmentAndLevel,
    onCourseClick: (CourseWithDepartmentAndLevel) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onCourseClick(courseWithDepartmentAndLevel) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = courseWithDepartmentAndLevel.courseEntity.courseTitle,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Code: ${courseWithDepartmentAndLevel.courseEntity.courseCode}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Department: ${courseWithDepartmentAndLevel.departmentEntity.name}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Level: ${courseWithDepartmentAndLevel.levelEntity.name}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}