package com.pizzy.ptilms.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pizzy.ptilms.ui.components.CourseItem
import com.pizzy.ptilms.ui.components.CourseListHeader
import com.pizzy.ptilms.ui.components.CourseSearchBar
import com.pizzy.ptilms.ui.components.ErrorScreen
import com.pizzy.ptilms.ui.components.ShimmerEffect
import com.pizzy.ptilms.viewmodel.CourseUiState
import com.pizzy.ptilms.viewmodel.CourseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoursesScreen(
    navController: NavHostController,
    departmentName: String,
    levelLevel: String,
    uiState: CourseUiState,
    viewModel: CourseViewModel
) {
    var searchQuery by remember { mutableStateOf("") }

    // Trigger fetching courses when the screen is composed
    LaunchedEffect(key1 = departmentName, key2 = levelLevel) {
        viewModel.fetchCoursesByName(departmentName, levelLevel)
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TopAppBar(
                    title = {
                        // You can add a title here if needed
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    actions = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = {
                                /* Handle add course*/
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "Add Course"
                                )
                            }
                            IconButton(onClick = {
                                /* Handle Edit course*/
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = "Edit Course"
                                )
                            }
                            IconButton(onClick = {
                                /* Handle add Delete*/
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Delete Course"
                                )
                            }
                            Spacer(modifier = Modifier.padding(2.dp))
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        scrolledContainerColor = MaterialTheme.colorScheme.surface,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                        actionIconContentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 4.dp), // Add elevation here
                    windowInsets = WindowInsets(0, 0, 0, 0)
                )
                HorizontalDivider(modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp, // Thin divider
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(1.dp))
            CourseListHeader(departmentName, levelLevel)
            CourseSearchBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it }
            )

            when (uiState) {
                is CourseUiState.Loading -> {
                    ShimmerEffect()
                }

                is CourseUiState.Error -> {
                    ErrorScreen(errorMessage = uiState.message)
                }

                is CourseUiState.Success -> {
                    val filteredCourses = uiState.data.filter {
                        it.courseEntity.courseTitle.contains(searchQuery, true) ||
                                it.courseEntity.courseCode.contains(searchQuery, true)
                    }

                    if (filteredCourses.isEmpty()) {
                        EmptyCoursesScreen()
                    } else {
                        LazyColumn {
                            items(filteredCourses) { course ->
                                CourseItem(courseWithDepartment = course, navController = navController)
                            }
                        }
                    }
                }

                is CourseUiState.Empty -> {
                    EmptyCoursesScreen()
                }
            }
        }
    }
}