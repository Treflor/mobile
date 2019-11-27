package com.treflor.data.repository

import androidx.lifecycle.LiveData
import com.treflor.data.remote.requests.SignUpRequest
import com.treflor.internal.AuthState
import com.treflor.models.User

interface Repository {

    fun signInWithGoogle(accessToken: String)
    fun signIn(email: String, password: String)
    fun signUp(signUpRequest: SignUpRequest)
    suspend fun getUser(): LiveData<User>
}