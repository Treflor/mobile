package com.treflor.data.db.datasources

import androidx.lifecycle.LiveData
import com.treflor.data.remote.response.JourneyResponse
import com.treflor.models.Journey

interface JourneyDBDataSource {
    val journey: LiveData<Journey>
    val JourneyResponses: LiveData<List<JourneyResponse>>

    fun upsert(journey: Journey)
    fun delete()

    fun upsertAllJourneyResponses(journeys: List<JourneyResponse>)
    fun getAllJourneyResponses()
    fun deleteAllJourneyResponses()
}