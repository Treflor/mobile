package com.treflor.data.provider

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.treflor.internal.AuthState

const val JWT_TOKEN = "JWT_TOKEN"

class JWTProviderImpl(context: Context) : PreferenceProvider(context), JWTProvider {

    private val _authState = MutableLiveData<AuthState>()
    override val authState: LiveData<AuthState> get() = _authState

    init {
        setAuthState(getJWT())
    }

    override fun getJWT(): String? {
        return preferences.getString(JWT_TOKEN, "")
    }

    override fun setJWT(token: String): Boolean {
        setAuthState(token)
        return preferences.edit().putString(JWT_TOKEN, token).commit()
    }

    override fun unsetJWT(): Boolean {
        setAuthState(null)
        return preferences.edit().remove(JWT_TOKEN).commit()
    }

    private fun setAuthState(jwt: String?) {
        if (jwt.isNullOrEmpty()) _authState.postValue(AuthState.UNAUTHENTICATED)
        if (!jwt.isNullOrEmpty()) _authState.postValue(AuthState.AUTHENTICATED)
    }
}