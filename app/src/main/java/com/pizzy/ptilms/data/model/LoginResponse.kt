package com.pizzy.ptilms.data.model

data class LoginResponse(
    val message: String,
    val token: String? = null,
    val userId: String? = null,
    val error: String? = null
)