package com.treflor.data.db.datasources

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.treflor.data.db.dao.TrackedLocationsDao
import com.treflor.internal.notifyObservers
import com.treflor.models.TrackedLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
        GlobalScope.launch(Dispatchers.IO) {
            trackedLocationsDao.insert(trackedLocation)
            _trackedLocationsList.add(trackedLocation)
            GlobalScope.launch(Dispatchers.Main) { _trackedLocations.value = _trackedLocationsList }
        }
    }

    override fun deleteTable() {
        trackedLocationsDao.deleteTabel()
        _trackedLocationsList.clear()
        _trackedLocations.postValue(null)
    }
}