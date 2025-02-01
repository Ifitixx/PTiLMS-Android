package com.pizzy.ptilms.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.pizzy.ptilms.data.model.CourseEntity
import com.pizzy.ptilms.data.model.CourseWithDepartment
import com.pizzy.ptilms.data.model.CourseWithDepartmentAndLevel
import com.pizzy.ptilms.data.model.UserCourseCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(course: CourseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: CourseEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(courses: List<CourseEntity>)

    @Update
    suspend fun updateCourse(course: CourseEntity)

    @Delete
    suspend fun deleteCourse(course: CourseEntity)

    @Query("SELECT * FROM courses WHERE courseTitle = :courseName")
    suspend fun getCourseByName(courseName: String): CourseEntity?

    @Query("UPDATE courses SET pdfPath = :pdfPath WHERE courseId = :courseId")
    suspend fun updatePdfPath(courseId: Int, pdfPath: String)

    @Query("UPDATE courses SET imagePath = :imagePath WHERE courseId = :courseId")
    suspend fun updateImagePath(courseId: Int, imagePath: String)

    @Query("UPDATE courses SET videoPath = :videoPath WHERE courseId = :courseId")
    suspend fun updateVideoPath(courseId: Int, videoPath: String)

    @Query("SELECT * FROM courses")
    fun getAllCourses(): Flow<List<CourseEntity>>

    @Query("SELECT * FROM courses WHERE courseId = :courseId")
    suspend fun getCourseById(courseId: Int): CourseEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserCourseCrossRef(userCourseCrossRef: UserCourseCrossRef)

    @Delete
    suspend fun deleteUserCourseCrossRef(userCourseCrossRef: UserCourseCrossRef)

    @Query("SELECT courseCode FROM courses")
    suspend fun getAllCourseCodes(): List<String>

    @Query("DELETE FROM courses")
    suspend fun clearAll()

    @Transaction
    @Query("""
        SELECT c.*, d.*, l.*
        FROM courses c
        INNER JOIN departments d ON c.departmentId = d.id
        INNER JOIN levels l ON c.levelId = l.id
        WHERE c.lecturerId = :lecturerId
    """)
    fun getCoursesByLecturer(lecturerId: Long): Flow<List<CourseWithDepartmentAndLevel>>

    @Transaction
    @Query("""
        SELECT c.*, d.*
        FROM courses c
        INNER JOIN departments d ON c.departmentId = d.id
        INNER JOIN user_course_cross_ref ucr ON c.courseId = ucr.courseId
        WHERE ucr.userId = :studentId
    """)
    suspend fun getCoursesByStudent(studentId: String): List<CourseWithDepartment>

    @Transaction
    @Query("""
        SELECT c.*, d.*, l.*
        FROM courses c
        INNER JOIN departments d ON c.departmentId = d.id
        INNER JOIN levels l ON c.levelId = l.id
        WHERE d.id = :departmentId AND l.id = :levelId
    """)
    fun getCoursesByDepartmentAndLevel(departmentId: Int, levelId: Int): Flow<List<CourseWithDepartmentAndLevel>>

    @Transaction
    @Query("""
        SELECT c.*, d.*, l.*
        FROM courses c
        INNER JOIN departments d ON c.departmentId = d.id
        INNER JOIN levels l ON c.levelId = l.id
    """)
    suspend fun getAllCoursesWithDepartment(): List<CourseWithDepartmentAndLevel>

    @Transaction
    @Query("""
        SELECT c.*, d.*
        FROM courses c
        INNER JOIN departments d ON c.departmentId = d.id
    """)
    suspend fun getAllCoursesWithDepartmentOnly(): List<CourseWithDepartment>

    @Transaction
    @Query("""
        SELECT c.*, d.*
        FROM courses c
        INNER JOIN departments d ON c.departmentId = d.id
        INNER JOIN user_course_cross_ref ucr ON c.courseId = ucr.courseId
        WHERE ucr.userId = :userId
    """)
    fun getMyCourses(userId: String): Flow<List<CourseWithDepartment>>

    @Transaction
    @Query("""
        SELECT c.*, d.*
        FROM courses c
        INNER JOIN departments d ON c.departmentId = d.id
        WHERE c.courseId = :courseId
    """)
    fun getCourseWithDepartmentById(courseId: Int): CourseWithDepartment?
}