package com.treflor.data.remote.datasources

import android.util.Log
import com.treflor.data.remote.api.TreflorAuthApiService
import com.treflor.data.remote.requests.SignUpRequest
import com.treflor.internal.NoConnectivityException
import retrofit2.HttpException

class AuthenticationNetworkDataSourceImpl(
    private val treflorAuthApiService: TreflorAuthApiService
) : AuthenticationNetworkDataSource {
    override suspend fun signInWithGoogle(idToken: String): String? {
        return try {
            val authResponse = treflorAuthApiService.signupWithGoogle(idToken).await()
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
        } catch (e: HttpException) {
            Log.e("Authentication", "401.", e)
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