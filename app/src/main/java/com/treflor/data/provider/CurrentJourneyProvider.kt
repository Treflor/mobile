package com.treflor.data.provider

import androidx.lifecycle.LiveData
import com.treflor.models.Journey

interface CurrentJourneyProvider {
    val currentJourney:LiveData<Journey>
    fun getCurrentJourney(): Journey?
    fun persistImages(base64Images:List<String>)
    fun getImages():List<String>
    fun deleteImages()
    fun persistCurrentJourney(journey: Journey)
    fun deleteJourney()
}