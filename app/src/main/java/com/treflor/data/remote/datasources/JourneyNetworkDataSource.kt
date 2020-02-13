package com.treflor.data.remote.datasources

import androidx.lifecycle.LiveData
import com.treflor.data.remote.requests.JourneyRequest
import com.treflor.data.remote.response.IDResponse
import com.treflor.data.remote.response.JourneyResponse

interface JourneyNetworkDataSource {
    val journeys: LiveData<List<JourneyResponse>>
    val userJourneys: LiveData<List<JourneyResponse>>
    val journey: LiveData<JourneyResponse>

    suspend fun uploadJourney(journeyRequest: JourneyRequest): IDResponse
    suspend fun fetchAllJourneys(): LiveData<List<JourneyResponse>>
    fun fetchJourney(): LiveData<JourneyResponse>
    suspend fun userJourneys(): LiveData<List<JourneyResponse>>
    suspend fun fetchJourneyById(journeyId: String): LiveData<JourneyResponse>
    suspend fun addJourneyFavorite(journeyId: String): IDResponse
    suspend fun removeJourneyFavorite(journeyId: String): IDResponse

}