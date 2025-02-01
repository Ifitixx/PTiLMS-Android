package com.pizzy.ptilms.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class CourseWithDepartmentAndLevel(
    @Embedded val courseEntity: CourseEntity, // Kept as courseEntity
    @Relation(
        parentColumn = "departmentId",
        entityColumn = "id"
    )
    val departmentEntity: DepartmentEntity, // Kept as departmentEntity
    @Relation(
        parentColumn = "levelId",
        entityColumn = "id"
    )
    val levelEntity: LevelEntity // Kept as levelEntity
)