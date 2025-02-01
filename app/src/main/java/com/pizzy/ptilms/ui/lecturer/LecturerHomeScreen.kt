package com.pizzy.ptilms.ui.lecturer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.pizzy.ptilms.data.model.DepartmentEntity
import com.pizzy.ptilms.data.model.LevelEntity
import com.pizzy.ptilms.navigation.Screen
import com.pizzy.ptilms.ui.components.BottomNavigationBar
import com.pizzy.ptilms.ui.components.LecturerHomeScreenContent
import com.pizzy.ptilms.viewmodel.CourseViewModel
import com.pizzy.ptilms.viewmodel.DashboardViewModel
import com.pizzy.ptilms.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LecturerHomeScreen(
    navController: NavHostController = rememberNavController(),
    courseViewModel: CourseViewModel = hiltViewModel(),
    dashboardViewModel: DashboardViewModel = hiltViewModel()
) {
    val courseUiState by courseViewModel.uiState.collectAsState() // Changed to uiState
    val homeUiState by dashboardViewModel.homeUiState.collectAsState()
    val announcementUiState by dashboardViewModel.announcements.collectAsState()
    val assignmentUiState by dashboardViewModel.assignments.collectAsState()

    var selectedDepartment: DepartmentEntity? by remember { mutableStateOf(null) }
    var selectedLevel: LevelEntity? by remember { mutableStateOf(null) }

    // Fetch courses when a department and level are selected
    LaunchedEffect(selectedDepartment, selectedLevel) {
        if (selectedDepartment != null && selectedLevel != null) {
            courseViewModel.fetchCourses(selectedDepartment!!.id, selectedLevel!!.id)
        }
    }

    // Set initial department and level
    LaunchedEffect(homeUiState) {
        if (homeUiState is UiState.Success) {
            val (departments, levels) = (homeUiState as UiState.Success<Pair<List<DepartmentEntity>, List<LevelEntity>>>).data
            selectedDepartment = departments.firstOrNull()
            selectedLevel = levels.firstOrNull()
        }
    }

    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Lecturer Home") },
                actions = {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "Options")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Profile") },
                            onClick = {
                                navController.navigate(Screen.Profile.route)
                                showMenu = false
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.AccountCircle,
                                    contentDescription = null
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Logout") },
                            onClick = {
                                navController.navigate(Screen.Logout.route)
                                showMenu = false
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.AutoMirrored.Filled.Logout,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddCourse.route) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Course")
            }
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                viewModel = dashboardViewModel,
                modifier = Modifier
            )
        },
        content = { innerPadding ->
            LecturerHomeScreenContent(
                padding = innerPadding,
                homeUiState = homeUiState,
                courseUiState = courseUiState, // Correct type
                announcementUiState = announcementUiState,
                assignmentUiState = assignmentUiState,
                selectedDepartment = selectedDepartment,
                selectedLevel = selectedLevel,
                navController = navController
            )
        }
    )
}