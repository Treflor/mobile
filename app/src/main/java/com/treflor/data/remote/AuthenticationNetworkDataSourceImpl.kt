package com.treflor.data.remote

import android.util.Log
import com.treflor.internal.NoConnectivityException

class AuthenticationNetworkDataSourceImpl(
    private val treflorAuthApiService: TreflorAuthApiService
) : AuthenticationNetworkDataSource {
    override suspend fun signInWithGoogle(accessToken: String): String? {
        return try {
            val authResponse = treflorAuthApiService.signupWithGoogle(accessToken).await()
            authResponse.token
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
            null
        }
    }
}