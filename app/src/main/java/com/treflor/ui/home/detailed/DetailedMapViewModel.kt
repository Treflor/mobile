package com.treflor.ui.home.detailed

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.treflor.data.repository.Repository
import com.treflor.internal.LocationUpdateReceiver
import com.treflor.internal.lazyDeferred

class DetailedMapViewModel(
    private val repository: Repository,
    private val journeyId: String
) : ViewModel() {

    val journey by lazyDeferred {
        repository.getJourneyById(journeyId)
    }
    val location: LiveData<Location> get() = repository.requestLocationUpdate(LocationUpdateReceiver.LOCATION_VIEW_MODEL)

}
