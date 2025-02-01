package com.pizzy.ptilms.data.repository

import com.pizzy.ptilms.data.local.CourseDao
import com.pizzy.ptilms.data.model.CourseEntity
import com.pizzy.ptilms.data.model.CourseWithDepartment
import com.pizzy.ptilms.data.model.CourseWithDepartmentAndLevel
import com.pizzy.ptilms.data.model.UserCourseCrossRef
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CourseRepositoryImpl @Inject constructor(
    private val courseDao: CourseDao
) : CourseRepository {
    override fun getCoursesByDepartmentAndLevel(
        departmentId: Int,
        levelId: Int
    ): Flow<List<CourseWithDepartmentAndLevel>> {
        return courseDao.getCoursesByDepartmentAndLevel(departmentId, levelId)
    }

    override suspend fun insertCourse(course: CourseEntity): Long {
        return courseDao.insertCourse(course)
    }

    override suspend fun insertAll(courses: List<CourseEntity>) {
        courseDao.insertAll(courses)
    }

    override suspend fun updateCourse(course: CourseEntity) {
        courseDao.updateCourse(course)
    }

    override suspend fun deleteCourse(course: CourseEntity) {
        courseDao.deleteCourse(course)
    }

    override suspend fun getCourseByName(courseName: String): CourseEntity? {
        return courseDao.getCourseByName(courseName)
    }

    override suspend fun updatePdfPath(courseId: Int, pdfPath: String) {
        courseDao.updatePdfPath(courseId, pdfPath)
    }

    override suspend fun updateImagePath(courseId: Int, imagePath: String) {
        courseDao.updateImagePath(courseId, imagePath)
    }

    override suspend fun updateVideoPath(courseId: Int, videoPath: String) {
        courseDao.updateVideoPath(courseId, videoPath)
    }

    override suspend fun getCourseById(courseId: Int): CourseEntity? {
        return courseDao.getCourseById(courseId)
    }

    override fun getAllCourses(): Flow<List<CourseEntity>> {
        return courseDao.getAllCourses()
    }

    override fun getMyCourses(userId: String): Flow<List<CourseWithDepartment>> { // Changed to String
        return courseDao.getMyCourses(userId)
    }

    override suspend fun grantAccessToCourse(userId: String, courseId: Int) { // Changed to String
        courseDao.insertUserCourseCrossRef(UserCourseCrossRef(userId, courseId)) // Updated call
    }

    override suspend fun revokeAccessToCourse(userId: String, courseId: Int) { // Changed to String
        courseDao.deleteUserCourseCrossRef(UserCourseCrossRef(userId, courseId)) // Updated call
    }

    override suspend fun getAllCoursesWithDepartment(): List<CourseWithDepartmentAndLevel> {
        return courseDao.getAllCoursesWithDepartment()
    }

    override fun getCoursesByLecturer(lecturerId: Long): Flow<List<CourseWithDepartmentAndLevel>> {
        return courseDao.getCoursesByLecturer(lecturerId)
    }

    override suspend fun getCoursesByStudent(studentId: String): List<CourseWithDepartment> { // Changed to String
        return courseDao.getCoursesByStudent(studentId)
    }

    override suspend fun getAllCoursesWithDepartmentOnly(): List<CourseWithDepartment> {
        return courseDao.getAllCoursesWithDepartmentOnly()
    }

    override fun getCourseWithDepartmentById(courseId: Int): Flow<CourseWithDepartment?> = flow {
        emit(courseDao.getCourseWithDepartmentById(courseId))
    }
}