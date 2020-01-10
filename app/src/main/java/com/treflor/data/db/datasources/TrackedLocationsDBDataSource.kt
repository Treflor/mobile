package com.treflor.data.db.datasources

import androidx.lifecycle.LiveData
import com.treflor.models.TrackedLocation

interface TrackedLocationsDBDataSource {
    val trackedLocations: LiveData<List<TrackedLocation>>

    fun insert(trackedLocation: TrackedLocation)
    fun deleteTable()
}