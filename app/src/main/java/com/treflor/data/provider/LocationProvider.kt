package com.treflor.data.provider

import android.location.Location
import androidx.lifecycle.LiveData
import com.treflor.internal.LocationUpdateReceiver

interface LocationProvider {
    val location: LiveData<Location>
    fun requestLocationUpdate(updateReceiver: LocationUpdateReceiver): LiveData<Location>
    fun removeLocationUpdate(updateReceiver: LocationUpdateReceiver)
    fun getLastKnownLocation(): LiveData<Location>
}