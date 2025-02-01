package com.pizzy.ptilms.navigation

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pizzy.ptilms.auth.SignUpScreen
import com.pizzy.ptilms.auth.AuthViewModel
import com.pizzy.ptilms.auth.EditPasswordScreen
import com.pizzy.ptilms.auth.ForgotPasswordScreen
import com.pizzy.ptilms.auth.LoginScreen
import com.pizzy.ptilms.data.model.UserRole
import com.pizzy.ptilms.ui.AddAnnouncementScreen
import com.pizzy.ptilms.ui.AddAssignmentScreen
import com.pizzy.ptilms.ui.AddCourseScreen
import com.pizzy.ptilms.ui.AnnouncementDetailScreen
import com.pizzy.ptilms.ui.AnnouncementScreen
import com.pizzy.ptilms.ui.AssignmentDetailScreen
import com.pizzy.ptilms.ui.AssignmentScreen
import com.pizzy.ptilms.ui.CalendarScreen
import com.pizzy.ptilms.ui.ChatDetailScreen
import com.pizzy.ptilms.ui.ChatListScreen
import com.pizzy.ptilms.ui.CommunicationScreen
import com.pizzy.ptilms.ui.CourseListScreen
import com.pizzy.ptilms.ui.CoursesScreen
import com.pizzy.ptilms.ui.MyCoursesScreen
import com.pizzy.ptilms.ui.NotificationsScreen
import com.pizzy.ptilms.ui.SearchScreen
import com.pizzy.ptilms.ui.SettingsScreen
import com.pizzy.ptilms.ui.WelcomeScreen
import com.pizzy.ptilms.ui.components.CourseContentDetailsScreen
import com.pizzy.ptilms.ui.components.CourseDetailsScreen
import com.pizzy.ptilms.ui.components.CourseDetailsScreenCompat
import com.pizzy.ptilms.ui.dashboard.DashboardScreen
import com.pizzy.ptilms.ui.dashboard.DepartmentListScreen
import com.pizzy.ptilms.ui.dashboard.LevelListScreen
import com.pizzy.ptilms.ui.lecturer.LecturerHomeScreen
import com.pizzy.ptilms.ui.profile.ProfileScreen
import com.pizzy.ptilms.ui.student.StudentHomeScreen
import com.pizzy.ptilms.viewmodel.AnnouncementViewModel
import com.pizzy.ptilms.viewmodel.AssignmentViewModel
import com.pizzy.ptilms.viewmodel.CommunicationViewModel
import com.pizzy.ptilms.viewmodel.CourseViewModel
import com.pizzy.ptilms.viewmodel.DashboardViewModel

