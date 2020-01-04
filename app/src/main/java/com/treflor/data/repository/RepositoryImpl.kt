package com.treflor.data.repository

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.treflor.data.db.datasources.DirectoionDBDataSource
import com.treflor.data.db.datasources.JourneyDBDataSource
import com.treflor.data.db.datasources.UserDBDataSource
import com.treflor.data.provider.JWTProvider
import com.treflor.data.provider.LocationProvider
import com.treflor.data.remote.datasources.AuthenticationNetworkDataSource
import com.treflor.data.remote.datasources.TreflorGoogleServicesNetworkDataSource
import com.treflor.data.remote.datasources.UserNetworkDataSource
import com.treflor.data.remote.requests.SignUpRequest
import com.treflor.data.remote.response.DirectionApiResponse
import com.treflor.internal.LocationUpdateReciever
import com.treflor.models.Journey
import com.treflor.models.User
import kotlinx.coroutines.*

class RepositoryImpl(
    private val jwtProvider: JWTProvider,
    private val authenticationNetworkDataSource: AuthenticationNetworkDataSource,
    private val userNetworkDataSource: UserNetworkDataSource,
    private val treflorGoogleServicesNetworkDataSource: TreflorGoogleServicesNetworkDataSource,
    private val userDBDataSource: UserDBDataSource,
    private val journeyDBDataSource: JourneyDBDataSource,
    private val directoionDBDataSource: DirectoionDBDataSource,
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
    }

    override fun finishJourney() {
        // TODO: upload data to server and delete cache
    }


    //TODO update after persist
    override fun getDirection(): LiveData<DirectionApiResponse> = directoionDBDataSource.direction

    override suspend fun fetchDirection(
        origin: String,
        destination: String,
        mode: String
    ): LiveData<DirectionApiResponse> {
        GlobalScope.launch(Dispatchers.IO) {
            treflorGoogleServicesNetworkDataSource.fetchDirection(origin, destination, mode)
        }
        return directoionDBDataSource.direction
    }

    override fun deleteDirection() = persistFetchedDirection(null)

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
            if (direction == null) return@launch directoionDBDataSource.delete()
            directoionDBDataSource.upsert(direction)
        }
    }
}