package com.treflor.data.repository

import android.location.Location
import androidx.lifecycle.LiveData
import com.treflor.data.remote.requests.SignUpRequest
import com.treflor.internal.AuthState
import com.treflor.internal.LocationUpdateReciever
import com.treflor.models.User

interface Repository {

    fun signInWithGoogle(idToken: String)
    fun signIn(email: String, password: String)
    fun signUp(signUpRequest: SignUpRequest)
    suspend fun getUser(): LiveData<User>
    fun requestLocationUpdate(updateReceiver: LocationUpdateReciever): LiveData<Location>
    fun removeLocationUpdate(updateReceiver: LocationUpdateReciever): LiveData<Location>
    fun getLastKnownLocation(): LiveData<Location>
}