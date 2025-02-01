package com.pizzy.ptilms.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "users",
    indices = [
        Index(value = ["email"], unique = true),
        Index(value = ["user_id"], unique = true),
        Index(value = ["username"], unique = true)
    ]
)
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val userId: String,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "password")
    val password: String,

    @ColumnInfo(name = "role")
    val role: UserRole,

    @ColumnInfo(name = "reset_token")
    val resetToken: String? = null,

    @ColumnInfo(name = "reset_token_expiry")
    val resetTokenExpiry: Long? = null,

    @ColumnInfo(name = "phone_number")
    val phoneNumber: String? = null,

    @ColumnInfo(name = "date_of_birth")
    val dateOfBirth: LocalDate? = null,

    @ColumnInfo(name = "sex")
    val sex: String? = null,

    @ColumnInfo(name = "profile_picture_url")
    val profilePictureUrl: String? = null
)