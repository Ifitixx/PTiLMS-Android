package com.pizzy.ptilms.network

import com.pizzy.ptilms.data.model.LoginResponse
import com.pizzy.ptilms.data.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/register")
    suspend fun registerUser(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("auth/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body request: Map<String, String>): Response<Void>

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body request: PasswordResetRequest): Response<Void>
}