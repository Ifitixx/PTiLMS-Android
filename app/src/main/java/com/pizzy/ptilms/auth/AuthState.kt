package com.pizzy.ptilms.auth

import com.pizzy.ptilms.data.model.UserRole

sealed class AuthState {
    object Unauthenticated : AuthState()
    data class Authenticated(val role: UserRole) : AuthState()
}