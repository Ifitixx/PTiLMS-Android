package com.pizzy.ptilms.network

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token") val token: String,
    @SerializedName("user") val user: User
)

data class RegisterResponse(
    val userId: String
)