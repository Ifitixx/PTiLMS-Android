package com.pizzy.ptilms.auth

data class ForgotPasswordFormState(
    val email: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isEmailSent: Boolean = false
)