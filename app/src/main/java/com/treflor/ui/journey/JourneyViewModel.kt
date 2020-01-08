package com.treflor.ui.journey

import android.content.Context
import android.content.Intent
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.treflor.data.repository.Repository
import com.treflor.internal.LocationUpdateReciever
import com.treflor.internal.lazyDeferred
import com.treflor.models.Journey
import com.treflor.services.LocationTrackService

class JourneyViewModel(
    private val repository: Repository,
    private val context: Context
) : ViewModel() {

    val location: LiveData<Location> get() = repository.requestLocationUpdate(LocationUpdateReciever.LOCATION_VIEW_MODEL)
    val journey by lazyDeferred { repository.getJourney() }
    val direction by lazyDeferred { repository.getDirection() }
    val trackedLocations by lazyDeferred { repository.getTrackedLocations() }

    fun stopJourney() {
        stopService()
        repository.breakJourney()
    }

    fun removeLocationUpdates() =
        repository.removeLocationUpdate(LocationUpdateReciever.LOCATION_VIEW_MODEL)

    fun requestLocationUpdates() =
        repository.requestLocationUpdate(LocationUpdateReciever.LOCATION_VIEW_MODEL)

    fun finishJourney() {
        stopService()
        repository.finishJourney()
    }

    private fun stopService() {
        val service = Intent(context, LocationTrackService::class.java)
        context.stopService(service)
    }
}
