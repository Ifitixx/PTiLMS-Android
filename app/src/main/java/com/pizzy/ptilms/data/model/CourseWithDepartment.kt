package com.pizzy.ptilms.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class CourseWithDepartment(
    @Embedded val courseEntity: CourseEntity, // Kept as courseEntity
    @Relation(
        parentColumn = "departmentId",
        entityColumn = "id"
    )
    val departmentEntity: DepartmentEntity // Kept as departmentEntity
)