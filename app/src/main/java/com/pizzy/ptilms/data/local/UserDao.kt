package com.pizzy.ptilms.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pizzy.ptilms.data.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE user_id = :userId")
    suspend fun getUserByUserId(userId: String): UserEntity?

    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE reset_token = :token")
    suspend fun getUserByResetToken(token: String): UserEntity?

    @Query("SELECT EXISTS(SELECT * FROM users WHERE username = :username)")
    suspend fun usernameExists(username: String): Boolean

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("UPDATE users SET password = :newPassword, reset_token = null, reset_token_expiry = null WHERE user_id = :userId")
    suspend fun updatePassword(userId: String, newPassword: String)

    @Query("UPDATE users SET reset_token = :token, reset_token_expiry = :expiry WHERE email = :email")
    suspend fun updateResetToken(email: String, token: String, expiry: Long)

    @Query("UPDATE users SET reset_token = null, reset_token_expiry = null WHERE user_id = :userId")
    suspend fun clearResetToken(userId: String)
}