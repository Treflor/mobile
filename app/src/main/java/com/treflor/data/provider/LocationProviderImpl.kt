package com.treflor.data.provider

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.treflor.internal.LocationUpdateReceiver

const val PREF_LATITUDE = "pref_latitude"
const val PREF_LONGITUDE = "pref_longitude"

class LocationProviderImpl(
    context: Context
) : PreferenceProvider(context),
    LocationProvider {
    override val location: LiveData<Location> get() = _location
    private val _location = MutableLiveData<Location>()
    private val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private val locationUpdateReceiver = mutableListOf<LocationUpdateReceiver>()

    private val locationRequest = LocationRequest().apply {
        interval = 3500
        fastestInterval = 2000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY

    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            if (locationResult.lastLocation != null) {
                persistLocation(locationResult.lastLocation)
                _location.postValue(locationResult.lastLocation)
            }
        }

    }

    override fun requestLocationUpdate(updateReceiver: LocationUpdateReceiver): LiveData<Location> {
        if (locationUpdateReceiver.isEmpty()) {
            getLastKnownLocation()
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
        locationUpdateReceiver.add(updateReceiver)
        return location
    }

    override fun removeLocationUpdate(updateReceiver: LocationUpdateReceiver) {
        locationUpdateReceiver.remove(updateReceiver)
        if (locationUpdateReceiver.isEmpty()) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    override fun getLastKnownLocation(): LiveData<Location> {
        fusedLocationProviderClient.lastLocation.addOnCompleteListener {
            if (it.result != null) persistLocation(it.result!!)
            _location.postValue(getPersistedLocation())
        }
        return location
    }

    private fun persistLocation(location: Location) {
        preferences.edit().putFloat(PREF_LATITUDE, location.latitude.toFloat()).apply()
        preferences.edit().putFloat(PREF_LONGITUDE, location.longitude.toFloat()).apply()
    }

    private fun getPersistedLocation(): Location? {
        val latitude = preferences.getFloat(PREF_LATITUDE, -1f)
        val longitude = preferences.getFloat(PREF_LONGITUDE, -1f)
        if (latitude == -1f || longitude == -1f) {
            return null
        }
        val location = Location(LocationManager.GPS_PROVIDER)
        location.latitude = latitude.toDouble()
        location.longitude = longitude.toDouble()
        return location
    }
}