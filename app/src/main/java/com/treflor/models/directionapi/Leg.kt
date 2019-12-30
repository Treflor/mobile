package com.treflor.models.directionapi


import com.google.gson.annotations.SerializedName

data class Leg(
    @SerializedName("distance")
    val distance: Distance,
    @SerializedName("duration")
    val duration: Duration,
    @SerializedName("end_address")
    val endAddress: String,
    @SerializedName("end_location")
    val endLocation: EndLocation,
    @SerializedName("start_address")
    val startAddress: String,
    @SerializedName("start_location")
    val startLocation: StartLocation
)