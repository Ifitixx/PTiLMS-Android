package com.pizzy.ptilms.data.repository

import com.pizzy.ptilms.data.model.ForgotPasswordRequest
import com.pizzy.ptilms.data.model.ForgotPasswordResponse
import com.pizzy.ptilms.data.model.LoginRequest
import com.pizzy.ptilms.data.model.LoginResponse
import com.pizzy.ptilms.data.model.RegisterRequest
import com.pizzy.ptilms.data.model.RegisterResponse
import com.pizzy.ptilms.data.model.ResetPasswordRequest
import com.pizzy.ptilms.data.model.ResetPasswordResponse
import com.pizzy.ptilms.network.LoginRequest
import com.pizzy.ptilms.network.RegisterRequest
import com.pizzy.ptilms.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(loginRequest: LoginRequest): Flow<Resource<LoginResponse>>
    fun register(registerRequest: RegisterRequest): Flow<Resource<RegisterResponse>>
    fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest): Flow<Resource<ForgotPasswordResponse>>
    fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Flow<Resource<ResetPasswordResponse>>
}