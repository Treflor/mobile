package com.treflor.data.remote.datasources

interface AuthenticationNetworkDataSource {
    suspend fun signInWithGoogle(accessToken: String): String?
}