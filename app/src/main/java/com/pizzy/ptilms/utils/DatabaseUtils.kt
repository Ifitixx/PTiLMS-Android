package com.pizzy.ptilms.utils

import com.pizzy.ptilms.data.model.CourseEntity

fun createCourseEntity(
    departmentId: Int,
    levelId: Int,
    courseTitle: String,
    courseCode: String,
    format: String,
    courseDescription: String,
    pdfPath: String? = null,
    imagePath: String? = null,
    videoPath: String? = null
): CourseEntity {
    return CourseEntity(
        courseTitle = courseTitle,
        courseCode = courseCode,
        departmentId = departmentId,
        levelId = levelId,
        format = format,
        category = "", // Consider removing category if redundant
        courseDescription = courseDescription,
        lecturerId = null,
        pdfPath = pdfPath,
        imagePath = imagePath,
        videoPath = videoPath
    )
}