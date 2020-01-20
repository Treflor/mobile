package com.treflor.data.repository

import android.location.Location
import androidx.lifecycle.LiveData
import com.treflor.data.db.entities.journey.JourneyListEntity
import com.treflor.data.remote.requests.SignUpRequest
import com.treflor.data.remote.response.DirectionApiResponse
import com.treflor.data.remote.response.IDResponse
import com.treflor.data.remote.response.JourneyResponse
import com.treflor.internal.LocationUpdateReciever
import com.treflor.models.Journey
import com.treflor.models.TrackedLocation
import com.treflor.models.User

interface Repository {

    // account things
    fun signInWithGoogle(idToken: String)

    fun signIn(email: String, password: String)
    fun signUp(signUpRequest: SignUpRequest)
    suspend fun getUser(): LiveData<User>

    // local locations
    fun requestLocationUpdate(updateReceiver: LocationUpdateReciever): LiveData<Location>

    fun removeLocationUpdate(updateReceiver: LocationUpdateReciever)
    fun getLastKnownLocation(): LiveData<Location>

    // journey
    fun persistJourney(journey: Journey)

    suspend fun getJourney(): LiveData<Journey>
    fun breakJourney()
    suspend fun finishJourney(
        journey: Journey,
        direction: DirectionApiResponse,
        trackedLocations: List<TrackedLocation>
    ): IDResponse

    suspend fun getAllJourneys(): LiveData<List<JourneyListEntity>>

    // direction
    suspend fun getDirection(): LiveData<DirectionApiResponse>

    suspend fun fetchDirection(
        origin: String,
        destination: String,
        mode: String
    ): LiveData<DirectionApiResponse>

    fun clearDirection()

    // location tracking
    suspend fun getTrackedLocations(): LiveData<List<TrackedLocation>>

    fun insertTackedLocations(trackedLocation: TrackedLocation)
    fun clearTrackedLocations()

}