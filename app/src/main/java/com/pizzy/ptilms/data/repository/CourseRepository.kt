package com.pizzy.ptilms.data.repository

import com.pizzy.ptilms.data.model.CourseEntity
import com.pizzy.ptilms.data.model.CourseWithDepartment
import com.pizzy.ptilms.data.model.CourseWithDepartmentAndLevel
import kotlinx.coroutines.flow.Flow

interface CourseRepository {
    fun getCoursesByDepartmentAndLevel(departmentId: Int, levelId: Int): Flow<List<CourseWithDepartmentAndLevel>>
    suspend fun insertCourse(course: CourseEntity): Long
    suspend fun insertAll(courses: List<CourseEntity>)
    suspend fun updateCourse(course: CourseEntity)
    suspend fun deleteCourse(course: CourseEntity)
    suspend fun getCourseByName(courseName: String): CourseEntity?
    suspend fun updatePdfPath(courseId: Int, pdfPath: String)
    suspend fun updateImagePath(courseId: Int, imagePath: String)
    suspend fun updateVideoPath(courseId: Int, videoPath: String)
    suspend fun getCourseById(courseId: Int): CourseEntity?
    fun getAllCourses(): Flow<List<CourseEntity>>
    fun getMyCourses(userId: String): Flow<List<CourseWithDepartment>> // Changed to String
    suspend fun grantAccessToCourse(userId: String, courseId: Int) // Changed to String
    suspend fun revokeAccessToCourse(userId: String, courseId: Int) // Changed to String
    suspend fun getAllCoursesWithDepartment(): List<CourseWithDepartmentAndLevel>
    fun getCoursesByLecturer(lecturerId: Long): Flow<List<CourseWithDepartmentAndLevel>>
    suspend fun getCoursesByStudent(studentId: String): List<CourseWithDepartment> // Changed to String
    suspend fun getAllCoursesWithDepartmentOnly(): List<CourseWithDepartment>
    fun getCourseWithDepartmentById(courseId: Int): Flow<CourseWithDepartment?>
}