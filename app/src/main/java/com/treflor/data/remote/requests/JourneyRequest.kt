package com.treflor.data.remote.requests

import com.google.gson.annotations.SerializedName
import com.treflor.data.remote.response.DirectionApiResponse

data class JourneyRequest(
    @SerializedName("direction")
    val direction: DirectionApiResponse?,
    @SerializedName("journey")
    val journey: Journey?,
    @SerializedName("tracked_locations")
    val trackedLocations: String
)