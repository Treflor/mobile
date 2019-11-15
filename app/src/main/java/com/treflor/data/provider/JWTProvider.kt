package com.treflor.data.provider

import androidx.lifecycle.LiveData
import com.treflor.internal.AuthState

interface JWTProvider {
    val authState: LiveData<AuthState>
    fun getJWT(): String?
    fun setJWT(token: String): Boolean
    fun unsetJWT(): Boolean
}