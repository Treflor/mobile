package com.treflor.data.remote.datasources

import android.util.Log
import com.treflor.data.provider.JWTProvider
import com.treflor.data.remote.api.TreflorApiService
import com.treflor.data.remote.requests.JourneyRequest
import com.treflor.internal.NoConnectivityException

class JourneyNetworkDataSourceImpl(
    private val treflorApiService: TreflorApiService,
    private val jwtProvider: JWTProvider
) : JourneyNetworkDataSource {
    override fun uploadJourney(journeyRequest: JourneyRequest) {
        try {
            treflorApiService.uploadJourney(jwtProvider.getJWT()!!, journeyRequest)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)

        }
    }
}