package com.pizzy.ptilms.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.ui.graphics.vector.ImageVector

// Screen sealed class (Navigation Destinations)
sealed class Screen(val route: String, val title: String? = null, val icon: ImageVector? = null) {
    object Welcome : Screen("welcome_screen", "Welcome", Icons.Filled.Home)
    object Login : Screen("login_screen", "Login", Icons.AutoMirrored.Filled.Login)
    object Logout : Screen("logout_screen", "Logout", Icons.AutoMirrored.Filled.Logout)
    object Auth : Screen("auth_screen", "Auth", Icons.Filled.VerifiedUser)
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Filled.Dashboard)
    object ForgotPassword : Screen("forgot_password_screen", "Forgot Password", Icons.Filled.Lock)
    object EditPassword : Screen("edit_password_screen/{token}", "Edit Password", Icons.Filled.Edit) {
        fun createRoute(token: String): String = "edit_password_screen/$token"
    }
    object AddCourse : Screen("add_course_screen", "Add Course", Icons.Filled.Add)

    object AnnouncementList : Screen("announcement_list/{courseId}/{userRole}", "Announcements") {
        fun createRoute(courseId: String, userRole: String): String =
            "announcement_list/$courseId/$userRole"
    }

    object AssignmentList : Screen("assignment_list/{courseId}/{userRole}", "Assignments") {
        fun createRoute(courseId: String, userRole: String): String =
            "assignment_list/$courseId/$userRole"
    }

    object AddAnnouncement : Screen("add_announcement/{courseId}", "Add Announcement") {
        fun createRoute(courseId: String): String = "add_announcement/$courseId"
    }

    object AnnouncementDetail :
        Screen("announcement_detail/{announcementId}", "Announcement Detail") {
        fun createRoute(announcementId: String): String =
            "announcement_detail/$announcementId"
    }

    object AddAssignment : Screen("add_assignment/{courseId}", "Add Assignment") {
        fun createRoute(courseId: String): String = "add_assignment/$courseId"
    }

    object AssignmentDetail : Screen("assignment_detail/{assignmentId}", "Assignment Detail") {
        fun createRoute(assignmentId: String): String = "assignment_detail/$assignmentId"
    }

    object StudentHome : Screen("student_home", "Student Home", Icons.Filled.Home)
    object LecturerHome : Screen("lecturer_home", "Lecturer Home", Icons.Filled.Home)
    object CoursesScreen : Screen("courses/{departmentName}/{levelLevel}") {
        fun createRoute(departmentName: String, levelLevel: String): String =
            "courses/$departmentName/$levelLevel"
    }

    object CourseDetailsScreen : Screen("course_details/{courseId}", "Course Details") {
        fun createRoute(courseId: Int): String = "course_details/$courseId"
    }

    object CourseContentDetailsScreen :
        Screen(
            "course_content_details_screen?courseId={courseId}",
            "Course Content Details",
            Icons.Filled.Info
        ) {
        fun createRoute(courseId: Int): String = "course_content_details_screen?courseId=$courseId"
    }

    object Profile : Screen("profile_screen?userId={userId}", "Profile", Icons.Filled.Person) {
        fun createRoute(userId: String): String = "profile_screen?userId=$userId"
    }

    object MyCourses : Screen("my_courses_screen", "My Courses", Icons.Filled.Book)
    object Settings : Screen("settings_screen", "Settings", Icons.Filled.Settings)
    object Notifications : Screen("notifications_screen", "Notifications", Icons.Filled.Notifications)
    object Search : Screen("search_screen", "Search", Icons.Filled.Search)
    object Calendar : Screen("calendar_screen", "Calendar", Icons.Filled.CalendarToday)

    object DepartmentList :
        Screen("department_list", "Departments", Icons.AutoMirrored.Filled.List)

    object LevelList : Screen("level_list/{departmentName}", "Levels", Icons.Filled.DateRange) {
        fun createRoute(departmentName: String): String = "level_list/$departmentName"
    }

    object Communication : Screen("communication_screen", "Communication", Icons.Filled.Email)
    object PdfViewer : Screen("pdf_viewer_screen", "Pdf Viewer")
    object CourseList : Screen("course_list_screen", "Course List", Icons.Filled.Book)
    object ChatList : Screen("chatList", "Chat List")
    object ChatDetail : Screen("chatDetail/{chatId}") {
        fun createRoute(chatId: Long): String = "chatDetail/$chatId"
    }
}