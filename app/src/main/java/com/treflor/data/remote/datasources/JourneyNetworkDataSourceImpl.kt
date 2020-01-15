package com.treflor.data.remote.datasources

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.treflor.data.provider.JWTProvider
import com.treflor.data.remote.api.TreflorApiService
import com.treflor.data.remote.requests.JourneyRequest
import com.treflor.data.remote.response.IDResponse
import com.treflor.data.remote.response.JourneyResponse
import com.treflor.internal.NoConnectivityException
import kotlinx.coroutines.*

class JourneyNetworkDataSourceImpl(
    private val treflorApiService: TreflorApiService,
    private val jwtProvider: JWTProvider
) : JourneyNetworkDataSource {
    override val journeys: LiveData<List<JourneyResponse>> get() = _journeys
    private val _journeys by lazy { MutableLiveData<List<JourneyResponse>>() }

    override val journey: LiveData<JourneyResponse>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override suspend fun uploadJourney(journeyRequest: JourneyRequest): IDResponse {
        return try {
            treflorApiService.uploadJourney(jwtProvider.getJWT()!!, journeyRequest).await()
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
            IDResponse(false, "Connection Error", "")
        }
    }

    override suspend fun fetchAllJourneys(): LiveData<List<JourneyResponse>> {
        return try {
            val journeysV = mutableListOf<JourneyResponse>()
            if (_journeys.value != null)
                journeysV.addAll(_journeys.value!!)
            journeysV.addAll(treflorApiService.allowedJourneys(jwtProvider.getJWT()!!).await())
            _journeys.value = journeysV
            _journeys
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
            _journeys
        }
    }

    override fun fetchJourney(): LiveData<JourneyResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}