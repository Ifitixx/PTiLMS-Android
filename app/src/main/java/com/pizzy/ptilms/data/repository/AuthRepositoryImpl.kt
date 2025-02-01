package com.pizzy.ptilms.data.repository

import com.pizzy.ptilms.data.model.ForgotPasswordRequest
import com.pizzy.ptilms.data.model.ForgotPasswordResponse
import com.pizzy.ptilms.data.model.LoginRequest
import com.pizzy.ptilms.data.model.LoginResponse
import com.pizzy.ptilms.data.model.RegisterRequest
import com.pizzy.ptilms.data.model.RegisterResponse
import com.pizzy.ptilms.data.model.ResetPasswordRequest
import com.pizzy.ptilms.data.model.ResetPasswordResponse
import com.pizzy.ptilms.data.remote.AuthApiService
import com.pizzy.ptilms.network.AuthApiService
import com.pizzy.ptilms.network.LoginRequest
import com.pizzy.ptilms.network.RegisterRequest
import com.pizzy.ptilms.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService
) : AuthRepository {
    override fun login(loginRequest: LoginRequest): Flow<Resource<LoginResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = authApiService.login(loginRequest)
            if (response.isSuccessful) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("An error occurred: ${response.message()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("An HTTP error occurred: ${e.message()}"))
        } catch (e: IOException) {
            emit(Resource.Error("Network error: ${e.message}"))
        } catch (e: Exception) {
            emit(Resource.Error("An unexpected error occurred: ${e.message}"))
        }
    }

    override fun register(registerRequest: RegisterRequest): Flow<Resource<RegisterResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = authApiService.register(registerRequest)
            if (response.isSuccessful) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("An error occurred: ${response.message()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("An HTTP error occurred: ${e.message()}"))
        } catch (e: IOException) {
            emit(Resource.Error("Network error: ${e.message}"))
        } catch (e: Exception) {
            emit(Resource.Error("An unexpected error occurred: ${e.message}"))
        }
    }

    override fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest): Flow<Resource<ForgotPasswordResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = authApiService.forgotPassword(forgotPasswordRequest)
            if (response.isSuccessful) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("An error occurred: ${response.message()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("An HTTP error occurred: ${e.message()}"))
        } catch (e: IOException) {
            emit(Resource.Error("Network error: ${e.message}"))
        } catch (e: Exception) {
            emit(Resource.Error("An unexpected error occurred: ${e.message}"))
        }
    }

    override fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Flow<Resource<ResetPasswordResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = authApiService.resetPassword(resetPasswordRequest)
            if (response.isSuccessful) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("An error occurred: ${response.message()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("An HTTP error occurred: ${e.message()}"))
        } catch (e: IOException) {
            emit(Resource.Error("Network error: ${e.message}"))
        } catch (e: Exception) {
            emit(Resource.Error("An unexpected error occurred: ${e.message}"))
        }
    }
}