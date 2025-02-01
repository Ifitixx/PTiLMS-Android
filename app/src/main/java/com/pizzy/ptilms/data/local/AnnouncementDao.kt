package com.pizzy.ptilms.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pizzy.ptilms.data.model.AnnouncementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnnouncementDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncement(announcement: AnnouncementEntity)

    @Query("SELECT * FROM announcements WHERE courseId = :courseId ORDER BY timestamp DESC")
    fun getAnnouncementsByCourseId(courseId: String): Flow<List<AnnouncementEntity>>

    @Query("SELECT * FROM announcements ORDER BY timestamp DESC")
    fun getAnnouncements(): Flow<List<AnnouncementEntity>>

    @Query("SELECT * FROM announcements WHERE announcementId = :announcementId")
    fun getAnnouncementById(announcementId: String): Flow<AnnouncementEntity?>

    @Delete
    suspend fun deleteAnnouncement(announcement: AnnouncementEntity)

    @Update
    suspend fun updateAnnouncement(announcement: AnnouncementEntity)

    @Query("SELECT * FROM announcements ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentAnnouncements(limit: Int): Flow<List<AnnouncementEntity>>
}