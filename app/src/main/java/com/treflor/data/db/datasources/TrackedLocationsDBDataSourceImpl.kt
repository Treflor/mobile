package com.treflor.data.db.datasources

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.treflor.data.db.dao.TrackedLocationsDao
import com.treflor.internal.notifyObservers
import com.treflor.models.TrackedLocation

class TrackedLocationsDBDataSourceImpl(
    private val trackedLocationsDao: TrackedLocationsDao
) : TrackedLocationsDBDataSource {
    override val trackedLocations: LiveData<List<TrackedLocation>>
        get() = _trackedLocations
    private val _trackedLocations: MutableLiveData<List<TrackedLocation>> by lazy {
        val locations = trackedLocationsDao.getLocations()
        _trackedLocationsList.addAll(locations)
        MutableLiveData<List<TrackedLocation>>(locations)
    }
    private val _trackedLocationsList: MutableList<TrackedLocation> = mutableListOf()

    override fun insert(trackedLocation: TrackedLocation) {
        trackedLocationsDao.insert(trackedLocation)
        _trackedLocationsList.add(trackedLocation)
        _trackedLocations.value = _trackedLocationsList
    }

    override fun deleteTable() {
        trackedLocationsDao.deleteTabel()
        _trackedLocationsList.clear()
        _trackedLocations.postValue(null)
    }
}