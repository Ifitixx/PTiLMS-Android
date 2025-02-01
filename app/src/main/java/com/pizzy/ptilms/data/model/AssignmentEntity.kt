package com.pizzy.ptilms.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "assignments")
data class AssignmentEntity(
    @PrimaryKey
    val assignmentId: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val dueDate: Date,
    val courseId: String
)