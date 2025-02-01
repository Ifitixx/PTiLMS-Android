package com.pizzy.ptilms.data.repository

import com.pizzy.ptilms.data.model.AnnouncementEntity
import com.pizzy.ptilms.data.model.ChatEntity
import com.pizzy.ptilms.data.model.ChatMessageEntity
import com.pizzy.ptilms.data.model.CommunicationType
import com.pizzy.ptilms.data.model.UserEntity
import com.pizzy.ptilms.viewmodel.UiState
import kotlinx.coroutines.flow.Flow

interface CommunicationRepository {
    fun getChatMessages(chatId: Long): Flow<UiState<List<ChatMessageEntity>>>
    suspend fun insertChatMessage(chatMessage: ChatMessageEntity)
    suspend fun updateChatMessage(chatMessage: ChatMessageEntity)
    suspend fun deleteChatMessage(chatMessage: ChatMessageEntity)
    fun getChatsByType(communicationType: CommunicationType): Flow<UiState<List<ChatEntity>>>
    suspend fun insertChat(chat: ChatEntity)
    suspend fun updateChat(chat: ChatEntity)
    suspend fun deleteChat(chat: ChatEntity)
    fun getAllUsers(): Flow<UiState<List<UserEntity>>>
    fun getAnnouncementsForCourse(courseId: String): Flow<UiState<List<AnnouncementEntity>>>
    suspend fun insertUser(user: UserEntity)
}