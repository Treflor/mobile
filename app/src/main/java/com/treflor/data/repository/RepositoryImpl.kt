package com.treflor.data.repository

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.android.libraries.maps.model.LatLng
import com.google.gson.Gson
import com.google.maps.android.PolyUtil
import com.treflor.data.db.datasources.DirectionDBDataSource
import com.treflor.data.db.datasources.JourneyDBDataSource
import com.treflor.data.db.datasources.TrackedLocationsDBDataSource
import com.treflor.data.db.datasources.UserDBDataSource
import com.treflor.data.provider.JWTProvider
import com.treflor.data.provider.LocationProvider
import com.treflor.data.remote.datasources.AuthenticationNetworkDataSource
import com.treflor.data.remote.datasources.JourneyNetworkDataSource
import com.treflor.data.remote.datasources.TreflorGoogleServicesNetworkDataSource
import com.treflor.data.remote.datasources.UserNetworkDataSource
import com.treflor.data.remote.requests.JourneyRequest
import com.treflor.data.remote.requests.SignUpRequest
import com.treflor.data.remote.response.DirectionApiResponse
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
    private val userDBDataSource: UserDBDataSource,
    private val journeyDBDataSource: JourneyDBDataSource,
    private val directionDBDataSource: DirectionDBDataSource,
    private val trackedLocationsDBDataSource: TrackedLocationsDBDataSource,
    private val locationProvider: LocationProvider
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
        GlobalScope.launch(Dispatchers.IO) {
            userNetworkDataSource.fetchUser()
        }
        return userDBDataSource.user
    }

    override fun requestLocationUpdate(updateReceiver: LocationUpdateReciever): LiveData<Location> =
        locationProvider.requestLocationUpdate(updateReceiver)

    override fun removeLocationUpdate(updateReceiver: LocationUpdateReciever) =
        locationProvider.removeLocationUpdate(updateReceiver)

    override fun getLastKnownLocation(): LiveData<Location> =
        locationProvider.getLastKnownLocation()

    override fun persistJourney(journey: Journey) {
        GlobalScope.launch(Dispatchers.IO) {
            journeyDBDataSource.upsert(journey)
        }
    }

    override fun getJourney(): LiveData<Journey> = journeyDBDataSource.journey
    override fun breakJourney() {
        journeyDBDataSource.delete()
        clearTrackedLocations()
        clearDirection()
    }

    override fun finishJourney() {
        // TODO: upload data to server and delete cache
        GlobalScope.launch(Dispatchers.IO) {
            val journey = getJourney().value
            val direction = getDirection().value
            val trackedLocations =
                PolyUtil.encode(getTrackedLocations().value!!.map { tl -> LatLng(tl.lat, tl.lng) })
            val user = getUser().value
            val journeyRequest = JourneyRequest(user, direction, journey, trackedLocations)
            Log.e("json", Gson().toJson(journeyRequest))
            journeyNetworkDataSource.uploadJourney(journeyRequest)
            breakJourney()
        }

    }

    override fun getDirection(): LiveData<DirectionApiResponse> = directionDBDataSource.direction

    override suspend fun fetchDirection(
        origin: String,
        destination: String,
        mode: String
    ): LiveData<DirectionApiResponse> {
        GlobalScope.launch(Dispatchers.IO) {
            treflorGoogleServicesNetworkDataSource.fetchDirection(origin, destination, mode)
        }
        return directionDBDataSource.direction
    }

    override fun clearDirection() = persistFetchedDirection(null)

    override fun getTrackedLocations(): LiveData<List<TrackedLocation>> =
        trackedLocationsDBDataSource.trackedLocations

    override fun insertTackedLocations(trackedLocation: TrackedLocation) =
        trackedLocationsDBDataSource.insert(trackedLocation)

    override fun clearTrackedLocations() = trackedLocationsDBDataSource.deleteTable()

    private fun unsetJWT(): Boolean = jwtProvider.unsetJWT()
    private fun getJWT(): String? = jwtProvider.getJWT()
    private fun setJWT(jwt: String): Boolean = jwtProvider.setJWT(jwt)

    private fun persistFetchedUser(user: User?) {
        GlobalScope.launch(Dispatchers.IO) {
            if (user == null) return@launch userDBDataSource.delete()
            userDBDataSource.upsert(user)
        }
    }

    private fun persistFetchedDirection(direction: DirectionApiResponse?) {
        GlobalScope.launch(Dispatchers.IO) {
            if (direction == null) return@launch directionDBDataSource.delete()
            directionDBDataSource.upsert(direction)
        }
    }
}