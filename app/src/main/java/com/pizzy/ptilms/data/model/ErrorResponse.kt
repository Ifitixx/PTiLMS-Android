package com.pizzy.ptilms.data.model

data class ErrorResponse(
    val message: String,
    val error: String? = null
)