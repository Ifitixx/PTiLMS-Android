package com.pizzy.ptilms.ui.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.pizzy.ptilms.navigation.Screen

data class DashboardGridItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

fun getDashboardGridItems(userRole: String?): List<DashboardGridItem> {
    return when (userRole?.lowercase()) {
        "student" -> listOf(
            DashboardGridItem("My Courses", Icons.Filled.Book, Screen.MyCourses.route),
            DashboardGridItem("Calendar", Icons.Filled.CalendarMonth, Screen.Calendar.route),
            DashboardGridItem("Search", Icons.Filled.Search, Screen.Search.route),
            DashboardGridItem("Notifications", Icons.Filled.Notifications, Screen.Notifications.route),
            DashboardGridItem("Profile", Icons.Filled.Person, Screen.Profile.route),
            DashboardGridItem("Settings", Icons.Filled.Settings, Screen.Settings.route),
            // Add more student-specific items here if needed
        )
        "lecturer" -> listOf(
            DashboardGridItem("My Courses", Icons.Filled.Book, Screen.MyCourses.route),
            DashboardGridItem("Calendar", Icons.Filled.CalendarMonth, Screen.Calendar.route),
            DashboardGridItem("Search", Icons.Filled.Search, Screen.Search.route),
            DashboardGridItem("Settings", Icons.Filled.Settings, Screen.Settings.route),
            DashboardGridItem("Notifications", Icons.Filled.Notifications, Screen.Notifications.route),
            DashboardGridItem("Profile", Icons.Filled.Person, Screen.Profile.route),
            // Add more lecturer-specific items here if needed
        )
        else -> listOf(
            DashboardGridItem("My Courses", Icons.Filled.Book, Screen.MyCourses.route),
            DashboardGridItem("Calendar", Icons.Filled.CalendarMonth, Screen.Calendar.route),
            DashboardGridItem("Search", Icons.Filled.Search, Screen.Search.route),
            DashboardGridItem("Settings", Icons.Filled.Settings, Screen.Settings.route),
            DashboardGridItem("Notifications", Icons.Filled.Notifications, Screen.Notifications.route),
            DashboardGridItem("Profile", Icons.Filled.Person, Screen.Profile.route),
        ) // Default items if the role is not recognized
    }
}