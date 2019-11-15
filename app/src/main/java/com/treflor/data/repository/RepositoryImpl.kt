package com.treflor.data.repository

import androidx.lifecycle.LiveData
import com.treflor.data.db.dao.UserDao
import com.treflor.data.provider.JWTProvider
import com.treflor.data.remote.datasources.AuthenticationNetworkDataSource
import com.treflor.data.remote.datasources.UserNetworkDataSource
import com.treflor.models.User
import kotlinx.coroutines.*

class RepositoryImpl(
    private val jwtProvider: JWTProvider,
    private val authenticationNetworkDataSource: AuthenticationNetworkDataSource,
    private val userNetworkDataSource: UserNetworkDataSource,
    private val userDao: UserDao
) : Repository {

    init {
        userNetworkDataSource.apply {
            user.observeForever { user -> persistFetchedUser(user) }
        }
    }

    override fun signInWithGoogle(accessToken: String) = runBlocking {
        val jwt = withContext(Dispatchers.IO) {
            authenticationNetworkDataSource.signInWithGoogle(accessToken)
        }
        if (jwt != null) {
            setJWT(jwt)
        }
    }

    override suspend fun getUser(): LiveData<User> {
        return withContext(Dispatchers.IO) {
            userNetworkDataSource.fetchUser()
            val user = userDao.getUser()
            println("Reading user...............................${user.value}")
            return@withContext user
        }
    }

    private fun unsetJWT(): Boolean = jwtProvider.unsetJWT()
    private fun getJWT(): String? = jwtProvider.getJWT()
    private fun setJWT(jwt: String): Boolean = jwtProvider.setJWT(jwt)
    fun isLogged(): Boolean = !getJWT().isNullOrEmpty()

    private fun persistFetchedUser(user: User) {
        GlobalScope.launch(Dispatchers.IO) {
            userDao.upsert(user)
        }
    }
}