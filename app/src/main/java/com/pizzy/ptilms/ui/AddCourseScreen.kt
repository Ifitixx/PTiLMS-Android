package com.pizzy.ptilms.ui

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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pizzy.ptilms.data.model.DepartmentEntity
import com.pizzy.ptilms.data.model.LevelEntity
import com.pizzy.ptilms.viewmodel.CourseViewModel
import com.pizzy.ptilms.viewmodel.SimpleUiState
import com.pizzy.ptilms.viewmodel.DepartmentViewModel
import com.pizzy.ptilms.viewmodel.LevelViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCourseScreen(
    navController: NavController,
    courseViewModel: CourseViewModel = hiltViewModel(),
    departmentViewModel: DepartmentViewModel = hiltViewModel(),
    levelViewModel: LevelViewModel = hiltViewModel()
) {
    var courseTitle by remember { mutableStateOf("") }
    var courseDescription by remember { mutableStateOf("") }
    var courseCode by remember { mutableStateOf("") }
    var departmentName by remember { mutableStateOf("") }
    var levelName by remember { mutableStateOf("") }
    var format by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var lecturerId by remember { mutableStateOf("") }
    var instructor by remember { mutableStateOf("") }
    var isDepartmental by remember { mutableStateOf(false) } // Default to false
    var units by remember { mutableStateOf("") }

    var departmentId by remember { mutableStateOf<Int?>(null) }
    var levelId by remember { mutableStateOf<Int?>(null) }

    val addCourseUiState by courseViewModel.addCourseUiState.collectAsState(initial = SimpleUiState.Loading)

    // Fetch department and level
    var department: DepartmentEntity? by remember { mutableStateOf(null) }
    var level: LevelEntity? by remember { mutableStateOf(null) }

    LaunchedEffect(key1 = departmentName) {
        if (departmentName.isNotEmpty()) {
            department = departmentViewModel.getDepartmentByName(departmentName)
            departmentId = department?.id
        }
    }

    LaunchedEffect(key1 = levelName) {
        if (levelName.isNotEmpty()) {
            level = levelViewModel.getLevelByName(levelName)
            levelId = level?.id
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add Course") },
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
            OutlinedTextField(
                value = courseTitle,
                onValueChange = { courseTitle = it },
                label = { Text("Course Title") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = courseDescription,
                onValueChange = { courseDescription = it },
                label = { Text("Course Description") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = courseCode,
                onValueChange = { courseCode = it },
                label = { Text("Course Code") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = departmentName,
                onValueChange = { departmentName = it },
                label = { Text("Department Name") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = levelName,
                onValueChange = { levelName = it },
                label = { Text("Level") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = format,
                onValueChange = { format = it },
                label = { Text("Format") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = lecturerId,
                onValueChange = { lecturerId = it },
                label = { Text("Lecturer Id") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = instructor,
                onValueChange = { instructor = it },
                label = { Text("Instructor") }
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = units,
                onValueChange = { units = it },
                label = { Text("Units") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            // You can use a Switch or Checkbox for isDepartmental
            // For simplicity, I'll use a TextField here, but a Switch is better
            OutlinedTextField(
                value = isDepartmental.toString(),
                onValueChange = { isDepartmental = it.toBoolean() },
                label = { Text("Is Departmental (true/false)") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if (departmentId != null && levelId != null) {
                    courseViewModel.addCourse(
                        courseTitle,
                        courseDescription,
                        courseCode,
                        departmentId!!,
                        levelId!!,
                        format,
                        category,
                        lecturerId.toLongOrNull(),
                        instructor,
                        units.toIntOrNull() ?: 0,
                        isDepartmental
                    )
                }
            }) {
                Text("Save Course")
            }
            when (addCourseUiState) {
                is SimpleUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is SimpleUiState.Empty -> {
                    LaunchedEffect(key1 = addCourseUiState) {
                        navController.popBackStack()
                    }
                }

                is SimpleUiState.Success -> {
                    LaunchedEffect(key1 = addCourseUiState) {
                        navController.popBackStack()
                    }
                }

                is SimpleUiState.Error -> {
                    Text(
                        text = "Error: ${(addCourseUiState as SimpleUiState.Error).message}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}