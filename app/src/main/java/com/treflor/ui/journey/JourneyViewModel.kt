package com.treflor.ui.journey

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.treflor.data.repository.Repository
import com.treflor.internal.LocationUpdateReciever

class JourneyViewModel(
    private val repository: Repository
) : ViewModel() {

    val location: LiveData<Location> get() = repository.requestLocationUpdate(LocationUpdateReciever.LOCATION_VIEW_MODEL)

}
