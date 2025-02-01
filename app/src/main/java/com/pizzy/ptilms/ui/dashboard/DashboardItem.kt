package com.pizzy.ptilms.ui.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.graphics.vector.ImageVector
import com.pizzy.ptilms.navigation.Screen

data class DashboardItem(
    val title: String,
    val icon: ImageVector,
    val route: String,
    val isCourseItem: Boolean = false
)

fun getDashboardItems(userRole: String?): List<DashboardItem> {
    return when (userRole?.lowercase()) {
        "student" -> listOf(
            DashboardItem("Courses", Icons.Filled.Book, Screen.DepartmentList.route, isCourseItem = true),
            DashboardItem("Announcements", Icons.Filled.Notifications, Screen.AnnouncementList.route),
            DashboardItem("Assignments", Icons.AutoMirrored.Filled.List, Screen.AssignmentList.route),
            DashboardItem("Communication", Icons.Filled.Email, Screen.Communication.route),
            DashboardItem("PDF Viewer", Icons.Filled.Description, Screen.PdfViewer.route),
        )
        "lecturer" -> listOf(
            DashboardItem("Courses", Icons.Filled.Book, Screen.DepartmentList.route, isCourseItem = true),
            DashboardItem("Announcements", Icons.Filled.Notifications, Screen.AnnouncementList.route),
            DashboardItem("Assignments", Icons.AutoMirrored.Filled.List, Screen.AssignmentList.route),
            DashboardItem("Communication", Icons.Filled.Email, Screen.Communication.route),
            DashboardItem("PDF Viewer", Icons.Filled.Description, Screen.PdfViewer.route),
        )
        else -> emptyList()
    }
}