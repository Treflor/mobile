package com.treflor.data.repository

import android.location.Location
import androidx.lifecycle.LiveData
import com.treflor.data.remote.requests.SignUpRequest
import com.treflor.models.directionapi.DirectionApiResponse
import com.treflor.data.remote.response.IDResponse
import com.treflor.data.remote.response.JourneyResponse
import com.treflor.internal.AuthState
import com.treflor.internal.LocationUpdateReceiver
import com.treflor.internal.SignUpState
import com.treflor.models.Journey
import com.treflor.models.Landmark
import com.treflor.models.TrackedLocation
import com.treflor.models.User

interface Repository {

    // account things
    fun signInWithGoogle(idToken: String)

    suspend fun signIn(email: String, password: String): AuthState
    suspend fun signUp(signUpRequest: SignUpRequest): SignUpState
    fun signOut()
    suspend fun getUser(): LiveData<User>
    fun getCurrentUserId(): String?

    // local locations
    fun requestLocationUpdate(updateReceiver: LocationUpdateReceiver): LiveData<Location>

    fun removeLocationUpdate(updateReceiver: LocationUpdateReceiver)
    fun getLastKnownLocation(): LiveData<Location>

    // journey
    fun persistJourney(journey: Journey)

    fun addImagesToJourney(base64Images: List<String>)
    fun deleteImagesOnJourney()
    fun getImagesToJourney(): List<String>

    suspend fun getJourney(): LiveData<Journey>
    fun breakJourney()
    suspend fun finishJourney(
        journey: Journey,
        direction: DirectionApiResponse,
        trackedLocations: List<TrackedLocation>
    ): IDResponse

    suspend fun getAllJourneys(): LiveData<List<JourneyResponse>>
    suspend fun getJourneyById(id: String): LiveData<JourneyResponse>
    suspend fun addJourneyFavorite(journeyId: String): IDResponse
    suspend fun removeJourneyFavorite(journeyId: String): IDResponse

    //user journey
    suspend fun userJourneys(): LiveData<List<JourneyResponse>>

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

    fun getCurrentLandmarks(): LiveData<List<Landmark>>
    fun persistLandmark(landmark: Landmark)
    fun deleteLandmarks()
}