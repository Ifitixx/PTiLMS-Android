package com.pizzy.ptilms.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pizzy.ptilms.auth.AuthViewModel
import com.pizzy.ptilms.data.model.UserRole
import com.pizzy.ptilms.navigation.Screen
import com.pizzy.ptilms.viewmodel.DashboardViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    viewModel: DashboardViewModel,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val userRoleString by viewModel.userRole.collectAsState(initial = null)
    val isLecturer = userRoleString?.uppercase() == UserRole.LECTURER.name
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
        ?: (if (isLecturer) Screen.LecturerHome.route else Screen.StudentHome.route)
    var selectedItem by remember { mutableStateOf(currentRoute) }

    ModalDrawerSheet(Modifier.width(250.dp)) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(12.dp))
            Text(
                "P-learner",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge
            )
            HorizontalDivider()

            NavigationDrawerItem(
                label = { Text("Home") },
                selected = selectedItem == (if (isLecturer) Screen.LecturerHome.route else Screen.StudentHome.route),
                icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                onClick = {
                    selectedItem =
                        if (isLecturer) Screen.LecturerHome.route else Screen.StudentHome.route
                    navController.navigate(if (isLecturer) Screen.LecturerHome.route else Screen.StudentHome.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                        restoreState = true
                    }
                    scope.launch { drawerState.close() }
                }
            )

            if (isLecturer) {
                NavigationDrawerItem(
                    label = { Text("Courses") },
                    selected = selectedItem == Screen.CoursesScreen.route,
                    icon = { Icon(Icons.Filled.Book, contentDescription = "Courses") },
                    onClick = {
                        selectedItem = Screen.CoursesScreen.route
                        navController.navigate(Screen.CoursesScreen.route) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                            restoreState = true
                        }
                        scope.launch { drawerState.close() }
                    }
                )
            } else {
                NavigationDrawerItem(
                    label = { Text("My Courses") },
                    selected = selectedItem == Screen.MyCourses.route,
                    icon = { Icon(Icons.Filled.Book, contentDescription = "My Courses") },
                    onClick = {
                        selectedItem = Screen.MyCourses.route
                        navController.navigate(Screen.MyCourses.route) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                            restoreState = true
                        }
                        scope.launch { drawerState.close() }
                    }
                )
            }

            NavigationDrawerItem(
                label = { Text("Profile") },
                selected = selectedItem == Screen.Profile.route,
                icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
                onClick = {
                    selectedItem = Screen.Profile.route
                    navController.navigate(Screen.Profile.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                        restoreState = true
                    }
                    scope.launch { drawerState.close() }
                }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            NavigationDrawerItem(
                label = { Text("Settings") },
                selected = selectedItem == Screen.Settings.route,
                icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
                onClick = {
                    selectedItem = Screen.Settings.route
                    navController.navigate(Screen.Settings.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                        restoreState = true
                    }
                    scope.launch { drawerState.close() }
                }
            )
            NavigationDrawerItem(
                label = { Text("Help and feedback") },
                selected = selectedItem == Screen.Notifications.route,
                icon = {
                    Icon(
                        Icons.AutoMirrored.Outlined.Help,
                        contentDescription = null
                    )
                },
                onClick = {
                    selectedItem = Screen.Notifications.route
                    navController.navigate(Screen.Notifications.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                        restoreState = true
                    }
                    scope.launch { drawerState.close() }
                }
            )
            Spacer(Modifier.height(12.dp))
            NavigationDrawerItem(
                label = { Text("Logout") },
                selected = false,
                icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout") },
                onClick = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Dashboard.route) {
                            inclusive = true
                        }
                    }
                    scope.launch { drawerState.close() }
                }
            )
        }
    }
}