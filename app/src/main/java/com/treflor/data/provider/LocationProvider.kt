package com.treflor.data.provider

import android.location.Location
import androidx.lifecycle.LiveData
import com.treflor.internal.LocationUpdateReciever

interface LocationProvider {
    val location: LiveData<Location>
    fun requestLocationUpdate(updateReceiver: LocationUpdateReciever): LiveData<Location>
    fun removeLocationUpdate(updateReceiver: LocationUpdateReciever): LiveData<Location>
    fun getLastKnownLocation(): LiveData<Location>
}