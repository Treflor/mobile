package com.treflor.data.repository

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.treflor.data.db.dao.UserDao
import com.treflor.data.db.datasources.JourneyDBDataSource
import com.treflor.data.db.datasources.UserDBDataSource
import com.treflor.data.provider.JWTProvider
import com.treflor.data.provider.LocationProvider
import com.treflor.data.remote.datasources.AuthenticationNetworkDataSource
import com.treflor.data.remote.datasources.UserNetworkDataSource
import com.treflor.data.remote.requests.SignUpRequest
import com.treflor.internal.AuthState
import com.treflor.internal.LocationUpdateReciever
import com.treflor.models.Journey
import com.treflor.models.User
import kotlinx.coroutines.*

class RepositoryImpl(
    private val jwtProvider: JWTProvider,
    private val authenticationNetworkDataSource: AuthenticationNetworkDataSource,
    private val userNetworkDataSource: UserNetworkDataSource,
    private val userDBDataSource: UserDBDataSource,
    private val journeyDBDataSource: JourneyDBDataSource,
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

    override fun removeLocationUpdate(updateReceiver: LocationUpdateReciever): LiveData<Location> =
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

    private fun unsetJWT(): Boolean = jwtProvider.unsetJWT()
    private fun getJWT(): String? = jwtProvider.getJWT()
    private fun setJWT(jwt: String): Boolean = jwtProvider.setJWT(jwt)

    private fun persistFetchedUser(user: User?) {
        GlobalScope.launch(Dispatchers.IO) {
            if (user == null) return@launch userDBDataSource.delete()
            userDBDataSource.upsert(user)
        }
    }
}