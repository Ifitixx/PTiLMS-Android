package com.pizzy.ptilms.data.model

import androidx.room.Entity

@Entity(tableName = "department_level_cross_ref", primaryKeys = ["departmentId", "levelId"])
data class DepartmentLevelCrossRef(
    val departmentId: Int,
    val levelId: Int
)