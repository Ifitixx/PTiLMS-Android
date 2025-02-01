package com.pizzy.ptilms.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "announcements")
data class AnnouncementEntity(
    @PrimaryKey
    val announcementId: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val lecturerId: String,
    val lecturerUsername: String,
    val timestamp: Long,
    val courseId: String
)