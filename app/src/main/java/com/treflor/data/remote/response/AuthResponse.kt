package com.treflor.data.remote.response

data class AuthResponse(
    val success: Boolean,
    val token: String,
    val message: String
)