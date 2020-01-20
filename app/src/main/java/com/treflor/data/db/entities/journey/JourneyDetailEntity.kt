package com.treflor.data.db.entities.journey

import androidx.room.Embedded
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.treflor.data.remote.response.DirectionApiResponse
import com.treflor.models.Journey
import com.treflor.models.User

class JourneyDetailEntity(
    @PrimaryKey
    @SerializedName("_id")
    val id: String,
    @Embedded(prefix = "user_")
    @SerializedName("user")
    val user: User,
    @Embedded(prefix = "direction_")
    @SerializedName("direction")
    val direction: DirectionApiResponse?,
    @Embedded(prefix = "journey_")
    @SerializedName("journey")
    val journey: Journey?,
    @SerializedName("tracked_locations")
    val trackedLocations: String
)