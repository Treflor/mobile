package com.treflor.data.provider

import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationServices
import com.treflor.internal.LocationUpdateReciever

const val PREF_LATITUDE = "pref_latitude"
const val PREF_LONGITUDE = "pref_longitude"

class LocationProviderImpl(
    private val context: Context
) : PreferenceProvider(context),
    LocationProvider {
    override val location: LiveData<Location> get() = _location
    private val _location = MutableLiveData<Location>()
    private val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    override fun requestLocationUpdate(updateReceiver: LocationUpdateReciever): LiveData<Location> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeLocationUpdate(updateReceiver: LocationUpdateReciever): LiveData<Location> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        location.longitude = latitude.toDouble()
        return location
    }
}