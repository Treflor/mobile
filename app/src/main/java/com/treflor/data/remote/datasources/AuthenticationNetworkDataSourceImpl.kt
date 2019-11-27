package com.treflor.data.remote.datasources

import android.util.Log
import com.treflor.data.remote.api.TreflorAuthApiService
import com.treflor.data.remote.requests.SignUpRequest
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

    override suspend fun signIn(email: String, password: String): String? {
        return try {
            val authResponse = treflorAuthApiService.signIn(email, password).await()
            authResponse.token
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
            null
        }
    }

    override suspend fun signUp(signUpRequest: SignUpRequest): String? {
        return try {
            val authResponse = treflorAuthApiService.signUp(signUpRequest).await()
            authResponse.token
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
            null
        }
    }
}