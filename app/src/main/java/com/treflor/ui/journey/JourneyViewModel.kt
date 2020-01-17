package com.treflor.ui.journey

import android.content.Context
import android.content.Intent
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.treflor.data.repository.Repository
import com.treflor.internal.LocationUpdateReciever
import com.treflor.internal.eventexcecutor.ActivityNavigation
import com.treflor.internal.eventexcecutor.LiveMessageEvent
import com.treflor.internal.lazyDeferred
import com.treflor.models.Journey
import com.treflor.services.LocationTrackService
import kotlinx.coroutines.*

class JourneyViewModel(
    private val repository: Repository,
    private val context: Context
) : ViewModel() {

    // for send events to fragment
    val liveMessageEvent =
        LiveMessageEvent<ActivityNavigation>()

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

    fun finishJourney() = runBlocking {
        stopService()
        val response = repository.finishJourney(
            journey.await().value!!,
            direction.await().value!!,
            trackedLocations.await().value!!
        )

        if (response.success) {
            GlobalScope.launch(Dispatchers.Main) { liveMessageEvent.sendEvent { showSnackBar("Journey Uploaded successfully") } }
        }
    }

    private fun stopService() {
        val service = Intent(context, LocationTrackService::class.java)
        context.stopService(service)
    }
}
