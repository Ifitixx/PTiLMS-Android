package com.pizzy.ptilms.data.repository

import com.pizzy.ptilms.data.local.AnnouncementDao
import com.pizzy.ptilms.data.local.ChatDao
import com.pizzy.ptilms.data.local.ChatMessageDao
import com.pizzy.ptilms.data.local.UserDao
import com.pizzy.ptilms.data.model.AnnouncementEntity
import com.pizzy.ptilms.data.model.ChatEntity
import com.pizzy.ptilms.data.model.ChatMessageEntity
import com.pizzy.ptilms.data.model.CommunicationType
import com.pizzy.ptilms.data.model.UserEntity
import com.pizzy.ptilms.viewmodel.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CommunicationRepositoryImpl @Inject constructor(
    private val chatDao: ChatDao,
    private val chatMessageDao: ChatMessageDao,
    private val userDao: UserDao,
    private val announcementDao: AnnouncementDao
) : CommunicationRepository {

    override fun getChatMessages(chatId: Long): Flow<UiState<List<ChatMessageEntity>>> =
        chatMessageDao.getChatMessages(chatId).map { UiState.Success(it) }

    override suspend fun insertChatMessage(chatMessage: ChatMessageEntity) {
        chatMessageDao.insertChatMessage(chatMessage)
    }

    override suspend fun updateChatMessage(chatMessage: ChatMessageEntity) {
        chatMessageDao.updateChatMessage(chatMessage)
    }

    override suspend fun deleteChatMessage(chatMessage: ChatMessageEntity) {
        chatMessageDao.deleteChatMessage(chatMessage)
    }

    override fun getChatsByType(communicationType: CommunicationType): Flow<UiState<List<ChatEntity>>> =
        chatDao.getChatsByType(communicationType).map { UiState.Success(it) }

    override suspend fun insertChat(chat: ChatEntity) {
        chatDao.insertChat(chat)
    }

    override suspend fun updateChat(chat: ChatEntity) {
        chatDao.updateChat(chat)
    }

    override suspend fun deleteChat(chat: ChatEntity) {
        chatDao.deleteChat(chat)
    }

    override fun getAllUsers(): Flow<UiState<List<UserEntity>>> =
        userDao.getAllUsers().map { UiState.Success(it) }

    override fun getAnnouncementsForCourse(courseId: String): Flow<UiState<List<AnnouncementEntity>>> =
        announcementDao.getAnnouncements().map { UiState.Success(it) }

    override suspend fun insertUser(user: UserEntity) {
        userDao.insert(user)
    }
}