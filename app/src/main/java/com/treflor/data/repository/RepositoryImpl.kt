package com.treflor.data.repository

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.android.libraries.maps.model.LatLng
import com.google.maps.android.PolyUtil
import com.treflor.data.db.dao.*
import com.treflor.data.provider.*
import com.treflor.data.remote.datasources.AuthenticationNetworkDataSource
import com.treflor.data.remote.datasources.JourneyNetworkDataSource
import com.treflor.data.remote.datasources.TreflorGoogleServicesNetworkDataSource
import com.treflor.data.remote.datasources.UserNetworkDataSource
import com.treflor.data.remote.requests.JourneyRequest
import com.treflor.data.remote.requests.SignUpRequest
import com.treflor.models.directionapi.DirectionApiResponse
import com.treflor.data.remote.response.IDResponse
import com.treflor.data.remote.response.JourneyResponse
import com.treflor.internal.LocationUpdateReciever
import com.treflor.models.Journey
import com.treflor.models.TrackedLocation
import com.treflor.models.User
import kotlinx.coroutines.*

class RepositoryImpl(
    private val jwtProvider: JWTProvider,
    private val authenticationNetworkDataSource: AuthenticationNetworkDataSource,
    private val userNetworkDataSource: UserNetworkDataSource,
    private val treflorGoogleServicesNetworkDataSource: TreflorGoogleServicesNetworkDataSource,
    private val journeyNetworkDataSource: JourneyNetworkDataSource,
    private val userDao: UserDao,
    private val journeyResponseDao: JourneyResponseDao,
    private val trackedLocationsDao: TrackedLocationsDao,
    private val locationProvider: LocationProvider,
    private val currentDirectionProvider: CurrentDirectionProvider,
    private val currentUserProvider: CurrentUserProvider,
    private val currentJourneyProvider: CurrentJourneyProvider
) : Repository {

    init {

        jwtProvider.apply {
            authState.observeForever {
                GlobalScope.launch(Dispatchers.IO) {
                    userNetworkDataSource.fetchUser()
                }
            }
        }

        userNetworkDataSource.apply {
            user.observeForever { user -> persistFetchedUser(user) }
        }

        treflorGoogleServicesNetworkDataSource.apply {
            direction.observeForever { direction -> persistFetchedDirection(direction) }
        }

        journeyNetworkDataSource.apply {
            journeys.observeForever { journeys -> persistJourneyResponses(journeys) }
        }
    }

    override fun signInWithGoogle(idToken: String) = runBlocking {
        val jwt = withContext(Dispatchers.IO) {
            authenticationNetworkDataSource.signInWithGoogle(idToken)
        }
        if (jwt != null) {
            Log.e("jwt", jwt)
            setJWT(jwt)
        }
    }

    override fun signIn(email: String, password: String) = runBlocking {
        val jwt = withContext(Dispatchers.IO) {
            authenticationNetworkDataSource.signIn(email, password)
        }
        if (jwt != null) {
            setJWT(jwt)
        }
    }

    override fun signUp(signUpRequest: SignUpRequest) = runBlocking {
        val jwt = withContext(Dispatchers.IO) {
            authenticationNetworkDataSource.signUp(signUpRequest)
        }
        if (jwt != null) {
            setJWT(jwt)
        }
    }

    override suspend fun getUser(): LiveData<User> {
        return withContext(Dispatchers.IO) {
            userNetworkDataSource.fetchUser()
            return@withContext currentUserProvider.currentUser
        }
    }

    override fun requestLocationUpdate(updateReceiver: LocationUpdateReciever): LiveData<Location> =
        locationProvider.requestLocationUpdate(updateReceiver)

    override fun removeLocationUpdate(updateReceiver: LocationUpdateReciever) =
        locationProvider.removeLocationUpdate(updateReceiver)

    override fun getLastKnownLocation(): LiveData<Location> =
        locationProvider.getLastKnownLocation()

    override fun persistJourney(journey: Journey) =
        currentJourneyProvider.persistCurrentJourney(journey)

    override suspend fun getJourney(): LiveData<Journey> = currentJourneyProvider.currentJourney
    override fun breakJourney() {
        currentJourneyProvider.deleteJourney()
        clearTrackedLocations()
        clearDirection()
    }

    override suspend fun finishJourney(
        journey: Journey,
        direction: DirectionApiResponse,
        trackedLocations: List<TrackedLocation>
    ): IDResponse {
        val trackedLocationsString =
            PolyUtil.encode(trackedLocations.map { tl -> LatLng(tl.lat, tl.lng) })
        val journeyRequest = JourneyRequest(direction, journey, trackedLocationsString)
        GlobalScope.launch(Dispatchers.IO) { breakJourney() }
        return withContext(Dispatchers.IO) {
            return@withContext journeyNetworkDataSource.uploadJourney(
                journeyRequest
            )
        }
    }

    override suspend fun getAllJourneys(): LiveData<List<JourneyResponse>> {
        return withContext(Dispatchers.IO) {
            journeyNetworkDataSource.fetchAllJourneys()
            return@withContext journeyResponseDao.getAllListJourneys()
        }
    }

    override suspend fun getJourneyById(id: String): LiveData<JourneyResponse> {
        return withContext(Dispatchers.IO) {
            return@withContext journeyResponseDao.getDetailedJourneyById(id)
        }
    }

    override suspend fun getDirection(): LiveData<DirectionApiResponse> =
        currentDirectionProvider.currentDirection

    override suspend fun fetchDirection(
        origin: String,
        destination: String,
        mode: String
    ): LiveData<DirectionApiResponse> {
        return withContext(Dispatchers.IO) {
            treflorGoogleServicesNetworkDataSource.fetchDirection(origin, destination, mode)
            return@withContext currentDirectionProvider.currentDirection
        }
    }

    override fun clearDirection() = currentDirectionProvider.deleteDirection()

    override suspend fun getTrackedLocations(): LiveData<List<TrackedLocation>> =
        trackedLocationsDao.getLocations()

    override fun insertTackedLocations(trackedLocation: TrackedLocation) =
        trackedLocationsDao.insert(trackedLocation)

    override fun clearTrackedLocations() = trackedLocationsDao.deleteTable()

    private fun unsetJWT(): Boolean = jwtProvider.unsetJWT()
    private fun getJWT(): String? = jwtProvider.getJWT()
    private fun setJWT(jwt: String): Boolean = jwtProvider.setJWT(jwt)

    private fun persistFetchedUser(user: User?) {
        GlobalScope.launch(Dispatchers.IO) {
            if (user == null) return@launch currentUserProvider.deleteCurrentUser()
            currentUserProvider.persistCurrentUser(user)
        }
    }

    private fun persistFetchedDirection(direction: DirectionApiResponse?) {
        if (direction == null) return currentDirectionProvider.deleteDirection()
        currentDirectionProvider.persistCurrentDirection(direction)
    }

    private fun persistJourneyResponses(journeys: List<JourneyResponse>?) {
        GlobalScope.launch(Dispatchers.IO) {
            if (journeys == null) return@launch journeyResponseDao.deleteAll()
            journeyResponseDao.upsertAll(journeys)
        }
    }
}