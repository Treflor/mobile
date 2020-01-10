package com.treflor.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.libraries.maps.model.LatLng

@Entity(tableName = "tracked_locations")
data class TrackedLocation(
    val lat: Double,
    val lng: Double
) {
    @PrimaryKey(autoGenerate = true)
    var pk: Int = 0

    fun toLatLng(): LatLng = LatLng(lat, lng)
}