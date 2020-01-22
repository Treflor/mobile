package com.treflor.data.provider

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.treflor.models.directionapi.*

const val DIRECTION_BOUNDS = "direction_bounds"
const val DIRECTION_DISTANCE = "direction_distance"
const val DIRECTION_DURATION = "direction_duration"
const val DIRECTION_END_LOCATION = "direction_end_location"
const val DIRECTION_END_ADDRESS = "direction_end_address"
const val DIRECTION_POINTS = "direction_points"
const val DIRECTION_START_ADDRESS = "direction_start_address"
const val DIRECTION_START_LOCATION = "direction_start_location"

class CurrentDirectionProviderImpl(context: Context) : PreferenceProvider(context),
    CurrentDirectionProvider {
    override val currentDirection: LiveData<DirectionApiResponse>
        get() = _currentDirection
    private val _currentDirection by lazy {
        MutableLiveData<DirectionApiResponse>(
            getCurrentDirection()
        )
    }

    override fun getCurrentDirection(): DirectionApiResponse? {
        val bounds =
            Gson().fromJson(preferences.getString(DIRECTION_BOUNDS, ""), Bounds::class.java)
        val distance =
            Gson().fromJson(preferences.getString(DIRECTION_DISTANCE, ""), Distance::class.java)
        val duration =
            Gson().fromJson(preferences.getString(DIRECTION_DURATION, ""), Duration::class.java)
        val endLocation =
            Gson().fromJson(preferences.getString(DIRECTION_END_LOCATION, ""), Location::class.java)
        val endAddress = preferences.getString(DIRECTION_END_ADDRESS, "")
        val points = preferences.getString(DIRECTION_POINTS, "")
        val startAddress = preferences.getString(DIRECTION_START_ADDRESS, "")
        val startLocation = Gson().fromJson(
            preferences.getString(
                DIRECTION_START_LOCATION, ""
            ), Location::class.java
        )

        return if (!points.isNullOrEmpty()) DirectionApiResponse(
            bounds,
            distance,
            duration,
            endAddress!!,
            endLocation,
            points,
            startAddress!!,
            startLocation
        ) else null
    }

    override fun persistCurrentDirection(directionApiResponse: DirectionApiResponse) {
        val editor = preferences.edit()
        editor.putString(DIRECTION_BOUNDS, Gson().toJson(directionApiResponse.bounds))
        editor.putString(DIRECTION_DISTANCE, Gson().toJson(directionApiResponse.distance))
        editor.putString(DIRECTION_DURATION, Gson().toJson(directionApiResponse.duration))
        editor.putString(DIRECTION_END_LOCATION, Gson().toJson(directionApiResponse.endLocation))
        editor.putString(DIRECTION_END_ADDRESS, directionApiResponse.endAddress)
        editor.putString(DIRECTION_POINTS, directionApiResponse.points)
        editor.putString(DIRECTION_START_ADDRESS, directionApiResponse.startAddress)
        editor.putString(
            DIRECTION_START_LOCATION,
            Gson().toJson(directionApiResponse.startLocation)
        )
        editor.apply()
        _currentDirection.postValue(directionApiResponse)
    }

    override fun deleteDirection() {
        val editor = preferences.edit()
        editor.remove(DIRECTION_BOUNDS)
        editor.remove(DIRECTION_DISTANCE)
        editor.remove(DIRECTION_DURATION)
        editor.remove(DIRECTION_END_LOCATION)
        editor.remove(DIRECTION_END_ADDRESS)
        editor.remove(DIRECTION_POINTS)
        editor.remove(DIRECTION_START_ADDRESS)
        editor.remove(DIRECTION_START_LOCATION)
        editor.apply()
        _currentDirection.postValue(null)
    }
}