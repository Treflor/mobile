package com.treflor.data.remote.datasources

import android.util.Log
import com.treflor.data.remote.api.TreflorApiService
import com.treflor.data.remote.requests.SignUpRequest
import com.treflor.internal.NoConnectivityException
import retrofit2.HttpException

class AuthenticationNetworkDataSourceImpl(
    private val treflorApiService: TreflorApiService
) : AuthenticationNetworkDataSource {
    override suspend fun signInWithGoogle(idToken: String): String? {
        return try {
            val authResponse = treflorApiService.signupWithGoogle(idToken).await()
            authResponse.token
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
            null
        }
    }

    override suspend fun signIn(email: String, password: String): String? {
        return try {
            val authResponse = treflorApiService.signIn(email, password).await()
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
            val authResponse = treflorApiService.signUp(signUpRequest).await()
            authResponse.token
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
            null
        }
    }
}