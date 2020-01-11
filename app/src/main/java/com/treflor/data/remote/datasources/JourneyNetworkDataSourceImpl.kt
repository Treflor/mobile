package com.treflor.data.remote.datasources

import android.util.Log
import androidx.lifecycle.LiveData
import com.treflor.data.provider.JWTProvider
import com.treflor.data.remote.api.TreflorApiService
import com.treflor.data.remote.requests.JourneyRequest
import com.treflor.data.remote.response.IDResponse
import com.treflor.internal.NoConnectivityException
import kotlinx.coroutines.*

class JourneyNetworkDataSourceImpl(
    private val treflorApiService: TreflorApiService,
    private val jwtProvider: JWTProvider
) : JourneyNetworkDataSource {
    override val journeys: LiveData<List<JourneyRequest>>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val journey: LiveData<JourneyRequest>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override suspend fun uploadJourney(journeyRequest: JourneyRequest): IDResponse {
        return try {
            treflorApiService.uploadJourney(jwtProvider.getJWT()!!, journeyRequest).await()
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
            IDResponse(false, "Connection Error", "")
        }
    }

    override fun fetchAllJourneys(): LiveData<List<JourneyRequest>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun fetchJourney(): LiveData<JourneyRequest> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}