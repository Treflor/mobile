package com.treflor.ui.journey.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.treflor.data.repository.Repository
import com.treflor.models.Journey

class StartJourneyViewModel(
    private val repository: Repository
) : ViewModel() {

    val journey:LiveData<Journey> get() = repository.getJourney()

    fun startJourney(journey: Journey){
        repository.persistJourney(journey)
    }

}
