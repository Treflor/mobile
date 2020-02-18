package com.treflor.data.remote.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.treflor.data.remote.intercepters.ConnectivityInterceptor
import com.treflor.data.remote.requests.JourneyRequest
import com.treflor.data.remote.requests.SignUpRequest
import com.treflor.data.remote.response.AuthResponse
import com.treflor.models.directionapi.DirectionApiResponse
import com.treflor.data.remote.response.IDResponse
import com.treflor.data.remote.response.JourneyResponse
import com.treflor.models.User
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface TreflorApiService {

    @FormUrlEncoded
    @POST("oauth/google")
    fun signupWithGoogle(
        @Field("id_token") idToken: String
    ): Deferred<AuthResponse>


    @POST("oauth/signup")
    fun signUp(
        @Body signUpRequest: SignUpRequest
    ): Deferred<AuthResponse>

    @FormUrlEncoded
    @POST("oauth/signin")
    fun signIn(
        @Field("email") email: String,
        @Field("password") password: String
    ): Deferred<AuthResponse>

    @GET("user")
    fun getUser(@Header("authorization") jwt: String): Deferred<User>

    @GET("services/google/direction")
    fun fetchDirection(
        @Header("authorization") jwt: String,
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("mode") mode: String = "driving"
    ): Deferred<DirectionApiResponse>

    @POST("journey")
    fun uploadJourney(
        @Header("authorization") jwt: String, @Body request: JourneyRequest
    ): Deferred<IDResponse>

    @GET("journey")
    fun allowedJourneys(
        @Header("authorization") jwt: String
    ): Deferred<List<JourneyResponse>>

    @GET("user/journeys")
    fun userJourneys(
        @Header("authorization") jwt: String
    ): Deferred<List<JourneyResponse>>
  
    @GET("journey/{journeyId}")
    fun journeyById(
        @Header("authorization") jwt: String,
        @Path(value = "journeyId", encoded = true) journeyId: String
    ): Deferred<JourneyResponse>


    @PUT("journey/{journeyId}/addFavorite")
    fun addFavorite(
        @Header("authorization") jwt: String,
        @Path(value = "journeyId", encoded = true) journeyId: String
    ): Deferred<IDResponse>

    @PUT("journey/{journeyId}/removeFavorite")
    fun removeFavorite(
        @Header("authorization") jwt: String,
        @Path(value = "journeyId", encoded = true) journeyId: String
    ): Deferred<IDResponse>

    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): TreflorApiService {
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://10.0.2.2:3000/")
//                .baseUrl("https://api-treflor.herokuapp.com/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TreflorApiService::class.java)
        }
    }
}