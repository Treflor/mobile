package com.treflor.data.remote.datasources

import android.util.Log
import com.treflor.data.provider.JWTProvider
import com.treflor.data.remote.api.TreflorApiService
import com.treflor.data.remote.requests.JourneyRequest
import com.treflor.internal.NoConnectivityException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class JourneyNetworkDataSourceImpl(
    private val treflorApiService: TreflorApiService,
    private val jwtProvider: JWTProvider
) : JourneyNetworkDataSource {
    override fun uploadJourney(journeyRequest: JourneyRequest) {
        try {
            GlobalScope.launch(Dispatchers.IO) {
                treflorApiService.uploadJourney(jwtProvider.getJWT()!!, journeyRequest).await()
            }
            Log.e("journey", journeyRequest.toString())
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)

        }
    }
}