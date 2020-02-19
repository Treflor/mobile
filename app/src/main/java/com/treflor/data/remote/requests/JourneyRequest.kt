package com.treflor.data.remote.requests

import com.google.gson.annotations.SerializedName
import com.treflor.models.Journey
import com.treflor.models.Landmark
import com.treflor.models.directionapi.DirectionApiResponse

data class JourneyRequest(
    @SerializedName("direction")
    val direction: DirectionApiResponse?,
    @SerializedName("journey")
    val journey: Journey?,
    @SerializedName("tracked_locations")
    val trackedLocations: String,
    @SerializedName("landmarks")
    val landmarks: List<Landmark>?,
    @SerializedName("images")
    val images: List<String>?
)