package com.pizzy.ptilms.auth

data class EditPasswordFormState(
    val newPassword: String = "",
    val confirmPassword: String = "",
    val errorMessage: String? = null,
)