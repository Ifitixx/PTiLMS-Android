package com.pizzy.ptilms.ui.student

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pizzy.ptilms.navigation.Screen
import com.pizzy.ptilms.ui.CoursesListWithDepartmentAndLevel
import com.pizzy.ptilms.ui.MyCoursesList
import com.pizzy.ptilms.ui.components.BottomNavigationBar
import com.pizzy.ptilms.ui.components.CustomTopAppBar
import com.pizzy.ptilms.ui.components.HomeScreenContent
import com.pizzy.ptilms.ui.components.NavigationDrawer
import com.pizzy.ptilms.viewmodel.AnnouncementViewModel
import com.pizzy.ptilms.viewmodel.AssignmentViewModel
import com.pizzy.ptilms.viewmodel.CourseViewModel
import com.pizzy.ptilms.viewmodel.DashboardViewModel
import com.pizzy.ptilms.viewmodel.HomeViewModel
import com.pizzy.ptilms.viewmodel.SelectionViewModel
import com.pizzy.ptilms.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentHomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel(),
    courseViewModel: CourseViewModel = hiltViewModel(),
    announcementViewModel: AnnouncementViewModel = hiltViewModel(),
    assignmentViewModel: AssignmentViewModel = hiltViewModel(),
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
    selectionViewModel: SelectionViewModel = hiltViewModel()
) {
    val homeUiState by dashboardViewModel.homeUiState.collectAsState()
    val selectedDepartment by selectionViewModel.selectedDepartment.collectAsState()
    val selectedLevel by selectionViewModel.selectedLevel.collectAsState()
    val courseUiState by dashboardViewModel.courseUiState.collectAsState()
    val myCoursesUiState by dashboardViewModel.myCourses.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val announcementsUiState by dashboardViewModel.announcements.collectAsState()
    val assignmentsUiState by dashboardViewModel.assignments.collectAsState()

    LaunchedEffect(key1 = Unit) {
        dashboardViewModel.fetchHomeData()
        dashboardViewModel.fetchMyCourses()
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(navController = navController)
        },
        bottomBar = {
            BottomNavigationBar(
                modifier = Modifier,
                navController = navController,
                viewModel = dashboardViewModel
            )
        },
        modifier = Modifier
    ) { innerPadding ->
        HomeScreenContent(
            modifier = Modifier.padding(innerPadding),
            padding = innerPadding,
            homeUiState = homeUiState,
            announcementUiState = announcementsUiState,
            assignmentUiState = assignmentsUiState,
            selectedDepartment = selectedDepartment,
            selectedLevel = selectedLevel,
            navController = navController,
            onViewCoursesClicked = { navController.navigate(Screen.CoursesScreen.route) }
        )
        if (courseUiState is UiState.Success) {
            CoursesListWithDepartmentAndLevel(
                uiState = courseUiState,
                navController = navController
            )
        }
        if (myCoursesUiState is UiState.Success) {
            MyCoursesList(
                uiState = myCoursesUiState,
                navController = navController
            )
        }
    }
}