@Composable
fun MainNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val userRole by authViewModel.userRole.collectAsState()
    val dashboardViewModel: DashboardViewModel = hiltViewModel()
    val announcementViewModel: AnnouncementViewModel = hiltViewModel()
    val courseViewModel: CourseViewModel = hiltViewModel()
    val assignmentViewModel: AssignmentViewModel = hiltViewModel()
    val communicationViewModel: CommunicationViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = if (userRole != null) Screen.Dashboard.route else Screen.Welcome.route,
        modifier = modifier
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(navController = navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(
                navController = navController,
                authViewModel = authViewModel,
            )
        }
        composable(Screen.Auth.route) {
            SignUpScreen(
                navController = navController,
                authViewModel = authViewModel,
            )
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(navController = navController, viewModel = dashboardViewModel)
        }
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(navController = navController, authViewModel = authViewModel)
        }

        composable(
            route = "${Screen.EditPassword.route}/{token}",
            arguments = listOf(navArgument("token") { type = NavType.StringType })
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token")
            if (token != null) {
                EditPasswordScreen(navController = navController, authViewModel = authViewModel, token = token)
            }
        }

        composable(Screen.AddCourse.route) {
            AddCourseScreen(navController = navController)
        }
        composable(
            route = Screen.AnnouncementList.route,
            arguments = listOf(
                navArgument("courseId") { type = NavType.StringType },
                navArgument("userRole") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId")
            val userRoleString = backStackEntry.arguments?.getString("userRole")
            val userRole = when (userRoleString) {
                UserRole.STUDENT.name -> UserRole.STUDENT
                UserRole.LECTURER.name -> UserRole.LECTURER
                UserRole.ADMIN.name -> UserRole.ADMIN
                else -> null
            }
            if (courseId != null && userRole != null) {
                AnnouncementScreen(
                    navController = navController,
                    announcementViewModel = announcementViewModel,
                    courseId = courseId,
                    userRole = userRole.name
                )
            }
        }
        composable(
            route = Screen.AddAnnouncement.route,
            arguments = listOf(navArgument("courseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId")
            if (courseId != null) {
                AddAnnouncementScreen(navController = navController, courseId = courseId)
            }
        }
        composable(
            route = Screen.AnnouncementDetail.route,
            arguments = listOf(navArgument("announcementId") { type = NavType.StringType })
        ) { backStackEntry ->
            val announcementId = backStackEntry.arguments?.getString("announcementId")
            if (announcementId != null) {
                AnnouncementDetailScreen(
                    navController = navController,
                    announcementId = announcementId,
                    viewModel = announcementViewModel
                )
            }
        }
        composable(Screen.StudentHome.route) {
            StudentHomeScreen(navController = navController, dashboardViewModel = dashboardViewModel)
        }
        composable(Screen.LecturerHome.route) {
            LecturerHomeScreen(
                navController = navController,
                courseViewModel = courseViewModel,
                dashboardViewModel = dashboardViewModel
            )
        }
        composable(
            route = Screen.CoursesScreen.route,
            arguments = listOf(
                navArgument("departmentName") {
                    type = NavType.StringType
                },
                navArgument("levelLevel") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val departmentName = backStackEntry.arguments?.getString("departmentName") ?: ""
            val levelLevel = backStackEntry.arguments?.getString("levelLevel") ?: ""
            val viewModel: CourseViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            CoursesScreen(
                navController = navController,
                departmentName = departmentName,
                levelLevel = levelLevel,
                uiState = uiState,
                viewModel = viewModel
            )
        }
        composable(
            route = Screen.CourseDetailsScreen.route,
            arguments = listOf(
                navArgument("courseId") {

                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getInt("courseId")
            if (courseId != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    CourseDetailsScreen(navController = navController, courseId = courseId)
                } else {
                    CourseDetailsScreenCompat(navController = navController, courseId = courseId)
                }
            }
        }
        composable(
            route = Screen.CourseContentDetailsScreen.route,
            arguments = listOf(
                navArgument("courseId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getInt("courseId")
            if (courseId != null) {
                CourseContentDetailsScreen(navController = navController, courseId = courseId)
            }
        }
        composable(
            route = Screen.Profile.route,
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            if (userId != null) {
                ProfileScreen(navController = navController, userId = userId)
            }
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
        composable(Screen.Notifications.route) {
            NotificationsScreen(navController = navController)
        }
        composable(Screen.Search.route) {
            SearchScreen(navController = navController)
        }
        composable(Screen.Calendar.route) {
            CalendarScreen(navController = navController)
        }
        composable(Screen.DepartmentList.route) {
            DepartmentListScreen(navController = navController)
        }
        composable(
            route = Screen.LevelList.route,
            arguments = listOf(
                navArgument("departmentName") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val departmentName = backStackEntry.arguments?.getString("departmentName") ?: ""
            LevelListScreen(navController = navController, departmentName = departmentName)
        }
        composable(Screen.MyCourses.route) {
            val courseViewModel: CourseViewModel = hiltViewModel()
            val uiState by courseViewModel.myCoursesUiState.collectAsState()
            MyCoursesScreen(navController = navController, uiState = uiState)
        }
        composable(
            route = Screen.AssignmentList.route,
            arguments = listOf(
                navArgument("courseId") { type = NavType.StringType },
                navArgument("userRole") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId")
            val userRoleString = backStackEntry.arguments?.getString("userRole")
            val userRole = when (userRoleString) {
                UserRole.STUDENT.name -> UserRole.STUDENT
                UserRole.LECTURER.name -> UserRole.LECTURER
                UserRole.ADMIN.name -> UserRole.ADMIN
                else -> null
            }
            if (courseId != null && userRole != null) {
                AssignmentScreen(
                    navController = navController,
                    assignmentViewModel = assignmentViewModel,
                    courseId = courseId,
                    userRole = userRole.name
                )
            }
        }
        composable(
            route = Screen.AddAssignment.route,
            arguments = listOf(navArgument("courseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId")
            if (courseId != null) {
                AddAssignmentScreen(navController = navController, courseId = courseId)
            }
        }
        composable(
            route = Screen.AssignmentDetail.route,
            arguments = listOf(navArgument("assignmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val assignmentId = backStackEntry.arguments?.getString("assignmentId")
            if (assignmentId != null) {
                AssignmentDetailScreen(
                    navController = navController,
                    assignmentId = assignmentId,
                    viewModel = assignmentViewModel
                )
            }
        }
        composable(Screen.CourseList.route) {
            CourseListScreen(navController = navController)
        }
        composable(Screen.Communication.route) {
            CommunicationScreen(navController = navController, viewModel = communicationViewModel)
        }
        composable(Screen.ChatList.route) {
            ChatListScreen(navController = navController)
        }
        composable(
            "chatDetail/{chatId}",
            arguments = listOf(navArgument("chatId") { type = NavType.LongType })
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getLong("chatId") ?: 0L
            ChatDetailScreen(chatId = chatId, navController = navController)
        }
    }
}