package com.pizzy.ptilms.data.model

data class RegisterResponse(
    val message: String,
    val userId: String? = null,
    val error: String? = null
)
