package com.pizzy.ptilms.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pizzy.ptilms.data.model.UserRole
import com.pizzy.ptilms.navigation.Screen
import com.pizzy.ptilms.viewmodel.DashboardViewModel
import kotlinx.coroutines.delay

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: DashboardViewModel
) {
    val userRoleString by viewModel.userRole.collectAsState(initial = null)
    var showNavigationBar by remember { mutableStateOf(false) }

    // Use LaunchedEffect to delay rendering until userRole is available
    LaunchedEffect(userRoleString) {
        delay(100) // Adjust delay if needed
        showNavigationBar = true
    }

    // Determine if the user is a lecturer based on the userRole
    val isLecturer = userRoleString?.uppercase() == UserRole.LECTURER.name

    // Only render NavigationBar if showNavigationBar is true
    if (showNavigationBar) {
        val items = if (isLecturer) {
            listOf(
                BottomNavigationItem(
                    title = "Home",
                    selectedIcon = Icons.Filled.Home,
                    unselectedIcon = Icons.Filled.Home,
                    hasNews = false,
                    route = Screen.LecturerHome.route
                ),
                BottomNavigationItem(
                    title = "Search",
                    selectedIcon = Icons.Filled.Search,
                    unselectedIcon = Icons.Filled.Search,
                    hasNews = false,
                    route = Screen.Search.route
                ),
                BottomNavigationItem(
                    title = "Profile",
                    selectedIcon = Icons.Filled.Person,
                    unselectedIcon = Icons.Filled.Person,
                    hasNews = false,
                    route = Screen.Profile.route
                ),
                BottomNavigationItem(
                    title = "Notifications",
                    selectedIcon = Icons.Filled.Notifications,
                    unselectedIcon = Icons.Filled.Notifications,
                    hasNews = true,
                    route = Screen.Notifications.route
                )
            )
        } else {
            listOf(
                BottomNavigationItem(
                    title = "Home",
                    selectedIcon = Icons.Filled.Home,
                    unselectedIcon = Icons.Filled.Home,
                    hasNews = false,
                    route = Screen.StudentHome.route
                ),
                BottomNavigationItem(
                    title = "Search",
                    selectedIcon = Icons.Filled.Search,
                    unselectedIcon = Icons.Filled.Search,
                    hasNews = false,
                    route = Screen.Search.route
                ),
                BottomNavigationItem(
                    title = "Profile",
                    selectedIcon = Icons.Filled.Person,
                    unselectedIcon = Icons.Filled.Person,
                    hasNews = false,
                    route = Screen.Profile.route
                ),
                BottomNavigationItem(
                    title = "Notifications",
                    selectedIcon = Icons.Filled.Notifications,
                    unselectedIcon = Icons.Filled.Notifications,
                    hasNews = true,
                    route = Screen.Notifications.route
                )
            )
        }
        Column(modifier = modifier.fillMaxWidth()) {
            HorizontalDivider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth()
            )
            NavigationBar(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 0.dp, // Remove the elevation/shadow
                containerColor = Color.White // Set the background color to white
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { item ->
                    NavigationBarItem(
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (item.hasNews) {
                                        Badge(
                                            modifier = Modifier.padding(4.dp),
                                            containerColor = MaterialTheme.colorScheme.primary // Changed to primary
                                        )
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = item.selectedIcon,
                                    contentDescription = item.title,
                                )
                            }
                        },
                        label = { Text(text = item.title) },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                navController.graph.startDestinationRoute?.let { screenRoute ->
                                    popUpTo(screenRoute) {
                                        saveState = true
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    }
}