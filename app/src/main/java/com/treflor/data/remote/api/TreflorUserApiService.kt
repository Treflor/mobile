package com.treflor.data.remote.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.treflor.data.provider.JWTProvider
import com.treflor.data.remote.ConnectivityInterceptor
import com.treflor.models.User
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface TreflorUserApiService {

    @GET("info")
    fun getUser(): Deferred<User>

    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor,
            jwtProvider: JWTProvider
        ): TreflorUserApiService {
            val requestInterceptor = Interceptor { chain ->
                val headers = chain.request()
                    .headers()
                    .newBuilder()
                    .add("authorization", jwtProvider.getJWT())
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(chain.request().url())
                    .headers(headers)
                    .build()
                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
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