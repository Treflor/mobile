package com.treflor.ui.journey

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.treflor.data.repository.Repository
import com.treflor.internal.LocationUpdateReciever
import com.treflor.internal.lazyDeferred

class JourneyViewModel(
    private val repository: Repository
) : ViewModel() {

    val location: LiveData<Location> get() = repository.requestLocationUpdate(LocationUpdateReciever.LOCATION_VIEW_MODEL)
    val journey by lazyDeferred { repository.getJourney() }
    val direction by lazyDeferred { repository.getDirection() }
    val trackedLocations by lazyDeferred { repository.getTrackedLocations() }

    fun stopJourney() {
        repository.breakJourney()
        repository.clearTrackedLocations()
        repository.clearDirection()
    }

    fun finishJourney() {

    }
}
