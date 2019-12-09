package com.treflor.ui.journey

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.treflor.data.repository.Repository

class JourneyViewModel(
    private val repository: Repository
) : ViewModel() {

    val location: LiveData<Location> get() = repository.getLastKnownLocation()

}
