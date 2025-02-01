package com.pizzy.ptilms.data.model

data class ForgotPasswordResponse(
    val message: String,
    val token: String? = null,
    val error: String? = null
)