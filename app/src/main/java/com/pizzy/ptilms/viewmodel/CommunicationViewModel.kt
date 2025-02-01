package com.pizzy.ptilms.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pizzy.ptilms.data.DataStoreManager
import com.pizzy.ptilms.data.model.AnnouncementEntity
import com.pizzy.ptilms.data.model.ChatEntity
import com.pizzy.ptilms.data.model.ChatMessageEntity
import com.pizzy.ptilms.data.model.CommunicationType
import com.pizzy.ptilms.data.model.UserEntity
import com.pizzy.ptilms.data.repository.CommunicationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunicationViewModel @Inject constructor(
    private val communicationRepository: CommunicationRepository,
    private val dataStoreManager: DataStoreManager // Inject DataStoreManager
) : ViewModel() {

    // Chat Messages
    private val _chatMessages = MutableStateFlow<UiState<List<ChatMessageEntity>>>(UiState.Empty)
    val chatMessages: StateFlow<UiState<List<ChatMessageEntity>>> = _chatMessages

    // Chats
    private val _chats = MutableStateFlow<UiState<List<ChatEntity>>>(UiState.Empty)
    val chats: StateFlow<UiState<List<ChatEntity>>> = _chats

    // Users
    private val _users = MutableStateFlow<UiState<List<UserEntity>>>(UiState.Empty)
    val users: StateFlow<UiState<List<UserEntity>>> = _users

    // Announcements
    private val _announcements = MutableStateFlow<UiState<List<AnnouncementEntity>>>(UiState.Empty)
    val announcements: StateFlow<UiState<List<AnnouncementEntity>>> = _announcements

    fun getChatMessages(chatId: Long) {
        viewModelScope.launch {
            communicationRepository.getChatMessages(chatId)
                .collectLatest { uiState ->
                    _chatMessages.value = uiState
                }
        }
    }

    fun insertChatMessage(chatMessage: ChatMessageEntity) {
        viewModelScope.launch {
            communicationRepository.insertChatMessage(chatMessage)
        }
    }

    fun updateChatMessage(chatMessage: ChatMessageEntity) {
        viewModelScope.launch {
            communicationRepository.updateChatMessage(chatMessage)
        }
    }

    fun deleteChatMessage(chatMessage: ChatMessageEntity) {
        viewModelScope.launch {
            communicationRepository.deleteChatMessage(chatMessage)
        }
    }

    fun getChatsByType(communicationType: CommunicationType) {
        viewModelScope.launch {
            communicationRepository.getChatsByType(communicationType)
                .collectLatest { uiState ->
                    _chats.value = uiState
                }
        }
    }

    fun insertChat(chat: ChatEntity) {
        viewModelScope.launch {
            communicationRepository.insertChat(chat)
        }
    }

    fun updateChat(chat: ChatEntity) {
        viewModelScope.launch {
            communicationRepository.updateChat(chat)
        }
    }

    fun deleteChat(chat: ChatEntity) {
        viewModelScope.launch {
            communicationRepository.deleteChat(chat)
        }
    }

    fun getAllUsers() {
        viewModelScope.launch {
            communicationRepository.getAllUsers().collectLatest { uiState ->
                _users.value = uiState
            }
        }
    }

    fun getAnnouncementsForCourse(courseId: String) {
        viewModelScope.launch {
            communicationRepository.getAnnouncementsForCourse(courseId).collectLatest { uiState ->
                _announcements.value = uiState
            }
        }
    }

    fun insertUser(user: UserEntity) {
        viewModelScope.launch {
            communicationRepository.insertUser(user)
        }
    }

    fun getUserId(): Flow<Long> {
        // Get the user ID from the DataStore
        return dataStoreManager.userId.map { it?.toLongOrNull() ?: 0L }
    }

    fun getUsername(): Flow<String> {
        // Get the username from the DataStore
        return dataStoreManager.username.map { it ?: "" }
    }
}