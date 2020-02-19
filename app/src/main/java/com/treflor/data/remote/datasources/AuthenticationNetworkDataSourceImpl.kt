package com.treflor.data.remote.datasources

import android.util.Log
import com.treflor.data.remote.api.TreflorApiService
import com.treflor.data.remote.requests.SignUpRequest
import com.treflor.internal.AuthState
import com.treflor.internal.NoConnectivityException
import com.treflor.internal.SignUpState
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

    override suspend fun signIn(email: String, password: String): Pair<String?, AuthState> {
        return try {
            val authResponse = treflorApiService.signIn(email, password).await()
            Pair(authResponse.token, AuthState.AUTHENTICATED)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
            Pair(null, AuthState.ERROR)
        } catch (e: HttpException) {
            Log.e("Authentication", "401.", e)
            Pair(null, AuthState.UNAUTHENTICATED)
        }
    }

    override suspend fun signUp(signUpRequest: SignUpRequest): Pair<String?, SignUpState> {
        return try {
            val authResponse = treflorApiService.signUp(signUpRequest).await()
            Pair(authResponse.token, SignUpState.DONE)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
            Pair(null, SignUpState.ERROR)
        } catch (e: HttpException) {
            Log.e("Authentication", "403.", e)
            Pair(null, SignUpState.EMAIL_ALL_READY)
        }
    }
}