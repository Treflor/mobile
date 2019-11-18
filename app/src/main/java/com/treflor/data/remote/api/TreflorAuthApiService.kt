package com.treflor.data.remote.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.treflor.data.remote.intercepters.ConnectivityInterceptor
import com.treflor.data.remote.response.AuthResponse
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface TreflorAuthApiService {

    @POST("google")
    fun signupWithGoogle(
        @Query("access_token") accessToken: String
    ): Deferred<AuthResponse>


    @POST("signup")
    fun signup(
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("password2") password2: String,
        @Query("given_name") givenName: String,
        @Query("family_name") familyName: String,
        @Query("gender") gender: String,
        @Query("birthday") birthday: String,
        @Query("photo") photo: String
    ): Deferred<AuthResponse>

    @POST("signin")
    fun signin(
        @Query("email") email: String,
        @Query("password") password: String
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