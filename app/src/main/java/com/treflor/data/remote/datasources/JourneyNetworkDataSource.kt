package com.treflor.data.remote.datasources

import androidx.lifecycle.LiveData
import com.treflor.data.remote.requests.JourneyRequest
import com.treflor.data.remote.response.IDResponse

interface JourneyNetworkDataSource {
    val journeys: LiveData<List<JourneyRequest>>
    val journey: LiveData<JourneyRequest>

    suspend fun uploadJourney(journeyRequest: JourneyRequest): IDResponse
    fun fetchAllJourneys(): LiveData<List<JourneyRequest>>
    fun fetchJourney(): LiveData<JourneyRequest>
}