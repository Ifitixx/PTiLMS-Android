package com.pizzy.ptilms.data.repository

import com.pizzy.ptilms.data.model.AnnouncementEntity
import kotlinx.coroutines.flow.Flow

interface AnnouncementRepository {
    fun getAnnouncements(): Flow<List<AnnouncementEntity>>
    fun getAnnouncementsByCourseId(courseId: String): Flow<List<AnnouncementEntity>>
    fun getRecentAnnouncements(limit: Int): Flow<List<AnnouncementEntity>>
    fun getAnnouncementById(announcementId: String): Flow<AnnouncementEntity?>
    suspend fun addAnnouncement(announcement: AnnouncementEntity)
    suspend fun deleteAnnouncement(announcement: AnnouncementEntity)
    suspend fun updateAnnouncement(announcement: AnnouncementEntity)
}