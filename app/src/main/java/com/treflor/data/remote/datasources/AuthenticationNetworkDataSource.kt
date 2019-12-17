package com.treflor.data.remote.datasources

import com.treflor.data.remote.requests.SignUpRequest

interface AuthenticationNetworkDataSource {
    suspend fun signInWithGoogle(idToken: String): String?
    suspend fun signIn(email: String, password: String): String?
    suspend fun signUp(signUpRequest: SignUpRequest): String?
}