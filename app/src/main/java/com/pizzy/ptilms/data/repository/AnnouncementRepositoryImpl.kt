package com.pizzy.ptilms.data.repository

import com.pizzy.ptilms.data.local.AnnouncementDao
import com.pizzy.ptilms.data.model.AnnouncementEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AnnouncementRepositoryImpl @Inject constructor(
    private val announcementDao: AnnouncementDao
) : AnnouncementRepository {
    override fun getAnnouncements(): Flow<List<AnnouncementEntity>> {
        return announcementDao.getAnnouncements()
    }

    override fun getAnnouncementsByCourseId(courseId: String): Flow<List<AnnouncementEntity>> {
        return announcementDao.getAnnouncementsByCourseId(courseId)
    }

    override fun getRecentAnnouncements(limit: Int): Flow<List<AnnouncementEntity>> {
        return announcementDao.getRecentAnnouncements(limit)
    }

    override fun getAnnouncementById(announcementId: String): Flow<AnnouncementEntity?> {
        return announcementDao.getAnnouncementById(announcementId)
    }

    override suspend fun addAnnouncement(announcement: AnnouncementEntity) {
        announcementDao.insertAnnouncement(announcement)
    }

    override suspend fun deleteAnnouncement(announcement: AnnouncementEntity) {
        announcementDao.deleteAnnouncement(announcement)
    }

    override suspend fun updateAnnouncement(announcement: AnnouncementEntity) {
        announcementDao.updateAnnouncement(announcement)
    }
}