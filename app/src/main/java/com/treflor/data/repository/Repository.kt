package com.treflor.data.repository

import androidx.lifecycle.LiveData
import com.treflor.internal.AuthState
import com.treflor.models.User

interface Repository {

    fun signInWithGoogle(accessToken: String)
    fun signIn(email: String,password:String)
    suspend fun getUser(): LiveData<User>
}