package com.treflor.data.provider

import com.treflor.models.Journey

interface CurrentJourneyProvider {
    fun getCurrentJourney(): Journey?
    fun persistCurrentJourney(journey: Journey)
    fun deleteJourney()
}