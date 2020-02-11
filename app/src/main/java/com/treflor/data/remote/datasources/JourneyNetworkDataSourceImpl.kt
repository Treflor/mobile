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
        get() = _journey
    private val _journey by lazy { MutableLiveData<JourneyResponse>() }

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
            Log.e("journeys", "fetching journeys")
            val journeysV = mutableListOf<JourneyResponse>()
            journeysV.addAll(treflorApiService.allowedJourneys(jwtProvider.getJWT()!!).await())
            _journeys.postValue(journeysV)
            _journeys
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
            _journeys
        }
    }

    override suspend fun fetchJourneyById(journeyId: String): LiveData<JourneyResponse> {
        return try {
            Log.e("journeys", "fetching journey ${journeyId}")
            val journey = treflorApiService.journeyById(jwtProvider.getJWT()!!, journeyId).await()
            _journey.postValue(journey)
            _journey
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
            _journey
        }
    }

    override suspend fun addJourneyFavorite(journeyId: String): IDResponse {
        return try {
            treflorApiService.addFavorite(jwtProvider.getJWT()!!, journeyId).await()
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
            IDResponse(false, "Connection Error", "")
        }
    }

    override suspend fun removeJourneyFavorite(journeyId: String): IDResponse {
        return try {
            treflorApiService.removeFavorite(jwtProvider.getJWT()!!, journeyId).await()
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
            IDResponse(false, "Connection Error", "")
        }
    }


}