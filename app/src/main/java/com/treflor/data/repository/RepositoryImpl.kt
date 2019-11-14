package com.treflor.data.repository

import com.treflor.data.provider.JWTProvider
import com.treflor.data.remote.datasources.AuthenticationNetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class RepositoryImpl(
    private val jwtProvider: JWTProvider,
    private val authenticationNetworkDataSource: AuthenticationNetworkDataSource
) : Repository {

    override fun signInWithGoogle(accessToken: String) = runBlocking {
        val jwt = withContext(Dispatchers.IO) {
            authenticationNetworkDataSource.signInWithGoogle(accessToken)
        }
        if (jwt != null) {
            setJWT(jwt)
        }
    }

    private fun unsetJWT(): Boolean = jwtProvider.unsetJWT()
    private fun getJWT(): String = jwtProvider.getJWT()
    private fun setJWT(jwt: String): Boolean = jwtProvider.setJWT(jwt)
}