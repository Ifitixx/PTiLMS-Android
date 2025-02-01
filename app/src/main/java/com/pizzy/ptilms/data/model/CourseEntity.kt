package com.pizzy.ptilms.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey(autoGenerate = true) val courseId: Int = 0,
    @ColumnInfo(name = "courseTitle") val courseTitle: String,
    @ColumnInfo(name = "courseDescription") val courseDescription: String,
    @ColumnInfo(name = "courseCode") val courseCode: String,
    @ColumnInfo(name = "departmentId") val departmentId: Int,
    @ColumnInfo(name = "levelId") val levelId: Int,
    @ColumnInfo(name = "format") val format: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "lecturerId") val lecturerId: Long? = null,
    @ColumnInfo(name = "pdfPath") val pdfPath: String? = null,
    @ColumnInfo(name = "imagePath") val imagePath: String? = null,
    @ColumnInfo(name = "videoPath") val videoPath: String? = null,
    @ColumnInfo(name = "instructor") val instructor: String = "",
    @ColumnInfo(name = "units") val units: Int = 0,
    @ColumnInfo(name = "isDepartmental") val isDepartmental: Boolean = false
)