package com.treflor.data.remote.response


import com.google.android.libraries.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import com.google.maps.android.PolyUtil
import com.treflor.models.directionapi.*

data class DirectionApiResponse(
    @SerializedName("bounds")
    val bounds: Bounds,
    @SerializedName("distance")
    val distance: Distance,
    @SerializedName("duration")
    val duration: Duration,
    @SerializedName("end_address")
    val endAddress: String,
    @SerializedName("end_location")
    val endLocation: Location,
    @SerializedName("points")
    val points: String,
    @SerializedName("start_address")
    val startAddress: String,
    @SerializedName("start_location")
    val startLocation: Location,
    @SerializedName("status")
    val status: String
) {
    fun decodedPoints(): List<LatLng> = PolyUtil.decode(points)
}