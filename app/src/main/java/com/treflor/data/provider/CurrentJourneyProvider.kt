package com.treflor.data.provider

import androidx.lifecycle.LiveData
import com.treflor.models.Journey

interface CurrentJourneyProvider {
    val currentJourney:LiveData<Journey>
    fun getCurrentJourney(): Journey?
    fun persistCurrentJourney(journey: Journey)
    fun deleteJourney()
}