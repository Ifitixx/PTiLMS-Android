package com.pizzy.ptilms.data.repository

import com.pizzy.ptilms.data.DataStoreManager
import com.pizzy.ptilms.data.local.UserDao
import com.pizzy.ptilms.data.model.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val dataStoreManager: DataStoreManager
) : UserRepository {

    override suspend fun updateUser(userEntity: UserEntity) {
        try {
            userDao.updateUser(userEntity)
            dataStoreManager.updateUsername(userEntity.username)
            dataStoreManager.updateUserId(userEntity.userId)
            dataStoreManager.updateUserRole(userEntity.role)
        } catch (e: Exception) {
            throw Exception("Failed to update user: ${e.localizedMessage}")
        }
    }

    override suspend fun getUserByUserId(userId: String): UserEntity? {
        return try {
            userDao.getUserByUserId(userId)
        } catch (e: Exception) {
            throw Exception("Failed to get user by ID: ${e.localizedMessage}")
        }
    }
    override suspend fun getUserById(userId: String): UserEntity? {
        return try {
            userDao.getUserByUserId(userId)
        } catch (e: Exception) {
            throw Exception("Failed to get user by ID: ${e.localizedMessage}")
        }
    }

    override suspend fun insertUser(user: UserEntity): Long {
        return try {
            val id = userDao.insert(user)
            dataStoreManager.updateUsername(user.username)
            dataStoreManager.updateUserId(user.userId)
            dataStoreManager.updateUserRole(user.role)
            id
        } catch (e: Exception) {
            throw Exception("Failed to insert user: ${e.localizedMessage}")
        }
    }

    override fun getCurrentUser(): Flow<UserEntity?> =
        dataStoreManager.userId.map { userId ->
            if (userId != null) {
                try {
                    userDao.getUserByUserId(userId)
                } catch (e: Exception) {
                    throw Exception("Failed to get current user: ${e.localizedMessage}")
                }
            } else {
                null
            }
        }

    override suspend fun isUserLoggedIn(): Boolean {
        return try {
            dataStoreManager.userId.firstOrNull() != null
        } catch (e: Exception) {
            throw Exception("Failed to check user login status: ${e.localizedMessage}")
        }
    }

    override suspend fun getUserByUsername(username: String): UserEntity? {
        return try {
            userDao.getUserByUsername(username)
        } catch (e: Exception) {
            throw Exception("Failed to get user by username: ${e.localizedMessage}")
        }
    }

    override suspend fun usernameExists(username: String): Boolean {
        return try {
            userDao.usernameExists(username)
        } catch (e: Exception) {
            throw Exception("Failed to check if username exists: ${e.localizedMessage}")
        }
    }

    override suspend fun deleteUser(user: UserEntity) {
        try {
            userDao.deleteUser(user)
            dataStoreManager.clearAll()
        } catch (e: Exception) {
            throw Exception("Failed to delete user: ${e.localizedMessage}")
        }
    }

    override suspend fun updatePassword(userId: String, newPassword: String) {
        try {
            userDao.updatePassword(userId, newPassword)
        } catch (e: Exception) {
            throw Exception("Failed to update password: ${e.localizedMessage}")
        }
    }

    override suspend fun updateResetToken(email: String, token: String, expiry: Long) {
        try {
            userDao.updateResetToken(email, token, expiry)
        } catch (e: Exception) {
            throw Exception("Failed to update reset token: ${e.localizedMessage}")
        }
    }

    override suspend fun getUserByResetToken(token: String): UserEntity? {
        return try {
            userDao.getUserByResetToken(token)
        } catch (e: Exception) {
            throw Exception("Failed to get user by reset token: ${e.localizedMessage}")
        }
    }

    override suspend fun getUserByEmail(email: String): UserEntity? {
        return try {
            userDao.getUserByEmail(email)
        } catch (e: Exception) {
            throw Exception("Failed to get user by email: ${e.localizedMessage}")
        }
    }

    override suspend fun clearResetToken(userId: String) {
        try {
            userDao.clearResetToken(userId)
        } catch (e: Exception) {
            throw Exception("Failed to clear reset token: ${e.localizedMessage}")
        }
    }
}