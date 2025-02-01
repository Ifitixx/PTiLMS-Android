package com.pizzy.ptilms.network

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val role: String,
    val phoneNumber: String? = null,
    val dateOfBirth: String? = null,
    val sex: String? = null,
    val profilePictureUrl: String? = null
)

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class PasswordResetRequest(
    val email: String,
    val token: String? = null,
    val newPassword: String? = null
)