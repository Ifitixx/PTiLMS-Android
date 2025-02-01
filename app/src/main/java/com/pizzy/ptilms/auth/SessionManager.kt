package com.pizzy.ptilms.auth

import com.pizzy.ptilms.data.DataStoreManager
import com.pizzy.ptilms.data.model.UserRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {
    val authState: Flow<AuthState> = combine(
        dataStoreManager.isLoggedIn,
        dataStoreManager.userRole
    ) { isLoggedIn, role ->
        when {
            !isLoggedIn -> AuthState.Unauthenticated
            role == null -> AuthState.Unauthenticated
            else -> AuthState.Authenticated(
                role = UserRole.valueOf(role.uppercase())
            )
        }
    }.distinctUntilChanged()
}