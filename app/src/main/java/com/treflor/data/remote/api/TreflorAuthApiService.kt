package com.treflor.data.remote.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.treflor.data.remote.intercepters.ConnectivityInterceptor
import com.treflor.data.remote.requests.SignUpRequest
import com.treflor.data.remote.response.AuthResponse
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface TreflorAuthApiService {

    @FormUrlEncoded
    @POST("google")
    fun signupWithGoogle(
        @Field("id_token") idToken: String
    ): Deferred<AuthResponse>


    @POST("signup")
    fun signUp(
        @Body signUpRequest: SignUpRequest
    ): Deferred<AuthResponse>

    @FormUrlEncoded
    @POST("signin")
    fun signIn(
        @Field("email") email: String,
        @Field("password") password: String
    ): Deferred<AuthResponse>

    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): TreflorAuthApiService {
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api-treflor.herokuapp.com/oauth/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TreflorAuthApiService::class.java)
        }
    }
}