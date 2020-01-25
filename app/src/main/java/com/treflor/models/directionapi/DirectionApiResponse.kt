package com.treflor.models.directionapi


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.LatLngBounds
import com.google.gson.annotations.SerializedName
import com.google.maps.android.PolyUtil


data class DirectionApiResponse(
    @SerializedName("bounds")
    @Embedded(prefix = "bound_")
    val bounds: Bounds,
    @SerializedName("distance")
    @Embedded(prefix = "distance_")
    val distance: Distance,
    @SerializedName("duration")
    @Embedded(prefix = "duration_")
    val duration: Duration,
    @SerializedName("end_address")
    val endAddress: String,
    @SerializedName("end_location")
    @Embedded(prefix = "end_location_")
    val endLocation: Location,
    @SerializedName("points")
    val points: String,
    @SerializedName("start_address")
    val startAddress: String,
    @SerializedName("start_location")
    @Embedded(prefix = "start_location_")
    val startLocation: Location
) {

    fun decodedPoints(): List<LatLng> = PolyUtil.decode(points)
    fun getLatLngBounds(): LatLngBounds = LatLngBounds.builder()
        .include(LatLng(bounds.northeast.lat, bounds.northeast.lng))
        .include(
            LatLng(bounds.southwest.lat, bounds.southwest.lng)
        ).build()
}