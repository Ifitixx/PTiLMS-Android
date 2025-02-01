package com.pizzy.ptilms.data.repository

import com.pizzy.ptilms.data.model.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun updateUser(userEntity: UserEntity)
    suspend fun getUserByUserId(userId: String): UserEntity?
    suspend fun insertUser(user: UserEntity): Long
    fun getCurrentUser(): Flow<UserEntity?>
    suspend fun isUserLoggedIn(): Boolean
    suspend fun getUserByUsername(username: String): UserEntity?
    suspend fun usernameExists(username: String): Boolean
    suspend fun deleteUser(user: UserEntity)
    suspend fun updatePassword(userId: String, newPassword: String)
    suspend fun updateResetToken(email: String, token: String, expiry: Long)
    suspend fun getUserByResetToken(token: String): UserEntity?
    suspend fun getUserByEmail(email: String): UserEntity?
    suspend fun clearResetToken(userId: String)
    suspend fun getUserById(userId: String): UserEntity?
}