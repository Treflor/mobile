package com.treflor.data.remote.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.treflor.data.remote.intercepters.ConnectivityInterceptor
import com.treflor.data.remote.intercepters.UnauthorizedInterceptor
import com.treflor.models.User
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

interface TreflorUserApiService {

    @GET("info")
    fun getUser(@Header("authorization") jwt: String): Deferred<User>

    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): TreflorUserApiService {

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api-treflor.herokuapp.com/user/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TreflorUserApiService::class.java)
        }

    }
}