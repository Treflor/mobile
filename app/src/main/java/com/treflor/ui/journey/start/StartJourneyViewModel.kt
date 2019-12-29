package com.treflor.ui.journey.start

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.treflor.data.repository.Repository
import com.treflor.models.Journey
import com.treflor.services.LocationTrackService

class StartJourneyViewModel(
    private val repository: Repository,
    private val context: Context
) : ViewModel() {

    val journey:LiveData<Journey> get() = repository.getJourney()

    fun startJourney(journey: Journey){
        repository.persistJourney(journey)
        val serviceIntent = Intent(context, LocationTrackService::class.java)
        context.startService(serviceIntent)
    }

}
