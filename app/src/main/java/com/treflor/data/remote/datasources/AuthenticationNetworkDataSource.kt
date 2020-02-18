package com.treflor.data.remote.datasources

import com.treflor.data.remote.requests.SignUpRequest
import com.treflor.internal.AuthState

interface AuthenticationNetworkDataSource {
    suspend fun signInWithGoogle(idToken: String): String?
    suspend fun signIn(email: String, password: String): Pair<String?, AuthState>
    suspend fun signUp(signUpRequest: SignUpRequest): String?
}