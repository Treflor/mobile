package com.treflor.data.repository

import androidx.lifecycle.LiveData
import com.treflor.models.User

interface Repository {
    fun signInWithGoogle(accessToken: String)
    suspend fun getUser():LiveData<User>
}