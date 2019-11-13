package com.treflor.data.remote

interface AuthenticationNetworkDataSource {
    suspend fun signInWithGoogle(accessToken: String): String?
}