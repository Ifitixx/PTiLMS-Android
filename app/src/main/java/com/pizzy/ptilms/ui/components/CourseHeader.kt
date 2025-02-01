package com.pizzy.ptilms.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pizzy.ptilms.data.model.CourseWithDepartmentAndLevel

@Composable
fun CourseHeader(courseWithDepartmentAndLevel: CourseWithDepartmentAndLevel?) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            courseWithDepartmentAndLevel?.courseEntity?.courseTitle ?: "Course Title", // Corrected: Access through courseEntity
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            "Department: ${courseWithDepartmentAndLevel?.departmentEntity?.name ?: "Unknown Department"}", // Corrected: Access through departmentEntity
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            "Level: ${courseWithDepartmentAndLevel?.levelEntity?.name ?: "Unknown Level"}", // Corrected: Access through levelEntity
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}