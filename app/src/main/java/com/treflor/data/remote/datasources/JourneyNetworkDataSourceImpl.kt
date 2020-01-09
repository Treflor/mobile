package com.treflor.data.remote.datasources

import android.util.Log
import com.treflor.data.provider.JWTProvider
import com.treflor.data.remote.api.TreflorApiService
import com.treflor.data.remote.requests.JourneyRequest
import com.treflor.internal.NoConnectivityException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class JourneyNetworkDataSourceImpl(
    private val treflorApiService: TreflorApiService,
    private val jwtProvider: JWTProvider
) : JourneyNetworkDataSource {
    override fun uploadJourney(journeyRequest: JourneyRequest) {
        try {
            GlobalScope.launch(Dispatchers.IO) {
                val response =
                    treflorApiService.uploadJourney(jwtProvider.getJWT()!!, journeyRequest).await()
                Log.e("gg", response.toString())
            }
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)

        }
    }
}